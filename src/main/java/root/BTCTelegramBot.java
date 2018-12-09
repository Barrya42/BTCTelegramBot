package root;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@EnableAutoConfiguration
@ComponentScan("root")
//@ComponentScan("root.Services")
//@Import(DataConfig.class)

public class BTCTelegramBot
{


    public static void main(String[] args)
    {
        //System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
        ApiContextInitializer.init();
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(new Class<?>[]{BTCTelegramBot.class}, args);
        BotClass botClass = configurableApplicationContext.getBean(BotClass.class);

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try
        {
            telegramBotsApi.registerBot(botClass);
        }
        catch (TelegramApiRequestException e)
        {
            e.printStackTrace();
        }

        //ApplicationContext ctx = new AnnotationConfigApplicationContext(BTCTelegramBotConfig.class);
        //BotClass bot = ctx.getBean("bot",BotClass.class);

    }
}

