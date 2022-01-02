package albertgame.avg.content;

import javafx.application.Application;
import javafx.stage.Stage;

public class RunMain extends Application {

    @Override
    public void start(Stage primaryStage){

        GameCenter center= GameCenter.getCenter();
        center.setStage(primaryStage);

        primaryStage.setTitle(ConfigCenter.WINDOW_TITLE);
        primaryStage.getIcons().add(ConfigCenter.WINDOW_ICON);

        center.setManageState(GameCenter.MANAGE_STORE_SCENE);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
