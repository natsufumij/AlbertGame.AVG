package albertgame.avg.content.n2;

import albertgame.avg.content.ConfigCenter;
import albertgame.avg.content.n2.lifes.game.GameFaceLife;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainEntry extends Application {

    public static GameController Controller() {
        return _controller;
    }

    private static GameController _controller;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle(ConfigCenter.WINDOW_TITLE);
        primaryStage.getIcons().add(ConfigCenter.WINDOW_ICON);

        _controller =GameController.gameController(
                ConfigCenter.WINDOW_WIDTH,ConfigCenter.WINDOW_HEIGHT,
                primaryStage,new StoreLife());
        _controller.initController();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
