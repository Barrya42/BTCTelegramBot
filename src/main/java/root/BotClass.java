package root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

import root.DBservices.UserService;
import root.entitys.UserEntity;

@Controller
public class BotClass extends TelegramLongPollingBot
{
    @Autowired
    UserService userService;
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
        long userId = update.getMessage()
                .getFrom()
                .getId();
        String text = update.getMessage()
                .getText();
        Optional<UserEntity> userEntity = userService.findUserById(userId);
        if (userEntity.isPresent())
        {

        }
        else
        {
            if (text.equals("/start"))
            {
//            userService
            }
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
}
