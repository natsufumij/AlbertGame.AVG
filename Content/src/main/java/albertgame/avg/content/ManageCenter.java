package albertgame.avg.content;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
    private Map<String, GameFunction> functionMap;

    private void init() {
        gameData = new GameData();
        header = new GameHeader(gameData);
        manageState = 0;
        functionMap = new HashMap<>();
//        initChapter();
        testLines();
    }

    public void update() {

        //如果正在等待下一条命令，
        //则读取剧本中下一条命令，并执行
        //TODO: 执行剧本的命令，更新剧本执行的相关数据
        if (gameData.getGameState() == GameData.GAME_STATE_WAIT_NEXT) {
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
    private final List<String[]> lines = new ArrayList<>();
    int index = 0;
    int dest;

    private void testLines() {

        functionMap.put("Dialog",new GameFunction.WordFunction());
        functionMap.put("Person",new GameFunction.PersonFunction());
        functionMap.put("Store",new GameFunction.StoreFunction());
        functionMap.put("Audio",new GameFunction.AudioFunction());
        functionMap.put("Select",new GameFunction.SelectFunction());
        functionMap.put("View",new GameFunction.ViewFunction());

        demoline = new Timeline();
        demoline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame frame = new KeyFrame(Duration.millis(30), event -> {
            if(gameData.getGameState()!=GameData.GAME_STATE_WAIT_NEXT)return;

            if (index != dest) {
                String[] nowCmd=lines.get(index);
                GameFunction.FunctionArg arg;
                if(nowCmd.length==3){
                    arg=new GameFunction.FunctionArg(
                            nowCmd[0],nowCmd[1],nowCmd[2],GameFunction.NONE_EXTRA);
                }else if(nowCmd.length==2){
                    arg=new GameFunction.FunctionArg(
                            nowCmd[0],nowCmd[1],"",GameFunction.NONE_EXTRA);
                }else {
                    String[] args=new String[nowCmd.length-3];
                    System.arraycopy(nowCmd, 3, args, 0, args.length);
                    arg=new GameFunction.FunctionArg(
                            nowCmd[0],nowCmd[1],nowCmd[2],args);
                }
                GameFunction function=functionMap.get(arg.type());
                System.out.println("Function: "+arg.type()+","+arg.name()+","+arg.value());
                function.fun(gameData,header,arg);
                ++index;
            }else {
                demoline.stop();
            }
        });
        demoline.getKeyFrames().add(frame);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    File file = ConfigCenter.loadFileInClasspath("demo/demo.avg");
                    if (file.exists() && file.isFile()) {
                        InputStreamReader reader = new InputStreamReader(new FileInputStream(file),
                                StandardCharsets.UTF_8);
                        BufferedReader r = new BufferedReader(reader);
                        String line;
                        while ((line = r.readLine()) != null) {
                            line = line.strip();
                            List<String[]> cmds = parseCmd(line);
                            System.out.println("");
                            for (int i = 0; i != cmds.size(); ++i) {
                                String[] c = cmds.get(i);
                                for (String cs : c) {
                                    System.out.print(cs + ",");
                                }
                                System.out.println("");
                            }
                            lines.addAll(cmds);
                        }
                        lines.removeIf(c -> c.length == 0);
                        succeeded();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            dest = lines.size();
            demoline.play();
        });
        Platform.runLater(task);
    }

    //我是和好的一个人
    //@S  你在说什么?
    //#Person  In  DataId
    //,这是注释
    private List<String[]> parseCmd(String s) {
        if (s.startsWith("#")) {
            String f = s.substring(1);
            f = f.strip();
            return Arrays.asList(new String[]{}, f.split("  "));
        } else if (s.startsWith("@")) {
            String f = s.substring(1);
            f = f.strip();
            String[] x = f.split("  ");
            if (x.length != 2) return Collections.emptyList();

            ArrayList<String[]> arrayList = new ArrayList<>();
            int page = x[1].length() / ConfigCenter.WORD_MAX_SIZE;
            ++page;
            int i;
            for (i = 0; i != page - 1; ++i) {
                String w = x[1].substring(
                        i * ConfigCenter.WORD_MAX_SIZE, (i + 1) * ConfigCenter.WORD_MAX_SIZE);
                String[] cmd = new String[]{"Dialog", "Word", x[0], w};
                arrayList.add(cmd);
            }
            String lastPage = x[1].substring(i * ConfigCenter.WORD_MAX_SIZE);
            String[] cmd = new String[]{"Dialog", "Word", x[0], lastPage};
            arrayList.add(cmd);
            return arrayList;
        } else if (!s.startsWith(",")) {
            String f = s.strip();
            ArrayList<String[]> arrayList = new ArrayList<>();
            int page = f.length() / ConfigCenter.WORD_MAX_SIZE;
            ++page;
            int i;
            for (i = 0; i != page - 1; ++i) {
                String w = f.substring(
                        i * ConfigCenter.WORD_MAX_SIZE, (i + 1) * ConfigCenter.WORD_MAX_SIZE);
                String[] cmd = new String[]{"Dialog", "Word", w};
                arrayList.add(cmd);
            }
            String lastPage = f.substring(i * ConfigCenter.WORD_MAX_SIZE);
            String[] cmd = new String[]{"Dialog", "Word", lastPage};
            arrayList.add(cmd);
            return arrayList;
        } else return Collections.emptyList();
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
        newScene.setOnKeyReleased(event -> {
            if(event.getCode()== KeyCode.ENTER &&
                    ManageCenter.getCenter().gameData.getGameState()==GameData.GAME_STATE_WAIT_PRESS){
                ManageCenter.getCenter().gameData.setGameState(GameData.GAME_STATE_WAIT_NEXT);
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
