package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    public static Connection connection = null;
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/File_Database?useSSL=false", "root", "root");
        } catch (ClassNotFoundException | SQLException e) {
             e.printStackTrace();
        }
        System.out.println("Connected!");
        return connection;
    }
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            }
            catch (SQLException e) {
                 e.printStackTrace();
            }
        }
    }
}
