package com.example.proba6.Repository;

import com.example.proba6.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {


    Users findByEmailAndPassword(String email, String password);
    Optional<Users> findByEmail(String email);
    Users findById(Long id);

}
