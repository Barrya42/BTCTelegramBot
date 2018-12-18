package root.DBservices.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.DBentitys.RoleEntity;
import root.DBservices.RoleService;
import root.repos.RolesRepository;

@Service
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
                    roleEntity.setId(1L);
                    roleEntity.setName("Operator");
                    return rolesRepository.save(roleEntity);
                });
    }
}
