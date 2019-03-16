package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
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
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable{
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
    void newuser(ActionEvent event) {

        String otp="127675";
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
    void checkuser(ActionEvent event) {
        if(age.getText().equals("127675"))
        {
            System.out.println("Correct");
        }
        else{
            System.out.println("Incorrect");
            JFXDialogLayout content = new JFXDialogLayout();
            content.setBody(new Label("Wrong OTP"));
            JFXDialog dialog = new JFXDialog(stack, content, JFXDialog.DialogTransition.LEFT);
            content.setStyle("-fx-background-color:#2ECC71;-fx-pref-width:150px;-fx-pref-height:50px;-fx-text-fill:#ff0000;-fx-text-color:#ff0000;");
            dialog.setContent(content);
            JFXButton button = new JFXButton("Okay");
            button.setStyle("-fx-background-color:#fff;-fx-text-fill:#000;-fx-font-weight:bold;-fx-pref-width:150px;-fx-pref-height:40px;-fx-background-radius:20px;-fx-border-radius:20px;");
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
        ImageView imvw=new ImageView(new Image("sample/img/user.png"));
        imvw.setFitHeight(30);
        imvw.setFitWidth(30);
        lab2.setGraphic(imvw);
    }
}
