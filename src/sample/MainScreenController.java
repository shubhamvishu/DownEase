package sample;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {

    public static User current;
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

    private DirectoryChooser dc;
    private File dirPath=null;
    private StringBuilder sb=new StringBuilder("");

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
            JFXSnackbar snack=new JFXSnackbar(imgApp);
            snack.show("No internet Connection",4000);
        }

        try {

            imgTypeCombobox.setItems(FXCollections.observableArrayList("Image","Icon"));
            imgTypeCombobox.setValue("Image");
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

            current=new User("aditya","abbc123@gmail.com","72982","bangalore","officer");
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
                                    }
                                });
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
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
            downloadTime.setText("Download Time : "+String.valueOf(System.currentTimeMillis()-s)+" ms");
        } catch (MalformedURLException e) {
            System.out.println(e+"1st");
            // e.printStackTrace();
        } catch(UnknownHostException ex)
        {   System.out.println("snackbar");
            downloadTime.setText("");
            JFXSnackbar snack=new JFXSnackbar(imgApp);
            snack.show("No internet Connection",6000);

        }
        catch (ConnectException ex)
        {
            System.out.println("snackbar");
            downloadTime.setText("");
            JFXSnackbar snack=new JFXSnackbar(imgApp);
            snack.show("No internet Connection",6000);
        }
        catch (IOException e) {
            System.out.println(e+"2nd Ex");
        }
        catch (Exception ex)
        {
            downloadTime.setText("");
            JFXSnackbar snack=new JFXSnackbar(imgApp);
            snack.show("No internet Connection",6000);
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
            downloadTime.setText("Download Time : "+String.valueOf(System.currentTimeMillis()-s)+" ms");
        } catch (MalformedURLException e) {
            System.out.println(e+"1st");
            // e.printStackTrace();
        } catch(UnknownHostException ex)
        {   System.out.println("snackbar");
            downloadTime.setText("");
            JFXSnackbar snack=new JFXSnackbar(imgApp);
            snack.show("No internet Connection",6000);

        }
        catch (ConnectException ex)
        {
            System.out.println("snackbar");
            downloadTime.setText("");
            JFXSnackbar snack=new JFXSnackbar(imgApp);
            snack.show("No internet Connection",6000);
        }
        catch (Exception ex)
        {
            downloadTime.setText("");
            JFXSnackbar snack=new JFXSnackbar(imgApp);
            snack.show("No internet Connection",6000);
            System.out.println(ex+"3rd exception");
        }

    }
    private void addToImglist(String link,String path,String search,int count,String bgcolor )
    {
        System.out.println("ADD"+link+" "+" "+count);
        logArea.clear();
        sb.append("  IMG"+count+" . . . .\n"+"  Sucessfully downloaded"+"\n");
        logArea.setText(sb.toString());
        AnchorPane an=new AnchorPane();
        imgList.setStyle("-fx-background-color:#000;");
        imgList.setExpanded(true);
        imgList.depthProperty().set(1);
        HBox hb=new HBox();
        an.setPrefWidth(544);
        an.setPrefHeight(90);
        ImageView im=new ImageView(new Image(link));
        im.setFitWidth(70);
        im.setFitHeight(70);
        Label lb=new Label("img"+count+"                 ");
        lb.setStyle("-fx-alignment:center;-fx-width:100px;-fx-height:50px;-fx-text-fill:#000;-fx-font-weight:bold;-fx-font-size:20px;-fx-text-align:center;-fx-justify-content:center;");
        lb.setAlignment(Pos.CENTER);
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(im,lb);
        hb.setSpacing(20);
        JFXButton btn1=new JFXButton("Copy URL");
        btn1.addEventHandler(MouseEvent.MOUSE_CLICKED,(e)->{
            Alert al=new Alert(Alert.AlertType.CONFIRMATION);
            al.setContentText("URL copied to Clipboard");
            al.show();
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
        content.setStyle("-fx-background-color:#8000FF;-fx-pref-width:350px;-fx-pref-height:150px;-fx-text-fill:#ff0000;-fx-text-color:#ff0000;");
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
}
