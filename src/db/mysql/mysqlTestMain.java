package db.mysql;

import java.sql.*;

public class mysqlTestMain {

    public static void main(String[] args) {
        Connection conn = null;
        try {
            String myConnectionString =
                    "jdbc:mysql://localhost:8889?" +
                    "useUnicode=yes&characterEncoding=UTF-8"+ "&autoReconnect=true&useSSL=false&serverTimezone=UTC";;
            conn = DriverManager.getConnection(myConnectionString, "root", "root");
            Statement stmt = conn.createStatement();
            stmt.execute("SHOW DATABASES");
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}