package ru.msu.cmc.webprac.dao.impl;

import ru.msu.cmc.webprac.dao.UserDao;
import ru.msu.cmc.webprac.models.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl extends CommonDaoImpl<User, Long> implements UserDao {

    public UserDaoImpl() {
        super(User.class);
    }

    @Override
    public List<User> getAllUsersByName(String UserName) {
        if (UserName == null || UserName.isBlank()) {
            return (List<User>) getAll();
        }

        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("FROM users WHERE username LIKE :queryName", User.class)
                    .setParameter("queryName", likeTemplate(UserName));
            return query.getResultList().size() == 0 ? null : query.getResultList();
        }
    }

    @Override
    public List<User> getAllUsersByEmail(String email) {
        String sqlAddress = "%" + email + "%";
        try(Session session = this.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM users WHERE email LIKE :email", User.class)
                    .setParameter("email", sqlAddress);
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    }

    @Override
    public List<User> getAllUsersByPhone(String phone) {
        try(Session session = this.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM users WHERE phone = :phone", User.class)
                    .setParameter("phone", phone);
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    }

    private String likeTemplate(String s) {
        return "%" + s + "%";
    }
}