package root.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import root.entitys.RoleEntity;

@Repository
public interface RolesRepository extends JpaRepository<RoleEntity, Long>
{
}
