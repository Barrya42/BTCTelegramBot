package root.Schedulers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        //simpleClientHttpRequestFactory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("167.16.1.250", 8080)));
        RestTemplate restTemplate = new RestTemplate(simpleClientHttpRequestFactory);

//        restTemplate.getForEntity()
        List<CurrencyEntity> enabledCurrencyEntities = currencyService.findAllUpdateRate();

        for (CurrencyEntity currencyEntity : enabledCurrencyEntities)
        {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAcceptCharset(Collections.singletonList(Charset.defaultCharset()));

            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            String request = "https://api.coincap.io/v2/rates/" + currencyEntity.getId();

            ResponseEntity<RateResponseEntity> response = restTemplate.exchange(request, HttpMethod.GET, entity, RateResponseEntity.class);

            RateResponseEntity RateResponse = response.getBody();

            if (RateResponse != null && RateResponse.getData() != null)
            {
                currencyEntity.setRateUsd(Double.parseDouble(RateResponse.getData()
                        .getRateUsd()));
            }

//            Double i = restTemplate.execute("https://www.cbr-xml-daily.ru/daily_json.js", HttpMethod.GET, null, new UsdRubExtractor());
//            if (i != null && i != 0)
//            {
//                currencyEntity.setRateRub(i);
//            }
        }
        Optional<CurrencyEntity> currencyEntityRUB = enabledCurrencyEntities.stream()
                .filter(currencyEntity -> currencyEntity.getName()
                        .equalsIgnoreCase("RUB"))
                .findFirst();

        currencyEntityRUB.ifPresent(currencyEntity1 -> enabledCurrencyEntities.stream()
//                .filter(currencyEntity -> !currencyEntity.getId()
//                        .equals(currencyEntity1
//                                .getId()))
                .forEach(currencyEntity ->
                        currencyEntity.setRateRub(currencyEntity.getRateUsd() * 1 / currencyEntity1
                                .getRateUsd())));

    }
}
