package albertgame.avg.content.n2.lifes.game;

import albertgame.avg.content.ConfigCenter;
import albertgame.avg.content.Person;
import albertgame.avg.content.Play;
import albertgame.avg.content.n2.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.*;

public class GameFaceLife implements FaceLife {

    public static final int GAME_STATE_WAIT_PRESS = 0;
    public static final int GAME_STATE_WAIT_NEXT = 1;
    public static final int GAME_STATE_SELECTING = 2;
    public static final int GAME_STATE_WORD_DISPLAYING = 3;

    static class SkipEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            System.out.println("Skip");
        }
    }

    static class AutoEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            System.out.println("Auto");
        }
    }

    static class SaveEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    super.succeeded();
                    return null;
                }
            };
            task.setOnSucceeded(event1 -> System.out.println("保存成功"));
            Platform.runLater(task);
        }
    }

    static class LoadEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            //TODO: 调出存档界面
        }
    }

    static class BackEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            //TODO: 返回主界面
        }
    }


    @Override
    public GameController.KeyInput handlerKeys() {
        EventHandler<KeyEvent> p = event -> {
        };
        EventHandler<KeyEvent> r = event -> {
            System.out.println("release...");
            FaceHandler.Arg arg=new FaceHandler.Arg("Dialog","Word",new String[]{"M","你好啊啊啊"});
            MainEntry.Controller().handleArguments(arg);
        };
        return new GameController.KeyInput(p, r);
    }


    @Override
    public void init(FaceData d, FaceHead h, Map<String, FaceHandler> p) {
        initData(d);
        initHead(d, h);
        initBind(d,h);
        initGame(d,h);
        p.put("Dialog",new FaceHandlers.DialogHandler());
    }

    private void initGame(FaceData data,FaceHead head){
        Play.GlobalConfig globalConfig=ConfigCenter.loadGlobalConfig();
        data.setObj("globalConfig",globalConfig);
        Map<String, Person.PersonData> personDataMap= (Map<String, Person.PersonData>) data.getObj("personDataMap");
        //添加人物信息
        for (Play.GlobalConfig.PersonConfig pc : globalConfig.personConfigs().values()) {
            Person.PersonData pd = new Person.PersonData(pc.id(), pc.name(), pc.state().get(0), pc.state());
            personDataMap.put(pd.id(), pd);
        }

        int index=Integer.parseInt(
                MainEntry.Controller().getData().get("index"));
        //导入缓存
        if (ConfigCenter.isCacheExist(index)) {
            //存在存档
            Map<String, String> p = ConfigCenter.loadCacheData(index);

            data.property("selects").putAll(p);
            data.property("cache").putAll(ConfigCenter.loadCache(index));
            restoreGame(data,head);
        } else {
            loadFromGlobal();
            System.out.println("");
        }
    }

    private void loadFromGlobal() {

    }

    private void restoreGame(FaceData data,FaceHead head) {


        Properties p=ConfigCenter.loadCache(
                Integer.parseInt(MainEntry.Controller().getData().get("index")));
        Properties properties=data.property("cache");
        properties.putAll(p);
        
        String chapter = properties.getProperty("chapter");
        String play = properties.getProperty("play");
        String struck = properties.getProperty("struck");
        String scene = properties.getProperty("scene");
        String bgm = properties.getProperty("bgm");
        String name = properties.getProperty("name");
        String word = properties.getProperty("word");
        String wordType = properties.getProperty("wordtype");
        String[] personIn = properties.getProperty("personin").split(",");
        String leftP = properties.getProperty("leftp");
        String centerP = properties.getProperty("centerp");
        String rightP = properties.getProperty("rightp");
        boolean wordpanelshow = Boolean.parseBoolean(properties.getProperty("wordpanelshow"));
        boolean maskshow = Boolean.parseBoolean(properties.getProperty("maskshow"));

        Play.Chapter c=ConfigCenter.loadChapter(chapter);
        Play play1=ConfigCenter.loadPlayInClasspath(chapter,play);
        Play.BodyStruck bodyStruck=play1.bodyStruckMap().get(struck);
        int index=Integer.parseInt(properties.getProperty("index"));
        data.setObj("nowChapter",c);
        data.setObj("nowPlay",play1);
        data.setObj("nowStruck",bodyStruck);
        data.intPro("nowIndex").set(index);

        Image sceneI = ConfigCenter.loadScene(scene);
        ((ImageView)head.fetch("scene")).setImage(sceneI);
        data.strPro("nameDisplay").set(name);

        List<FaceHandler.Arg> list=new ArrayList<>();
        //TODO: 导入数据的任务仍需继续完成
    }

    @Override
    public void update(FaceData d, FaceHead h) {

    }

    @Override
    public void pause(FaceData d, FaceHead h) {

    }

    @Override
    public void resume(FaceData d, FaceHead h) {

    }

    @Override
    public void stop(FaceData d, FaceHead h) {

    }

    private void initData(FaceData data) {
        data.strPro("leftPerson");
        data.strPro("centerPerson");
        data.strPro("rightPerson");

        data.boolPro("selectShow");
        data.boolPro("wordPanelShow");
        data.boolPro("maskShow");
        data.boolPro("struckEnd");
        data.boolPro("auto");

        data.setObj("personDataMap",new HashMap<String, Person.PersonData>());
        data.setObj("playedPersons",new HashMap<String,Person>());
        data.setObj("globalConfig",ConfigCenter.loadGlobalConfig());

        //导入数据方法
//        data.objectPro("nowChapter");
//        data.objectPro("nowPlay");
//        data.objectPro("nowStruck");

        data.intPro("nowIndex");
        data.intPro("gameState");

        data.strPro("nowSelectId");
        data.strPro("nameDisplay");

        int max = ConfigCenter.WORD_LINE_ROW * ConfigCenter.WORD_LINE_COLUMN;
        for (int i = 0; i != max; ++i) {
            int cx = i / ConfigCenter.WORD_LINE_COLUMN;
            int cy = i % ConfigCenter.WORD_LINE_COLUMN;
            data.strPro(findWordAt(cx, cy));
        }

        for (int i = 0; i != selectText.length; ++i) {
            data.strPro(findSelectAt(i));
        }

        //缓存的数据
        data.property("cache");
        //缓存的选择分支数据
        data.property("selects");
    }


    private static final String[] buttonNames = {
            "SKI P", "AUTO", "SAVE", "LOAD", "BACK"
    };

    ImageView leftPerson;
    ImageView centerPerson;
    ImageView rightPerson;

    Rectangle globalMask;

    final Text[] buttonList = new Text[buttonNames.length];
    final Text[] words=new Text[ConfigCenter.WORD_MAX_SIZE];
    final Text[] selectText = new Text[ConfigCenter.SELECT_MAX_SIZE];
    final Rectangle[] selectMasks = new Rectangle[ConfigCenter.SELECT_MAX_SIZE];

    Text name;
    Rectangle wordPaneFrame;

    private void initHead(FaceData data, FaceHead head) {
        initBackground(head);
        initPersons(head);
        initNameAndWordPanel(head);
        initPanelWord(head);
        initTool(head);
        initSelectPanel(head, data);
        initGlobalMask(head);
    }

    private void initGlobalMask(FaceHead head) {
        globalMask = new Rectangle(ConfigCenter.WINDOW_WIDTH, ConfigCenter.WINDOW_HEIGHT);
        globalMask.setFill(Color.color(0, 0, 0, 0));
        globalMask.setTranslateX(0);
        globalMask.setTranslateY(0);
        globalMask.setVisible(false);

        head.attach(globalMask);
        head.mark("globalMask", FaceHead.THIS_FACE, globalMask);
    }

    private void initBackground(FaceHead head) {
        ImageView background = new ImageView(ConfigCenter.loadScene("back_demo1"));
        background.setFitWidth(ConfigCenter.WINDOW_WIDTH);
        background.setFitHeight(ConfigCenter.WINDOW_HEIGHT);
//        background.imageProperty().set(null);
        head.attach(background);
        head.mark("scene", FaceHead.THIS_FACE, background);
    }

    private void initPersons(FaceHead head) {
        Image ona = ConfigCenter.loadPersonState("onna", "1");
        Image otoko = ConfigCenter.loadPersonState("otoko", "1");
        Image bishojo = ConfigCenter.loadPersonState("bishojo", "1");

        leftPerson = new ImageView(ona);
        leftPerson.setFitWidth(ConfigCenter.PERSON_WIDTH);
        leftPerson.setFitHeight(ConfigCenter.PERSON_HEIGHT);
        leftPerson.imageProperty().set(null);

        centerPerson = new ImageView(bishojo);
        centerPerson.setFitWidth(ConfigCenter.PERSON_WIDTH);
        centerPerson.setFitHeight(ConfigCenter.PERSON_HEIGHT);
        centerPerson.setTranslateX(ConfigCenter.PERSON_WIDTH);
//        centerPerson.imageProperty().set(null);

        rightPerson = new ImageView(otoko);
        rightPerson.setFitWidth(ConfigCenter.PERSON_WIDTH);
        rightPerson.setFitHeight(ConfigCenter.PERSON_HEIGHT);
        rightPerson.setTranslateX(ConfigCenter.PERSON_WIDTH * 2);
//        rightPerson.imageProperty().set(null);

        head.attach(leftPerson);
        head.attach(centerPerson);
        head.attach(rightPerson);

        head.mark("leftPerson", FaceHead.THIS_FACE, leftPerson);
        head.mark("centerPerson", FaceHead.THIS_FACE, centerPerson);
        head.mark("rightPerson", FaceHead.THIS_FACE, rightPerson);
    }

    private void initNameAndWordPanel(FaceHead head) {
        name = new Text("这就是我");
        name.setFont(ConfigCenter.NAME_FONT);
        name.setFill(Color.WHEAT);
        name.setTranslateX(ConfigCenter.NAME_DISPLAY_X);
        name.setTranslateY(ConfigCenter.NAME_DISPLAY_Y);
        name.setOnMouseEntered(event -> name.setEffect(new Bloom(0.1)));
        name.setOnMouseExited(event -> name.setEffect(null));

        wordPaneFrame = new Rectangle(ConfigCenter.WORD_PANEL_WIDTH,
                ConfigCenter.WORD_PANEL_HEIGHT, Color.color(0.2, 0.2, 0.2, 0.5));
        wordPaneFrame.setTranslateY(ConfigCenter.WORD_PANEL_DISPLAY_Y);
        wordPaneFrame.setTranslateX(ConfigCenter.WORD_PANEL_DISPLAY_X);
        head.attach(wordPaneFrame);
        head.attach(name);
    }

    private void initPanelWord(FaceHead head) {
        Font f;
        for (int i = 0; i != ConfigCenter.WORD_MAX_SIZE; ++i) {
            int row = i / ConfigCenter.WORD_LINE_COLUMN;
            int column = i % ConfigCenter.WORD_LINE_COLUMN;
            Text t = new Text("我");
            t.setEffect(new DropShadow(1, 1, 1, Color.BLACK));
            t.setStroke(Color.WHITE);
            t.setFont(ConfigCenter.WORD_FONT);
            f = t.getFont();
            t.setTranslateX(ConfigCenter.WORD_START_X +
                    column * f.getSize() +
                    column * ConfigCenter.WORD_TAP);
            t.setTranslateY(ConfigCenter.WORD_START_Y +
                    (row + 1) * f.getSize() +
                    (row - 1) * ConfigCenter.WORD_LINE_TAP);
            t.visibleProperty().bindBidirectional(wordPaneFrame.visibleProperty());
            words[i]=t;
            head.attach(t);
        }
    }

    private void initSelectPanel(FaceHead head, FaceData data) {
        for (int i = 0; i != selectText.length; ++i) {

            double ty = ConfigCenter.SELECT_Y + i * ConfigCenter.SELECT_FONT.getSize() * 1.8;
            Rectangle rectangle = new Rectangle(ConfigCenter.SELECT_WIDTH, ConfigCenter.SELECT_HEIGHT);
            rectangle.setUserData((i + 1));
            Color selectC = Color.color(0.2, 0.3, 0.2, 0.3);
            Color noneC = Color.color(0.1, 0.1, 0.1, 0.2);
            rectangle.setArcWidth(4.0);
            rectangle.setArcHeight(3.0);
            rectangle.setFill(selectC);
            rectangle.setOnMouseEntered(event -> {
                rectangle.setFill(selectC);
            });
            rectangle.setOnMouseExited(event -> {
                rectangle.setFill(noneC);
            });
            rectangle.setTranslateX(ConfigCenter.SELECT_X - 10);
            rectangle.setTranslateY(ty - 27.0);
            rectangle.setOnMouseClicked(event -> {
                System.out.println("You Select " + rectangle.getUserData());
                for (int j = 0; j != selectText.length; ++j) {
                    Text desist = selectText[j];
                    desist.visibleProperty().set(false);
                }
                data.property("selects").put(data.strPro("nowSelectId"),
                        String.valueOf(rectangle.getUserData()));
                data.intPro("gameState").set(GAME_STATE_WAIT_NEXT);
            });

            Text t = new Text("");
            t.setUserData(rectangle);
            t.setFont(ConfigCenter.SELECT_FONT);
            t.setTranslateY(ty);
            t.setTranslateX(ConfigCenter.SELECT_X);
            t.setFill(Color.RED);
            selectText[i] = t;
            selectMasks[i] = rectangle;
            t.visibleProperty().bindBidirectional(rectangle.visibleProperty());
            head.attach(t);
            head.attach(rectangle);
        }
    }

    private void initTool(FaceHead head) {
        List<EventHandler<MouseEvent>> eventHandlers = new ArrayList<>(Arrays.asList(
                new SkipEventHandler(),
                new AutoEventHandler(),
                new SaveEventHandler(),
                new LoadEventHandler(),
                new BackEventHandler()
        ));

        double prefX = ConfigCenter.TOOL_DISPLAY_X_R - 55;
        for (int i = buttonList.length - 1; i != -1; --i) {
            Text button = new Text(buttonNames[i]);
            button.setOnMouseClicked(eventHandlers.get(i));
            button.setOnMouseEntered(event -> button.setEffect(new Bloom(0.1)));
            button.setOnMouseExited(event -> button.setEffect(null));
            button.setLayoutX(prefX);
            button.setLayoutY(ConfigCenter.TOOL_DISPLAY_Y);
            button.setFont(ConfigCenter.WORD_FONT);
            button.setStroke(Color.WHITE);
            prefX -= 55;

            head.attach(button);
        }
    }

    private void initBind(FaceData data, FaceHead head) {
        //maskShow Bind
        data.boolPro("maskShow").bindBidirectional(globalMask.visibleProperty());

        //SelectShow Bind
        BooleanProperty selectShow = data.boolPro("selectShow");
        for (int i = 0; i != selectText.length; ++i) {
            selectShow.bindBidirectional(selectText[i].visibleProperty());
        }
        //Select Text Bind
        for(int i=0;i!=selectText.length;++i){
            data.strPro(findSelectAt(i)).
                    bindBidirectional(selectText[i].textProperty());
        }

        //WordPanelShow Bind
        BooleanProperty wordPanelShow = data.boolPro("wordPanelShow");
        wordPanelShow.bindBidirectional(wordPaneFrame.visibleProperty());
        //Name Text Bind
        data.strPro("nameDisplay").bindBidirectional(name.textProperty());
        //Word Text Bind
        for(int i=0;i!=words.length;++i){
            int cx=i/ConfigCenter.WORD_LINE_COLUMN;
            int cy=i%ConfigCenter.WORD_LINE_COLUMN;
            data.strPro(findWordAt(cx,cy)).bindBidirectional(words[i].textProperty());
        }
    }

    public static String findWordAt(int cx, int cy) {
        return "word_" + cx + "/" + cy;
    }

    public static String findSelectAt(int index) {
        return "select_" + index;
    }
}
