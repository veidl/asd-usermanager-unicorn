package com.example.manager;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.example.manager.domain.User;
import com.example.manager.controller.UserController;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableMongoRepositories
public class AsdUserManagerApplication implements CommandLineRunner {
	
	@Autowired
	private UserController uc;
	Scanner scn = new Scanner( System.in );
	int cnt = 0;
	
	public static void main(String[] args) {
		SpringApplication.run(AsdUserManagerApplication.class, args);
		
	}


	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Creating User!");
		User firstUser = new User("Arnold", "Schwarzenegger", "arnie", "Bizeps");
		showMenu();
//		System.out.println("Registering User!");
//		uc.register(firstUser);
//		
//		if(uc.login("arnie", "Bizeps")) {
//			System.out.println("Success!");
//		} else {
//			System.out.println("Wrong my G");
//		}
		
		//System.out.println("This happened" + uc.deleteAccount(firstUser.getUsername()));
		
		
	}
	
	private void showMenu() {
		System.out.println("------------------------------------------");
		System.out.println("++++++ Welcome to the user manager ++++++");
		System.out.println("------------------------------------------");
		System.out.println("Please enter number to choose an option: \n (1)Register \n (2)Login \n (3)Exit \n");
		
		Scanner scn = new Scanner( System.in );
		int userInput = scn.nextInt();
		
		switch(userInput) {
		case 1:
			registerMenu();
			break;
		case 2:
			loginMenu();
			break;
		case 3:
			System.out.println("Closed UserManager!");
			System.exit(0);
		default:
			System.out.println("Invalid Input! Restarting....");
			showMenu();
		}
	}
	
	private void registerMenu() {
		System.out.println("--- Register Menu ---");
				
		System.out.println("Please enter firstname:");
		String firstname = scn.next();
		System.out.println("Please enter lastname:");
		String lastname = scn.next();
		System.out.println("Please enter username:");
		String username = scn.next();
		
		while(uc.checkIfUsernameTaken(username)) {
			System.out.println("This username is already taken. Try another name: \n");
			username = scn.next();
		}
		
		System.out.println("Please enter password:");
		String password = scn.next();
		
		User user = new User(firstname, lastname, username, password);
		uc.register(user);
		
	}
	
	private void loginMenu() {
		System.out.println("--- Login Menu ---");
		
		
		System.out.println("Please enter username:");
		String username = scn.next();
		System.out.println("Please enter password:");
		String password = scn.next();
		
		while(!uc.login(username, password)) {
			System.out.println("Username or password incorrect. Try again!");
			System.out.println("Please enter username:");
			username = scn.next();
			System.out.println("Please enter password:");
			password = scn.next();
			
		}
		
		loggedInMenu(username);
	}
	
	private void loggedInMenu(String username) {

		System.out.println("\n--- Logged in as " + username + "---\n");
		System.out.println("Choose Options: \n (1)Change password \n (2)Delete Account");
		
		
		int userInput = scn.nextInt();
		switch(userInput) {
		case 1:
			//changePasswordMenu();
			break;
		case 2:
			deleteAccountMenu(username);
			break;
		}
		
	}
	
	private void deleteAccountMenu(String username) {
		System.out.println("\n--- Account Deletion ---");
		
		System.out.println("Please enter password for account deletion:");
		String passwordIn = scn.next();
		
		while(!uc.login(username, passwordIn)) {
			System.out.println("Password is incorrect. Try again!");
			passwordIn = scn.next();
		}
			uc.deleteAccount(username);
			System.out.println("--- Account log out successful ! \n --- Your account has been deleted successfully! --- \n");
			showMenu();
	}
	
}
