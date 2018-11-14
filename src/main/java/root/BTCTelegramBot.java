package root;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.telegram.telegrambots.ApiContextInitializer;

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
        SpringApplication.run(new Class<?>[]{BTCTelegramBot.class}, args);

        //ApplicationContext ctx = new AnnotationConfigApplicationContext(BTCTelegramBotConfig.class);
        //BotClass bot = ctx.getBean("bot",BotClass.class);

    }
}

