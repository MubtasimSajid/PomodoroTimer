import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("startingScene.fxml"));
            Scene scene = new Scene(root);
            Image icon = new Image("icon.jpg");

            stage.getIcons().add(icon);
            stage.setTitle("Pomodoro Timer");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}