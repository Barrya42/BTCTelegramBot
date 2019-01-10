package root;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

import root.DBentitys.ChatEntity;
import root.DBentitys.CurrencyEntity;
import root.DBentitys.UserEntity;
import root.DBservices.ChatService;
import root.DBservices.CurrencyService;
import root.DBservices.RoleService;
import root.DBservices.UserService;

@Component
public class MessageHandlerImpl implements MessageHandler, InitializingBean
{
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private CurrencyService currencyService;


    private UserEntity currentUser;
    private ChatEntity currentChat;

    private Set<String> phonesForOperators = new HashSet<>();

    private double tax;
    //@Value(value = "#{bot.admin.secret}")
    private String adminSecret;

    @Override
    @Transactional
    public synchronized SendMessage handleResponse(Message message)
    {
        SendMessage responseMessage = new SendMessage();
        try
        {
            currentUser = findUserEntity(message.getFrom());
            currentChat = findUserChat(message.getChat());
            String text = message.getText() == null ? "" : message.getText();
            responseMessage.setChatId(currentChat.getId());
            if (currentUser.getUserRole()
                    .equals(roleService.getUserRole()))
            {
                //сообщения для пользователя
                processUserTextResponse(responseMessage, text);
                if (responseMessage.getText() == null || responseMessage.getText()
                        .isEmpty())
                {
                    prepareUserButtons(responseMessage);
                }
            }
            else
            {
                //сообщения для оператора
                processOperatorTextResponse(responseMessage, text);
                //if (responseMessage.getText() == null || responseMessage.getText()
                //        .isEmpty())
                //{
                prepareOperatorButtons(responseMessage);
                // }
            }
        }
        catch (RuntimeException e)
        {
            responseMessage.setChatId(currentChat.getId());
            e.printStackTrace();
            return responseMessage.setText(e.getMessage());
        }
        return responseMessage;
    }

    private UserEntity findUserEntity(User user)
    {
        Optional<UserEntity> userEntity = userService.findUserById(user.getId());
        return userEntity.orElseGet(() ->
        {
            UserEntity newUserEntity = new UserEntity();
            newUserEntity.setId(user.getId());
            newUserEntity.setName(user.getUserName());
            newUserEntity.setBlocked(false);
            newUserEntity.setUserRole(roleService.getUserRole());
            return userService.saveUser(newUserEntity);
        });
    }

    private ChatEntity findUserChat(Chat chat)
    {
        Optional<ChatEntity> chatEntity = chatService.findChatById(chat.getId());
        return chatEntity.orElseGet(() ->
        {
            ChatEntity newChatEntity = new ChatEntity();
            newChatEntity.setChatStage(ChatEntity.CHAT_STAGE_NONE);
            newChatEntity.setId(chat.getId());
            newChatEntity.setUserEntity(currentUser);
            return chatService.saveChat(newChatEntity);
        });

    }

