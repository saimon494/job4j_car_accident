package ru.job4j.accident.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;

import java.util.List;
import java.util.Optional;

@Repository
public class AccidentHbm {
    private final SessionFactory sf;

    public AccidentHbm(SessionFactory sf) {
        this.sf = sf;
    }

    public Accident save(Accident accident) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(accident);
            session.getTransaction().commit();
        }
        return accident;
    }

    public boolean update(Accident accident) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.update(accident);
            session.getTransaction().commit();
        }
        return true;
    }

    public boolean deleteAccidentById(int id) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.delete(session.load(Accident.class, id));
            session.getTransaction().commit();
        }
        return true;
    }

    public List<Accident> findAllAccidents() {
        try (Session session = sf.openSession()) {
            return session.createQuery(
                    "select distinct a from Accident a "
                            + "join fetch a.type "
                            + "left join fetch a.rules order by a.id",
                    Accident.class).list();
        }
    }

    public Optional<Accident> findAccidentById(int id) {
        Accident accident;
        try (Session session = sf.openSession()) {
            accident = session
                    .createQuery(
                            "select distinct a from Accident a "
                                    + "join fetch a.type "
                                    + "left join fetch a.rules where a.id =:paramId",
                            Accident.class)
                    .setParameter("paramId", id)
                    .getSingleResult();
        }
        return accident == null ? Optional.empty() : Optional.of(accident);
    }

    public List<AccidentType> findAllTypes() {
        try (Session session = sf.openSession()) {
            return session
                    .createQuery("select t from AccidentType t", AccidentType.class)
                    .list();
        }
    }

    public Optional<AccidentType> findTypeById(int id) {
        AccidentType type;
        try (Session session = sf.openSession()) {
            type = session
                    .createQuery(
                            "select t from AccidentType t "
                                    + "where t.id =:paramId",
                            AccidentType.class)
                    .setParameter("paramId", id)
                    .getSingleResult();
        }
        return type == null ? Optional.empty() : Optional.of(type);
    }

    public List<Rule> findAllRules() {
        try (Session session = sf.openSession()) {
            return session
                    .createQuery("select r from Rule r", Rule.class)
                    .list();
        }
    }

    public Optional<Rule> findRuleById(int id) {
        Rule rule;
        try (Session session = sf.openSession()) {
            rule = session
                    .createQuery(
                            "select r from Rule r "
                                    + "where r.id =:paramId",
                            Rule.class)
                    .setParameter("paramId", id)
                    .getSingleResult();
        }
        return rule == null ? Optional.empty() : Optional.of(rule);
    }
}
