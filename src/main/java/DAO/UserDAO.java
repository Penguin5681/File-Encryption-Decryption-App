package DAO;

import DB.MyConnection;
import DataModels.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public static boolean doesUserExists(String email) throws SQLException {
        Connection connection = MyConnection.getConnection();
        String query = "select email from users";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rsl =  ps.executeQuery();

        while (rsl.next()) {
            String email_set = rsl.getString(1);
            if (email_set.equals(email))
                return true;
        }
        return false;
    }
    public static int saveUser(User user) throws SQLException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("insert into users values(default, ?, ?)");
        ps.setString(1, user.getName());
        ps.setString(2, user.getEmail());
        return ps.executeUpdate(); // rows affected
    }
}