    private void processUserTextResponse(SendMessage sendMessage, String incomingText)
    {
        switch (currentChat.getChatStage())
        {
            case ChatEntity.CHAT_STAGE_NONE: // В начальном состоянии бот ждет слова старт
            {

                if (incomingText
                        .equalsIgnoreCase("/start") || incomingText.equalsIgnoreCase("старт"))
                {
                    currentChat.setChatStage(ChatEntity.CHAT_STAGE_START);
                }
                else if (incomingText.equalsIgnoreCase("оператор"))
                {
                    String phone;
                    try
                    {
                        phone = incomingText.replaceAll("\\D{11}", "");
                    }
                    catch (PatternSyntaxException e)
                    {
                        sendMessage.setText("Не верный формат телефона.");
                        break;
                    }
                    if (phonesForOperators.remove(phone))
                    {
                        currentUser.setUserRole(roleService.getOperatorRole());
                        sendMessage.setText("Теперь вы оператор. (Напишите что нибудь)");
                    }
                }
                break;
            }
            case ChatEntity.CHAT_STAGE_START: //ждем ввода валюты которую пользователь хочет поменять
            {

                List<CurrencyEntity> enabledCurrencies = currencyService.findAllEnabled();
                if (!incomingText.isEmpty())
                {
                    Optional<CurrencyEntity> selectedCurrency = enabledCurrencies.stream()
                            .filter(currencyEntity -> currencyEntity.getName()
                                    .equals(incomingText))
                            .findFirst();
                    if (!selectedCurrency.isPresent())
                    {
                        sendMessage.setText("Не верное имя валюты.");
                        break;
                    }
                    else
                    {
                        //Чувак ввел верную валюту
                        currentChat.setChatCurrencyToGive(selectedCurrency.get());
                        currentChat.setChatStage(ChatEntity.CHAT_STAGE_CURRENCY_TO_GIVE_SELECTED);
                    }
                }
                break;
            }
            case ChatEntity.CHAT_STAGE_CURRENCY_TO_GIVE_SELECTED: // ждем ввода количества валюты для обмена
            {
                try
                {
                    double currencyToGiveCount = Double.parseDouble(incomingText);
                    currentChat.setCurrencyCount(currencyToGiveCount);
                    currentChat.setChatStage(ChatEntity.CHAT_STAGE_COUNT_TO_GIVE_ENTERED);
                }
                catch (NumberFormatException e)
                {
                    sendMessage.setText("Не верное количество.");
                }
                break;
            }
            case ChatEntity.CHAT_STAGE_COUNT_TO_GIVE_ENTERED: //Ждем ввода валюты которую пользователь хочет получить
            {
                List<CurrencyEntity> allEnabledToGive = currencyService.findAllEnabledToGive();
                if (!incomingText.isEmpty())
                {
                    Optional<CurrencyEntity> selectedCurrency = allEnabledToGive.stream()
                            .filter(currencyEntity -> currencyEntity.getName()
                                    .equals(incomingText))
                            .findFirst();
                    if (!selectedCurrency.isPresent())
                    {
                        sendMessage.setText("Не верное имя валюты.");
                        break;
                    }
                    else
                    {
                        //Чувак ввел верную валюту
                        currentChat.setChatCurrencyToReceive(selectedCurrency.get());
                        currentChat.setChatStage(ChatEntity.CHAT_STAGE_CURRENCY_TO_RECIVE_SELECTED);
                    }
                }
                break;
            }
            case ChatEntity.CHAT_STAGE_CURRENCY_TO_RECIVE_SELECTED:
            {
                if (incomingText.matches("\\d{5}")) // допустим номер кошелька состоит из 5 цифр
                {
                    currentChat.setChatStage(ChatEntity.CHAT_STAGE_ACCOUNT_NUMBER_ENTERED);
                    currentChat.setClientMoneyAccount(incomingText);
                }
                else if (incomingText
                        .equalsIgnoreCase("отмена"))
                {
                    currentChat.setChatStage(ChatEntity.CHAT_STAGE_NONE);
                }
                break;
            }
            case ChatEntity.CHAT_STAGE_ACCOUNT_NUMBER_ENTERED:
            {
                String phone;
                try
                {
                    phone = incomingText.replaceAll("\\D{11}", "");
                }
                catch (PatternSyntaxException e)
                {
                    sendMessage.setText("Не верный формат телефона.");
                    break;
                }


                currentChat.setContactPhone(phone);
                sendMessage.setText("Спасибо. Мы вам перезвоним.");
                currentChat.setChatStage(ChatEntity.CHAT_STAGE_PHONE_ENTERED);
                break;
            }
            case ChatEntity.CHAT_STAGE_PHONE_ENTERED:
            {
                if (incomingText.equalsIgnoreCase("подтвердить"))
                {
                    // TODO: 23.11.2018 заверщить чат
                    chatService.deleteChat(currentChat);
                    sendMessage.setText("Обмен завершен. Спасибо");
                }
                break;
            }
            default:
            {
                sendMessage.setText("Что бы начать нажмите Старт");
                break;
            }
            //chatService.saveChat(currentChat);
        }
    }

