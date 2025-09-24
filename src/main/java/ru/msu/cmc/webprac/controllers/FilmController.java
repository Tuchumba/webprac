package ru.msu.cmc.webprac.controllers;

import ru.msu.cmc.webprac.dao.impl.FilmDaoImpl;
import  ru.msu.cmc.webprac.models.Film;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import  ru.msu.cmc.webprac.dao.FilmDao;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class FilmController {

    Logger logger = LoggerFactory.getLogger(FilmController.class);

    @Autowired
    private final FilmDao filmDao = new FilmDaoImpl();

    @GetMapping("/films")
    public String filmsPage(Model model) {
        List<Film> films = (List<Film>) filmDao.getAll();
        model.addAttribute("films", films);
        model.addAttribute("search", false);
        return "films";
    }

    @GetMapping("/filmInfo")
    public String filmInfoPage(@RequestParam(name = "filmId") Integer filmId, Model model) {
        Film film = filmDao.getById(filmId);

        if (film == null) {
            model.addAttribute("error_msg", "В базе нет фильма с ID " + filmId);
            return "error";
        }

        model.addAttribute("filmService", filmDao);
        model.addAttribute("film", film);
        return "filmInfo";
    }

    @GetMapping("/filmEdit")
    public String filmEditPage(@RequestParam(name = "filmId", required = false) Integer filmId, Model model) {
        if (filmId == null) {
            model.addAttribute("film", new Film());
            return "filmEdit";
        }

        Film film = filmDao.getById(filmId);

        if (film == null) {
            model.addAttribute("error_msg", "В базе нет фильма с ID " + filmId);
            return "error";
        }

        model.addAttribute("film", film);
        return "filmEdit";
    }

    @GetMapping("/filmSearch")
    public String filmSearch(@RequestParam(name = "title", required = false) String title,
                             @RequestParam(name = "genre", required = false) String genre,
                             @RequestParam(name = "company", required = false) String company,
                             @RequestParam(name = "director", required = false) String director,
                             @RequestParam(name = "releaseYearFrom", required = false) Long releaseYearFrom,
                             @RequestParam(name = "releaseYearTo", required = false) Long releaseYearTo,
                             Model model) {
        List<Film> res = filmDao.getAllFilmByTitle(title);
        logger.info("1:" + res.size());
        res = myRetainAll(res, filmDao.getAllFilmByGenre(genre));
        logger.info("2:" + res.size());
        res = myRetainAll(res, filmDao.getAllFilmByCompany(company));
        logger.info("3:" + res.size());
        res = myRetainAll(res, filmDao.getAllFilmByDirector(director));
        logger.info("4:" + res.size());
        res = myRetainAll(res, filmDao.getAllFilmByYear(releaseYearFrom, releaseYearTo));
        logger.info("5:" + res.size());
        model.addAttribute("films", res);
        model.addAttribute("search", true);
        return "films";
    }

    @PostMapping("/filmSave")
    public String filmSave(@RequestParam(name = "title") String title,
                           @RequestParam(name = "genre") String genre,
                           @RequestParam(name = "company") String company,
                           @RequestParam(name = "director") String director,
                           @RequestParam(name = "releaseYear") Long releaseYear,
                           @RequestParam(name = "description") String description
                           ) {
        Film film = new Film(null, title, genre, company, director, releaseYear, description);
        filmDao.save(film);
        return String.format("redirect:/filmInfo?filmId=%d", film.getId());
    }

    @PostMapping("/filmDelete")
    public String filmDelete(@RequestParam(name = "filmId") Integer filmId) {
        filmDao.deleteById(filmId);
        return "redirect:/films";
    }

    private List<Film> myRetainAll(Collection<Film> a, Collection<Film> b) {
        if (a == null || b == null) {
            return new ArrayList<>();
        }

        List<Film> res = new ArrayList<>();
        Map<Integer, Film> filmMapA = new HashMap<>();
        Map<Integer, Film> filmMapB = new HashMap<>();

        for (Film elem_a : a) {
            filmMapA.put(elem_a.getId(), elem_a);
        }

        for (Film elem_b : b) {
            filmMapB.put(elem_b.getId(), elem_b);
        }

        for (Integer id : filmMapA.keySet()) {
            if (filmMapB.containsKey(id)) {
                res.add(filmMapA.get(id));
            }
        }

        return res;
    }
}