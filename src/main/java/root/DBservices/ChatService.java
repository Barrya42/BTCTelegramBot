package root.DBservices;

import java.util.List;
import java.util.Optional;

import root.entitys.ChatEntity;

public interface ChatService
{
    public Optional<ChatEntity> findChatById(long id);

    public void deleteChat(ChatEntity chatEntity);

    public ChatEntity addChat(ChatEntity chatEntity);

    public List<ChatEntity> findAll();

    public void deleteAll();
}
