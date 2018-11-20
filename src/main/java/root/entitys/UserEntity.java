package root.entitys;

import org.hibernate.annotations.ColumnDefault;

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
    @ColumnDefault(value = "false")
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

    public void setId(long id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setBlocked(boolean blocked)
    {
        this.blocked = blocked;
    }

    public void setUserRole(RoleEntity userRole)
    {
        this.userRole = userRole;
    }

    public UserEntity()
    {

    }
}
