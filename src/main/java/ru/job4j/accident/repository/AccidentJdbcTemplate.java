package ru.job4j.accident.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;

import java.sql.PreparedStatement;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class AccidentJdbcTemplate {

    private final JdbcTemplate jdbc;

    public AccidentJdbcTemplate(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Accident save(Accident accident) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into accident(name, text, address, type_id) values(?, ?, ?, ?)",
                    new String[]{"id"});
            ps.setString(1, accident.getName());
            ps.setString(2, accident.getText());
            ps.setString(3, accident.getAddress());
            ps.setInt(4, accident.getType().getId());
            return ps;
        }, keyHolder);
        accident.setId((int) keyHolder.getKey());
        for (Rule rule : accident.getRules()) {
            jdbc.update(
                    "insert into accident_rule (accident_id, rule_id) values (?, ?)",
                    accident.getId(), rule.getId());
        }
        return accident;
    }

    public boolean update(Accident accident) {
        int updated = jdbc.update(
                "update accident set name = ?, text = ?, address = ?, type_id = ? where id = ?",
                accident.getName(),
                accident.getText(),
                accident.getAddress(),
                accident.getType().getId(),
                accident.getId());
        int deleted = jdbc.update(
                "delete from accident_rule where accident_id = ?",
                accident.getId());
        int inserted = 0;
        for (Rule rule : accident.getRules()) {
            inserted += jdbc.update(
                    "insert into accident_rule (accident_id, rule_id) values (?, ?)",
                    accident.getId(),
                    rule.getId());
        }
        return updated > 0 && deleted > 0 && inserted > 0;
    }

    public List<Accident> findAllAccidents() {
        List<Accident> rsl = jdbc.query("select a.id as accident_id, a.name as accident_name, "
                        + "a.text, a.address, a.type_id, t.name as type_name from accident as a "
                        + "left join type as t on a.type_id = t.id",
                (rs, row) -> {
                    var accident = new Accident();
                    accident.setId(rs.getInt("accident_id"));
                    accident.setName(rs.getString("accident_name"));
                    accident.setText(rs.getString("text"));
                    accident.setAddress(rs.getString("address"));
                    var type = new AccidentType();
                    type.setId(rs.getInt("type_id"));
                    type.setName(rs.getString("type_name"));
                    accident.setType(type);
                    return accident;
                });
        for (Accident accident : rsl) {
            List<Rule> rules = jdbc.query("select * from accident_rule as a "
                            + "left join rule as r on a.rule_id = r.id "
                            + "where a.accident_id = ?",
                    (rs, row) -> {
                        var rule = new Rule();
                        rule.setId(rs.getInt("id"));
                        rule.setName(rs.getString("name"));
                        return rule;
                    }, accident.getId());
            for (Rule rule : rules) {
                accident.addRule(rule);
            }
        }
        rsl.sort(Comparator.comparing(Accident::getId));
        return rsl;
    }

    public Optional<Accident> findAccidentById(int id) {
        Accident accident = jdbc.query("select a.id as accident_id, a.name as accident_name, "
                        + "a.text, a.address, a.type_id, t.name as type_name from accident as a "
                        + "left join type as t on a.type_id = t.id where a.id = ?",
                rs -> {
                    if (!rs.next()) {
                        return null;
                    }
                    Accident acc = new Accident();
                    acc.setId(rs.getInt("accident_id"));
                    acc.setName(rs.getString("accident_name"));
                    acc.setText(rs.getString("text"));
                    acc.setAddress(rs.getString("address"));
                    var type = new AccidentType();
                    type.setId(rs.getInt("type_id"));
                    type.setName(rs.getString("type_name"));
                    acc.setType(type);
                    return acc;
                }, id);
        if (accident == null) {
            return Optional.empty();
        }
        List<Rule> rules = jdbc.query("select * from accident_rule as a "
                        + "left join rule as r on a.rule_id = r.id "
                        + "where a.accident_id = ?",
                (rs, row) -> {
                    Rule rule = new Rule();
                    rule.setId(rs.getInt("id"));
                    rule.setName(rs.getString("name"));
                    return rule;
                }, accident.getId());
        for (Rule rule : rules) {
            accident.addRule(rule);
        }
        return Optional.of(accident);
    }

    public List<AccidentType> findAllTypes() {
        return jdbc.query(
                "select * from type",
                (rs, rowNum) -> {
                    var type = new AccidentType();
                    type.setId(rs.getInt("id"));
                    type.setName(rs.getString("name"));
                    return type;
                });
    }

    public Optional<AccidentType> findTypeById(int id) {
        return jdbc.query("select * from type where id = ?",
                rs -> {
                    if (!rs.next()) {
                        return Optional.empty();
                    }
                    var type = new AccidentType();
                    type.setId(rs.getInt("id"));
                    type.setName(rs.getString("name"));
                    return Optional.of(type);
                }, id);
    }

    public List<Rule> findAllRules() {
        return jdbc.query(
                "select * from rule",
                (rs, rowNum) -> {
                    var rule = new Rule();
                    rule.setId(rs.getInt("id"));
                    rule.setName(rs.getString("name"));
                    return rule;
                });
    }

    public Optional<Rule> findRuleById(int id) {
        return jdbc.query("select * from rule where id = ?",
                rs -> {
                    if (!rs.next()) {
                        return Optional.empty();
                    }
                    var rule = new Rule();
                    rule.setId(rs.getInt("id"));
                    rule.setName(rs.getString("name"));
                    return Optional.of(rule);
                }, id);
    }
}
