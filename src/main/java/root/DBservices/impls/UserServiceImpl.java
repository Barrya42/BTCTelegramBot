package root.DBservices.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import root.DBentitys.RoleEntity;
import root.DBentitys.UserEntity;
import root.DBservices.UserService;
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
        return userRepository.save(userEntity);
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

    @Override
    public List<UserEntity> findAllWithRole(RoleEntity roleEntity)
    {
        return userRepository.findAllByuserRole(roleEntity);
    }

    @Override
    public void deleteUser(UserEntity userEntity)
    {
        userRepository.deleteById(userEntity.getId());
    }

    @Override
    public void deleteUser(long id)
    {
        userRepository.deleteById(id);
    }
}
