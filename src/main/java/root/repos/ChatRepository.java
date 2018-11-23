package root.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import root.entitys.ChatEntity;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long>
{
}
