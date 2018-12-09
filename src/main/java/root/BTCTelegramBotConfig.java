package root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

@Configuration
@PropertySource("app.properties")
public class BTCTelegramBotConfig
{

    @Autowired
    private Environment properties;
//    @Autowired
//    private BotClass botClass;

    @Bean
    DefaultBotOptions defaultBotOptions()
    {
        boolean useProxy = properties.getProperty("bot.proxy.use", Boolean.class);
        DefaultBotOptions defaultBotOptions = new DefaultBotOptions();
        if (useProxy)
        {
            String proxyHost = properties.getProperty("bot.proxy.host");
            int proxyPort = properties.getProperty("bot.proxy.port", Integer.class);
            String proxyLogin = properties.getProperty("bot.proxy.login");
            String proxyPass = properties.getProperty("bot.proxy.pass");
            //tg://socks?server=sr.spry.fail&port=1080&user=telegram&pass=telegram
            defaultBotOptions.setProxyHost(proxyHost);
            defaultBotOptions.setProxyPort(proxyPort);
            defaultBotOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);

            Authenticator.setDefault(new Authenticator()
            {
                @Override
                protected PasswordAuthentication getPasswordAuthentication()
                {
                    return new PasswordAuthentication(proxyLogin, proxyPass.toCharArray());
                }
            });
//            defaultBotOptions.setBaseUrl("http://api.telegram.org/bot");
        }
        return defaultBotOptions;
    }

//    @Bean
//    BotSession botSession()
//    {
//        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
//        try
//        {
//            return telegramBotsApi.registerBot(botClass);
//        }
//        catch (TelegramApiRequestException e)
//        {
//            e.printStackTrace();
//        }
//        return null;
//    }

}
