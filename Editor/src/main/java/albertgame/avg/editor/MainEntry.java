package albertgame.avg.editor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MainEntry extends Application {
    @Override
    public void start(Stage primaryStage) {

        try {
//            File f=new File("Assets/form.fxml");
//            Parent parent = FXMLLoader.load(f.toURI().toURL());
            Parent parent=FXMLLoader.load(MainEntry.class.getResource("a/form.fxml"));
            URL url=MainEntry.class.getResource("a/1.avg");
            System.out.println("url0:"+url.getPath());
            url=MainEntry.class.getResource("config/images/bishojo_1.png");
            System.out.println("url1:"+url.toExternalForm());
            Scene scene = new Scene(parent);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
