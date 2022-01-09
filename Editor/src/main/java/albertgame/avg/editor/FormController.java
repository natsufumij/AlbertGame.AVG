package albertgame.avg.editor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FormController {

    private static File selectFile;

    public static File getSelectFile() {
        return selectFile;
    }

    public static void setSelectFile(File selectFile) {
        FormController.selectFile = selectFile;
    }

    private static String nowPath = "Assets";

    public static String getNowPath() {
        return nowPath;
    }

    public static void setNowPath(String nowPath) {
        FormController.nowPath = nowPath;
    }

    public static class MediaC {
        String id;
        String name;

        public MediaC(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    //里面的Id全部为Name
    public static class StoryView {
        //在BodyStruck里的位置
        int bindLine;
        String type;
        String name;
        String[] data;

        public StoryView(int bindLine, String type, String name, String[] data) {
            this.type = type;
            this.bindLine = bindLine;
            this.name = name;
            this.data = data;
        }
    }

    public static class StoryBody {
        enum Type {
            GLOBAL, CHAPTER, PLAY
        }

        Type type;
        String name;
        String id;
        String parent_name;

        public StoryBody(Type type, String name, String id, String parent_name) {
            this.type = type;
            this.name = name;
            this.id = id;
            this.parent_name = parent_name;
        }
    }

    public static class PersonC {
        String id, name;
        String stateId, stateName;

        public PersonC(String id, String name, String stateId, String stateName) {
            this.id = id;
            this.name = name;
            this.stateId = stateId;
            this.stateName = stateName;
        }
    }



    private static FormController _instance;

    public static FormController get() {
        if (_instance == null) {
            synchronized (_lock) {
                _instance = new FormController();
            }
        }
        return _instance;
    }

    private static final Object _lock = new Object();

    private static int uniqueId = 0;

    //aaaa0000 - 00000000
    public static void setId(String uid) {
        if (uid.length() != 8) {
            System.out.println("Invalid Uid :" + uid);
            return;
        }
        final int[] level1 = new int[]{10000000, 1000000, 100000, 10000};
        int dest = 0;
        for (int i = 0; i != 4; ++i) {
            int a = uid.charAt(i) - (int) 'a';
            dest += (a * level1[i]);
        }
        final int[] level2 = new int[]{1000, 100, 10, 1};
        for (int i = 0; i != 4; ++i) {
            int a = Integer.parseInt(uid.charAt(i + 4) + "");
            dest += (a * level2[i]);
        }
        uniqueId = dest;
    }

    public static String getUniqueId() {
        ++uniqueId;
        StringBuilder builder = new StringBuilder();
        final int[] level = new int[]{10000000, 1000000, 100000, 10000};
        for (int i = 0; i != 4; ++i) {
            char c = (char) ((int) ('a') + (uniqueId % (level[i] * 10)) / level[i]);
            builder.append(c);
        }
        final int[] level2 = new int[]{1000, 100, 10, 1};
        for (int i = 0; i != 4; ++i) {
            char a = (char) ((int) ('0') + (uniqueId % (level2[i] * 10)) / level2[i]);
            builder.append(a);
        }
        return builder.toString();
    }

    //均为Name - Map的映射
    private final Map<String, MediaC> audioMap;
    private final Map<String, MediaC> bgmMap;
    private final Map<String, MediaC> sceneMap;
    private final Map<String, PersonC> personMap;

    private TreeItem<StoryBody> global;
    private final Map<String, TreeItem<StoryBody>> chapterTree;
    private final Map<String, ObservableList<StoryBody>> playInChapterMap;
    private final Map<String, GraphHelper.ShapeNode> shapeNodeMap;

    private Play.GlobalConfig globalConfig;
    private Play.Chapter nowChapter;
    private Play nowPlay;
    private Play.BodyStruck nowStruck;

    private StoryView nowEditExpression;

    private FormController() {
        audioMap = new HashMap<>();
        bgmMap = new HashMap<>();
        sceneMap = new HashMap<>();
        personMap = new HashMap<>();
        playInChapterMap = new HashMap<>();
        chapterTree = new HashMap<>();
        shapeNodeMap = new HashMap<>();
    }

    public Map<String, MediaC> getAudioMap() {
        return audioMap;
    }

    public Map<String, MediaC> getBgmMap() {
        return bgmMap;
    }

    public Map<String, MediaC> getSceneMap() {
        return sceneMap;
    }

    public Map<String, PersonC> getPersonMap() {
        return personMap;
    }

    public Map<String, ObservableList<StoryBody>> getPlayInChapterMap() {
        return playInChapterMap;
    }

    public Map<String, TreeItem<StoryBody>> getChapterTree() {
        return chapterTree;
    }

    public TreeItem<StoryBody> getGlobal() {
        return global;
    }

    public void setGlobal(TreeItem<StoryBody> global) {
        this.global = global;
    }

    public Play.GlobalConfig getGlobalConfig() {
        return globalConfig;
    }

    public void setGlobalConfig(Play.GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
    }

    public Play.Chapter getNowChapter() {
        return nowChapter;
    }

    public void setNowChapter(Play.Chapter nowChapter) {
        this.nowChapter = nowChapter;
    }

    public Play getNowPlay() {
        return nowPlay;
    }

    public void setNowPlay(Play nowPlay) {
        this.nowPlay = nowPlay;
    }

    public Play.BodyStruck getNowStruck() {
        return nowStruck;
    }

    public void setNowStruck(Play.BodyStruck nowStruck) {
        this.nowStruck = nowStruck;
    }

    public StoryView getNowEditExpression() {
        return nowEditExpression;
    }

    public void setNowEditExpression(StoryView nowEditExpression) {
        this.nowEditExpression = nowEditExpression;
    }

    public Map<String, GraphHelper.ShapeNode> getShapeNodeMap() {
        return shapeNodeMap;
    }

    //1. Load Project Path
    //2. Load Person
    //3. Load Chapters Info
    //4. Load Plays Info
    //5. Load Audio\Bgm\Scene
    public void loadProject(Properties properties) {
        //TODO: LOADING CODE
    }

    public void saveProject(Properties properties) {
        //TODO: SAVING CODE
    }

}
