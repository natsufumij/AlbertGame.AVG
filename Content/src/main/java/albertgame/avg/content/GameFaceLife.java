package albertgame.avg.content;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
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

    class SkipEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            _d.boolPro("skip").set(true);
            _d.intPro("gameState").set(GAME_STATE_WAIT_NEXT);
        }
    }

    class AutoEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            IntegerProperty in = _d.intPro("gameState");
            if (in.get() == GAME_STATE_WAIT_PRESS) {
                in.set(GAME_STATE_WAIT_NEXT);
            }

            System.out.println("Auto");
            boolean b = _d.boolPro("auto").get();
            _d.boolPro("auto").set(!b);
        }
    }

    class SaveEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    int ind = Integer.parseInt(MainEntry.Controller().getData().get("index"));
                    ConfigCenter.saveCache(ind, _d.property("cache"));
                    ConfigCenter.saveSelects(ind, _d.property("selects"));
                    return null;
                }
            };
            task.setOnSucceeded(event1 -> System.out.println("Save Succeed"));
            Platform.runLater(task);
        }
    }

    static class LoadEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            MainEntry.Controller().replaceFace(new StoreLife());
        }
    }

    static class BackEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            MainEntry.Controller().popFace();
        }
    }

    public static final Map<String, Person> playedPersons = new HashMap<>();
    public static final Map<String, Person.PersonData> personDataMap = new HashMap<>();
    public static Person leftPerson, centerPerson, rightPerson;

    private static Play.GlobalConfig globalConfig;
    private static Play.Chapter chapter;
    private static Play play;
    private static Play.BodyStruck bodyStruck;

    public static void clearStaticCache() {
        playedPersons.clear();
        personDataMap.clear();
    }

    public GameController.MouseInput handlerKeys() {
        EventHandler<MouseEvent> p = event -> {
        };
        EventHandler<MouseEvent> r = event -> {
            if (_d.intPro("gameState").get() == GAME_STATE_WAIT_PRESS) {
                _d.intPro("gameState").set(GAME_STATE_WAIT_NEXT);
            }
        };
        EventHandler<MouseEvent> rb = event -> {
        };
        return new GameController.MouseInput(p, r, rb);
    }

    FaceData _d;

    @Override
    public void init(FaceData d, FaceHead h, Map<String, FaceHandler> p) {
        _d = d;
        p.put("Dialog", new FaceHandlers.DialogHandler());
        p.put("View", new FaceHandlers.ViewHandler());
        p.put("Person", new FaceHandlers.PersonHandle());
        p.put("Audio", new FaceHandlers.AudioHandler());
        p.put("Store", new FaceHandlers.StoreHandler());
        p.put("Select", new FaceHandlers.SelectHandler());

        clearStaticCache();
        initData(d);
        initHead(d, h);
        initBind(d, h);
        initGame(d, h);
    }

    private void initGame(FaceData data, FaceHead head) {
        GameFaceLife.globalConfig = ConfigCenter.loadGlobalConfig();
        //添加人物信息
        for (Play.GlobalConfig.PersonConfig pc : globalConfig.personConfigs().values()) {
            Person.PersonData pd = new Person.PersonData(pc.id(), pc.name(), pc.state().get(0), pc.state());
            GameFaceLife.personDataMap.put(pd.id(), pd);
        }

        int index = Integer.parseInt(
                MainEntry.Controller().getData().get("index"));
        //导入缓存
        if (ConfigCenter.isCacheExist(index)) {
            //存在存档
            Map<String, String> p = ConfigCenter.loadCacheData(index);

            data.property("selects").putAll(p);
            data.property("cache").putAll(ConfigCenter.loadCache(index));
            restoreGame(data, head);
        } else {
            loadFromGlobal(data);
        }
    }

    private void loadFromGlobal(FaceData data) {
        Play.GlobalConfig config = GameFaceLife.globalConfig;
        GameFaceLife.chapter = ConfigCenter.loadChapter(config.startChapter());
        GameFaceLife.play = ConfigCenter.loadPlay(
                config.startChapter(), GameFaceLife.chapter.startPlayId());
        GameFaceLife.bodyStruck = GameFaceLife.play.bodyStruckMap().get(GameFaceLife.play.startStruck());
        resetStruck(data, GameFaceLife.chapter, GameFaceLife.play, GameFaceLife.bodyStruck);
        data.intPro("nowIndex").set(0);
    }

    private void restoreGame(FaceData data, FaceHead head) {

        Properties p = ConfigCenter.loadCache(
                Integer.parseInt(MainEntry.Controller().getData().get("index")));
        Properties properties = data.property("cache");
        properties.putAll(p);

        String chapter = properties.getProperty("chapter");
        String play = properties.getProperty("play");
        String struck = properties.getProperty("struck");
        String scene = properties.getProperty("scene");
        String bgm = properties.getProperty("bgm");

        String[] personIn;
        if (properties.contains("personin")) {
            personIn = properties.getProperty("personin").split(",");
        } else {
            personIn = new String[0];
        }
        String leftP = properties.getProperty("leftp");
        String centerP = properties.getProperty("centerp");
        String rightP = properties.getProperty("rightp");
        String leftS = properties.getProperty("leftstate");
        String centerS = properties.getProperty("centerstate");
        String rightS = properties.getProperty("rightstate");
        boolean wordpanelshow = Boolean.parseBoolean(properties.getProperty("wordpanelshow"));
        boolean maskshow = Boolean.parseBoolean(properties.getProperty("maskshow"));

        GameFaceLife.chapter = ConfigCenter.loadChapter(chapter);
        GameFaceLife.play = ConfigCenter.loadPlay(chapter, play);
        GameFaceLife.bodyStruck = GameFaceLife.play.bodyStruckMap().get(struck);
        resetStruck(data, GameFaceLife.chapter, GameFaceLife.play, GameFaceLife.bodyStruck);

        int index = Integer.parseInt(properties.getProperty("index"));
        data.intPro("nowIndex").set(index);

        Image sceneI = ConfigCenter.loadScene(scene);
        ((ImageView) head.fetch("scene")).setImage(sceneI);
        if (!maskshow) {
            //初始状态为黑的状态，如果遮罩没有，则代表全亮的，否则则为初始状态
            Rectangle n = (Rectangle) head.fetch("globalMask");
            n.setFill(Color.color(0, 0, 0, 0.0));
        }
        data.boolPro("maskShow").set(maskshow);

        List<FaceHandler.Arg> list = new ArrayList<>();
        FaceHandler.Arg bgmArg = new FaceHandler.Arg(
                "Audio", "Bgm.Play", new String[]{bgm});
        list.add(bgmArg);

        for (String s : personIn) {
            if (s.isBlank()) continue;

            FaceHandler.Arg arg = new FaceHandler.Arg("Person", "In", new String[]{s});
            list.add(arg);
        }

        if (leftP != null && !leftP.isBlank()) {
            FaceHandler.Arg arg = new FaceHandler.Arg("Person", "Show", new String[]{"L", leftP});
            FaceHandler.Arg arg2 = new FaceHandler.Arg("Person", "Change.State", new String[]{"L", leftS});
            list.add(arg);
            list.add(arg2);
        }
        if (centerP != null && !centerP.isBlank()) {
            FaceHandler.Arg arg = new FaceHandler.Arg("Person", "Show", new String[]{"C", centerP});
            FaceHandler.Arg arg2 = new FaceHandler.Arg("Person", "Change.State", new String[]{"C", centerS});
            list.add(arg);
            list.add(arg2);
        }
        if (rightP != null && !rightP.isBlank()) {
            FaceHandler.Arg arg = new FaceHandler.Arg("Person", "Show", new String[]{"R", rightP});
            FaceHandler.Arg arg2 = new FaceHandler.Arg("Person", "Change.State", new String[]{"R", rightS});
            list.add(arg);
            list.add(arg2);
        }

        data.boolPro("wordPanelShow").set(wordpanelshow);

        for (FaceHandler.Arg ar : list) {
            MainEntry.Controller().handleArguments(ar);
        }
    }

    @Override
    public void update(FaceData d, FaceHead h) {
        if (d.intPro("gameState").get() == GAME_STATE_WAIT_NEXT) {
            if (!d.boolPro("struckEnd").get()) {
                if (!d.boolPro("skip").get()) {
                    FaceHandler.Arg arg = getArgAndSetNext(d);
                    MainEntry.Controller().handleArguments(arg);
                } else {
                    FaceHandler.Arg arg = getArgAndSetNext(d);
                    if (!arg.type().equals("Select")) {
                        MainEntry.Controller().handleArguments(arg);
                    } else {
                        MainEntry.Controller().handleArguments(arg);
                        d.boolPro("skip").set(false);
                    }
                }
            } else {
                nextPlayLine(d);
            }
        }
    }

    private Map<String, String> helpData(FaceData data) {
        Map<String, String> d = new HashMap<>();
        for (Map.Entry<Object, Object> en : data.property("selects").entrySet()) {
            d.put((String) en.getKey(), (String.valueOf(en.getValue())));
        }
        return d;
    }

    private void nextPlayLine(FaceData data) {
        if (data.boolPro("struckEnd").get()) {
            Play.BodyStruck struck = GameFaceLife.bodyStruck;
            Map<String, String> da = helpData(data);
            //如果有下一个body块
            if (struck.optionStruck() != Play.OptionStruck.NONE_OPTION) {
                //查找下一个body块
                Play.BodyStruck nextStruck = GameFaceLife.play.nextBodyStruck(struck.id(), da);
                if (nextStruck != Play.BodyStruck.NONE_BODY) {
                    resetStruck(data, GameFaceLife.chapter, GameFaceLife.play, nextStruck);
                }
            } else {
                //body块结束，并且没有下一块
                //寻找下一个play
                String destId = GameFaceLife.chapter.nextPlay(GameFaceLife.play.id(), da);

                //如果destId不是NONE_ID，表示play存在
                if (!Objects.equals(destId, Play.OptionStruck.NONE_ID)) {
                    Play play = ConfigCenter.loadPlay(GameFaceLife.chapter.id(), destId);
                    final String beginStruckName = play.startStruck();
                    //找到了play，寻找begins，作为第一个body块
                    resetStruck(data, GameFaceLife.chapter, play, play.bodyStruckMap().get(beginStruckName));
                } else {
                    //是NONE_ID，表示play不存在，则寻找下一个Chapter
                    destId = globalConfig.nextChapter(GameFaceLife.chapter.id(), da);
                    if (Objects.equals(destId, Play.OptionStruck.NONE_ID)) {
                        //无下一个Chapter
                        //已经全部结束，返回到主菜单
                        System.out.println("All Done.");
                        MainEntry.Controller().popFace();
                    } else {
                        Play.Chapter chapter = ConfigCenter.loadChapter(destId);
                        String startPlayId = chapter.startPlayId();
                        Play play = ConfigCenter.loadPlay(chapter.id(), startPlayId);
                        GameFaceLife.chapter = chapter;
                        resetStruck(data, GameFaceLife.chapter, play, play.bodyStruckMap().get("begins"));
                    }
                }
            }
        }
    }

    private void resetStruck(FaceData d, Play.Chapter c, Play p, Play.BodyStruck struck) {
        GameFaceLife.chapter = c;
        GameFaceLife.play = p;
        GameFaceLife.bodyStruck = struck;
        d.intPro("nowIndex").set(0);
        d.property("cache").setProperty("chapter", c.id());
        d.property("cache").setProperty("play", p.id());
        d.property("cache").setProperty("struck", struck.id());
        d.property("cache").setProperty("index", "0");
        d.property("cache").setProperty("chaptername", c.name());
        d.property("cache").setProperty("playname", p.name());
        d.boolPro("struckEnd").set(false);
    }

    private FaceHandler.Arg getArgAndSetNext(FaceData data) {
        if (data.boolPro("struckEnd").get()) {
            return FaceHandler.Arg.NONE_ARG;
        }

        int lineIndex = data.intPro("nowIndex").get();
        String[] nowCmd = GameFaceLife.bodyStruck.expressions().get(lineIndex).split(" {2}");

        ++lineIndex;
        data.intPro("nowIndex").set(lineIndex);
        data.property("cache").put("index", "" + lineIndex);
        if (lineIndex == GameFaceLife.bodyStruck.expressions().size()) {
            data.boolPro("struckEnd").set(true);
        }

        String[] a = new String[nowCmd.length - 2];
        System.arraycopy(nowCmd, 2, a, 0, a.length);
        return new FaceHandler.Arg(nowCmd[0], nowCmd[1], a);
    }

    @Override
    public void pause(FaceData d, FaceHead h) {
        FaceHandler.Arg arg = new FaceHandler.Arg("Audio", "Bgm.Pause", null);
        MainEntry.Controller().handleArguments(arg);
    }

    @Override
    public void resume(FaceData d, FaceHead h) {
        FaceHandler.Arg arg = new FaceHandler.Arg("Audio", "Bgm.Resume", null);
        MainEntry.Controller().handleArguments(arg);
    }

    @Override
    public void stop(FaceData d, FaceHead h) {
        FaceHandler.Arg arg = new FaceHandler.Arg("Audio", "Bgm.Stop", null);
        MainEntry.Controller().handleArguments(arg);
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
        data.boolPro("skip");

        data.intPro("nowIndex");
        data.intPro("gameState").set(1);

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

    ImageView leftPersonImage;
    ImageView centerPersonImage;
    ImageView rightPersonImage;

    Rectangle globalMask;

    final Text[] buttonList = new Text[buttonNames.length];
    final Text[] words = new Text[ConfigCenter.WORD_MAX_SIZE];
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
        globalMask.setFill(Color.color(0, 0, 0, 1.0));
        globalMask.setTranslateX(0);
        globalMask.setTranslateY(0);
        globalMask.setVisible(true);

        head.attach(globalMask);
        head.mark("globalMask", FaceHead.THIS_FACE, globalMask);
    }

    private void initBackground(FaceHead head) {
        ImageView background = new ImageView(ConfigCenter.loadScene("back_demo1"));
        background.setFitWidth(ConfigCenter.WINDOW_WIDTH);
        background.setFitHeight(ConfigCenter.WINDOW_HEIGHT);
        background.imageProperty().set(null);
        head.attach(background);
        head.mark("scene", FaceHead.THIS_FACE, background);
    }

    private void initPersons(FaceHead head) {
        Image ona = ConfigCenter.loadPersonState("onna", "1");
        Image otoko = ConfigCenter.loadPersonState("otoko", "1");
        Image bishojo = ConfigCenter.loadPersonState("bishojo", "1");

        leftPersonImage = new ImageView(ona);
        leftPersonImage.setFitWidth(ConfigCenter.PERSON_WIDTH);
        leftPersonImage.setFitHeight(ConfigCenter.PERSON_HEIGHT);
        leftPersonImage.imageProperty().set(null);

        centerPersonImage = new ImageView(bishojo);
        centerPersonImage.setFitWidth(ConfigCenter.PERSON_WIDTH);
        centerPersonImage.setFitHeight(ConfigCenter.PERSON_HEIGHT);
        centerPersonImage.setTranslateX(ConfigCenter.PERSON_WIDTH);
        centerPersonImage.imageProperty().set(null);

        rightPersonImage = new ImageView(otoko);
        rightPersonImage.setFitWidth(ConfigCenter.PERSON_WIDTH);
        rightPersonImage.setFitHeight(ConfigCenter.PERSON_HEIGHT);
        rightPersonImage.setTranslateX(ConfigCenter.PERSON_WIDTH * 2);
        rightPersonImage.imageProperty().set(null);

        head.attach(leftPersonImage);
        head.attach(centerPersonImage);
        head.attach(rightPersonImage);

        head.mark("leftPerson", FaceHead.THIS_FACE, leftPersonImage);
        head.mark("centerPerson", FaceHead.THIS_FACE, centerPersonImage);
        head.mark("rightPerson", FaceHead.THIS_FACE, rightPersonImage);
    }

    private void initNameAndWordPanel(FaceHead head) {
        name = new Text("");
        name.setFont(ConfigCenter.getSystemFont("Name"));
        name.setFill(ConfigCenter.getSystemColor("Name"));
        name.setTranslateX(ConfigCenter.NAME_DISPLAY_X);
        name.setTranslateY(ConfigCenter.NAME_DISPLAY_Y);
        name.setOnMouseEntered(event -> name.setEffect(new Bloom(0.1)));
        name.setOnMouseExited(event -> name.setEffect(null));

        wordPaneFrame = new Rectangle(ConfigCenter.WORD_PANEL_WIDTH,
                ConfigCenter.WORD_PANEL_HEIGHT, Color.color(0.2, 0.2, 0.2, 0.5));
        wordPaneFrame.setTranslateY(ConfigCenter.WORD_PANEL_DISPLAY_Y);
        wordPaneFrame.setTranslateX(ConfigCenter.WORD_PANEL_DISPLAY_X);
        wordPaneFrame.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                FaceHandlers.DialogHandler.shiftWord();
            }
        });
        head.attach(wordPaneFrame);
        head.attach(name);
    }

    private void initPanelWord(FaceHead head) {
        Font f=ConfigCenter.getSystemFont("Word");
        for (int i = 0; i != ConfigCenter.WORD_MAX_SIZE; ++i) {
            int row = i / ConfigCenter.WORD_LINE_COLUMN;
            int column = i % ConfigCenter.WORD_LINE_COLUMN;
            Text t = new Text("");
            t.setEffect(new DropShadow(1, 1, 1, Color.BLACK));
            t.setFont(ConfigCenter.getSystemFont("Word"));
            t.setFill(ConfigCenter.getSystemColor("Word"));
            f = t.getFont();
            t.setTranslateX(ConfigCenter.WORD_START_X +
                    column * f.getSize() +
                    column * ConfigCenter.WORD_TAP);
            t.setTranslateY(ConfigCenter.WORD_START_Y +
                    (row + 1) * f.getSize() +
                    (row - 1) * ConfigCenter.WORD_LINE_TAP);
            t.visibleProperty().bindBidirectional(wordPaneFrame.visibleProperty());
            words[i] = t;
            head.attach(t);
        }
        Rectangle mouseClickRect=new Rectangle();
        mouseClickRect.setTranslateY(ConfigCenter.WORD_START_Y - f.getSize());
        mouseClickRect.setWidth(ConfigCenter.WINDOW_WIDTH);
        mouseClickRect.setHeight(ConfigCenter.WORD_LINE_ROW * f.getSize() * 3.0);
        mouseClickRect.setFill(Color.color(0.0,0.0,0.0,0.0));
        mouseClickRect.setOnMouseClicked(event -> {
            if(event.getClickCount()==1){
                if(_d.intPro("gameState").get()==GAME_STATE_WAIT_PRESS){
                    _d.intPro("gameState").set(GAME_STATE_WAIT_NEXT);
                }
            }else {
                if(_d.intPro("gameState").get()==GAME_STATE_WORD_DISPLAYING){
                    FaceHandlers.DialogHandler.shiftWord();
                }
            }
        });
        head.attach(mouseClickRect);
    }

    private void initSelectPanel(FaceHead head, FaceData data) {
        for (int i = 0; i != selectText.length; ++i) {

            double ty = ConfigCenter.SELECT_Y + i * ConfigCenter.SELECT_FONT.getSize() * 1.8;
            Rectangle rectangle = new Rectangle(ConfigCenter.SELECT_WIDTH, ConfigCenter.SELECT_HEIGHT);
            rectangle.setUserData((i + 1));
            Color selectC = ConfigCenter.getSystemColor("SelectRect0");
            Color noneC = ConfigCenter.getSystemColor("SelectRect1");
            rectangle.setArcWidth(10.0);
            rectangle.setArcHeight(10.0);
            rectangle.setFill(selectC);
            rectangle.setOnMouseEntered(event -> rectangle.setFill(noneC));
            rectangle.setOnMouseExited(event -> rectangle.setFill(selectC));
            rectangle.setTranslateX(ConfigCenter.SELECT_X - 10);
            rectangle.setTranslateY(ty - 27.0);
            rectangle.setOnMouseClicked(event -> {
                for (int j = 0; j != selectText.length; ++j) {
                    Text desist = selectText[j];
                    desist.visibleProperty().set(false);
                }
                data.property("selects").put(data.strPro("nowSelectId").get(),
                        String.valueOf(rectangle.getUserData()));
                data.intPro("gameState").set(GAME_STATE_WAIT_NEXT);
            });

            Text t = new Text("");
            t.setOnMouseEntered(event -> rectangle.setFill(noneC));
            t.setOnMouseExited(event -> rectangle.setFill(selectC));
            t.setOnMouseClicked(event -> {
                for (int j = 0; j != selectText.length; ++j) {
                    Text desist = selectText[j];
                    desist.visibleProperty().set(false);
                }
                data.property("selects").put(data.strPro("nowSelectId").get(),
                        rectangle.getUserData());
                data.intPro("gameState").set(GAME_STATE_WAIT_NEXT);
            });
            t.setId(i + "");
            t.setFont(ConfigCenter.getSystemFont("Select"));
            t.setTranslateY(ty);
            t.setTranslateX(ConfigCenter.SELECT_X);
            t.setFill(ConfigCenter.getSystemColor("Select"));
            selectText[i] = t;
            selectMasks[i] = rectangle;
            t.visibleProperty().bindBidirectional(rectangle.visibleProperty());
            head.attach(rectangle);
            head.attach(t);
            head.mark(findSelectAt(i), FaceHead.THIS_FACE, t);
        }
    }

    private void initTool(FaceHead head) {
        final List<EventHandler<MouseEvent>> eventHandlers = new ArrayList<>(Arrays.asList(
                new SkipEventHandler(),
                new AutoEventHandler(),
                new SaveEventHandler(),
                new LoadEventHandler(),
                new BackEventHandler()
        ));

        double prefX = ConfigCenter.TOOL_DISPLAY_X_R - 55;
        for (int i = buttonList.length - 1; i != -1; --i) {
            Text button = new Text(buttonNames[i]);
            button.setOnMouseEntered(event -> button.setEffect(new Bloom(0.1)));
            button.setOnMouseExited(event -> button.setEffect(null));
            button.setLayoutX(prefX);
            button.setLayoutY(ConfigCenter.TOOL_DISPLAY_Y);
            button.setFill(ConfigCenter.getSystemColor("Button0"));
            button.setFont(ConfigCenter.getSystemFont("Word"));
            button.visibleProperty().bindBidirectional(wordPaneFrame.visibleProperty());
            if (i == 1) {
                button.setUserData(true);
                EventHandler<MouseEvent> e = (m) -> {
                    //表示初始状态
                    if ((Boolean) button.getUserData()) {
                        button.setText("SLOW");
                        button.setUserData(false);
                    } else {
                        button.setText("AUTO");
                        button.setUserData(true);
                    }
                    eventHandlers.get(1).handle(m);
                };
                button.setOnMouseClicked(e);
            } else {
                button.setOnMouseClicked(eventHandlers.get(i));
            }
            prefX -= 55;

            head.attach(button);
        }
    }

    private void initBind(FaceData data, FaceHead head) {
        //maskShow Bind
        data.boolPro("maskShow").bindBidirectional(globalMask.visibleProperty());

        //Select Text Bind
        for (int i = 0; i != selectText.length; ++i) {
            data.strPro(findSelectAt(i)).
                    bindBidirectional(selectText[i].textProperty());
            data.boolPro(findSelectAt(i))
                    .bindBidirectional(selectText[i].visibleProperty());
            data.boolPro(findSelectAt(i))
                    .set(false);
            data.boolPro(findSelectAt(i)).addListener((v, o, n) -> {
            });
        }

        //WordPanelShow Bind
        BooleanProperty wordPanelShow = data.boolPro("wordPanelShow");
        wordPanelShow.bindBidirectional(wordPaneFrame.visibleProperty());
        wordPanelShow.set(false);

        //Name Text Bind
        data.strPro("nameDisplay").bindBidirectional(name.textProperty());
        //Word Text Bind
        for (int i = 0; i != words.length; ++i) {
            int cx = i / ConfigCenter.WORD_LINE_COLUMN;
            int cy = i % ConfigCenter.WORD_LINE_COLUMN;
            data.strPro(findWordAt(cx, cy)).bindBidirectional(words[i].textProperty());
        }
    }

    public static String findWordAt(int cx, int cy) {
        return "word_" + cx + "/" + cy;
    }

    public static String findSelectAt(int index) {
        return "select_" + index;
    }
}
