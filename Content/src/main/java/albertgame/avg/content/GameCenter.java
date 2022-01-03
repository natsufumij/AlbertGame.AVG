package albertgame.avg.content;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class GameCenter {

    private static GameCenter center;

    public static GameCenter getCenter() {
        if (center == null) {
            synchronized (GameCenter.class) {
                center = new GameCenter();
                center.init();
            }
        }

        return center;
    }

    public static final char MANAGE_START_SCENE = 0;
    public static final char MANAGE_GAME_SCENE = 1;
    public static final char MANAGE_STORE_SCENE = 2;

    private GameCenter() {
    }

    private char manageState;

    private Parent nowScene;

    private Stage stage;

    //游戏数据
    private GameData gameData;
    //游戏场景界面
    private GameHeader header;

    //游戏功能
    private Map<String, GameFunction> functionMap;

    public static int storeSelect;

    private void init() {
        storeSelect = 0;
        manageState = 0;
        gameData = new GameData();
        header = new GameHeader(gameData);
        functionMap = new HashMap<>();
    }

    private void handleArg(GameFunction.FunctionArg arg) {
        GameFunction function = this.functionMap.get(arg.type());
        function.fun(gameData, header, arg);
    }

    private void initGame() {

        //导入基本配置
        gameData.setGlobalConfig(ConfigCenter.loadGlobalConfig());
        //添加人物信息
        for (Play.GlobalConfig.PersonConfig pc : gameData.getGlobalConfig().personConfigs().values()) {
            Person.PersonData pd = new Person.PersonData(pc.id(), pc.name(), pc.state().get(0), pc.state());
            gameData.getPersonDataMap().put(pd.id(), pd);
        }

        //导入缓存
        if (ConfigCenter.isCacheExist(GameCenter.storeSelect)) {
            //存在存档
            Map<String, String> p = ConfigCenter.loadCacheData(GameCenter.storeSelect);

            gameData.setProperties(ConfigCenter.loadCache(GameCenter.storeSelect));
            gameData.getData().putAll(p);
            restoreGame();
        } else {
            gameData.loadFromGlobal();
            System.out.println("");
        }
    }

    private void restoreGame() {
        String chapter = gameData.getProperties().getProperty("chapter");
        String play = gameData.getProperties().getProperty("play");
        String struck = gameData.getProperties().getProperty("struck");
        String scene = gameData.getProperties().getProperty("scene");
        String bgm = gameData.getProperties().getProperty("bgm");
        String name = gameData.getProperties().getProperty("name");
        String word = gameData.getProperties().getProperty("word");
        String wordType = gameData.getProperties().getProperty("wordtype");
        String[] personIn = gameData.getProperties().getProperty("personin").split(",");
        String leftP = gameData.getProperties().getProperty("leftp");
        String centerP = gameData.getProperties().getProperty("centerp");
        String rightP = gameData.getProperties().getProperty("rightp");
        boolean wordpanelshow = Boolean.parseBoolean(gameData.getProperties().getProperty("wordpanelshow"));
        boolean maskshow = Boolean.parseBoolean(gameData.getProperties().getProperty("maskshow"));

        gameData.setNowChapter(ConfigCenter.loadChapter(chapter));
        gameData.setNowPlay(ConfigCenter.loadPlayInClasspath(chapter, play));
        gameData.setStruck(gameData.getNowPlay().bodyStruckMap().get(struck));
        gameData.setLineIndex(Integer.parseInt(gameData.getProperties().getProperty("index")));

        Image sceneI = ConfigCenter.loadScene(scene);
        gameData.backgroundImageProperty().set(sceneI);
        gameData.nameDisplayProperty().set(name);

        List<GameFunction.FunctionArg> argList = new ArrayList<>();

        GameFunction.FunctionArg arg = new GameFunction.FunctionArg(
                "Audio", "Bgm.Play", bgm, GameFunction.NONE_EXTRA);
        argList.add(arg);

        for (String s : personIn) {
            GameFunction.FunctionArg arg2 = new GameFunction.FunctionArg(
                    "Person", "In", s, GameFunction.NONE_EXTRA);
            argList.add(arg2);
        }

        if (leftP != null) {
            GameFunction.FunctionArg arg3 = new GameFunction.FunctionArg(
                    "Person", "Show", "L", new String[]{leftP});
            argList.add(arg3);
        }
        if (centerP != null) {
            GameFunction.FunctionArg arg3 = new GameFunction.FunctionArg(
                    "Person", "Show", "C", new String[]{centerP});
            argList.add(arg3);
        }
        if (rightP != null) {
            GameFunction.FunctionArg arg3 = new GameFunction.FunctionArg(
                    "Person", "Show", "R", new String[]{rightP});
            argList.add(arg3);
        }
        gameData.wordLineShowProperty().set(wordpanelshow);
        gameData.maskShowProperty().set(maskshow);
        GameFunction.FunctionArg arg1 = null;
        if (wordType.startsWith("@")) {
            String dataId = wordType.substring(1);
            arg1 = new GameFunction.FunctionArg("Dialog", "Word", dataId, new String[]{word});
        } else if (wordType.equals("P")) {
            arg1 = new GameFunction.FunctionArg("Dialog", "Word", word, GameFunction.NONE_EXTRA);
        } else if (wordType.equals("M") || wordType.equals("S")) {
            arg1 = new GameFunction.FunctionArg("Dialog", "Word", wordType, new String[]{word});
        }

        if (arg1 != null) {
            argList.add(arg1);
        }

        for (GameFunction.FunctionArg arg2 : argList) {
            handleArg(arg2);
        }
    }

    public void loadFromGlobalConfig() {

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

        GameFunction.FunctionArg arg1 = new GameFunction.FunctionArg(
                "Person", "In", "bishojo", GameFunction.NONE_EXTRA);
        GameFunction.FunctionArg arg2 = new GameFunction.FunctionArg(
                "Person", "Show", "R", new String[]{"bishojo"}
        );
        GameFunction.FunctionArg arg3 = new GameFunction.FunctionArg(
                "Person", "Hide", "R", GameFunction.NONE_EXTRA
        );

        //测试人物功能
        GameFunction.PersonFunction personFunction = new GameFunction.PersonFunction();
        personFunction.fun(gameData, header, arg1);
        personFunction.fun(gameData, header, arg2);
        personFunction.fun(gameData, header, arg3);

        GameFunction.FunctionArg arg4 = new GameFunction.FunctionArg(
                "Audio", "Bgm.Play", "bgm1", GameFunction.NONE_EXTRA
        );
        GameFunction.FunctionArg arg5 = new GameFunction.FunctionArg(
                "Audio", "Sound.Play", "audio1", GameFunction.NONE_EXTRA);

        //测试音频功能
        GameFunction.AudioFunction audioFunction = new GameFunction.AudioFunction();
        audioFunction.fun(gameData, header, arg4);
        audioFunction.fun(gameData, header, arg5);

        GameFunction.FunctionArg arg = new GameFunction.FunctionArg(
                "View", "Scene", "demo2", GameFunction.NONE_EXTRA);
        GameFunction.ViewFunction viewFunction = new GameFunction.ViewFunction();

        GameFunction.FunctionArg arg7 = new GameFunction.FunctionArg(
                "View", "Lighting", "3000", GameFunction.NONE_EXTRA);

//        viewFunction.fun(gameData,header,arg);
//        viewFunction.fun(gameData,header,arg7);

        GameFunction.FunctionArg arg8 = new GameFunction.FunctionArg("Select", "Go", "ABC", new String[]{
                "这是一条很好的道路",
                "我才不想走呢",
                "如果你一定要这样子的话，那我……"
        });
        GameFunction.SelectFunction function = new GameFunction.SelectFunction();
        function.fun(gameData, header, arg8);
    }

    private Timeline demoline;

    private void testLines() {

        functionMap.put("Dialog", new GameFunction.WordFunction());
        functionMap.put("Person", new GameFunction.PersonFunction());
        functionMap.put("Store", new GameFunction.StoreFunction());
        functionMap.put("Audio", new GameFunction.AudioFunction());
        functionMap.put("Select", new GameFunction.SelectFunction());
        functionMap.put("View", new GameFunction.ViewFunction());

        demoline = new Timeline();
        demoline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame frame = new KeyFrame(Duration.millis(30), event -> {
            if (gameData.getGameState()
                    == GameData.GAME_STATE_WAIT_NEXT &&
                    !gameData.isStruckEnd()) {
                GameFunction.FunctionArg arg = gameData.getArgAndSetNext();
                handleArg(arg);
            } else if (gameData.getGameState() == GameData.GAME_STATE_WAIT_NEXT
                    && gameData.isStruckEnd()) {
                gameData.nextPlayLine();
            }
        });
        demoline.getKeyFrames().add(frame);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    initGame();
                    succeeded();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            demoline.play();
        });

        Platform.runLater(task);
    }

    public Parent getNowScene() {
        return nowScene;
    }

    public char getManageState() {
        return manageState;
    }

    public void setManageState(char manageState) {
        if (this.manageState == manageState) return;

        switch (manageState) {
            case MANAGE_START_SCENE:
                return;
            case MANAGE_STORE_SCENE:
                nowScene = new StoreHeader();
                break;
            case MANAGE_GAME_SCENE:
                nowScene = header;
                break;
        }

        this.manageState = manageState;
        Scene newScene = new Scene(nowScene, ConfigCenter.WINDOW_WIDTH,
                ConfigCenter.WINDOW_HEIGHT);
        newScene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER &&
                    GameCenter.getCenter().gameData.getGameState() == GameData.GAME_STATE_WAIT_PRESS) {
                GameCenter.getCenter().gameData.setGameState(GameData.GAME_STATE_WAIT_NEXT);
            }
        });

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
