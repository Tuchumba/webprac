package ru.msu.cmc.webprac.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.webprac.models.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class UserDaoTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    void addUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1000L, "Гэри Джон Браннан", "garybrannan@gmail.com", "+44718979983"));
        userList.add(new User(1001L, "Марта Элизабет Браннан", "martha121212@hotmail.com", "+44818767009"));
        userList.add(new User(null, "Кристофер Френк Карандини Ли", "christopherlee@yahoo.com", "+44746551910"));
        userList.add(new User(128L, "Чарльз Спенсер Чаплин", "chaplin@chaplin.net", null));
        userList.add(new User("Айседора Дункан", "i.duncan@gmail.com"));
        userList.add(new User(null, "Спенсер Джонс", "spenser0jones@gmail.com", "+17338901991"));
        userDao.saveCollection(userList);
    }

    @AfterEach
    void eraseUsers() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE users RESTART IDENTITY CASCADE;").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void testGetAll() {
        List<User> userList = (List<User>) userDao.getAll();
        assertEquals(6, userList.size());
    }

    @Test
    void testGetMultiple() {
        List<User> userList = userDao.getAllUserByName("Спенсер");
        assertEquals(2, userList.size());
    }

    @Test
    void testId1() {
        User user = userDao.getById(1L);
        assertEquals(1, user.getId());
    }

    @Test
    void testTooBigId() {
        User user = userDao.getById(1000L);
        assertNull(user);
    }

    @Test
    void testEmail() {
        List<User> userList = userDao.getAllUserByName("Чаплин");
        assertEquals("chaplin@chaplin.net", userList.get(0).getEmail());
    }

    @Test
    void testPhone() {
        List<User> userList = userDao.getAllUserByName("Спенсер Джонс");
        assertEquals("+17338901991", userList.get(0).getPhone());
    }

    @Test
    void testUpdate() {
        User user = userDao.getById(1L);
        user.setPhone("+78005553535");
        userDao.update(user);
        assertEquals("+78005553535", userDao.getById(1L).getPhone());
    }

    @Test
    void testDelete() {
        List<User> users = userDao.getAllUserByName("Айседора Дункан");
        userDao.delete(users.get(0));
        assertNull(userDao.getAllUserByName("Айседора Дункан"));
    }

    @Test
    void testDeleteById() {
        userDao.deleteById(3L);
        assertNull(userDao.getById(3L));
    }
}