package root.entitys;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Currencies")
public class CurrencyEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ColumnDefault(value = "empty")
    private String name;
    @ColumnDefault(value = "true")
    private boolean enabled = true; //принимаем валюту
    @ColumnDefault(value = "true")
    private boolean enabledToGive = true; // отдаем валюту
    @ColumnDefault(value = "0")
    private double courseInBTC = 0;
    @ColumnDefault(value = "0")
    private double courseInUSD = 0;

    public CurrencyEntity()
    {

    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
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

    public double getCourseInBTC()
    {
        return courseInBTC;
    }

    public void setCourseInBTC(double courseInBTC)
    {
        this.courseInBTC = courseInBTC;
    }

    public double getCourseInUSD()
    {
        return courseInUSD;
    }

    public void setCourseInUSD(double courseInUSD)
    {
        this.courseInUSD = courseInUSD;
    }
}
