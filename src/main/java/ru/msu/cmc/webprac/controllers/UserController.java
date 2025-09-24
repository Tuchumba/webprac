package ru.msu.cmc.webprac.controllers;

import ru.msu.cmc.webprac.dao.impl.UserDaoImpl;
import ru.msu.cmc.webprac.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.msu.cmc.webprac.dao.UserDao;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class UserController {

    @Autowired
    private final UserDao clientDao = new UserDaoImpl();

    @GetMapping("/clients")
    public String clientsPage(Model model) {
        List<User> clients = (List<User>) clientDao.getAll();
        model.addAttribute("clients", clients);
        model.addAttribute("search", false);
        return "clients";
    }

    @GetMapping("/clientInfo")
    public String clientInfoPage(@RequestParam(name = "clientId") Integer clientId, Model model) {
        User client = clientDao.getById(clientId);

        if (client == null) {
            model.addAttribute("error_msg", "В базе нет криента с ID " + clientId);
            return "error";
        }

        model.addAttribute("clientService", clientDao);
        model.addAttribute("client", client);
        return "clientInfo";
    }

    @GetMapping("/clientEdit")
    public String clientEditPage(@RequestParam(name = "clientId", required = false) Integer clientId, Model model) {
        if (clientId == null) {
            model.addAttribute("client", new User());
            return "clientEdit";
        }

        User client = clientDao.getById(clientId);

        if (client == null) {
            model.addAttribute("error_msg", "В базе нет клиента с ID " + clientId);
            return "error";
        }

        model.addAttribute("client", client);
        return "clientEdit";
    }

    @GetMapping("/clientSearch")
    public String clientSearch(@RequestParam(name = "fullName", required = false) String fullName,
                               Model model) {
        List<User> res = clientDao.getAllUsersByName(fullName);

        if (res == null) {
            res = new ArrayList<>();
        }

        model.addAttribute("clients", res);
        model.addAttribute("search", true);
        return "clients";
    }

    @PostMapping("/clientSave")
    public String clientSave(@RequestParam(name = "clientId") Integer clientId,
                             @RequestParam(name = "fullName") String fullName,
                             @RequestParam(name = "email") String email,
                             @RequestParam(name = "phone") String phone) {
//        clientDao.deleteById(clientId);
        User client = new User(clientId, fullName, email, phone);
        clientDao.update(client);
        return String.format("redirect:/clientInfo?clientId=%d", client.getId());
    }

    @PostMapping("/clientDelete")
    public String clientDelete(@RequestParam(name = "clientId") Integer clientId) {
        clientDao.deleteById(clientId);
        return "redirect:/clients";
    }
}
