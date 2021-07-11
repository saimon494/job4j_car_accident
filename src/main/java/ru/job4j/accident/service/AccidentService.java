package ru.job4j.accident.service;

import org.springframework.stereotype.Service;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.repository.AccidentHbm;

import java.util.Collection;
import java.util.function.Consumer;

@Service
public class AccidentService {

    private final AccidentHbm store;

    public AccidentService(AccidentHbm store) {
        this.store = store;
    }

    public Collection<Accident> findAllAccidents() {
        return store.findAllAccidents();
    }

    public void save(Accident accident, String[] rIds) {
        saveUpdate(accident, rIds, accident1 -> store.save(accident));
    }

    public void update(Accident accident, String[] rIds) {
        saveUpdate(accident, rIds, accident1 -> store.update(accident));
    }

    private void saveUpdate(Accident accident, String[] rIds, Consumer<Accident> cons) {
        AccidentType type = store.findTypeById(accident.getType().getId()).get();
        accident.setType(type);
        for (String rId : rIds) {
            accident.addRule(store.findRuleById(Integer.parseInt(rId)).get());
        }
        cons.accept(accident);
    }

    public void delete(int id) {
        store.deleteAccidentById(id);
    }

    public Accident findAccidentById(int id) {
        return store.findAccidentById(id).orElse(null);
    }

    public Collection<AccidentType> findAllTypes() {
        return store.findAllTypes();
    }

    public Collection<Rule> findAllRules() {
        return store.findAllRules();
    }
}