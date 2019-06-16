package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnect {

    private static String url="jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12295723?useSSL=false";
    private static String user="sql12295723";
    private static String pass="Bm9bb6ecwQ";

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
            System.out.println("CONNECTED");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
