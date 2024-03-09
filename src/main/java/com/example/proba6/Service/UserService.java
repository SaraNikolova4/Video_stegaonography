package com.example.proba6.Service;
import com.example.proba6.Entity.Users;
import com.example.proba6.Exception.Login;
import com.example.proba6.Exception.Registration;

public interface UserService {
    Users registration(String username, String email, String password, String rpassword, String phone) throws Registration;
    boolean check(String password);

    boolean login(String password, String email) throws Login;




}