package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {

    @FXML
    private StackPane rootPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new SplashScreen().start();
    }
    class SplashScreen extends Thread{
        @Override
        public void run()
        {
            try{
                System.out.println("Ready");
                Thread.sleep(4000);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try{

                            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
                            Stage primaryStage=new Stage();
                            Scene scene=new Scene(root);
                            primaryStage.setTitle("Hello World");
                            primaryStage.resizableProperty().setValue(false);
                            primaryStage.getIcons().add(new Image("sample/img/astronaut-2.png"));
                            primaryStage.setScene(scene);
                            primaryStage.show();
                            rootPane.getScene().getWindow().hide();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
            catch (InterruptedException ex)
            {
                System.out.println(ex);
            }
        }
    }
}
