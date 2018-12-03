package root.DBservices;

import java.util.List;
import java.util.Optional;

import root.entitys.ChatEntity;
import root.entitys.RoleEntity;

public interface ChatService
{
    public Optional<ChatEntity> findChatById(long id);

    public void deleteChat(ChatEntity chatEntity);

    public ChatEntity addChat(ChatEntity chatEntity);

    public List<ChatEntity> findAll();

    List<ChatEntity> findAllWithRole(RoleEntity roleEntity);

    public void deleteAll();
}
