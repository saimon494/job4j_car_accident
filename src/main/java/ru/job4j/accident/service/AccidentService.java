package ru.job4j.accident.service;

import org.springframework.stereotype.Service;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.repository.AccidentMem;

import java.util.Collection;
import java.util.stream.Stream;

@Service
public class AccidentService {

    private final AccidentMem rep;

    public AccidentService(AccidentMem rep) {
        this.rep = rep;
    }

    public Collection<Accident> findAllAccidents() {
        return rep.findAllAccidents();
    }

    public void save(Accident accident, String[] ruleIds) {
        rep.save(accident, Stream.of(ruleIds).mapToInt(Integer::parseInt).toArray());
    }

    public Accident findAccidentById(int id) {
        return rep.findAccidentById(id);
    }

    public Collection<AccidentType> findAllTypes() {
        return rep.findAllTypes();
    }

    public Collection<Rule> findAllRules() {
        return rep.findAllRules();
    }
}