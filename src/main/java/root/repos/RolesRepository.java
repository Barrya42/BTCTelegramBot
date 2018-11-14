package root.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import root.entitys.RoleEntity;

public interface RolesRepository extends JpaRepository<RoleEntity, Long>
{
}
