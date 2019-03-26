package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnect {

    private static String url="jdbc:mysql://localhost:3306/downease?useSSL=false";
    private static String user="root";
    private static String pass="shubham";

   /* public DbConnect(String urldb,String username,String password)
    {
        url=urldb;
        user=username;
        pass=password;
    }*/
    public Connection getConnection()
    {
        Connection conn=null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection(url,user,pass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
