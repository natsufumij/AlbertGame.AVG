package albertgame.avg.editor;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainEntry extends Application {
    @Override
    public void start(Stage primaryStage){

        Scene scene=new Scene(new Group(),ConfigM.WINDOW_WIDTH,
                ConfigM.WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
