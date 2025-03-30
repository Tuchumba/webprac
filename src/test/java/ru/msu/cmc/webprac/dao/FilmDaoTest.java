package ru.msu.cmc.webprac.dao;

import ru.msu.cmc.webprac.models.Film;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class FilmDaoTest {

    @Autowired
    private FilmDao filmDao;
    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    void addFilms() {
        List<Film> films = new ArrayList<>();
        films.add(new Film("Закованная фильмой", "неизвестно", "Нептун", "Никанор Туркин", 1918L, ""));
        films.add(new Film("Трон", "научная фантастика", "Walt Disney Pictures", "Стивен Лисбергер", 1982L, ""));
        films.add(new Film("Звёздные войны: Эпизод 4 - Новая надежда", "фантастика",
                "Lucasfilm", "Джордж Лукас", 1977L, ""));
        films.add(new Film("Звёздные войны: Эпизод 5 - Империя наносит ответный удар", "фантастика",
                "Lucasfilm", "Джордж Лукас", 1981L, ""));
        films.add(new Film("Звёздные войны: Эпизод 6 - Возвращение джедая", "фантастика",
                "Lucasfilm", "Джордж Лукас", 1983L, ""));
        films.add(new Film("Аватар", "фантастика", "20th Century Fox", "Джеймс Кэмерон", 2009L, ""));
        films.add(new Film("Аватар: Путь воды", "фантастика", "Lightstorm Entertainment", "Джеймс Кэмерон", 2022L, ""));

        filmDao.saveCollection(films);
    }

    @AfterEach
    void eraseFilms() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE films RESTART IDENTITY CASCADE;").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void testId() {
        Film film = filmDao.getById(1);
        assertNotNull(film);
        assertEquals(1, film.getId());
    }

    @Test
    void testYear() {
        List<Film> films = filmDao.getAllFilmByYear(1900L, 1983L);
        assertEquals(5, films.size());

        films = filmDao.getAllFilmByYear(null, 1983L);
        assertEquals(5, films.size());

        films = filmDao.getAllFilmByYear(null, null);
        assertEquals(7, films.size());

        films = filmDao.getAllFilmByYear(2009L, null);
        assertEquals(2, films.size());

        films = filmDao.getAllFilmByYear(2025L, null);
        assertNull(films);
    }

    @Test
    void testTitle() {
        List<Film> films = filmDao.getAllFilmByTitle("Звёздные войны");
        assertEquals(3, films.size());

        films = filmDao.getAllFilmByTitle("Брат 2");
        assertNull(films);

        films = filmDao.getAllFilmByTitle(null);
        assertEquals(7, films.size());

        films = filmDao.getAllFilmByTitle("");
        assertEquals(7, films.size());


    }

    @Test
    void testCompany() {
        List<Film> films = filmDao.getAllFilmByCompany("Disney");
        assertEquals(1, films.size());

        films = filmDao.getAllFilmByCompany("Мосфильм");
        assertNull(films);

        films = filmDao.getAllFilmByCompany(null);
        assertEquals(7, films.size());

        films = filmDao.getAllFilmByCompany("");
        assertEquals(7, films.size());
    }

    @Test
    void testDirector() {
        List<Film> films = filmDao.getAllFilmByDirector("Кэмерон");
        assertEquals(2, films.size());

        films = filmDao.getAllFilmByDirector("Нолан");
        assertNull(films);

        films = filmDao.getAllFilmByDirector(null);
        assertEquals(7, films.size());

        films = filmDao.getAllFilmByDirector("");
        assertEquals(7, films.size());
    }

    @Test
    void testGenre() {
        List<Film> films = filmDao.getAllFilmByGenre("фантастика");
        assertEquals(6, films.size());

        films = filmDao.getAllFilmByGenre("ужасы");
        assertNull(films);

        films = filmDao.getAllFilmByGenre(null);
        assertEquals(7, films.size());

        films = filmDao.getAllFilmByGenre("");
        assertEquals(7, films.size());
    }

    @Test
    public void testFilter() {
        List<Film> filmList = filmDao.findFilm("Аватар", "", null, "");
        assertNotNull(filmList);
        assertEquals(2, filmList.size());

        List<Film> grFilms = filmDao.findFilm("", null, "Джордж Лукас", null);
        assertEquals(3, grFilms.size());

        List<Film> year = filmDao.findFilm(null, null, "", "1981");
        assertEquals(1, year.size());

        List<Film> dir_year = filmDao.findFilm(null, null, "Джордж Лукас", "1983");
        assertEquals("Звёздные войны: Эпизод 6 - Возвращение джедая", dir_year.get(0).getTitle());

        List<Film> company = filmDao.findFilm(null, "Walt Disney Pictures", null, null);
        assertEquals(1, company.size());

        List<Film> dop = filmDao.findFilm(null, null, null, null);
        assertEquals(7, dop.size());

        List<Film> dop1 = filmDao.findFilm("Начало", null, null, null);
        assertTrue(dop1.isEmpty());
    }
}
