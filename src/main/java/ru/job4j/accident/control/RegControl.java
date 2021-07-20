package ru.job4j.accident.control;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.accident.model.User;
import ru.job4j.accident.service.UserService;

@Controller
public class RegControl {

    private final UserService userService;

    public RegControl(UserService userService) {
        this.userService = userService;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolationException() {
        return "redirect:/reg?error=true";
    }

    @GetMapping("/reg")
    public String reg(@RequestParam(value = "error", required = false) String error,
                      Model model) {
        String errorMessage = null;
        if (error != null) {
            errorMessage = "Пользователь уже существует";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "reg";
    }

    @PostMapping("/reg")
    public String save(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/login";
    }
}
