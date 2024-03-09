package com.example.proba6.Service;
import com.example.proba6.Entity.Users;
import com.example.proba6.Exception.Login;
import com.example.proba6.Exception.Registration;
import com.example.proba6.Repository.UsersRepository;
import com.example.proba6.config.Email;
import com.example.proba6.config.Encrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    public final UsersRepository repository;
    private final Email emailSender;

    public UserServiceImpl(UsersRepository repository, Email emailSender) {
        this.repository = repository;
        this.emailSender = emailSender;
    }

    public boolean check(String pass) {
        int proverka = 0;
        for (char c : pass.toCharArray()) {
            if (Character.isUpperCase(c)) {
                proverka++;
            }
        }
        for (char c : pass.toCharArray()) {
            if (c == '!' || c == '?' || c == '@' || c == '#') {
                proverka++;
            }
        }
        for (char c : pass.toCharArray()) {
            if (Character.isDigit(c)) {
                proverka++;
            }
        }
        if (proverka >= 3) return true;
        else return false;
    }

    public Users registration(String username, String email, String password, String rpassword, String phone) throws Registration {

        List<Users> usersList = repository.findAll();
        for(Users u : usersList)
        {
            if(u.getEmail().equals(email))
            {
                throw new Registration("This email is already in use!");
            }
        }
        if (username.isEmpty() || username == null) {
            throw new Registration("You must enter a username!");
        }
        if (password.isEmpty() || password == null) {
            throw new Registration("You must enter a username!");
        }
        if (phone.isEmpty() || phone == null) {
            throw new Registration("You must enter a phone!");
        }
        if (email.isEmpty() || email == null) {
            throw new Registration("You must enter a email!");
        }

        if (!Objects.equals(password, rpassword)) {
            throw new Registration("Passwords is not same!");
        }
        boolean proveri = check(password);
        if (proveri) {
            Users novuser = new Users(username, email, password, phone);
            emailSender.sendemail(email);
            return repository.save(novuser);
        } else {
            throw new Registration("Password is not secure!");
        }


    }

    public boolean login(String email, String password) throws Login {

        if (email.isEmpty() || email == null) {
            throw new Login("You must enter a email");
        }
        if(password.isEmpty() || password == null)
        {
            throw new Login("You must enter a password");
        }
        Users u = (Users) repository.findByEmail(email)
                .orElseThrow(() -> new Login("No found user with this email"));
        if(u != null)
        {
            String pass = Encrypt.generatespassword(password, repository.findByEmail(email).get().getRandom());
            if(!u.getPassword().equals(pass))
            {
                throw new Login("The password is incorrect");
            }
        }
        String novpass = Encrypt.generatespassword(password, repository.findByEmail(email).get().getRandom());
        Users s = repository.findByEmailAndPassword(email, novpass);

        if (s == null) {
            throw new Login("User not found");
        } else {
            emailSender.sendemail(email);
            return true;
        }
    }

}