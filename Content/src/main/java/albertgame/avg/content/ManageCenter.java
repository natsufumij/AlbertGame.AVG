package albertgame.avg.content;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

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
    //游戏功能
    private Map<String,GameFunction> functionMap;

    private void init() {
        gameData = new GameData();
        header = new GameHeader(gameData);
        manageState = 0;
        functionMap=new HashMap<>();
        functionMap.put("Dialog",new GameFunction.WordFunction());
        initChapter();
    }

    public void update() {

        //如果正在等待下一条命令，
        //则读取剧本中下一条命令，并执行
        //TODO: 执行剧本的命令，更新剧本执行的相关数据
        if(gameData.getGameState()==GameData.GAME_STATE_WAIT_NEXT){
            gameData.nextPlayLine();
        }
    }

    public void initChapter() {

        //TODO: 初始化章节内容，本次用以测试剧本解析是否正常
        //测试会话功能
//        GameFunction.WordFunction function=new GameFunction.WordFunction();
//        GameFunction.FunctionArg arg=new GameFunction.FunctionArg("Dialog","Word","S",new String[]{
//                "这真是一个好日子啊啊啊啊啊啊啊啊啊啊啊 啊啊啊啊"
//        });
//        GameFunction.FunctionArg arg0=new GameFunction.FunctionArg("Dialog","Clear","", GameFunction.NONE_EXTRA);
//        function.fun(gameData,header, arg0);
//        function.fun(gameData,header,arg);

        GameFunction.FunctionArg arg1=new GameFunction.FunctionArg(
                "Person","In","bishojo",GameFunction.NONE_EXTRA);
        GameFunction.FunctionArg arg2=new GameFunction.FunctionArg(
                "Person","Show","R",new String[]{"bishojo"}
        );
        GameFunction.FunctionArg arg3=new GameFunction.FunctionArg(
                "Person","Hide","R",GameFunction.NONE_EXTRA
        );

        //测试人物功能
        GameFunction.PersonFunction personFunction=new GameFunction.PersonFunction();
        personFunction.fun(gameData,header,arg1);
        personFunction.fun(gameData,header,arg2);
        personFunction.fun(gameData,header,arg3);

        GameFunction.FunctionArg arg4=new GameFunction.FunctionArg(
                "Audio","Bgm.Play","bgm1",GameFunction.NONE_EXTRA
        );
        GameFunction.FunctionArg arg5=new GameFunction.FunctionArg(
                "Audio","Sound.Play","audio1",GameFunction.NONE_EXTRA);

        //测试音频功能
        GameFunction.AudioFunction audioFunction=new GameFunction.AudioFunction();
        audioFunction.fun(gameData,header,arg4);
        audioFunction.fun(gameData,header,arg5);

        GameFunction.FunctionArg arg6=new GameFunction.FunctionArg(
                "Select","SwitchA","你喜欢什么样的游戏？",
                new String[]{
                   "galgame","动作类","音乐类","你猜？"
                });

        //测试选择功能
        GameFunction.SelectFunction selectFunction=new GameFunction.SelectFunction();
        selectFunction.fun(gameData,header,arg6);
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

    public Map<String, GameFunction> getFunctionMap() {
        return functionMap;
    }
}
