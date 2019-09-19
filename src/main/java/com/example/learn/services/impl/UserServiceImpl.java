package com.example.learn.services.impl;

import com.example.learn.models.User;
import com.example.learn.repositories.UserRepository;
import com.example.learn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createNewUser(String name, String email, String password) {
        User user = new User(name, email, password);
        User loginUser = userRepository.save(user);
        return loginUser;
    }

    @Override
    public boolean checkAuthentication(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        Object loginSession = session.getAttribute("loginSession");
        return loginSession != null;
    }

    @Override
    public void setAuthenticate(HttpServletRequest request, User user) {
        HttpSession session = request.getSession(true);
        session.setAttribute("loginSession", user);
    }

    @Override
    public User findUserById(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    @Override
    public User loginByEmailAndPassword(String email, String password) {
        User loginUser = userRepository.findUserByEmailAndPassword(email, password);
        return loginUser;
    }

    @Override
    public long getCurrentUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        User loginSession = (User) session.getAttribute("loginSession");
        if (loginSession != null) {
            return loginSession.getId();
        }
        return 0;
    }
}
