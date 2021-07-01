package ru.job4j.accident.service;

import org.springframework.stereotype.Service;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.repository.AccidentMem;

import java.util.Collection;

@Service
public class AccidentService {

    private final AccidentMem rep;

    public AccidentService(AccidentMem rep) {
        this.rep = rep;
    }

    public Collection<Accident> findAllAccidents() {
        return rep.findAllAccidents();
    }

    public void save(Accident accident) {
        rep.save(accident);
    }

    public Accident findAccidentById(int id) {
        return rep.findAccidentById(id);
    }

    public Collection<AccidentType> findAllAccidentTypes() {
        return rep.findAllAccidentTypes();
    }
}