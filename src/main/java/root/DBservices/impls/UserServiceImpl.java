package root.DBservices.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import root.DBservices.UserService;
import root.entitys.UserEntity;
import root.repos.UserRepository;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    UserRepository userRepository;

    @Override
    public Optional<UserEntity> findUserById(long id)
    {
        return userRepository.findById(id);
    }

    @Override
    public UserEntity addUser(UserEntity userEntity)
    {
        return null;
    }

    @Override
    public UserEntity blockUser(UserEntity userEntity)
    {
        return null;
    }

    @Override
    public UserEntity unBlockUser(UserEntity userEntity)
    {
        return null;
    }
}
