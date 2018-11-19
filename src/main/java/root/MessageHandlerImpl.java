package root;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

import root.DBservices.ChatService;
import root.DBservices.RoleService;
import root.DBservices.UserService;
import root.entitys.ChatEntity;
import root.entitys.UserEntity;

public class MessageHandlerImpl implements MessageHandler
{
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ChatService chatService;

    private UserEntity currentUser;
    private ChatEntity currentChat;

    @Override
    @Transactional
    public Optional<SendMessage> handleResponse(Message message)
    {
        SendMessage sendMessage = new SendMessage();


        currentUser = findUserEntity(message.getFrom());
        currentChat = findUserChat(message.getChat());
        prepareButtons(sendMessage);
        // TODO: 20.11.18 обновить чат после отпправки ответа
        return Optional.of(sendMessage);
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
            userService.addUser(newUserEntity);
            return newUserEntity;
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

    private void prepareButtons(SendMessage sendMessage)
    {
        switch (currentChat.getChatStage())
        {
            case ChatEntity.CHAT_STAGE_NONE:
            {
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
