package root.DBservices;

import java.util.List;
import java.util.Optional;

import root.DBentitys.CurrencyEntity;

public interface CurrencyService
{
    public CurrencyEntity addCurrency(CurrencyEntity currencyEntity);

    public void removeCurrency(CurrencyEntity currencyEntity);

    public List<CurrencyEntity> findAllEnabled();

    public List<CurrencyEntity> findAllEnabledToGive();

    public Optional<CurrencyEntity> findById(String id);
}
