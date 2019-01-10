package unitTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import root.DBentitys.RoleEntity;
import root.DBentitys.UserEntity;
import root.DBservices.UserService;
import root.repos.UserRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)

public class UserServiceMethodsTest
{
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

//    @PersistenceContext
//    EntityManager em;

    @Before
    public void Setup()
    {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("test");
        roleEntity.setId(0L);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(12345L);
        userEntity.setUserRole(roleEntity);
        userEntity.setName("testUser");
        userRepository.save(userEntity);
    }

    @Test
    public void TestFindUserEntityByID()
    {
        Optional<UserEntity> userEntity = userRepository.findById(12345L);
        assertTrue(userEntity.isPresent());
    }
}
