package root.DBservices.impls;

import org.springframework.beans.factory.annotation.Autowired;

import root.DBservices.RoleService;
import root.entitys.RoleEntity;
import root.repos.RolesRepository;

public class RoleServiceImpl implements RoleService
{
    @Autowired
    private RolesRepository rolesRepository;

    @Override
    public RoleEntity getUserRole()
    {
        return rolesRepository.findById(0L)
                .orElseGet(() ->
                {
                    RoleEntity roleEntity = new RoleEntity();
                    roleEntity.setId(0L);
                    roleEntity.setName("User");
                    return rolesRepository.save(roleEntity);
                });
    }

    @Override
    public RoleEntity getOperatorRole()
    {
        return rolesRepository.findById(1L)
                .orElseGet(() ->
                {
                    RoleEntity roleEntity = new RoleEntity();
                    roleEntity.setId(0L);
                    roleEntity.setName("User");
                    return rolesRepository.save(roleEntity);
                });
    }
}
