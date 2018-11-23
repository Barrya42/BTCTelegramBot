package root;


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
import java.util.List;
import java.util.Optional;

import root.DBservices.ChatService;
import root.DBservices.CurrencyService;
import root.DBservices.RoleService;
import root.DBservices.UserService;
import root.entitys.ChatEntity;
import root.entitys.CurrencyEntity;
import root.entitys.UserEntity;

@Component
public class MessageHandlerImpl implements MessageHandler
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

    @Override
    @Transactional
    public SendMessage handleResponse(Message message)
    {
        SendMessage responseMessage = new SendMessage();

        try
        {
            currentUser = findUserEntity(message.getFrom());
            currentChat = findUserChat(message.getChat());
            processTextResponse(responseMessage, message.getText());
            prepareButtons(responseMessage);
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
            return userService.addUser(newUserEntity);
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
            return chatService.addChat(newChatEntity);
        });

    }

    private void processTextResponse(SendMessage sendMessage, String incomingText)
    {
        switch (currentChat.getChatStage())
        {
            case ChatEntity.CHAT_STAGE_NONE: // В начальном состоянии мбот ждет слова старт
            {
                if (incomingText.toLowerCase()
                        .equals("/start") || incomingText.toLowerCase()
                        .equals("старт"))
                {
                    currentChat.setChatStage(ChatEntity.CHAT_STAGE_START);
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
            case ChatEntity.CHAT_STAGE_COUNT_TO_GIVE_ENTERED: //Ждем ввода ввалбты которую пользователь хочет получить
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
                if (incomingText.toLowerCase()
                        .matches("\\d{5}")) // допустим номер кошелька состоит из 5 цифр
                {
                    currentChat.setChatStage(ChatEntity.CHAT_STAGE_ACCOUNT_NUMBER_ENTERED);
                    currentChat.setClientMoneyAccount(incomingText);
                }
                else if (incomingText.toLowerCase()
                        .equals("отмена"))
                {
                    currentChat.setChatStage(ChatEntity.CHAT_STAGE_NONE);
                }
                break;
            }
            case ChatEntity.CHAT_STAGE_ACCOUNT_NUMBER_ENTERED:
            {
                currentChat.setContactPhone(incomingText.replaceAll("\\D",""));
                sendMessage.setText("Спасибо. Мы вам перезвоним.");
                currentChat.setChatStage(ChatEntity.CHAT_STAGE_PHONE_ENTERED);
                break;
            }
            case ChatEntity.CHAT_STAGE_PHONE_ENTERED:
            {
                if(incomingText.toLowerCase().equals("подтвердить"))
                {
                    // TODO: 23.11.2018 заверщить чат
                }
                break;
            }
            default:
            {
                sendMessage.setText("Что бы начать нажмите Старт");
                break;
            }
        }
    }

    private void prepareButtons(SendMessage sendMessage)
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
                KeyboardRow firstRow = new KeyboardRow();
                keyboardRows.add(firstRow);
                firstRow.add(keyboardButton);

                sendMessage.setText("Чтобы начать нажмите Старт");

                break;
            }
            case ChatEntity.CHAT_STAGE_START: // предлагаем кнопки для выбора валюты, которую мы принимаем
            {
                List<CurrencyEntity> enabledCurrencies = currencyService.findAllEnabled();
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
            case ChatEntity.CHAT_STAGE_CURRENCY_TO_RECIVE_SELECTED:
            {
                String stringBuilder = "За ваши " +
                        currentChat.getCurrencyCount() +
                        " " +
                        currentChat.getChatCurrencyToGive()
                                .getName() +
                        " " +
                        " вы получите " +
                        calculateCurrencyCountForUser() +
                        currentChat.getChatCurrencyToReceive()
                                .getName() +
                        " Если вы согласны, тогда напишите номер кошелька для получения валюты, в противном случае надмите отмена";
                KeyboardRow buttonRow = new KeyboardRow();
                keyboardRows.add(buttonRow);
                buttonRow.add("Отмена");

                sendMessage.setText(stringBuilder);
                break;
            }
            case ChatEntity.CHAT_STAGE_ACCOUNT_NUMBER_ENTERED:
            {
                sendMessage.setText("Введите ваш номер телефона, мы с вами свяжемся пссле проведения выплаты.");
                break;
            }
            case ChatEntity.CHAT_STAGE_PHONE_ENTERED:
            {
                sendMessage.setText("Ожидайте нашего звонка. Или подтвердите получение выплаты.");
                KeyboardRow buttonRow = new KeyboardRow();
                keyboardRows.add(buttonRow);
                buttonRow.add("Подтвердить.");
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
                .getCourseInBTC() / currentChat.getChatCurrencyToReceive()
                .getCourseInBTC() * currentChat.getCurrencyCount();
    }
}
