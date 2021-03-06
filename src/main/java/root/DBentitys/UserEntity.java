package root.DBentitys;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
//@Table(name = "users")
public class UserEntity
{
    @Id
    //@Cascade(value = CascadeType.SAVE_UPDATE)
    private long id;
    @ColumnDefault(value = "'empty'")
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
    public boolean equals(Object o)
    {
        boolean res = super.equals(o);
        if (res)
        {

        }
        else
        {
            res = ((UserEntity) o).getId() == getId();
        }
        return res;
    }

    @Override
    public String toString()
    {

        return "id: " + getId() + "\n" +
                "name: " + getName() + "\n" +
                "blocked: " + isBlocked();
    }
}
