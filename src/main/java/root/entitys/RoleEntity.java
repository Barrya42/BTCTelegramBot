package root.entitys;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RoleEntity
{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    public RoleEntity()
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
}
