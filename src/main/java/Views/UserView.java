package Views;

import DAO.DataDAO;
import DataModels.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UserView {
    private String email;
    UserView(String email) {
        this.email = email;
    }
    public void home() {
        do {
            System.out.println("Welcome to the App " + this.email);
            System.out.println("Choose a operation: ");
            System.out.println("1. Show all hidden files");
            System.out.println("2. Hide a new file");
            System.out.println("3. Unhide a file");
            System.out.println("0. Exit the Program");

            Scanner cin = new Scanner(System.in);
            int ch = Integer.parseInt(cin.nextLine());
            switch (ch) {
                case 1 -> {
                    try {
                        List<Data> files = DataDAO.getAllFiles(this.email);
                        System.out.println("Id   -   File Name");
                        for (Data file : files) {
                            System.out.println(file.getId() + " - " + file.getFileName());
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                case 2 -> {
                    System.out.println("Enter file path: ");
                    String path = cin.nextLine();
                    File f = new File(path);
                    Data file = new Data(0, f.getName(), path);
                    try {
                        DataDAO.hideFile(file);
                    } catch (SQLException | FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                case 3 -> {
                    List<Data> files = null;
                    try {
                        files = DataDAO.getAllFiles(this.email);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Id   -   File Name");
                    for (Data file : files) {
                        System.out.println(file.getId() + " - " + file.getFileName());
                    }
                    System.out.println("Enter the Id of the file to un-hide");
                    int id = Integer.parseInt(cin.nextLine());
                    boolean isValidId = false;
                    for (Data file : files) {
                        if (file.getId() == id) {
                            isValidId = true;
                            break;
                        }
                    }
                    if (isValidId) {
                        try {
                            DataDAO.unhideFile(id);
                        } catch (SQLException | IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else {
                        System.out.println("Invalid Id");
                    }
                }
                case 4 -> {
                    System.exit(0);
                }
            }
        } while (true);
    }
}
