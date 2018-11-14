package root.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import root.entitys.ChatEntity;


public interface ChatRepository extends JpaRepository<ChatEntity,Long>
{
}
