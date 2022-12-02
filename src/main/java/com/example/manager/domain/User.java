package com.example.manager.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Document(collection = "Users")
public class User {

    @Id
    private String username;

    private String firstname;
    private String lastname;
    private String password;
    private int failedAttempt;
    private boolean accountNonLocked;
    private Date lockTime;

    public User(String firstname, String lastname, String username, String password, int failedAttempt, boolean accountNonLocked, Date lockTime) {
        super();
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.failedAttempt = failedAttempt;
        this.accountNonLocked = accountNonLocked;
        this.lockTime = lockTime;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getFailedAttempt() {
        return failedAttempt;
    }

    public void setFailedAttempt(int failedAttempt) {
        this.failedAttempt = failedAttempt;
    }

    public boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) { this.lockTime = lockTime; }
}
