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
            prepareButtons(responseMessage, message.getText());
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

    private void prepareButtons(SendMessage sendMessage, String incomingText)
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
            case ChatEntity.CHAT_STAGE_NONE:
            {
                if (incomingText.toLowerCase()
                        .equals("/start") || incomingText.toLowerCase()
                        .equals("старт"))
                {
                    currentChat.setChatStage(ChatEntity.CHAT_STAGE_START);
                    prepareButtons(sendMessage, incomingText);
                }
                else
                {
                    KeyboardButton keyboardButton = new KeyboardButton();

                    keyboardButton.setText("Старт");
                    KeyboardRow firstRow = new KeyboardRow();
                    keyboardRows.add(firstRow);
                    firstRow.add(keyboardButton);

                    sendMessage.setText("Чтобы начать нажмите Старт");
                }
                break;
            }
            case ChatEntity.CHAT_STAGE_START:
            {
                int rowSize = 3;
                List<CurrencyEntity> enabledCurrencies = currencyService.findAllEnabled();
                if (enabledCurrencies.size() > 0)
                {
                    sendMessage.setText("Выберите валюту для обмена");
                    for (int i = 0; i < enabledCurrencies.size(); i++)
                    {
// TODO: 21.11.18 Доделать кнопки в ряд

                    }
                }
                else
                {
                    sendMessage.setText("Нет активных валют.");
                }

                break;
            }
            case ChatEntity.CHAT_STAGE_CURRENCY_TO_GIVE_SELECTED:
            {
                sendMessage.setText("currency selected");
                break;
            }
            case ChatEntity.CHAT_STAGE_COUNT_TO_GIVE_ENTERED:
            {
                sendMessage.setText("Count entered");
                break;
            }
            case ChatEntity.CHAT_STAGE_CURRENCY_TO_RECIVE_SELECTED:
            {
                sendMessage.setText("currency to receive selected");
                break;
            }
            case ChatEntity.CHAT_STAGE_ACCOUNT_NUMBER_ENTERED:
            {
                sendMessage.setText("account number entered");
                break;
            }
            case ChatEntity.CHAT_STAGE_PHONE_ENTERED:
            {
                sendMessage.setText("phone entered");
                break;
            }
            default:
            {
                sendMessage.setText("Что бы начать нажмите Старт");
                break;
            }
        }


    }
}
