package sample;

import sample.User;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DownloadImage extends User implements Runnable{

    private String url="";
    private String path="";
    private String search="";
    private String ext="";
    private int count;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public DownloadImage(){
        super();
    }

    public DownloadImage(User user, String url, String path, String search, int count,String ext) {
        super(user.name, user.emailid, user.phno, user.loc, user.occ);
        this.url=url;
        this.path=path;
        this.search=search;
        this.count=count;
        this.ext=ext;
    }

    @Override
    public void run() {
        if(true) {
            try {//System.out.println("start"+count);
                //System.out.println(url);
                URL link = new URL(url);
                InputStream in = new BufferedInputStream(link.openStream());
                OutputStream out = new BufferedOutputStream(new FileOutputStream(path+"/"+search+"/img" + String.valueOf(count)+ext));
                for (int i; (i = in.read()) != -1; ) {
                    out.write(i);
                }
                in.close();
                out.close();
                //System.out.println("end"+count);
                //count++;
            } catch (MalformedURLException ex) {
                System.out.println(ex+" Wrong URL");
                // e.printStackTrace();
            } catch (IOException ex) {
                System.out.println(ex+" IOException");
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }

    }
    public static void storeImgDownloadInfo(String search,int timetaken){
        try{

            System.out.println("storeImgDownloadinfo");
            Integer id=getUserId(User.currUser.user,User.currUser.pass);
            DbConnect db = new DbConnect();
            Connection conn = db.getConnection();
            System.out.println("insert into downloadimage values("+id+",'"+search+"',"+timetaken+",'"+ LocalDate.now().toString() +"');");
            PreparedStatement pst = conn.prepareStatement("insert into downloadimage values("+id+",'"+search+"',"+timetaken+",'"+ LocalDate.now().toString() +"');");
            pst.executeUpdate();
            System.out.println("Img Record in DB");
        }
        catch (Exception ex){
            System.out.println("Operation failed"+ex);
        }

    }
    public static ResultSet findSpeed()
    {
        try {
            System.out.println("storeImgDownloadinfo");
            Integer id = getUserId(User.currUser.user, User.currUser.pass);
            DbConnect db = new DbConnect();
            Connection conn = db.getConnection();
            PreparedStatement pst = conn.prepareStatement("select taken from downloadimage where id="+id+" order by dod");
            ResultSet resultSet=pst.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
