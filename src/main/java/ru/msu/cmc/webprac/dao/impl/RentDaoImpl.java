package ru.msu.cmc.webprac.dao.impl;

import ru.msu.cmc.webprac.dao.RentDao;
import ru.msu.cmc.webprac.models.Rent;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class RentDaoImpl extends CommonDaoImpl<Rent, Integer> implements RentDao {

    public RentDaoImpl() {
        super(Rent.class);
    }

    public List<Rent> getAllRentByPeriod(Timestamp from, Timestamp to) {
        try (Session session = sessionFactory.openSession()) {
            if (to == null && from == null) {
                return (List<Rent>) getAll();
            } else if (to == null) {
                Query<Rent> query = session.createQuery("FROM Rent WHERE start_time >= :queryFrom", Rent.class)
                        .setParameter("queryFrom", from);
                return query.getResultList().size() == 0 ? null : query.getResultList();
            } else if (from == null) {
                Query<Rent> query = session.createQuery("FROM Rent WHERE start_time <= :queryTo", Rent.class)
                        .setParameter("queryTo", to);
                return query.getResultList().size() == 0 ? null : query.getResultList();
            } else {
                Query<Rent> query = session.createQuery("FROM Rent WHERE start_time BETWEEN :queryFrom AND :queryTo", Rent.class)
                        .setParameter("queryFrom", from)
                        .setParameter("queryTo", to);
                return query.getResultList().size() == 0 ? null : query.getResultList();
            }
        }
    }
}