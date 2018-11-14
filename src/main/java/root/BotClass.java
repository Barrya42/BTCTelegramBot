package root;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
@Component
public class BotClass extends TelegramLongPollingBot
{
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
