package root;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import root.DBservices.RoleService;
import root.DBservices.UserService;

@Service
public class BotClass extends TelegramLongPollingBot implements InitializingBean
{
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MessageHandler messageHandler;
    private String username = "CryptoExch42Bot";
    private String name = "BTCBot";
    private String apiToken = "764998454:AAH9vhYHWsUPa5Ix6Pd7vr69yoIra_vFJmQ";


    public BotClass(DefaultBotOptions options)
    {

        super(options);
    }

    @Override
    public void onUpdateReceived(Update update)
    {
        try
        {
            SendMessage sendMessage = messageHandler.handleResponse(update.getMessage());
            execute(sendMessage);
        }
        catch (TelegramApiException throwable)
        {
            throwable.printStackTrace();
        }
    }


    @Override
    public String getBotUsername()
    {
        return username;
    }

    @Override
    public String getBotToken()
    {
        return apiToken;
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
//        UserEntity newUser = new UserEntity();
//        newUser.setId(455776162);
//        newUser.setUserRole(roleService.getOperatorRole());
//        userService.addUser(newUser);
    }
}
