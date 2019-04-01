package sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    public static User currUser;
    protected String user;
    protected String pass;
    protected String name;
    protected String emailid;
    protected String phno;
    protected String loc;
    protected String occ;

    public User(){}

    public User(String name, String emailid, String phno, String loc, String occ) {
        this.name = name;
        this.emailid = emailid;
        this.phno = phno;
        this.loc = loc;
        this.occ = occ;
    }

    public User(String user, String pass)
    {
        this.user=user;
        this.pass=pass;
    }
    public String getName()
    {
        return this.name;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean authenticate()
    {
        try {
            DbConnect db = new DbConnect();
            Connection conn = db.getConnection();
            PreparedStatement pst = conn.prepareStatement("select * from user where username='" + user + "' and password='" + pass + "';");
            ResultSet res=pst.executeQuery();
            boolean b=false;
            while(res.next())
            {   b=true;
                System.out.println("User found");
                System.out.println(res.getString("id"));
                this.name=res.getString("name");
                this.emailid=res.getString("email");
                this.emailid=res.getString("phno");
                this.emailid=res.getString("loc");
                this.emailid=res.getString("occ");
                return true;
              //  System.out.println("USER "+res.getString("username")+" "+" PASS : "+res.getString("password"));
               // System.out.println();
            }
            if(b==false) {
                System.out.println("No record found !!!");
                return false;
            }
        }
        catch (SQLException sq)
        {
            System.out.println("Invalid syntax");
        }
        return false;
    }
    public void addNewUser()
    {
        try {
            DbConnect db = new DbConnect();
            Connection conn = db.getConnection();
            System.out.println("insert into user(username,password,name,email,phno,loc,occ) values('"+this.user+"','"+this.pass+"','"+this.name+"','"+this.emailid+"','"+this.phno+"','"+this.loc+"','"+this.occ+"');");
            PreparedStatement pst = conn.prepareStatement("insert into user(username,password,name,email,phno,loc,occ) values('"+this.user+"','"+this.pass+"','"+this.name+"','"+this.emailid+"','"+this.phno+"','"+this.loc+"','"+this.occ+"');");
            pst.executeUpdate();
            System.out.println("User Addded");
        }
        catch (SQLException sq)
        {
            System.out.println("Invalid syntax ,operation failed");
        }

    }
    protected static int getUserId(String username,String password){
        try{
            DbConnect db = new DbConnect();
            Connection conn = db.getConnection();
            PreparedStatement pst = conn.prepareStatement("select id from user where username='" + username + "' and password='" + password + "';");
            ResultSet res=pst.executeQuery();
            while(res.next())
            {
                System.out.println("User found");
                return Integer.parseInt(res.getString("id"));
            }
        }
        catch (Exception ex)
        {
            System.out.println("No user founf");
        }
        return 0;
    }
}
