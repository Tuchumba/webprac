package ru.msu.cmc.webprac.dao;

import ru.msu.cmc.webprac.models.Copy;
import ru.msu.cmc.webprac.models.User;
import ru.msu.cmc.webprac.models.Rent;
import ru.msu.cmc.webprac.models.Film;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class RentDaoTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private FilmDao filmDao;
    @Autowired
    private CopyDao copyDao;
    @Autowired
    private RentDao rentDao;
    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    void addRents() {
        List<User> userList = new ArrayList<>();
        userList.add(new User(null, "Гэри Джон Браннан", "garybrannan@gmail.com", "+44718979983"));
        userList.add(new User(null, "Марта Элизабет Браннан", "martha121212@hotmail.com", "+44818767009"));
        userDao.saveCollection(userList);

        List<Film> filmList = new ArrayList<>();
        filmList.add(new Film("Закованная фильмой", "неизвестно", "Нептун", "Никанор Туркин", 1918L, ""));
        filmList.add(new Film("Трон", "научная фантастика", "Walt Disney Pictures", "Стивен Лисбергер", 1982L, ""));
        filmList.add(new Film("Звёздные войны: Эпизод 4 - Новая надежда", "фантастика",
                "Lucasfilm", "Джордж Лукас", 1977L, ""));
        filmList.add(new Film("Звёздные войны: Эпизод 5 - Империя наносит ответный удар", "фантастика",
                "Lucasfilm", "Джордж Лукас", 1981L, ""));
        filmList.add(new Film("Звёздные войны: Эпизод 6 - Возвращение джедая", "фантастика",
                "Lucasfilm", "Джордж Лукас", 1983L, ""));
        filmList.add(new Film("Аватар", "фантастика", "20th Century Fox", "Джеймс Кэмерон", 2009L, ""));
        filmList.add(new Film("Аватар: Путь воды", "фантастика", "Lightstorm Entertainment", "Джеймс Кэмерон", 2022L, ""));

        filmDao.saveCollection(filmList);

        List<Copy> copyList = new ArrayList<>();
        copyList.add(new Copy(filmList.get(0),
                Copy.CopyType.tape, Copy.RentStatus.free, 1977L));
        copyList.add(new Copy(filmList.get(2), Copy.CopyType.DVD,
                Copy.RentStatus.free, 1981L));
        copyList.add(new Copy(filmList.get(3),
                Copy.CopyType.tape, Copy.RentStatus.free, 1983L));
        copyDao.saveCollection(copyList);



        List<Rent> rentList = new ArrayList<>();
        rentList.add(new Rent(copyList.get(0), userList.get(0),
                Timestamp.valueOf("2016-11-01 12:00:00"),
                Timestamp.valueOf("2016-11-02 12:00:00"),
                99L));
        rentList.add(new Rent(copyList.get(0), userList.get(1),
                Timestamp.valueOf("2016-11-01 12:03:11"),
                Timestamp.valueOf("2016-11-02 12:03:11"),
                99L));
        rentList.add(new Rent(copyList.get(0), userList.get(0),
                Timestamp.valueOf("2016-11-02 14:17:51"),
                Timestamp.valueOf("2016-11-03 14:17:51"),
                249L));
        rentList.add(new Rent(copyList.get(1), userList.get(0),
                Timestamp.valueOf("2016-11-02 14:20:03"),
                Timestamp.valueOf("2016-11-02 19:20:03"),
                249L));
        rentList.add(new Rent(copyList.get(2), userList.get(0),
                Timestamp.valueOf("2016-11-02 14:22:59"),
                Timestamp.valueOf("2016-11-04 14:20:03"),
                249L));
        rentDao.saveCollection(rentList);
    }

    @Test
    void testId() {
        Rent rent = rentDao.getById(1);
        assertEquals(1, rent.getId());
        assertEquals(1, rent.getCopy().getFilm_id().getId());
        assertEquals("Гэри Джон Браннан", rent.getUser().getUsername());
        assertEquals(Timestamp.valueOf("2016-11-01 12:00:00"), rent.getDate_of_transfer());
        assertEquals(Timestamp.valueOf("2016-11-02 12:00:00"), rent.getDate_of_receipt());
        assertEquals(99L, rent.getTransfer_amount());
    }

    @AfterEach
    void eraseRents() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE rents RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("TRUNCATE users RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("TRUNCATE copies RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("TRUNCATE films RESTART IDENTITY CASCADE;").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void testPeriod() {
        Timestamp ts1 = Timestamp.valueOf("2016-11-01 00:00:00");
        Timestamp ts2 = Timestamp.valueOf("2016-11-01 23:59:59");
        Timestamp ts3 = Timestamp.valueOf("2016-10-02 00:00:00");
        Timestamp ts4 = Timestamp.valueOf("2016-11-05 00:00:00");
        Timestamp ts5 = Timestamp.valueOf("2016-10-30 00:00:00");


        List<Rent> rents = rentDao.getAllRentByPeriod(ts1, ts2);
        assertEquals(2, rents.size());

        rents = rentDao.getAllRentByPeriod(ts1, null);
        assertEquals(5, rents.size());

        rents = rentDao.getAllRentByPeriod(null, ts2);
        assertEquals(2, rents.size());

        rents = rentDao.getAllRentByPeriod(null, null);
        assertEquals(5, rents.size());

        rents = rentDao.getAllRentByPeriod(null, ts3);
        assertNull(rents);

        rents = rentDao.getAllRentByPeriod(ts4, null);
        assertNull(rents);

        rents = rentDao.getAllRentByPeriod(ts3, ts5);
        assertNull(rents);
    }
}