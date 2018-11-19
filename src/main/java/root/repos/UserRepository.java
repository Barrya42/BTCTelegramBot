package root.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import root.entitys.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>
{
}
