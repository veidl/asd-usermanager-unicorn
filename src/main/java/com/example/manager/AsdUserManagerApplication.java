package com.example.manager;

import java.util.Date;
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
    Scanner scn = new Scanner(System.in);
    int cnt = 0;

    public static void main(String[] args) {
        SpringApplication.run(AsdUserManagerApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        showMenu();
    }

    private void showMenu() {
        System.out.println("------------------------------------------");
        System.out.println("++++++ Welcome to the user manager ++++++");
        System.out.println("------------------------------------------");
        System.out.println("Please enter number to choose an option: \n (1)Register \n (2)Login \n (3)Exit \n");

        try (Scanner scn = new Scanner(System.in)) {
            int userInput = scn.nextInt();

            switch (userInput) {
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
        catch (Exception e){
            System.out.println("\nError while getting user input!\n Shutting down User Manager...\n");
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

        while (uc.checkIfUsernameTaken(username)) {
            System.out.println("This username is already taken. Try another name: \n");
            username = scn.next();
        }

        System.out.println("Please enter password:");
        String password = scn.next();

        String bCryptEncodedPassword = uc.passwordEncoder().encode(password);

        int failedAttempt = 0;
        boolean accountNonLocked = true;
        Date lockTime = null;

        User user = new User(firstname, lastname, username, bCryptEncodedPassword, failedAttempt, accountNonLocked, lockTime);
        uc.register(user);
        System.out.println("User was successfully created.");
        showMenu();
    }

    private void loginMenu() {

        System.out.println("--- Login Menu ---");


        System.out.println("Please enter username:");
        String username = scn.next();
        System.out.println("Please enter password:");
        String password = scn.next();
        
        
        while (!uc.login(username, password)) {
            System.out.println("Username or password incorrect. Try again!\n");
            System.out.println("Please enter username:");
            username = scn.next();
            System.out.println("Please enter password:");
            password = scn.next();

        }
        loggedInMenu(username, password);
    }

    private void loggedInMenu(String username, String password) {

        System.out.println("\n--- Logged in as " + username + "---\n");
        System.out.println("Choose Options: \n (1)Change password \n (2)Delete Account \n (3)Logout");

        int userInput = scn.nextInt();
        switch (userInput) {
            case 1:
                changePasswordMenu(username, password);
                break;
            case 2:
                deleteAccountMenu(username);
                break;
            case 3:
                uc.logout();
                showMenu();
        }

    }

    private void changePasswordMenu(String username, String password) {
        boolean passwordResetSucess = false;
        String newPassword = null;
        String confirmNewPassword = null;

        while(!passwordResetSucess){
            String confirmPassword = "";

            while (!confirmPassword.equals(password)) {
                System.out.println("Please enter your current password to be able to change it: ");
                confirmPassword = scn.next();
            }

            System.out.println("Please enter your new Password: ");
            newPassword = scn.next();
            System.out.println("Please confirm the new Password: ");
            confirmNewPassword = scn.next();

            if(newPassword.equals(confirmNewPassword)){
                passwordResetSucess = true;
                uc.changePassword(username, newPassword);
                System.out.println("Password changed successfully!");
                loggedInMenu(username, newPassword);
                //System.out.println("Password changed successfully!\nLogout necessary for reset!\n");
                //showMenu();
            }
            else{
                System.out.println("Passwords do not match! Try again!\n");
            }
        }
    }

    private void deleteAccountMenu(String username) {
        System.out.println("\n--- Account Deletion ---");

        System.out.println("Please enter your password for account deletion:");
        String passwordIn = scn.next();

        while (!uc.login(username, passwordIn)) {
            System.out.println("Password is incorrect. Try again!");
            passwordIn = scn.next();
        }
        uc.deleteAccount(username);
        System.out.println("--- Account log out successful ! \n --- Your account has been deleted successfully! --- \n");
        showMenu();
    }

}
