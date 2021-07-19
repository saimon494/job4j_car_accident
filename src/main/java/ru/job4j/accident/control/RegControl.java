package ru.job4j.accident.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.accident.model.User;
import ru.job4j.accident.service.UserService;

@Controller
public class RegControl {

    private final UserService userService;

    public RegControl(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/reg")
    public String reg(Model model) {
        return "reg";
    }

    @PostMapping("/reg")
    public String save(@ModelAttribute User user, Model model) {
        String errorMessage;
        if (userService.findUser(user.getUsername()) != null) {
            errorMessage = "Пользователь уже существует";
            model.addAttribute("errorMessage", errorMessage);
            return "reg";
        }
        userService.saveUser(user);
        return "redirect:/login";
    }
}
