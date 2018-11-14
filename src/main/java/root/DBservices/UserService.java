package root.DBservices;

import java.util.Optional;

import root.entitys.UserEntity;

public interface UserService
{
    Optional<UserEntity> findUserById(long id);
    UserEntity addUser(UserEntity userEntity);
    UserEntity blockUser(UserEntity userEntity);
    UserEntity unBlockUser(UserEntity userEntity);
}
