package DAO;

import DB.MyConnection;
import DataModels.Data;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataDAO {
    public static List<Data> getAllFiles(String email) throws SQLException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("select * from data where email = ?");
        ps.setString(1, email);
        ResultSet rsl = ps.executeQuery();
        List<Data> files = new ArrayList<>();
        while (rsl.next()) {
            int id = rsl.getInt(1);
            String name = rsl.getString(2);
            String path = rsl.getString(3);
            files.add(new Data(id, name, path));
        }
        return files;
    }
    public static int hideFile(Data file) throws SQLException, FileNotFoundException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("insert into data(name, path, email, bin_data) values(?, ?, ?, ?)");
        ps.setString(1, file.getFileName());
        ps.setString(2, file.getPath());
        ps.setString(3, file.getEmail());
        File f = new File(file.getPath());
        FileReader fr = new FileReader(f);
        ps.setCharacterStream(4, fr, f.length());
        int ans = ps.executeUpdate();
        try {
            fr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        f.delete();
        return ans;
    }
    public static void unhideFile(int id) throws SQLException, IOException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("select path, bin_data from data where id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        rs.next();
        String path = rs.getString(3);
        Clob clob = rs.getClob("bin_data");

        Reader reader = clob.getCharacterStream();
        FileWriter fileWriter = new FileWriter(path);
        int idx;
        while ((idx = reader.read()) != -1) {
            fileWriter.write((char)idx);
        }
        fileWriter.close();
        ps = connection.prepareStatement("delete from data where id = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("File unhidden successfully");
    }
}