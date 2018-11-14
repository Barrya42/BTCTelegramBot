package root.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import root.entitys.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>
{
}
