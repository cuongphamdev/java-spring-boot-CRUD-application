package com.example.learn.repositories;

import com.example.learn.dtos.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmailAndPassword(String email, String password);

    @Query(value = "INSERT INTO users (name, email, password) VALUES (:name, :email, :password)",
            nativeQuery = true)
    void createNewUser (@Param("name") String name,@Param("email") String email,@Param("password") String password);
}
