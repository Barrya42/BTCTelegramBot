package root.DBservices;

import java.util.List;
import java.util.Optional;

import root.entitys.RoleEntity;
import root.entitys.UserEntity;

public interface UserService
{
    public Optional<UserEntity> findUserById(long id);

    public UserEntity addUser(UserEntity userEntity);

    public UserEntity blockUser(UserEntity userEntity);

    public UserEntity unBlockUser(UserEntity userEntity);

    public List<UserEntity> findAllWithRole(RoleEntity roleEntity);

    public void deleteUser(UserEntity userEntity);

    public void deleteUser(long id);
}