    private void processOperatorTextResponse(SendMessage sendMessage, String incomingText)
    {
        if (incomingText.equalsIgnoreCase("Получить все заявки."))
        {
            List<ChatEntity> chatEntities = chatService.findAllWithRole(roleService.getUserRole());
            if (chatEntities.isEmpty())
            {
                sendMessage.setText("Нет активных заявок.");
            }
            else
            {
                String res = "";
                for (ChatEntity chatEntity : chatEntities)
                {

                    res += chatEntity.toString();
                    res += "\n";

                }
                sendMessage.setText(res);
            }
        }
        else if (incomingText.equalsIgnoreCase("Удалить все заявки."))
        {
            chatService.deleteAll();
            sendMessage.setText("Все заявки удалены.");
        }
        else if (incomingText.equalsIgnoreCase("Удалить по номеру."))
        {
            try
            {
                Optional<ChatEntity> chatEntity = chatService.findChatById(Long.parseLong(incomingText));
                if (chatEntity.isPresent())
                {
                    chatService.deleteChat(chatEntity.get());
                }
                else
                {
                    sendMessage.setText("Заказа с таким номером не существует.");
                }
            }
            catch (NumberFormatException e)
            {
                sendMessage.setText("Не верный номер(только цифры)");
            }
        }
        else if (incomingText.equals(adminSecret))
        {
            //Admin mode
            currentChat.setAdminMode(!currentChat.getAdminMode());
            if (currentChat.getAdminMode())
            {
                sendMessage.setText("Режим администратора!\n" +
                        "Команды: \n" +
                        "Добавить 89123457788 (добавит в список операторов человека по номеру телефона, " +
                        "для этого ему нужно найти бота и нажать старт, он будет автоматически добавлен.)\n" +
                        "\n" +
                        "Установить 12 (установит процент комиссии взимаемый с клиента за пользование сервисом. Процент будет минусоваться из вознаграждения пользователя.)\n" +
                        "\n" +
                        "Список операторов (выведет список активных операторов)\n" +
                        "\n" +
                        "Удалить 112244456 (Удалить оператора из списка по коду, см \"Список операторов\")");
            }
            else
            {
                sendMessage.setText("Выход из режима админимтартора.");
            }
        }
        else if (currentChat.getAdminMode())
        {
            if (incomingText.toLowerCase()
                    .startsWith("установить"))
            {
                try
                {
                    tax = Double.parseDouble(incomingText.replaceAll("установить", "")
                            .trim());
                }
                catch (NumberFormatException e)
                {
                    sendMessage.setText("не верный формат числа.");
                }
            }
            else if (incomingText.equalsIgnoreCase("Список операторов"))
            {
                List<UserEntity> userEntities = userService.findAllWithRole(roleService.getOperatorRole());
                sendMessage.setText(userEntities.toString());
            }
            else if (incomingText.toLowerCase()
                    .startsWith("удалить"))
            {
                try
                {
                    long id = Long.parseLong(incomingText.replaceAll("Удалить", "")
                            .trim());
                    //userService.deleteUser(id);
                    Optional<UserEntity> user = userService.findUserById(id);
                    if (user.isPresent())
                    {
                        user.get()
                                .setUserRole(roleService.getUserRole());
                        if (currentChat.getUserEntity()
                                .equals(user.get()))
                        {
                            currentChat.setAdminMode(false);
                        }
                        sendMessage.setText("Пользователь теперь не оператор.");
                    }
                    else
                    {
                        sendMessage.setText("Нет такого пользователя.");
                    }
                }
                catch (NumberFormatException e)
                {
                    sendMessage.setText("не верный формат числа.");
                }
            }
            else if (incomingText.toLowerCase()
                    .startsWith("добавить"))
            {
                String phone;
                try
                {
                    phone = incomingText.replaceAll("\\D{11}", "");
                    if (phonesForOperators.add(phone))
                    {
                        sendMessage.setText("Пользователь, который введет команду \"оператор 'телефон(11 знаков)'\" станет оператором системы и сможет просматривать заявки.");
                    }
                    else
                    {
                        sendMessage.setText("Такой номер уже есть");
                    }
                }
                catch (PatternSyntaxException e)
                {
                    sendMessage.setText("Не верный формат телефона.");

                }


            }
            else
            {
                sendMessage.setText("Не верная команда.");
            }
        }

        else
        {
            sendMessage.setText("Не верная команда.");
        }
    }

    private void prepareOperatorButtons(SendMessage sendMessage)
    {
        sendMessage.setChatId(currentChat.getId());

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);

        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        replyKeyboardMarkup.setKeyboard(keyboardRows);


