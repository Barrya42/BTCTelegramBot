package root.DBentitys;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "currencies")
public class CurrencyEntity
{
    @Id
    private String id;
    @ColumnDefault(value = "'empty'")
    private String name;
    @ColumnDefault(value = "true")
    private boolean enabled = true; //принимаем валюту
    @ColumnDefault(value = "true")
    private boolean enabledToGive = true; // отдаем валюту
    @ColumnDefault(value = "0")
    private double rateUsd = 0;
    @ColumnDefault(value = "0")
    private double rateRub = 0;
    public CurrencyEntity()
    {

    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public boolean isEnabledToGive()
    {
        return enabledToGive;
    }

    public void setEnabledToGive(boolean enabledToGive)
    {
        this.enabledToGive = enabledToGive;
    }

    public double getRateUsd()
    {
        return rateUsd;
    }

    public void setRateUsd(double rateUsd)
    {
        this.rateUsd = rateUsd;
    }

    public double getRateRub()
    {
        return rateRub;
    }

    public void setRateRub(double rateRub)
    {
        this.rateRub = rateRub;
    }
}
