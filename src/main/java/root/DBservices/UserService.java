package root.DBservices;

import java.util.Optional;

import root.entitys.UserEntity;

public interface UserService
{
    public Optional<UserEntity> findUserById(long id);

    public UserEntity addUser(UserEntity userEntity);

    public UserEntity blockUser(UserEntity userEntity);

    public UserEntity unBlockUser(UserEntity userEntity);
}
