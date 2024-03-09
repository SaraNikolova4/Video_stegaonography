package com.example.proba6.Controller;

import com.example.proba6.Entity.Users;
import com.example.proba6.Exception.Login;
import com.example.proba6.Exception.Registration;
import com.example.proba6.Repository.UsersRepository;
import com.example.proba6.Service.UserService;
import com.example.proba6.config.Email;
import com.example.proba6.config.Encrypt;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UsersController{
    private final UserService userService;
    private final UsersRepository userRepository;
    private final ResourceLoader resourceLoader;

    private final Email emailSender;

    public UsersController(UserService userService, UsersRepository userRepository, ResourceLoader resourceLoader, Email emailSender) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.resourceLoader = resourceLoader;
        this.emailSender = emailSender;
    }

    @GetMapping("/login")
    public String login() {

        return "Login";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session)
    {
        Users users = (Users) session.getAttribute("account");
        session.removeAttribute("account");
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String log(@RequestParam String email,
                      @RequestParam String password,
                      Model model, HttpSession httpSession) {
        try {
            if (userService.login(email, password)) {
                String novpass = Encrypt.generatespassword(password, userRepository.findByEmail(email).get().getRandom());
                Users user = userRepository.findByEmailAndPassword(email, novpass);
                httpSession.setAttribute("account", user);
                model.addAttribute("username", user.getUsername());
                System.out.println(user.getUsername());
                return "Email";
            }
        } catch (Login e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "Login";
        }
        return "Email";
    }

    @GetMapping("/registration")
    public String registration()
    {

        return "Registration";
    }
    @PostMapping("/registration")
    public String registration(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String cpassword,
                               @RequestParam String phone, Model model) throws Registration {
        try {
            userService.registration(username, email, password, cpassword, phone);
        }
        catch (Registration ex)
        {
            model.addAttribute("errorMessage", ex.getMessage());
            return "Registration";
        }
        model.addAttribute("username", username);
        return "Email";
    }


}
