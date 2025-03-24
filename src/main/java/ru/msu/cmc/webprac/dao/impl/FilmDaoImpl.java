package ru.msu.cmc.webprac.dao.impl;

import ru.msu.cmc.webprac.dao.FilmDao;
import ru.msu.cmc.webprac.models.Film;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;

@Repository
public class FilmDaoImpl extends CommonDaoImpl<Film, Long> implements FilmDao {

    public FilmDaoImpl() {
        super(Film.class);
    }

    @Override
    public List<Film> getAllFilmByTitle(String filmTitle) {
        if (filmTitle == null || filmTitle.isBlank()) {
            return (List<Film>) getAll();
        }

        try (Session session = sessionFactory.openSession()) {
            Query<Film> query = session.createQuery("FROM films WHERE title LIKE :filmTitle", Film.class)
                    .setParameter("filmTitle", likeTemplate(filmTitle));
            return query.getResultList().size() == 0 ? null : query.getResultList();
        }
    }

    @Override
    public List<Film> getAllFilmByYear(Long from, Long to) {
        try (Session session = sessionFactory.openSession()) {
            if (from == null) {
                from = 0L;
            }

            if (to == null) {
                to = Long.MAX_VALUE;
            }

            Query<Film> query = session.createQuery("FROM films WHERE release_year BETWEEN :from AND :to", Film.class)
                    .setParameter("from", from)
                    .setParameter("to", to);
            return query.getResultList().size() == 0 ? null : query.getResultList();
        }
    }

    @Override
    public List<Film> getAllFilmByGenre(String genre) {
        if (genre == null || genre.isBlank()) {
            return (List<Film>) getAll();
        }

        try (Session session = sessionFactory.openSession()) {
            Query<Film> query = session.createQuery("FROM films WHERE genre LIKE :queryGenre", Film.class)
                    .setParameter("queryGenre", likeTemplate(genre));
            return query.getResultList().size() == 0 ? null : query.getResultList();
        }
    }

    @Override
    public List<Film> getAllFilmByCompany(String company) {
        if (company == null || company.isBlank()) {
            return (List<Film>) getAll();
        }

        try (Session session = sessionFactory.openSession()) {
            Query<Film> query = session.createQuery("FROM films WHERE company LIKE :queryCompany", Film.class)
                    .setParameter("queryCompany", likeTemplate(company));
            return query.getResultList().size() == 0 ? null : query.getResultList();
        }
    }

    @Override
    public List<Film> getAllFilmByDirector(String director) {
        if (director == null || director.isBlank()) {
            return (List<Film>) getAll();
        }

        try (Session session = sessionFactory.openSession()) {
            Query<Film> query = session.createQuery("FROM films WHERE director LIKE :queryDirector", Film.class)
                    .setParameter("queryDirector", likeTemplate(director));
            return query.getResultList().size() == 0 ? null : query.getResultList();
        }
    }

    @Override
    public List<Film> findFilm(String title, String company, String director, String year) {
        try(Session session = this.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Film> query = cb.createQuery(Film.class);
            Root<Film> root = query.from(Film.class);

            // create path for dynamic filter query
            List<Predicate> predicates = new ArrayList<>();
            if(title != null) {
                //create LIKE expr
                String sqlTitle = "%" + title + "%";

                predicates.add(cb.like(root.get("title"), sqlTitle));
            }
            if(company != null && company != "") {
                predicates.add(cb.equal(root.get("company"), company));
            }
            if(director != null && director != "") {
                predicates.add(cb.equal(root.get("director"), director));
            }
            if(year != null && year != "") {
                predicates.add(cb.equal(root.get("year_of_release"), year));
            }

            //run query
            query.select(root).where(cb.and(predicates.toArray(new Predicate[0])));

            return session.createQuery(query).getResultList();
        }
    }

    private String likeTemplate(String s) {
        return "%" + s + "%";
    }
}