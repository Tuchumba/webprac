package ru.msu.cmc.webprac.controllers;

import ru.msu.cmc.webprac.dao.CopyDao;
import ru.msu.cmc.webprac.dao.UserDao;
import ru.msu.cmc.webprac.dao.FilmDao;
import ru.msu.cmc.webprac.dao.RentDao;
import ru.msu.cmc.webprac.dao.impl.CopyDaoImpl;
import ru.msu.cmc.webprac.dao.impl.UserDaoImpl;
import ru.msu.cmc.webprac.dao.impl.FilmDaoImpl;
import ru.msu.cmc.webprac.dao.impl.RentDaoImpl;
import ru.msu.cmc.webprac.models.Copy;
import ru.msu.cmc.webprac.models.User;
import ru.msu.cmc.webprac.models.Film;
import ru.msu.cmc.webprac.models.Rent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static ru.msu.cmc.webprac.models.Rent.RentMethod.PURCHASE;
import static ru.msu.cmc.webprac.models.Rent.RentMethod.RENT;

import static ru.msu.cmc.webprac.models.Copy.CopyType.*;

@Controller
public class RentController {
    
    @Autowired
    private final RentDao rentalDao = new RentDaoImpl();
    @Autowired
    private final UserDao clientDao = new UserDaoImpl();
    @Autowired
    private final CopyDao copyDao = new CopyDaoImpl();
    @Autowired
    private final FilmDao filmDao = new FilmDaoImpl();

    @GetMapping("/rentals")
    public String rentalsPage(Model model) {
        List<Rent> rentals = (List<Rent>) rentalDao.getAll();
        model.addAttribute("rentals", rentals);
        model.addAttribute("RENT", RENT);
        model.addAttribute("PURCHASE", PURCHASE);
        model.addAttribute("search", false);
        return "rentals";
    }

    @GetMapping("/rentalAdd")
    public String rentalAddPage(@RequestParam(name = "clientId") Integer clientId, Model model) {
        User client = clientDao.getById(clientId);
        List<Film> copies = (List<Film>) filmDao.getAll();

        if (client == null) {
            model.addAttribute("error_msg", "В базе нет криента с ID " + clientId);
            return "error";
        }

        if (copies.isEmpty()) {
            model.addAttribute("error_msg", "В базе нет ни одного фильма.");
            return "error";
        }

        model.addAttribute("client", client);
        model.addAttribute("films", copies);
        model.addAttribute("RENT", RENT);
        model.addAttribute("PURCHASE", PURCHASE);
        model.addAttribute("DVD", DVD);
        model.addAttribute("tape", tape);
        return "rentalAdd";
    }
    
    @GetMapping("/rentalSearch")
    public String rentalSearch(@RequestParam(name = "from", required = false) String fromTimeStr,
                               @RequestParam(name = "to", required = false) String toTimeStr,
                               Model model) {
        Timestamp fromTime, toTime;

        if (fromTimeStr == null || fromTimeStr.isBlank()) {
            fromTime = null;
        } else {
            LocalDateTime fromTimeLdt = LocalDateTime.parse(fromTimeStr);
            fromTime = Timestamp.valueOf(fromTimeLdt);
        }

        if (toTimeStr == null || toTimeStr.isBlank()) {
            toTime = null;
        } else {
            LocalDateTime toTimeLdt = LocalDateTime.parse(toTimeStr);
            toTime = Timestamp.valueOf(toTimeLdt);
        }

        List<Rent> res = rentalDao.getAllRentByPeriod(fromTime, toTime);

        if (res == null) {
            res = new ArrayList<>();
        }

        model.addAttribute("rentals", res);
        model.addAttribute("search", true);
        return "rentals";
    }
    
    @PostMapping("/rentalSave")
    public String rentalSave(@RequestParam(name = "clientId") Integer clientId,
                             @RequestParam(name = "filmId") Integer filmId,
                             @RequestParam(name = "rentalMethod") Rent.RentMethod rentalMethod,
                             @RequestParam(name = "type") Copy.CopyType type,
                             @RequestParam(name = "startTime") String startTimeStr) {
        User client = clientDao.getById(clientId);
        Film film = filmDao.getById(filmId);
        Copy copy = copyDao.findCopy(film, type).get(0);
        LocalDateTime startTimeLdt = LocalDateTime.parse(startTimeStr);
        Timestamp startTime = Timestamp.valueOf(startTimeLdt);
        Timestamp endTime;
        Long price;

        if (rentalMethod == RENT) {
            // every rent lasts a day (or 86_400_000 millis)
            endTime = new Timestamp(startTime.getTime() + TimeUnit.DAYS.toMillis(1));
            price = copy.getRent_price();
        } else {
            endTime = null;
            price = copy.getPurchase_price();
        }

        Rent rental = new Rent(null, copy, client, rentalMethod, startTime, endTime, price);
//        Rent rental = new Rent(null, film, client, startTime, endTime, price);
        rentalDao.save(rental);
        return "redirect:/rentals";
    }

    @PostMapping("/rentalDelete")
    public String rentalDelete(@RequestParam(name = "rentalId") Integer rentalId) {
        rentalDao.deleteById(rentalId);
        return "redirect:/rentals";
    }
}
