package root.entitys;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Chats")
public class ChatEntity
{
    public static int CHAT_STAGE_START = 0;
    public static int CHAT_STAGE_CURRENCY_TO_GIVE_SELECTED = 1;
    public static int CHAT_STAGE_COUNT_TO_GIVE_ENTERED = 2;
    public static int CHAT_STAGE_CURRENCY_TO_RECIVE_SELECTED = 3;
    public static int CHAT_STAGE_ACCOUNT_NUMBER_ENTERED = 4;
    public static int CHAT_STAGE_PHONE_ENTERED = 5;

    @Id
    private long id;
    @ManyToOne
    private UserEntity userEntity;
    private int chatStage = -1;

    public long getId()
    {
        return id;
    }

    public UserEntity getUserEntity()
    {
        return userEntity;
    }

    public int getChatStage()
    {
        return chatStage;
    }

    public void setChatStage(int chatStage)
    {
        this.chatStage = chatStage;
    }

    public ChatEntity()
    {

    }
}
