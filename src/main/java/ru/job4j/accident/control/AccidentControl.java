package ru.job4j.accident.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.service.AccidentService;

import javax.servlet.http.HttpServletRequest;
import java.util.function.BiConsumer;

@Controller
public class AccidentControl {

    private final AccidentService accidentService;

    public AccidentControl(AccidentService accidentService) {
        this.accidentService = accidentService;
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("types", accidentService.findAllTypes());
        model.addAttribute("rules", accidentService.findAllRules());
        return "accident/create";
    }

    @GetMapping("/update")
    public String update(@RequestParam("id") int id, Model model) {
        model.addAttribute("accident", accidentService.findAccidentById(id));
        model.addAttribute("types", accidentService.findAllTypes());
        model.addAttribute("rules", accidentService.findAllRules());
        return "accident/update";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") int id) {
        accidentService.delete(id);
        return "redirect:/";
    }

    private String saveUpdate(Accident accident, HttpServletRequest request,
                              BiConsumer<Accident, String[]> biCons) {
        String[] rIds = request.getParameterValues("rIds");
        biCons.accept(accident, rIds);
        return "redirect:/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Accident accident, HttpServletRequest request) {
        return saveUpdate(accident, request, accidentService::save);
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Accident accident, HttpServletRequest request) {
        return saveUpdate(accident, request, accidentService::update);
    }
}
