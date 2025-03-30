package ru.msu.cmc.webprac.dao;

import ru.msu.cmc.webprac.models.Film;

import java.util.List;

public interface FilmDao extends CommonDao<Film, Integer> {

    List<Film> getAllFilmByTitle(String filmTitle);
    List<Film> getAllFilmByYear(Long from, Long to);
    List<Film> getAllFilmByGenre(String genre);
    List<Film> getAllFilmByCompany(String companyName);
    List<Film> getAllFilmByDirector(String director);

    List<Film> findFilm(String title, String company, String director, String year);
}