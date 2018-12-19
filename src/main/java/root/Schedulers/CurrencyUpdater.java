package root.Schedulers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import root.DBservices.CurrencyService;

@Component
public class CurrencyUpdater
{
    @Autowired
    CurrencyService currencyService;

    @Scheduled(fixedDelay = 10000)
    private void updateCurrencies()
    {
        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.getForEntity()
    }
}
