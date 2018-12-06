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
    @ColumnDefault(value = "")
    private String name;
    @ColumnDefault(value = "false")
    private boolean blocked = false;
    @ManyToOne
    private RoleEntity userRole;

    public UserEntity()
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

    public RoleEntity getUserRole()
    {
        return userRole;
    }

    public void setUserRole(RoleEntity userRole)
    {
        this.userRole = userRole;
    }

    public boolean isBlocked()
    {
        return blocked;
    }

    public void setBlocked(boolean blocked)
    {
        this.blocked = blocked;
    }

    @Override
    public String toString()
    {

        return "id: " + getId() + "\n" +
                "name: " + getName() + "\n" +
                "blocked: " + isBlocked();
    }
}
