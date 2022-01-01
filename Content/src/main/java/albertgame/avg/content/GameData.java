package albertgame.avg.content;

import javafx.beans.property.*;
import javafx.scene.image.Image;

import java.util.*;

public class GameData {

    //游戏当前状态
    //  等待用户响应
    //  等待下一个命令
    //  正在显示文字中
    //  正在选择分支中
    //  正在动画中

    public static final char GAME_STATE_WAIT_PRESS = 0;
    public static final char GAME_STATE_WAIT_NEXT = 1;
    public static final char GAME_STATE_SELECTING = 2;
    public static final char GAME_STATE_WORD_DISPLAYING = 3;
    public static final char GAME_STATE_ANIMATING = 4;

    private final ObjectProperty<Image> backgroundImage;

    private final ObjectProperty<Image> leftPersonImage;
    private final ObjectProperty<Image> centerPersonImage;
    private final ObjectProperty<Image> rightPersonImage;

    private final StringProperty nameDisplay;
    private final StringProperty[] displayWords;

    private final BooleanProperty wordLineShow;
    private final BooleanProperty maskShow;

    private final Map<String, String> data;

    private Person leftPerson;
    private Person centerPerson;
    private Person rightPerson;
    private final Map<String, Person> playedPersons;
    private final Map<String, Person.PersonData> personDataMap;

    private final Map<String, Play.Chapter> chapterSet;
    private final Map<String, Play> playSet;

    private Properties properties;

    private Play.GlobalConfig globalConfig;
    private Play.Chapter nowChapter;
    private Play nowPlay;
    private Play.BodyStruck struck;
    private int lineIndex;

    private char gameState;
    private String nowSelectId;

    private boolean end = false;

    //是否自动
    private boolean auto;

    public GameData() {
        leftPerson = Person.NONE_PERSON;
        centerPerson = Person.NONE_PERSON;
        rightPerson = Person.NONE_PERSON;
        playedPersons = new HashMap<>();
        backgroundImage = new SimpleObjectProperty<>();
        leftPersonImage = new SimpleObjectProperty<>();
        centerPersonImage = new SimpleObjectProperty<>();
        rightPersonImage = new SimpleObjectProperty<>();
        nameDisplay = new SimpleStringProperty();
        displayWords = new StringProperty[ConfigCenter.WORD_LINE_ROW * ConfigCenter.WORD_LINE_COLUMN];
        for (int i = 0; i != displayWords.length; ++i) {
            StringProperty text = new SimpleStringProperty(" ");
            displayWords[i] = text;
        }
        wordLineShow = new SimpleBooleanProperty(Boolean.TRUE);
        maskShow=new SimpleBooleanProperty(false);
        gameState = GAME_STATE_WAIT_PRESS;
        chapterSet = new HashMap<>();
        data = new HashMap<>();
        playSet = new HashMap<>();
        personDataMap = new HashMap<>();
    }

    public Map<String, Person> getPlayedPersons() {
        return playedPersons;
    }

    public void nextPlayLine() {
        if (end) return;

        if (lineIndex == struck.expressions().size()) {

            //如果有下一个body块
            if (struck.optionStruck() != Play.OptionStruck.NONE_OPTION) {
                //查找下一个body块
                Play.BodyStruck nextStruck = nowPlay.nextBodyStruck(struck.id(), this.data);
                resetStruck(nowChapter, nowPlay, nextStruck);
            } else {
                //body块结束，并且没有下一块
                //寻找下一个play
                String destId = nowChapter.nextPlay(nowPlay.id(), this.data);

                //如果destId不是NONE_ID，表示play存在
                if (!Objects.equals(destId, Play.OptionStruck.NONE_ID)) {
                    Play play = ConfigCenter.loadPlayInClasspath(nowChapter.id(), destId);
                    final String beginStruckName = "begins";

                    //找到了play，寻找begins，作为第一个body块
                    resetStruck(nowChapter, play, play.bodyStruckMap().get(beginStruckName));
                } else {
                    //是NONE_ID，表示play不存在，则寻找下一个Chapter
                    destId = globalConfig.nextChapter(nowChapter.id(), this.data);
                    if (Objects.equals(destId, Play.OptionStruck.NONE_ID)) {
                        //无下一个Chapter
                        System.out.println("All Done.");
                        end = true;
                    } else {
                        Play.Chapter chapter = ConfigCenter.loadChapter(destId);
                        String startPlayId = chapter.startPlayId();
                        Play play = ConfigCenter.loadPlayInClasspath(chapter.id(), startPlayId);
                        nowChapter = chapter;
                        resetStruck(nowChapter, play, play.bodyStruckMap().get("begins"));
                    }
                }
            }
        }
    }


