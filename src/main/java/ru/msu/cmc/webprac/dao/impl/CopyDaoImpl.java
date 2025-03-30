package ru.msu.cmc.webprac.dao.impl;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.msu.cmc.webprac.dao.CopyDao;
import ru.msu.cmc.webprac.models.Copy;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class CopyDaoImpl extends CommonDaoImpl<Copy, Integer> implements CopyDao {
    public CopyDaoImpl() {
        super(Copy.class);
    }

    @Override
    public List<Copy> findCopy(Integer filmId, Copy.CopyType type, Copy.RentStatus status, Integer price) {
        try(Session session = this.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Copy> query = cb.createQuery(Copy.class);
            Root<Copy> root = query.from(Copy.class);

            // create path for dynamic filter query
            List<Predicate> predicates = new ArrayList<>();
            if (filmId != null) {
                predicates.add(cb.equal(root.get("film_id"), filmId));
            }
            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }
            if(status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (price != null) {
                predicates.add(cb.equal(root.get("price"), price));
            }

            //run query
            query.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public List<Copy> getAllCopyByRentPrice(Long from, Long to) {
        try (Session session = sessionFactory.openSession()) {
            if (from == null && to == null) {
                return (List<Copy>) getAll();
            } else if (to == null) {
                Query<Copy> query = session.createQuery("FROM Copy WHERE price >= :from", Copy.class)
                        .setParameter("from", from);
                return query.getResultList().isEmpty() ? null : query.getResultList();
            } else if (from == null) {
                Query<Copy> query = session.createQuery("FROM Copy WHERE price <= :to", Copy.class)
                        .setParameter("to", to);
                return query.getResultList().isEmpty() ? null : query.getResultList();
            } else {
                Query<Copy> query = session.createQuery("FROM Copy WHERE price BETWEEN :from AND :to", Copy.class)
                        .setParameter("from", from)
                        .setParameter("to", to);
                return query.getResultList().isEmpty() ? null : query.getResultList();
            }
        }
    }
}