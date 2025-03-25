package ru.msu.cmc.webprac.dao;

import ru.msu.cmc.webprac.models.Copy;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.webprac.models.Film;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class CopyDaoTest {

    @Autowired
    private CopyDao copyDao;
    @Autowired
    private FilmDao filmDao;
    @Autowired
    private SessionFactory sessionFactory;
    List<Film> films;

    @BeforeEach
    void addCopies() {
        films = new ArrayList<>();
        films.add(new Film("Закованная фильмой", "неизвестно", "Нептун", "Никанор Туркин", 1918L, ""));
        films.add(new Film("Трон", "научная фантастика", "Walt Disney Pictures", "Стивен Лисбергер", 1982L, ""));
        films.add(new Film("Звёздные войны: Эпизод 4 - Новая надежда", "фантастика",
                "Lucasfilm", "Джордж Лукас", 1977L, ""));
        filmDao.saveCollection(films);

        List<Copy> copies = new ArrayList<>();
        copies.add(new Copy(films.get(0), Copy.CopyType.tape, Copy.RentStatus.reserved, 120L));

        copies.add(new Copy(films.get(1), Copy.CopyType.tape, Copy.RentStatus.free, 120L));

        copies.add(new Copy(films.get(2), Copy.CopyType.DVD, Copy.RentStatus.reserved, 180L));
        copies.add(new Copy(films.get(1), Copy.CopyType.DVD, Copy.RentStatus.free, 140L));

        copyDao.saveCollection(copies);
    }

    @AfterEach
    void eraseCopys() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE copies RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("TRUNCATE films RESTART IDENTITY CASCADE;").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    public void testFilter() {
        List<Copy> copyList = copyDao.findCopy(null, Copy.CopyType.DVD, null, null);
        assertEquals(2, copyList.size());

        List<Copy> fidList = copyDao.findCopy(films.get(1).getId(), null, null, null);
        assertEquals(2, fidList.size());

        copyList = copyDao.findCopy(null, null, Copy.RentStatus.free, 120);
        assertEquals(1, copyList.size());
    }

    @Test
    void testRentPrice() {
        List<Copy> copies = copyDao.getAllCopyByRentPrice(0L, 150L);
        assertEquals(3, copies.size());

        copies = copyDao.getAllCopyByRentPrice(null, null);
        assertEquals(4, copies.size());

        copies = copyDao.getAllCopyByRentPrice(140L, null);
        assertEquals(2, copies.size());

        copies = copyDao.getAllCopyByRentPrice(null, 160L);
        assertEquals(3, copies.size());

        copies = copyDao.getAllCopyByRentPrice(200L, 250L);
        assertNull(copies);

        copies = copyDao.getAllCopyByRentPrice(200L, null);
        assertNull(copies);

        copies = copyDao.getAllCopyByRentPrice(null, 100L);
        assertNull(copies);
    }
}