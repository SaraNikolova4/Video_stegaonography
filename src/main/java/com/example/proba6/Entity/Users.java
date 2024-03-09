package com.example.proba6.Entity;
import com.example.proba6.config.Encrypt;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String username;
    private String email;
    private String password;
    private String cpassword;
    private String phone;
    private String random;
    public Users(String username, String email, String password, String phone) {
        this.username = username;
        this.email = email;
        this.random = Encrypt.randomstring(30);
        this.password = Encrypt.generatespassword(password, this.random);
        this.phone = phone;
      ;
    }
}