package root.entitys;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
public class UserEntity
{
    @Id
    private long id;
    private String name;
    private boolean blocked = false;
    @ManyToOne
    private RoleEntity userRole;

    public long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public RoleEntity getUserRole()
    {
        return userRole;
    }

    public boolean isBlocked()
    {
        return blocked;
    }

    public UserEntity()
    {

    }
}
