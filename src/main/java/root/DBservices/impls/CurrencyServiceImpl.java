package root.DBservices.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import root.DBservices.CurrencyService;
import root.entitys.CurrencyEntity;
import root.repos.CurrencyRepository;

@Service
public class CurrencyServiceImpl implements CurrencyService
{
    @Autowired
    CurrencyRepository currencyRepository;

    @Override
    public CurrencyEntity addCurrency(CurrencyEntity currencyEntity)
    {
        return currencyRepository.save(currencyEntity);
    }

    @Override
    public void removeCurrency(CurrencyEntity currencyEntity)
    {
        currencyRepository.delete(currencyEntity);
    }

    @Override
    public List<CurrencyEntity> findAllEnabled()
    {

        return currencyRepository.findAllByenabled(true);
    }
}
