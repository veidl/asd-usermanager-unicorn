package com.example.manager.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.manager.repository.UserRepository;

import com.example.manager.domain.User;

@Service
public class UserController {
	
	
	@Autowired
	private UserRepository repo;
	
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
		User temp = repo.findById(username).get();
		
		if(temp.getPassword().equals(password)) {
			return true;
		} else {
			return false;
		}
	}
	
	public User changePassword(String username, String newPassword) {
		User temp = repo.findById(username).get();
		temp.setPassword(newPassword);
		return repo.save(temp);
		
	}
	
	private void logout() {
		System.out.println("You are logged out!");
	}
	
	
	public void deleteAccount(String username) {
		repo.deleteById(username);
	}
	
	
}
