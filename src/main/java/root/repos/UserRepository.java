package root.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import root.entitys.RoleEntity;
import root.entitys.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>
{
    List<UserEntity> findAllByuserRole(RoleEntity roleEntity);
}
