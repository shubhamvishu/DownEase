package sample;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.ResourceBundle;


public class Controller implements Initializable{


    public static Stage primaryStage=new Stage();

    private String otp=null;
    @FXML
    private StackPane stack1;
    @FXML
    private StackPane stack2;

    @FXML
    private JFXTextField user=null;

    @FXML
    private JFXPasswordField pwd=null;

    @FXML
    private CheckBox checkbox;

    @FXML
    private JFXButton signin;

    @FXML
    private JFXButton logout;

    @FXML
    private Label lab2;
    @FXML
    private AnchorPane signup;

    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton signupbtn;

    @FXML
    private TextField name;

    @FXML
    private TextField emailid;

    @FXML
    private TextField phno;

    @FXML
    private TextField loc;

    @FXML
    private TextField occ;

    @FXML
    private JFXTextField otpverify;

    private String tempMail;
    private boolean isdialogopen=false;

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

    @FXML
    private void login(ActionEvent event) throws IOException ,InterruptedException{

        try {
            if (!(user.getText().isEmpty()) && !(pwd.getText().isEmpty())) {   //System.out.println("shubham");
                User u = new User(user.getText(), pwd.getText());
                if (u.authenticate()) {
                    User.currUser=u;
                    System.out.println("Login succesfull");
                    successDialog(stack1, "Login succesfull");
                    System.out.println(Thread.currentThread());
                    logout.setDisable(true);
                    if (!checkbox.isSelected()) {
                        SplashController.loginStage.close();
                    }
                    Parent root = FXMLLoader.load(getClass().getResource("design/mainscreen.fxml"));
                    primaryStage.setTitle("DownEase");
                    primaryStage.resizableProperty().setValue(false);
                    //primaryStage.initStyle(StageStyle.UNDECORATED);
                    Scene scene=new Scene(root);
                    scene.getStylesheets().add(Controller.class.getResource("snack.css").toExternalForm());
                    primaryStage.setScene(scene);
                    primaryStage.show();

                } else {
                    System.out.println("Login failed");
                    failedDialog(stack1, "Login failed");
                }
            } else {
                showSnack("Required field empty");
                System.out.println("Mandatory fill");
            }
        }catch (Exception ex)
        {
            System.out.println("Ex: "+ex);
        }
    }