        KeyboardRow firstRow = new KeyboardRow();
        keyboardRows.add(firstRow);
        firstRow.add("Получить все заявки.");
        firstRow.add("Удалить все заявки.");
        firstRow.add("Удалить по коду.");
//        if (currentChat.getAdminMode())
//        {
//            KeyboardRow secondRow = new KeyboardRow();
//            secondRow.add("Установить комиссию.");
//            secondRow.add("Список операторов.");
//            secondRow.add("Удалить оператора по коду.");
//            secondRow.add("Добавить оператора(телефон).");
//
//            keyboardRows.add(secondRow);

//        }

    }

    private void prepareUserButtons(SendMessage sendMessage)
    {
        sendMessage.setChatId(currentChat.getId());

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);

        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        switch (currentChat.getChatStage())
        {
            case ChatEntity.CHAT_STAGE_NONE: // выводим пользователю кнопку старт для запуска диалога
            {

                KeyboardButton keyboardButton = new KeyboardButton();

                keyboardButton.setText("Старт");
//                keyboardButton.setRequestContact(true);
                KeyboardRow firstRow = new KeyboardRow();
                keyboardRows.add(firstRow);
                firstRow.add(keyboardButton);

                sendMessage.setText("Чтобы начать нажмите Старт");

                break;
            }
            case ChatEntity.CHAT_STAGE_START: // предлагаем кнопки для выбора валюты, которую мы принимаем
            {
                List<CurrencyEntity> enabledCurrencies = currencyService.findAllEnabledToGive();
                int rowSize = 3;
                if (enabledCurrencies.size() > 0)
                {
                    KeyboardRow buttonRow = null;
                    sendMessage.setText("Выберите валюту для обмена");
                    for (int i = 0; i < enabledCurrencies.size(); i++)
                    {

                        if (i % rowSize == 0)
                        {
                            buttonRow = new KeyboardRow();
                            keyboardRows.add(buttonRow);
                        }

                        KeyboardButton keyboardButton = new KeyboardButton();
                        keyboardButton.setText(enabledCurrencies.get(i)
                                .getName());
                        buttonRow.add(keyboardButton);
                    }
                }
                else
                {
                    sendMessage.setText("Нет активных валют.");
                }

                break;
            }
            case ChatEntity.CHAT_STAGE_CURRENCY_TO_GIVE_SELECTED:   //запрашиваем количество валюты
            {
                sendMessage.setText("Введите количество для обмена");
                break;
            }
            case ChatEntity.CHAT_STAGE_COUNT_TO_GIVE_ENTERED: //Запрашиваем валюту которую пользователь хочет получить
            {
                List<CurrencyEntity> enabledToGiveCurrencies = currencyService.findAllEnabledToGive();
                int rowSize = 3;
                if (enabledToGiveCurrencies.size() > 0)
                {
                    KeyboardRow buttonRow = null;
                    sendMessage.setText("Выберите валюту на которую хотите поменять");
                    for (int i = 0; i < enabledToGiveCurrencies.size(); i++)
                    {

                        if (i % rowSize == 0)
                        {
                            buttonRow = new KeyboardRow();
                            keyboardRows.add(buttonRow);
                        }

                        KeyboardButton keyboardButton = new KeyboardButton();
                        keyboardButton.setText(enabledToGiveCurrencies.get(i)
                                .getName());
                        buttonRow.add(keyboardButton);

                    }
                }
                else
                {
                    sendMessage.setText("Нет активных валют.");
                }
                break;
            }
            case ChatEntity.CHAT_STAGE_CURRENCY_TO_RECIVE_SELECTED: //Запрашиваем номер счета для перевода денег
            {
                currentChat.setCurrencyCountToReceive(calculateCurrencyCountForUser());
                String stringBuilder = "За ваши " +
                        currentChat.getCurrencyCount() +
                        " " +
                        currentChat.getChatCurrencyToGive()
                                .getName() +
                        " " +
                        " вы получите " +
                        currentChat.getCurrencyCountToReceive() +
                        " " +
                        currentChat.getChatCurrencyToReceive()
                                .getName() +
                        " Если вы согласны, тогда напишите номер кошелька для получения валюты, в противном случае нажмите отмена";
                KeyboardRow buttonRow = new KeyboardRow();
                keyboardRows.add(buttonRow);
                buttonRow.add("Отмена");

                sendMessage.setText(stringBuilder);
                break;
            }
            case ChatEntity.CHAT_STAGE_ACCOUNT_NUMBER_ENTERED: // Запрашиваем номер телефона
            {
                sendMessage.setText("Введите ваш номер телефона, мы с вами свяжемся пссле проведения выплаты.");
                break;
            }
            case ChatEntity.CHAT_STAGE_PHONE_ENTERED:
            {
                sendMessage.setText("Ожидайте нашего звонка. Или подтвердите получение выплаты.");
                KeyboardRow buttonRow = new KeyboardRow();
                keyboardRows.add(buttonRow);
                buttonRow.add("Подтвердить");
                break;
            }
            default:
            {
                sendMessage.setText("Что бы начать нажмите Старт");
                break;
            }
        }


    }

    private double calculateCurrencyCountForUser()
    {
        return currentChat.getChatCurrencyToGive()
                .getRateUsd() / currentChat.getChatCurrencyToReceive()
                .getRateUsd() * currentChat.getCurrencyCount() * ((100 - tax) / 100);
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        adminSecret = System.getenv("adminSecret");
    }
}