    public Play.GlobalConfig getGlobalConfig() {
        return globalConfig;
    }

    private void resetStruck(Play.Chapter c, Play p, Play.BodyStruck struck) {
        this.nowChapter = c;
        this.nowPlay = p;
        this.struck = struck;
        lineIndex = 0;
    }

    public Image getBackgroundImage() {
        return backgroundImage.get();
    }

    public ObjectProperty<Image> backgroundImageProperty() {
        return backgroundImage;
    }

    public Image getLeftPersonImage() {
        return leftPersonImage.get();
    }

    public ObjectProperty<Image> leftPersonImageProperty() {
        return leftPersonImage;
    }

    public Image getCenterPersonImage() {
        return centerPersonImage.get();
    }

    public ObjectProperty<Image> centerPersonImageProperty() {
        return centerPersonImage;
    }

    public Image getRightPersonImage() {
        return rightPersonImage.get();
    }

    public ObjectProperty<Image> rightPersonImageProperty() {
        return rightPersonImage;
    }

    public String getNameDisplay() {
        return nameDisplay.get();
    }

    public StringProperty nameDisplayProperty() {
        return nameDisplay;
    }

    public StringProperty[] getDisplayWords() {
        return displayWords;
    }

    public boolean isWordLineShow() {
        return wordLineShow.get();
    }

    public BooleanProperty wordLineShowProperty() {
        return wordLineShow;
    }

    public Person getLeftPerson() {
        return leftPerson;
    }

    public Person getCenterPerson() {
        return centerPerson;
    }

    public Person getRightPerson() {
        return rightPerson;
    }

    public void setLeftPerson(Person leftPerson) {
        this.leftPerson = leftPerson;
    }

    public void setCenterPerson(Person centerPerson) {
        this.centerPerson = centerPerson;
    }

    public void setRightPerson(Person rightPerson) {
        this.rightPerson = rightPerson;
    }

    public char getGameState() {
        return gameState;
    }

    public void setGameState(char gameState) {
        this.gameState = gameState;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public boolean isMaskShow() {
        return maskShow.get();
    }

    public BooleanProperty maskShowProperty() {
        return maskShow;
    }

    public Map<String, Play.Chapter> getChapterSet() {
        return chapterSet;
    }

    public Map<String, Play> getPlaySet() {
        return playSet;
    }

    public Map<String, String> getData() {
        return data;
    }

    public String getNowSelectId() {
        return nowSelectId;
    }

    public void setNowSelectId(String nowSelectId) {
        this.nowSelectId = nowSelectId;
    }

    public Map<String, Person.PersonData> getPersonDataMap() {
        return personDataMap;
    }

    public void setGlobalConfig(Play.GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setNowChapter(Play.Chapter nowChapter) {
        this.nowChapter = nowChapter;
    }

    public void setNowPlay(Play nowPlay) {
        this.nowPlay = nowPlay;
    }

    public void setStruck(Play.BodyStruck struck) {
        this.struck = struck;
    }

    public void setLineIndex(int lineIndex) {
        this.lineIndex = lineIndex;
    }

    public Play.Chapter getNowChapter() {
        return nowChapter;
    }

    public Play getNowPlay() {
        return nowPlay;
    }
}
