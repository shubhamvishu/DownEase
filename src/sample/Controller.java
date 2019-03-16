package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
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
    private Label lab2;
    @FXML
    private AnchorPane signup;

    @FXML
    private AnchorPane rootAnc;

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
    private TextField age;

    @FXML
    private JFXTextField otpverify;

    @FXML
    void newuser(ActionEvent event) {


        Random rand=new Random();
        otp=String.valueOf(rand.nextInt(10000));
        System.out.println("otp:"+otp);
        if(emailid.getText()!=null && emailid.getText().endsWith("@gmail.com")) {
            SendMail send = new SendMail(emailid.getText(), otp);
        }
        else{
            Alert al=new Alert(Alert.AlertType.ERROR);
            al.setContentText("Invalid email");
            al.show();
        }

    }
    @FXML
    void checkuser(ActionEvent event) throws FileNotFoundException {
        System.out.println("OTP: "+age.getText()+" "+otpverify.getText());
        if(otpverify.getText().equals(otp))
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
        else{
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
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*ImageView imvw=new ImageView(new Image("sample/img/user.png"));
        imvw.setFitHeight(30);
        imvw.setFitWidth(30);
        lab2.setGraphic(imvw);*/

    }
}
