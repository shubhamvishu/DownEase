package sample;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;


public class Controller implements Initializable{

    private String otp=null;
    @FXML
    private StackPane stack;
    @FXML
    private JFXButton signin;

    @FXML
    private JFXTextField user=null;

    @FXML
    private JFXPasswordField pwd=null;

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
    @FXML
    void login(ActionEvent event)
    {

        if(!(user.getText().isEmpty()) && !(pwd.getText().isEmpty()))
        {   //System.out.println("shubham");
            User u=new User(user.getText(),pwd.getText());
            if(u.authenticate())
            {
                System.out.println("Login succesfull");
            }
            else{
                System.out.println("Login failed");
            }
        }
        else{
            System.out.println("Mandatory fill");
        }
    }

    @FXML
    void newuser(ActionEvent event) {


        Random rand=new Random();
        otp=String.valueOf(rand.nextInt(10000));
        System.out.println("otp:"+otp);
        if(emailid.getText()!=null && emailid.getText().endsWith("@gmail.com")) {
            SendMail send = new SendMail(emailid.getText(), otp);
            tempMail=emailid.getText();
        }
        else{
            Alert al=new Alert(Alert.AlertType.ERROR);
            al.setContentText("Invalid email");
            al.show();
        }

    }
    @FXML
    void checkuser(ActionEvent event) throws FileNotFoundException {
        System.out.println("OTP: "+otpverify.getText());
        if(otpverify.getText().equals(otp) && !name.getText().isEmpty() && emailid.getText().equals(tempMail) && !phno.getText().isEmpty() && !loc.getText().isEmpty() && !occ.getText().isEmpty())
        {
            chooseUserPass();
        }
        else{
            failedSignUp();
        }
    }
    public void chooseUserPass()
    {
        User curr=new User(name.getText(),emailid.getText(),phno.getText(),loc.getText(),occ.getText());
        JFXDialogLayout content=new JFXDialogLayout();
        VBox vb=new VBox();
        JFXTextField username=new JFXTextField();
        username.setPromptText("Username");
        JFXPasswordField pwd1=new JFXPasswordField();
        pwd1.setPromptText("Password");
        JFXPasswordField pwd2=new JFXPasswordField();
        pwd2.setPromptText("Confirm Password");
        vb.getChildren().addAll(username,pwd1,pwd2);
        vb.setMinWidth(300);
        content.setBody(vb);
        JFXDialog dialog = new JFXDialog(stack, content, JFXDialog.DialogTransition.LEFT);
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
                            failedSignUp();

                        }
                        else{
                            System.out.println("Signin succesfull");
                            curr.addNewUser();
                            dialog.close();
                            successSignUp();
                        }

                    }
                    else{
                        failedSignUp();
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
    public void successSignUp()
    {
        JFXDialogLayout content=new JFXDialogLayout();
        HBox hb=new HBox();
        Label lb=new Label("Success");
        lb.setStyle("-fx-font-weight:bold;-fx-text-fill:#000;-fx-prewf-width:300px;");
        lb.setMinWidth(200);
        ImageView im=new ImageView(new Image("sample/img/success.png"));
        im.setFitWidth(50);
        im.setFitHeight(50);
        hb.getChildren().addAll(lb,im);
        content.setStyle("-fx-background-color:#ddd;-fx-pref-width:250px;-fx-pref-height:50px;-fx-text-fill:#000;-fx-text-color:#000;");
        content.setBody(hb);
        JFXDialog dialog = new JFXDialog(stack, content, JFXDialog.DialogTransition.TOP);
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
        System.out.println("Correct");
    }
    public void failedSignUp()
    {
        System.out.println("Incorrect");
        JFXDialogLayout content=new JFXDialogLayout();
        HBox hb=new HBox();
        Label lb=new Label("Authentication failed");
        lb.setStyle("-fx-font-weight:bold;-fx-text-fill:#000;-fx-prewf-width:300px;");
        lb.setMinWidth(200);
        hb.getChildren().addAll(lb);
        content.setStyle("-fx-background-color:#ddd;-fx-pref-width:250px;-fx-pref-height:50px;-fx-text-fill:#000;-fx-text-color:#000;");
        content.setBody(hb);
        JFXDialog dialog = new JFXDialog(stack, content, JFXDialog.DialogTransition.LEFT);
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user.setText("");
        pwd.setText("");
        /*ImageView imvw=new ImageView(new Image("sample/img/user.png"));
        imvw.setFitHeight(30);
        imvw.setFitWidth(30);
        lab2.setGraphic(imvw);*/

    }
}