    @FXML
    private void newuser(ActionEvent event) {


        Random rand=new Random();
        otp=String.valueOf(rand.nextInt(10000));
        System.out.println("otp:"+otp);
        try {
            if (emailid.getText() != null && emailid.getText().endsWith("@gmail.com")) {
                SendMail sendMail = new SendMail(emailid.getText(), "Your one time password for DownEase : "+otp);
                sendMail.send();
                tempMail = emailid.getText();
                JFXSnackbar snack=new JFXSnackbar(root);
                snack.show("Mail successfullt sent",4000);
            } else {
                Alert al = new Alert(Alert.AlertType.ERROR);
                al.setContentText("Invalid email");
                al.show();
            }
        }catch (Exception ex)
        {
            System.out.println("Exception here");
            emailid.setText("");
            failedDialog(stack2,"Invalid email");
        }

    }
    @FXML
    private void checkuser(ActionEvent event) throws FileNotFoundException
    {
        System.out.println("OTP: "+otpverify.getText());
        if(otpverify.getText().equals(otp) && !name.getText().isEmpty() && emailid.getText().equals(tempMail) && !phno.getText().isEmpty() && !loc.getText().isEmpty() && !occ.getText().isEmpty())
        {
            chooseUserPass();
        }
        else{
            failedDialog(stack2,"Wrong details");
        }
    }
    private void chooseUserPass()
    {
        User curr=new User(name.getText(),emailid.getText(),phno.getText(),loc.getText(),occ.getText());
        JFXDialogLayout content=new JFXDialogLayout();
        VBox vb=new VBox();
        vb.setPrefWidth(325);
        JFXTextField username=new JFXTextField();
        username.setPromptText("Username");
        JFXPasswordField pwd1=new JFXPasswordField();
        pwd1.setPromptText("Password");
        JFXPasswordField pwd2=new JFXPasswordField();
        pwd2.setPromptText("Confirm Password");
        vb.getChildren().addAll(username,pwd1,pwd2);
        content.setBody(vb);
        JFXDialog dialog = new JFXDialog(stack2, content, JFXDialog.DialogTransition.LEFT);
        content.setPrefWidth(325);
        dialog.setContent(content);
        JFXButton button = new JFXButton("Okay");
        button.setStyle("-fx-background-color:#1ABC9C;-fx-text-fill:#fff;-fx-font-weight:bold;-fx-pref-width:100px;-fx-pref-height:40px;-fx-background-radius:20px;-fx-border-radius:20px;");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    if(pwd1.getText().equals(pwd2.getText()) && username.getLength()>=5 && pwd1.getLength()>=6)
                    {
                        System.out.println("Macthing");
                        curr.setUser(username.getText());
                        curr.setPass(pwd1.getText());
                        if(curr.authenticate())
                        {
                            System.out.println("Signin failed...already exits");
                            failedDialog(stack2,"Record Exists");

                        }
                        else{
                            System.out.println();
                            curr.addNewUser();
                            dialog.close();
                            successDialog(stack2,"SignUp succesfull");
                            clearSignUp();
                        }

                    }
                    else{
                        failedDialog(stack2,"Wrong username/password");
                        System.out.println("Not Macthing");
                    }
                    // Thread.sleep(2000);
                }
                catch(Exception ex)
                {System.out.println(ex);}
            }
        });
        content.setActions(button);
        dialog.show();
    }
    private void successDialog(StackPane stackPane,String str) throws InterruptedException
    {
        JFXDialogLayout content=new JFXDialogLayout();
        HBox hb=new HBox();
        Label lb=new Label(str);
        lb.setStyle("-fx-font-weight:bold;-fx-text-fill:#000;-fx-prewf-width:300px;");
        lb.setMinWidth(200);
        ImageView im=new ImageView(new Image("sample/img/success.png"));
        im.setFitWidth(50);
        im.setFitHeight(50);
        hb.getChildren().addAll(lb,im);
        content.setStyle("-fx-background-color:#ddd;-fx-pref-width:250px;-fx-pref-height:50px;-fx-text-fill:#000;-fx-text-color:#000;");
        content.setBody(hb);
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);
        dialog.setContent(content);
        JFXButton button = new JFXButton("Okay");
        button.setStyle("-fx-background-color:#333;-fx-text-fill:#fff;-fx-font-weight:bold;-fx-pref-width:100px;-fx-pref-height:40px;-fx-background-radius:20px;-fx-border-radius:20px;");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        content.setActions(button);
        dialog.show();
        Thread.sleep(1000);
        isdialogopen=true;
        System.out.println(Thread.currentThread());
        //Thread.sleep(2000);
        System.out.println("Correct");
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
    private void clearSignUp()
    {
        otp="";
        otpverify.clear();
        name.clear();
        emailid.clear();
        phno.clear();
        loc.clear();
        occ.clear();
    }
    private void showNoConnection()
    {
        JFXSnackbar snack=new JFXSnackbar(root);
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
        JFXSnackbar snack=new JFXSnackbar(root);
        EventHandler handler=new EventHandler() {
            @Override
            public void handle(Event event) {
                snack.close();
            }
        };
        snack.show(str,2000);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if(!isNetAvailable())
        {
            showNoConnection();
        }
        user.setText("");
        pwd.setText("");
        logout.setDisable(true);
        /*ImageView imvw=new ImageView(new Image("sample/img/user.png"));
        imvw.setFitHeight(30);
        imvw.setFitWidth(30);
        lab2.setGraphic(imvw);*/

    }
}
