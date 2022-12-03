package com.example.manager.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.manager.repository.UserRepository;

import com.example.manager.domain.User;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserController {

    public static final int maxFailedAttempts = 3;

    // 60 Seconds
    private static final long lockTimeDuration = 60 * 1000;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserRepository repo;

    final private PasswordEncoder passwordEncoder = passwordEncoder();

    public UserController() {
        super();
    }


    public User register(User user) {
        return repo.save(user);
    }

    public boolean checkIfUsernameTaken(String username) {
        return repo.existsById(username);
    }


    public boolean login(String username, String password) {
        User temp = null;
        try {
            temp = repo.findById(username).get();
        } catch (Exception e) {

        }
        if (temp != null) {
            if (passwordEncoder.matches(password, temp.getPassword()) && temp.getAccountNonLocked()) {
                resetFailedAttempts(username);
                return true;
            } else {
                unlockAfterTimeExpired(username);
                temp = repo.findById(username).get();
                if (temp.getAccountNonLocked() && temp.getFailedAttempt() < maxFailedAttempts) {
                    increaseFailedAttempts(username);
                    temp = repo.findById(username).get();
                    System.out.println("Failed attempt " + temp.getFailedAttempt() + "/4.");
                } else {
                    temp = repo.findById(username).get();
                    if (temp.getFailedAttempt() == maxFailedAttempts) {
                        lock(username);
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                        LocalDateTime  loginFailed = LocalDateTime.now().plusMinutes(1);
                        System.out.println("User locked until " + dtf.format(loginFailed) + ".\n");
                        return false;
                    }
                    unlockAfterTimeExpired(username);
                }
                return false;
            }
        }
        System.out.println("Username does not match an existing one. \n Please enter a valid username\n");
        return false;
    }

    public User changePassword(String username, String newPassword) {
        User temp = repo.findById(username).get();
        temp.setPassword(passwordEncoder.encode(newPassword));
        repo.save(temp);
        return repo.save(temp);
    }

    public void logout() {
        System.out.println("You are logged out!\n");
    }

    public void increaseFailedAttempts(String username) {
        User temp = repo.findById(username).get();
        int failedAttempttempt = temp.getFailedAttempt() + 1;
        temp.setFailedAttempt(failedAttempttempt);
        repo.save(temp);
    }

    public void resetFailedAttempts(String username) {
        User temp = repo.findById(username).get();
        temp.setFailedAttempt(0);
        repo.save(temp);
    }

    private void lock(String username) {
        User temp = repo.findById(username).get();
        temp.setAccountNonLocked(false);
        temp.setLockTime(new Date());
        repo.save(temp);
    }

    public boolean unlockAfterTimeExpired(String username) {
        User temp = repo.findById(username).get();
        if (temp.getLockTime() != null) {
            long lockTime = temp.getLockTime().getTime();
            long currentTime = System.currentTimeMillis();

            if (lockTime + lockTimeDuration < currentTime) {
                temp.setFailedAttempt(0);
                temp.setAccountNonLocked(true);
                temp.setLockTime(null);

                repo.save(temp);

                return true;
            }

            return false;
        }

        return false;
    }

    public void deleteAccount(String username) {
        repo.deleteById(username);
    }


}
