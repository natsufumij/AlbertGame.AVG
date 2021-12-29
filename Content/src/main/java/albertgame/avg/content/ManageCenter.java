package albertgame.avg.content;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ManageCenter {

    private static ManageCenter center;

    public static ManageCenter getCenter() {
        if (center == null) {
            synchronized (ManageCenter.class) {
                center = new ManageCenter();
                center.init();
            }
        }

        return center;
    }

    public static final char MANAGE_START_SCENE = 0;
    public static final char MANAGE_GAME_SCENE = 1;
    public static final char MANAGE_STORE_SCENE = 2;

    private ManageCenter() {
    }

    private char manageState;

    private Parent nowScene;

    private Stage stage;

    //游戏数据
    private GameData gameData;
    //游戏场景界面
    private GameHeader header;

    private void init() {
        gameData = new GameData();
        header = new GameHeader(gameData);
        manageState = 0;
    }

    public void update() {

        //如果正在等待下一条命令，
        //则读取剧本中下一条命令，并执行
        //TODO: 执行剧本的命令，更新剧本执行的相关数据
    }

    public Parent getNowScene() {
        return nowScene;
    }

    public char getManageState() {
        return manageState;
    }

    public void setManageState(char manageState) {
        if (this.manageState == manageState) return;

        //TODO: 等待完善开始界面与存档界面
        switch (manageState) {
            case 0:
            case 2:
                return;
            case 1:
                nowScene = header;
                break;
        }

        this.manageState = manageState;
        Scene newScene = new Scene(nowScene, ConfigCenter.WINDOW_WIDTH,
                ConfigCenter.WINDOW_HEIGHT);
        stage.setScene(newScene);
    }

    public GameData getGameData() {
        return gameData;
    }

    public GameHeader getHeader() {
        return header;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setResizable(false);
    }
}
