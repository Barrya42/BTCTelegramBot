package root.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import root.DBentitys.RoleEntity;

@Repository
public interface RolesRepository extends JpaRepository<RoleEntity, Long>
{
}
