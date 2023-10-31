package Views;

import DAO.UserDAO;
import DataModels.User;
import Services.GenerateOTP;
import Services.SendOTPService;
import Services.UserService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Scanner;

public class Welcome {
    public void welcomeScreen() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to File Encryption/Decryption CLI App");
        System.out.println("Please proceed to select a operation");
        System.out.println("1. Login");
        System.out.println("2. Sign-Up");
        System.out.println("0. Exit");

        int choice = 0;
        try {
            choice = Integer.parseInt(br.readLine());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        switch (choice) {
            case 1 -> logIn();
            case 2 -> signUp();
            case 0 -> System.exit(0);
        }
    }
    Scanner cin = new Scanner(System.in);
    private void signUp() {
        System.out.println("Enter Name: ");
        String name = cin.nextLine();
        System.out.println("Enter Email: ");
        String email = cin.nextLine();

        String genOTP = GenerateOTP.getOTP();
        SendOTPService.sendOTP(email, genOTP);
        System.out.println("Enter OTP");
        String userOTP = cin.nextLine();

        if (userOTP.equals(genOTP)) {
            User user = new User(name, email);
            int response = UserService.saveUser(user);
            switch (response) {
                case 0 -> System.out.println("Registration Complete");
                case 1 -> System.out.println("User already exists");
            }
        }
        else
            System.out.println("Invalid OTP");
    }
    private void logIn() {
        System.out.println("Enter Email: ");
        String email = cin.nextLine();
        try {
            if (UserDAO.doesUserExists(email)) {
                String genOTP = GenerateOTP.getOTP();
                SendOTPService.sendOTP(email, genOTP);
                System.out.println("Enter OTP");
                String userOTP = cin.nextLine();

                if (userOTP.equals(genOTP)) {
                    new UserView(email).home();
                }
                else
                    System.out.println("Invalid OTP");
            }
            else
                System.out.println("User not found!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
//