package root.DBservices.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import root.DBentitys.ChatEntity;
import root.DBentitys.RoleEntity;
import root.DBservices.ChatService;
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
    public void deleteChat(ChatEntity chatEntity)
    {
        chatRepository.delete(chatEntity);
    }

    @Override
    public ChatEntity saveChat(ChatEntity chatEntity)
    {
        return chatRepository.save(chatEntity);
    }

    @Override
    public List<ChatEntity> findAll()
    {
        return chatRepository.findAll();
    }

    @Override
    public List<ChatEntity> findAllWithRole(RoleEntity roleEntity)
    {
        return chatRepository.findAll()
                .stream()
                .filter(chatEntity ->
                {
                    return chatEntity.getUserEntity()
                            .getUserRole()
                            .equals(roleEntity);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAll()
    {
        chatRepository.deleteAll();
    }
}
