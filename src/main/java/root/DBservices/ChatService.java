package root.DBservices;

import java.util.Optional;

import root.entitys.ChatEntity;

public interface ChatService
{
    public Optional<ChatEntity> findChatById(long id);

    public void cancelChat(ChatEntity chatEntity);

    public ChatEntity addChat(ChatEntity chatEntity);
}
