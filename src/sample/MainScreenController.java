package sample;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {

    public static User current;
    @FXML
    private StackPane outerStackPane;
    @FXML
    private StackPane stack1;
    @FXML
    private AnchorPane imgApp;

    @FXML
    private JFXTabPane imgTab;

    @FXML
    private JFXTabPane docTab;

    @FXML
    private JFXHamburger ham;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private JFXListView<AnchorPane>imgList;

    @FXML
    private JFXListView<AnchorPane>docList;

    @FXML
    private TextField searchImg;

    @FXML
    private Label pathLabel;

    @FXML
    private Label downloadTime;

    @FXML
    private JFXTextArea logArea;

    @FXML
    private JFXComboBox imgTypeCombobox;

    @FXML
    private Label userNameLabel;

    @FXML
    private LineChart speedChartImage;
    @FXML
    private PieChart pieChartImage;
    @FXML
    private JFXTextArea pieChartAreaImg;

    private DirectoryChooser dc;
    private File dirPath=null;
    private StringBuilder sb=new StringBuilder("");
    private int imgcount=0;

    private boolean isNetAvailable()
    {
        try{
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            //conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            //e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if(!isNetAvailable())
        {
            showNoConnection();
        }

        try {

            if(!User.currUser.getName().isEmpty())
            userNameLabel.setText("  "+User.currUser.getName());
            imgTypeCombobox.setItems(FXCollections.observableArrayList("Image","Icon"));
            imgTypeCombobox.setValue("Image");
            searchImg.textProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println("textfield changed from " + oldValue + " to " + newValue);
            });
            HamburgerBackArrowBasicTransition burger1=new HamburgerBackArrowBasicTransition(ham);
            drawer.open();
            burger1.setRate(1);
            burger1.play();
            ham.addEventHandler(MouseEvent.MOUSE_PRESSED,(e)->{
                burger1.setRate(burger1.getRate()*-1);
                burger1.play();
                if(drawer.isOpened()){
                    drawer.close();
                }
                else {
                    drawer.toFront();
                    drawer.open();
                }

            });
            VBox box= FXMLLoader.load(getClass().getResource("design/vbox.fxml"));
            drawer.setSidePane(box);
            for(Node node: box.getChildren())
            {   System.out.println("shubh1");
                AnchorPane an=(AnchorPane)node;
                for(Node n:an.getChildren()) {
                    System.out.println("n="+n);
                    if(n instanceof VBox) {
                        VBox vb = (VBox) n;
                        for(Node butt:vb.getChildren()) {
                            System.out.println(butt);
                            if (butt instanceof JFXButton && butt.getAccessibleText() != null) {
                                System.out.println("shubh2" + butt.getAccessibleText());
                                butt.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                                    switch (butt.getAccessibleText()) {
                                        case "Image":
                                            System.out.println("IMG");
                                            //side1.toFront();
                                            imgTab.setVisible(true);
                                            docTab.setVisible(false);
                                            burger1.setRate(burger1.getRate()*-1);
                                            burger1.play();
                                            drawer.close();
                                            break;
                                        case "Document":
                                            System.out.println("DOC");
                                            //side2.toFront();
                                            docTab.setVisible(true);
                                            imgTab.setVisible(false);
                                            burger1.setRate(burger1.getRate()*-1);
                                            burger1.play();
                                            drawer.close();
                                            break;
                                        case "Logout":
                                            logoutDialog();
                                            break;
                                    }
                                });
                            }
                        }
                    }
                }
            }
            welcomeDialog(stack1,"    Wecome  "+User.currUser.getName());
            loadGraphs();
            /*Thread t1=new Thread(new Runnable() {
                @Override
                public void run() {
                    loadGraphs();
                }
            });*/
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private void loadGraphs()
    {
        try{
            loadSpeedChartImage();
            loadPieDistChartImage();

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    @FXML
    private void addnew1(ActionEvent event)
    {
        AnchorPane an=new AnchorPane();
        HBox hb=new HBox();
        an.setPrefWidth(544);
        an.setPrefHeight(40);
        Label lb=new Label("Shubham                                         ");
        lb.setStyle("-fx-font-weight:bold;-fx-font-size:20px;-fx-margin:100px;");
        hb.getChildren().add(lb);
        JFXButton btn1=new JFXButton("Submit");
        btn1.addEventHandler(MouseEvent.MOUSE_CLICKED,(e)->{
            Alert al=new Alert(Alert.AlertType.CONFIRMATION);
            al.setContentText("its me here dude");
            al.show();
        });
        btn1.setStyle("-fx-width:100px;-fx-background-color:#333;-fx-text-fill:#fff;-fx-margin:200px;");
        JFXButton btn2=new JFXButton("Login");
        btn1.setStyle("-fx-width:100px;-fx-background-color:#333;-fx-text-fill:#fff;-fx-margin:200px;");
        hb.getChildren().addAll(btn1,btn2);
        an.getChildren().add(hb);
        imgList.getItems().add(an);
    }
    @FXML
    private void addnew2(ActionEvent event)
    {
        System.out.println("here");
        AnchorPane an=new AnchorPane();
        HBox hb=new HBox();
        an.setPrefWidth(544);
        an.setPrefHeight(40);
        Label lb=new Label("Shubham                                         ");
        lb.setStyle("-fx-font-weight:bold;-fx-font-size:20px;-fx-margin:100px;");
        hb.getChildren().add(lb);
        JFXButton btn1=new JFXButton("Submit");
        btn1.addEventHandler(MouseEvent.MOUSE_CLICKED,(e)->{
            Alert al=new Alert(Alert.AlertType.CONFIRMATION);
            al.setContentText("its me here dude");
            al.show();
        });
        btn1.setStyle("-fx-width:100px;-fx-background-color:#333;-fx-text-fill:#fff;-fx-margin:200px;");
        JFXButton btn2=new JFXButton("Login");
        btn1.setStyle("-fx-width:100px;-fx-background-color:#333;-fx-text-fill:#fff;-fx-margin:200px;");
        hb.getChildren().addAll(btn1,btn2);
        an.getChildren().add(hb);
        docList.getItems().add(an);
    }
    @FXML
    private void temp()
    {
        System.out.println("temp");
    }
    @FXML
    private void chooseDirectory()
    {
        try {
            dc = new DirectoryChooser();
            //dc.setInitialDirectory(new File("/home/shubham"));
            System.out.println("valid dir");
            dirPath = dc.showDialog(Controller.primaryStage);
            pathLabel.setText("  "+dirPath.getAbsolutePath());
            System.out.println("valid dir");
        }catch (Exception e)
        {   pathLabel.setText("");
            System.out.println(e);
        }
    }

    @FXML
    public void downloadFile(ActionEvent event)
    {
        if(imgTypeCombobox.getValue().equals("Image"))
        {
            System.out.println("Choosed image");
            downloadImg(event);
        }
        else if(imgTypeCombobox.getValue().equals("Icon"))
        {
            System.out.println("Choosed icon");
            downloadIcon(event);
        }
    }
    @FXML
    private void downloadImg(ActionEvent event)
    {
        File newdir=null;
        long en=0,s=0;
        try {

            newdir = dirPath;
            imgList.getItems().clear();
            downloadTime.setText("");
            System.out.println(newdir.getAbsolutePath());
            if (newdir.getAbsolutePath() == null || searchImg.getText().isEmpty()) {
                failedDialog(stack1,"Nothing to search");
                System.out.println("NULL");return;
            }
        }catch (Exception e)
        {
            failedDialog(stack1,"No directory choosen");
            System.out.println(e);
            return;
        }

        try {

            //System.out.println("home/Pics"+"/"+str+"/img");
            // File abcd=new File("yo.txt");abc

            //boolean c=abcd.mkdir();
            File fl=new File(newdir.getAbsolutePath()+"/"+searchImg.getText());
            boolean created=fl.mkdir();
            String u = "https://www.google.com/search?tbm=isch&q="+searchImg.getText();
            Document document = Jsoup.connect(u).get();
            Elements link = document.select("img");
            System.out.println("size:"+link.size());
            if(link.size()<=10)
            {
                failedDialog(stack1,"No such images found");
                fl.delete();
                return;
            }
            int count=0;
            s=System.currentTimeMillis();
            System.out.println("I :"+s);
            for(Element e:link) {
                count++;
                //System.out.print(count++ +" ");
                //System.out.println(e.attr("data-src"));
                DownloadImage di=new DownloadImage(current,e.attr("data-src"),newdir.getAbsolutePath(),searchImg.getText(),count,".jpg");
                if(!e.attr("data-src").isEmpty())
                    addToImglist(e.attr("data-src"),newdir.getAbsolutePath(),searchImg.getText(),count,"#111");
                Thread t=new Thread(di);
                t.start();

                //System.out.println("end"+count);

            }
            Thread.sleep(1000);
            successDialog();
            en=System.currentTimeMillis();
            System.out.println("II :"+en);
            System.out.println(User.currUser.getName()+" RESULT :"+(en-s));
            int timetaken= (int) (System.currentTimeMillis()-s);
            downloadTime.setText("Download Time : "+String.valueOf(timetaken)+" ms");
            DownloadImage.storeImgDownloadInfo(searchImg.getText(),timetaken);
            loadGraphs();
            imgcount=0;
        } catch (MalformedURLException e) {
            System.out.println(e+"1st");
            // e.printStackTrace();
        } catch(UnknownHostException ex)
        {   System.out.println("snackbar");
            downloadTime.setText("");
            showNoConnection();

        }
        catch (ConnectException ex)
        {
            System.out.println("snackbar");
            downloadTime.setText("");
            showNoConnection();
        }
        catch (IOException e) {
            System.out.println(e+"2nd Ex");
        }
        catch (Exception ex)
        {
            downloadTime.setText("");
            showNoConnection();
            System.out.println(ex+"3rd exception");
        }

    }
    private void downloadIcon(ActionEvent event)
    {
        File newdir=null;
        long en=0,s=0;
        try {

            newdir = dirPath;
            imgList.getItems().clear();
            downloadTime.setText("");
            System.out.println(newdir.getAbsolutePath());
            if (newdir.getAbsolutePath() == null || searchImg.getText().isEmpty()) {
                failedDialog(stack1,"Nothing to search");
                System.out.println("NULL");return;
            }
        }catch (Exception e)
        {
            failedDialog(stack1,"No directory choosen");
            System.out.println(e);
            return;
        }

        try {

            //System.out.println("home/Pics"+"/"+str+"/img");
            // File abcd=new File("yo.txt");abc

            //boolean c=abcd.mkdir();
            File fl=new File(newdir.getAbsolutePath()+"/"+searchImg.getText()+"-icon");
            boolean created=fl.mkdir();
            String u = "https://www.flaticon.com/search?word="+searchImg.getText();
            Document document = Jsoup.connect(u).get();
            Elements link = document.getElementsByClass("icon--holder");
            System.out.println(link);
            System.out.println("size:"+link.size());
            if(link.size()<=0)
            {
                failedDialog(stack1,"No such icons found");
                fl.delete();
                return;
            }
            int count=0;
            s=System.currentTimeMillis();
            System.out.println("I :"+s);
            for(Element e:link) {
                count++;
                Elements imglink=e.getElementsByTag("img");
                System.out.println("icons size:"+imglink.size());
                //System.out.print(count++ +" ");
                //System.out.println(e.attr("data-src"));
                for(Element ele:imglink)
                {
                    DownloadImage di=new DownloadImage(current,ele.attr("src"),newdir.getAbsolutePath(),searchImg.getText()+"-icon",count,".png");
                    if(!ele.attr("src").isEmpty())
                        addToImglist(ele.attr("src"),newdir.getAbsolutePath(),searchImg.getText(),count,"#fff");
                    Thread t=new Thread(di);
                    t.start();
                }

                //System.out.println("end"+count);

            }
            Thread.sleep(1000);
            successDialog();
            en=System.currentTimeMillis();
            System.out.println("II :"+en);
            System.out.println(User.currUser.getName()+" RESULT :"+(en-s));
            int timetaken= (int) (System.currentTimeMillis()-s);
            downloadTime.setText("Download Time : "+String.valueOf(timetaken)+" ms");
            DownloadImage.storeImgDownloadInfo(searchImg.getText()+" icon",timetaken);
            loadGraphs();
            imgcount=0;
        } catch (MalformedURLException e) {
            System.out.println(e+"1st");
            // e.printStackTrace();
        } catch(UnknownHostException ex)
        {   System.out.println("snackbar");
            downloadTime.setText("");
            showNoConnection();

        }
        catch (ConnectException ex)
        {
            System.out.println("snackbar");
            downloadTime.setText("");
            showNoConnection();
        }
        catch (Exception ex)
        {
            downloadTime.setText("");
            showNoConnection();
            System.out.println(ex+"3rd exception");
        }

    }
    private void addToImglist(String link,String path,String search,int count,String bgcolor )
    {
        imgcount++;
        System.out.println("ADD"+link+" "+" "+count);
        logArea.clear();
        sb.append("  IMG"+count+" . . . .\n"+"  Sucessfully downloaded"+"\n");
        logArea.setText(sb.toString());
        AnchorPane an=new AnchorPane();
        an.setPrefWidth(544);
        an.setPrefHeight(90);
        imgList.setStyle("-fx-background-color:#000;");
        imgList.setExpanded(true);
        imgList.depthProperty().set(1);
        HBox hb=new HBox();
        JFXButton btn=new JFXButton();
        btn.setText(String.valueOf(imgcount));
        btn.setStyle("-fx-background-color:#333;-fx-text-fill:#fff;-fx-font-weight:bold;");
        ImageView im=new ImageView(new Image(link));
        im.setFitWidth(70);
        im.setFitHeight(70);
        Label lb=new Label("img"+count+"           ");
        lb.setStyle("-fx-alignment:center;-fx-width:100px;-fx-height:50px;-fx-text-fill:#000;-fx-font-weight:bold;-fx-font-size:20px;-fx-text-align:center;-fx-justify-content:center;");
        lb.setAlignment(Pos.CENTER);
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(btn,im,lb);
        hb.setSpacing(20);
        JFXButton btn1=new JFXButton("Copy URL");
        ImageView copyIcon=new ImageView(new Image("sample/img/copy.png"));
        copyIcon.setFitWidth(15);
        copyIcon.setFitHeight(15);
        btn1.setGraphic(copyIcon);
        btn1.addEventHandler(MouseEvent.MOUSE_CLICKED,(e)->{
            Toolkit toolkit=Toolkit.getDefaultToolkit();
            Clipboard clipboard=toolkit.getSystemClipboard();
            StringSelection selection=new StringSelection(link);
            clipboard.setContents(selection, null);
        });
        btn1.setStyle("-fx-width:100px;-fx-background-color:#2E2EFE;-fx-text-fill:#fff;-fx-margin:200px;-fx-font-weight:bold;");
        JFXButton btn2=new JFXButton(" Open ");
        btn2.setStyle("-fx-width:100px;-fx-background-color:#04B486;-fx-text-fill:#fff;-fx-margin:200px;-fx-font-weight:bold;");
        btn2.addEventHandler(MouseEvent.MOUSE_CLICKED,(e)->{

            JFXDialogLayout content = new JFXDialogLayout();
            VBox ver=new VBox();
            ImageView img=new ImageView(new Image(link));
            img.setFitWidth(200);
            img.setFitHeight(200);
            ver.getChildren().add(img);
            // ver.getChildren().add(new Label(link));
            //content.setHeading(new Label("Your Image"));
            content.setBody(ver);
            JFXDialog dialog = new JFXDialog(stack1, content, JFXDialog.DialogTransition.RIGHT);
            content.setStyle("-fx-background-color:"+bgcolor+";-fx-pref-width:200px;-fx-pref-height:200px;-fx-text-fill:#ff0000;-fx-text-color:#ff0000;");
            dialog.setContent(content);
            JFXButton button = new JFXButton("Okay");
            button.setStyle("-fx-background-color:#303030;-fx-text-fill:#fff;-fx-font-weight:bold;-fx-pref-width:100px;-fx-pref-height:40px;-fx-background-radius:20px;-fx-border-radius:20px;");
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    dialog.close();
                }
            });
            //content.setActions(button);
            dialog.show();
        });
        hb.getChildren().addAll(btn1,btn2);
        an.getChildren().add(hb);
        imgList.getItems().add(an);
        imgList.setExpanded(true);
        imgList.depthProperty().set(1);
        System.out.println("ADDED");
    }
    @FXML
    private void loadSpeedChartImage() throws SQLException
    {
        speedChartImage.getData().clear();
        XYChart.Series<String,Number> series=new XYChart.Series<String, Number>();
        StringBuilder stb=new StringBuilder(" SEARCH"+"\t\t\t\t"+"SPEED\n\n");
        ResultSet result=DownloadImage.findSpeed();
        int i=0;
        while (result.next()) {
            int value=Integer.parseInt(result.getString("taken"));
            Number number1 =value;
            stb.append(" "+result.getString("search")+"\t\t\t"+value+"\n");
            series.getData().add(new XYChart.Data<String, Number>(String.valueOf(i), number1));
            i++;
        }
        //lab1.setText(stb1.toString());
        speedChartImage.getData().addAll(series);

        for(final XYChart.Data<String,Number> data:series.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    //System.out.println("X : "+data.getXValue()+"\nY : "+data.getYValue());
                    Tooltip.install(data.getNode(),new Tooltip("X : "+data.getXValue()+"\nY : "+data.getYValue()));
                }
            });
        }

    }
    @FXML
    private void loadPieDistChartImage() throws SQLException
    {
        ObservableList<PieChart.Data> list=FXCollections.observableArrayList();
        pieChartImage.getData().clear();
        StringBuilder stb=new StringBuilder("\n");
        stb.append("  Mon"+"\t\t"+"Downloads\n  -------------------------------\n\n");
        ResultSet resultSet=DownloadImage.downloadsByDate();
        while (resultSet.next())
        {
            Integer month=Integer.parseInt(resultSet.getString("month(dod)"));
            String months[]={"January","February","March","April","May","June","July","August","September","October","November","December"};
            String reqMonth=months[month-1];
            Integer num=Integer.parseInt(resultSet.getString("count(search)"));
            stb.append("  "+reqMonth.substring(0,3)+"          "+num+"\n");
            list.add(new PieChart.Data(reqMonth,num));

        }
        pieChartImage.setData(list);
        pieChartAreaImg.setText(stb.toString());
    }
    private void welcomeDialog(StackPane stackPane,String str) throws InterruptedException
    {
        JFXDialogLayout content=new JFXDialogLayout();
        VBox vb=new VBox();
        vb.setSpacing(30);
        Label lb=new Label(str);
        lb.setStyle("-fx-font-weight:bold;-fx-text-fill:#000;-fx-pref-width:300px;");
        lb.setMinWidth(200);
        lb.setAlignment(Pos.CENTER);
        ImageView im=new ImageView(new Image("sample/img/confetti2.png"));
        im.setFitWidth(150);
        im.setFitHeight(150);
        vb.getChildren().addAll(im,lb);
        content.setStyle("-fx-background-color:#fff;-fx-pref-width:350px;-fx-pref-height:200px;-fx-text-fill:#000;-fx-text-color:#000;");
        content.setBody(vb);
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);
        dialog.setContent(content);
        JFXButton button = new JFXButton(" Lets Get Started ");
        button.setStyle("-fx-background-color:#3498DB;-fx-text-fill:#fff;-fx-font-weight:bold;-fx-pref-width:150px;-fx-pref-height:40px;-fx-background-radius:20px;-fx-border-radius:20px;");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("closing");dialog.close();
            }
        });
        content.setActions(button);
        dialog.show();
        Thread.sleep(1000);
        System.out.println(Thread.currentThread());
        //Thread.sleep(2000);
        System.out.println("Welcome dialog");
    }
    private void successDialog()
    {
        JFXDialogLayout content = new JFXDialogLayout();
        HBox hb=new HBox();
        Label label = new Label("Download Completed");
        label.setStyle("-fx-text-fill:#fff;-fx-font-weight:bold;-fx-font-size:18px;-fx-alignment:center;-fx-font-family:Lato;");
        label.setAlignment(Pos.CENTER);
        ImageView im=new ImageView(new Image("sample/img/success.png"));
        im.setFitHeight(50);
        im.setFitWidth(50);
        hb.getChildren().addAll(label,im);
        content.setBody(hb);
        JFXDialog dialog = new JFXDialog(stack1, content, JFXDialog.DialogTransition.TOP);
        content.setStyle("-fx-background-color:#8000FF;-fx-pref-width:350px;-fx-pref-height:160px;-fx-text-fill:#ff0000;-fx-text-color:#ff0000;");
        dialog.setContent(content);
        JFXButton button = new JFXButton("Okay");
        button.setStyle("-fx-background-color:#303030;-fx-text-fill:#fff;-fx-font-weight:bold;-fx-pref-width:100px;-fx-pref-height:40px;-fx-background-radius:20px;-fx-border-radius:20px;");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        content.setActions(button);
        dialog.show();
    }
    private void failedDialog(StackPane stackPane,String str)
    {
        System.out.println("Failed");
        JFXDialogLayout content=new JFXDialogLayout();
        HBox hb=new HBox();
        Label lb=new Label(str);
        lb.setStyle("-fx-font-weight:bold;-fx-text-fill:#000;-fx-prewf-width:300px;");
        lb.setMinWidth(200);
        hb.getChildren().addAll(lb);
        content.setStyle("-fx-background-color:#ddd;-fx-pref-width:250px;-fx-pref-height:50px;-fx-text-fill:#000;-fx-text-color:#000;");
        content.setBody(hb);
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.LEFT);
        dialog.setContent(content);
        JFXButton button = new JFXButton("Okay");
        button.setStyle("-fx-background-color:#1ABC9C;-fx-text-fill:#fff;-fx-font-weight:bold;-fx-pref-width:100px;-fx-pref-height:40px;-fx-background-radius:20px;-fx-border-radius:20px;");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        content.setActions(button);
        dialog.show();
    }
    private void logoutDialog()
    {
        JFXDialogLayout content = new JFXDialogLayout();
        HBox hb=new HBox();
        Label label = new Label("Are you sure you want to logout?");
        label.setStyle("-fx-text-fill:#fff;-fx-font-weight:bold;-fx-font-size:18px;-fx-alignment:center;-fx-font-family:Lato;");
        label.setAlignment(Pos.CENTER);
        content.setBody(label);
        JFXDialog dialog = new JFXDialog(outerStackPane, content, JFXDialog.DialogTransition.TOP);
        content.setStyle("-fx-background-color:#8000FF;-fx-pref-width:400px;-fx-pref-height:160px;-fx-text-fill:#ff0000;-fx-text-color:#ff0000;");
        dialog.setContent(content);
        JFXButton yesBtn = new JFXButton("Yes");
        yesBtn.setStyle("-fx-background-color:#eee;-fx-text-fill:#000;-fx-font-weight:bold;-fx-pref-width:100px;-fx-pref-height:40px;-fx-background-radius:20px;-fx-border-radius:20px;");
        yesBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Controller.primaryStage.close();
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("design/login.fxml"));
                    Scene scene = new Scene(root);
                    scene.getStylesheets().add(SplashController.class.getResource("snack.css").toExternalForm());
                    SplashController.loginStage.setTitle("DownEase");
                    SplashController.loginStage.resizableProperty().setValue(false);
                    SplashController.loginStage.getIcons().add(new Image("sample/img/astronaut-2.png"));
                    SplashController.loginStage.setScene(scene);
                    SplashController.loginStage.show();
                }catch (Exception ex){
                    System.out.println("Error opening stage");
                }
            }
        });
        JFXButton cancelBtn = new JFXButton("Cancel");
        cancelBtn.setStyle("-fx-background-color:#eee;-fx-text-fill:#000;-fx-font-weight:bold;-fx-pref-width:100px;-fx-pref-height:40px;-fx-background-radius:20px;-fx-border-radius:20px;");
        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        content.setActions(yesBtn,cancelBtn);
        dialog.show();
    }
    private void showNoConnection()
    {
        JFXSnackbar snack=new JFXSnackbar(imgApp);
        EventHandler handler=new EventHandler() {
            @Override
            public void handle(Event event) {
                snack.close();
            }
        };
        snack.show("No internet Connection","Okay",6000,handler);
    }
    private void showSnack(String str)
    {
        JFXSnackbar snack=new JFXSnackbar(imgApp);
        EventHandler handler=new EventHandler() {
            @Override
            public void handle(Event event) {
                snack.close();
            }
        };
        snack.show(str,2000);
    }

}
