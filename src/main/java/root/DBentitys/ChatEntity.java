package root.DBentitys;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
//@Table(name = "chats")
public class ChatEntity
{
    public static final int CHAT_STAGE_NONE = -1;
    public static final int CHAT_STAGE_START = 0;
    public static final int CHAT_STAGE_CURRENCY_TO_GIVE_SELECTED = 1;
    public static final int CHAT_STAGE_COUNT_TO_GIVE_ENTERED = 2;
    public static final int CHAT_STAGE_CURRENCY_TO_RECIVE_SELECTED = 3;
    public static final int CHAT_STAGE_ACCOUNT_NUMBER_ENTERED = 4;
    public static final int CHAT_STAGE_PHONE_ENTERED = 5;

    @Id
    private long id;
    @ManyToOne(cascade = {CascadeType.ALL})
    private UserEntity userEntity;
    @ColumnDefault(value = "-1")
    private int chatStage = CHAT_STAGE_NONE;
    @OneToOne
    private CurrencyEntity chatCurrencyToGive;
    @OneToOne
    private CurrencyEntity chatCurrencyToReceive;
    @ColumnDefault(value = "0")
    private double currencyCount = 0;
    @ColumnDefault(value = "0")
    private double currencyCountToReceive = 0;
    @ColumnDefault(value = "0")
    private String contactPhone = "00000";
    @ColumnDefault(value = "00000")
    private String clientMoneyAccount = "";
    @ColumnDefault(value = "false")
    private Boolean adminMode = false;

    public ChatEntity()
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

    public UserEntity getUserEntity()
    {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity)
    {
        this.userEntity = userEntity;
    }

    public int getChatStage()
    {
        return chatStage;
    }

    public void setChatStage(int chatStage)
    {
        this.chatStage = chatStage;
    }

    public CurrencyEntity getChatCurrencyToGive()
    {
        return chatCurrencyToGive;
    }

    public void setChatCurrencyToGive(CurrencyEntity chatCurrencyToGive)
    {
        this.chatCurrencyToGive = chatCurrencyToGive;
    }

    public CurrencyEntity getChatCurrencyToReceive()
    {
        return chatCurrencyToReceive;
    }

    public void setChatCurrencyToReceive(CurrencyEntity chatCurrencyToReceive)
    {
        this.chatCurrencyToReceive = chatCurrencyToReceive;
    }

    public double getCurrencyCount()
    {
        return currencyCount;
    }

    public void setCurrencyCount(double currencyCount)
    {
        this.currencyCount = currencyCount;
    }

    public double getCurrencyCountToReceive()
    {
        return currencyCountToReceive;
    }

    public void setCurrencyCountToReceive(double currencyCountToReceive)
    {
        this.currencyCountToReceive = currencyCountToReceive;
    }

    public String getContactPhone()
    {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone)
    {
        this.contactPhone = contactPhone;
    }

    public String getClientMoneyAccount()
    {
        return clientMoneyAccount;
    }

    public void setClientMoneyAccount(String clientMoneyAccount)
    {
        this.clientMoneyAccount = clientMoneyAccount;
    }

    public Boolean getAdminMode()
    {
        return adminMode;
    }

    public void setAdminMode(Boolean adminMode)
    {
        this.adminMode = adminMode;
    }

    @Override
    public String toString()
    {

        return String.format("Заявка номер: %d\n" +
                "Отдают валюту: %s " + " количество: %s\n" +
                "Хотят валюту: %s " + " количество: %s\n" +
                "Телефон для связи: %s", getId(), getChatCurrencyToGive().getName(), getCurrencyCount(), getChatCurrencyToReceive().getName(), 0, getContactPhone());
    }
}
