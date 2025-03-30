package ru.msu.cmc.webprac.dao;

import ru.msu.cmc.webprac.models.User;

import java.util.List;

public interface UserDao extends CommonDao<User, Integer> {

    List<User> getAllUsersByName(String username);
    List<User> getAllUsersByPhone(String phone);
    List<User> getAllUsersByEmail(String email);
}