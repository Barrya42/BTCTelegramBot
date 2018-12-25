package root.Schedulers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import root.DBentitys.CurrencyEntity;
import root.DBservices.CurrencyService;
import root.json.RateResponseEntity;

@Component
public class CurrencyUpdater
{
    @Autowired
    private CurrencyService currencyService;

    @Scheduled(fixedDelay = 10000)
    private void updateCurrencies()
    {
        RestTemplate restTemplate = new RestTemplate();


//        restTemplate.getForEntity()
        List<CurrencyEntity> enabledCurrencyEntities = currencyService.findAllEnabled();

        for (CurrencyEntity currencyEntity : enabledCurrencyEntities)
        {
            String request = "https://api.coincap.io/v2/rates/" + currencyEntity.getId();
            RateResponseEntity response = restTemplate.getForObject(request, RateResponseEntity.class);
//            ResponseEntity<String> response = restTemplate.exchange(
//                    url, HttpMethod.GET, entity, String.class, param);
            if (response.getData() != null)
            {
                currencyEntity.setRateUsd(Double.parseDouble(response.getData()
                        .getRateUsd()));
            }

            Double i = restTemplate.execute("https://www.cbr-xml-daily.ru/daily_json.js", HttpMethod.GET, null, new UsdRubExtractor());
            if (i != null && i != 0)
            {
                currencyEntity.setRateRub(currencyEntity.getRateUsd() * i);
            }
        }


    }
}
