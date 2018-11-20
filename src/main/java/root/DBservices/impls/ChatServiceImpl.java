package root.DBservices.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import root.DBservices.ChatService;
import root.entitys.ChatEntity;
import root.repos.ChatRepository;

@Service
public class ChatServiceImpl implements ChatService
{
    @Autowired
    private ChatRepository chatRepository;

    @Override
    public Optional<ChatEntity> findChatById(long id)
    {
        return chatRepository.findById(id);
    }

    @Override
    public void cancelChat(ChatEntity chatEntity)
    {
        chatRepository.delete(chatEntity);
    }

    @Override
    public ChatEntity addChat(ChatEntity chatEntity)
    {
        return chatRepository.save(chatEntity);
    }
}
