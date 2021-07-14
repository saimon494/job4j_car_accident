package ru.job4j.accident.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.Type;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.repository.AccidentRepository;
import ru.job4j.accident.repository.RuleRepository;
import ru.job4j.accident.repository.TypeRepository;

import java.util.Collection;
import java.util.function.Consumer;

@Service
public class AccidentService {

    private final AccidentRepository accidentRepository;
    private final RuleRepository ruleRepository;
    private final TypeRepository typeRepository;

    public AccidentService(AccidentRepository accidentRepository,
                           RuleRepository ruleRepository,
                           TypeRepository typeRepository) {
        this.accidentRepository = accidentRepository;
        this.ruleRepository = ruleRepository;
        this.typeRepository = typeRepository;
    }

    public Collection<Accident> findAllAccidents() {
        return accidentRepository.findAllAccidents();
    }

    @Transactional
    public void save(Accident accident, String[] rIds) {
        saveUpdate(accident, rIds, accident1 -> accidentRepository.save(accident));
    }

    private void saveUpdate(Accident accident, String[] rIds, Consumer<Accident> cons) {
        Type type = typeRepository.findById(accident.getType().getId()).orElse(null);
        accident.setType(type);
        for (String rId : rIds) {
            Rule rule = ruleRepository.findById(Integer.parseInt(rId)).orElse(null);
            accident.addRule(rule);
        }
        cons.accept(accident);
    }

    @Transactional
    public void delete(int id) {
        accidentRepository.delete(findAccidentById(id));
    }

    @Transactional
    public Accident findAccidentById(int id) {
        return accidentRepository.findAccidentById(id).orElse(null);
    }

    @Transactional
    public Collection<Type> findAllTypes() {
        return (Collection<Type>) typeRepository.findAll();
    }

    @Transactional
    public Collection<Rule> findAllRules() {
        return (Collection<Rule>) ruleRepository.findAll();
    }
}