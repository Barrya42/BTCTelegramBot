package root.DBservices;

import java.util.List;

import root.DBentitys.CurrencyEntity;

public interface CurrencyService
{
    public CurrencyEntity addCurrency(CurrencyEntity currencyEntity);

    public void removeCurrency(CurrencyEntity currencyEntity);

    public List<CurrencyEntity> findAllEnabled();

    public List<CurrencyEntity> findAllEnabledToGive();
}
