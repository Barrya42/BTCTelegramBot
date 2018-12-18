package root.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import root.DBentitys.RoleEntity;
import root.DBentitys.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>
{
    List<UserEntity> findAllByuserRole(RoleEntity roleEntity);
}
