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

        public StoryView(int bindLine,String type, String name, String[] data) {
            this.type = type;
            this.bindLine=bindLine;
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

        public StoryBody(Type type, String name, String id,String parent_name) {
            this.type = type;
            this.name = name;
            this.id = id;
            this.parent_name=parent_name;
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

    public static class AudioListCell extends ListCell<MediaC> {
        @Override
        protected void updateItem(MediaC item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                Label label = new Label(item.name);
                Button play = new Button("Play");
                play.setOnAction(event -> {
                    AudioClip audioClip = ConfigCenter.getClip(item.id);
                    if (audioClip != null) {
                        audioClip.play();
                    }
                });
                BorderPane pane = ConfigCenter.createBorderPane(new Node[]{label}, play);
                setGraphic(pane);
            } else {
                setText(null);
                setGraphic(null);
            }
        }
    }

    public static class BgmListCell extends ListCell<MediaC> {

        MediaView view;

        public BgmListCell(MediaView mediaView) {
            view = mediaView;
        }

        @Override
        protected void updateItem(MediaC item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                Label label = new Label(item.name);
                Button play = new Button("Play");
                play.setUserData(Boolean.TRUE);
                play.setOnAction(event -> {
                    if ((Boolean) play.getUserData()) {
                        Media bgm = ConfigCenter.getBgm(item.id);
                        if (bgm != null) {
                            MediaPlayer player = view.getMediaPlayer();
                            if (player != null) {
                                player.stop();
                            }
                            player = new MediaPlayer(bgm);
                            view.setMediaPlayer(player);

                            player.play();
                            play.setText("Pause");
                            play.setUserData(Boolean.FALSE);
                        }
                    } else {
                        MediaPlayer player = view.getMediaPlayer();
                        if (player != null) {
                            player.stop();
                        }
                        view.setMediaPlayer(null);
                        play.setText("Play");
                        play.setUserData(Boolean.TRUE);
                    }
                });
                BorderPane pane = ConfigCenter.createBorderPane(new Node[]{label}, play);
                setGraphic(pane);
            } else {
                setText(null);
                setGraphic(null);
            }
        }
    }

    public static class PersonListCell extends ListCell<PersonC> {
        @Override
        protected void updateItem(PersonC item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                Label label = new Label(item.name);
                label.setPadding(new Insets(0, 10, 0, 0));
                Label state = new Label(item.stateName);
                state.setPadding(new Insets(0, 0, 0, 10));
                ImageView view = new ImageView(ConfigCenter.getPersonState(item.id, item.stateId));
                view.setFitHeight(32);
                view.setFitWidth(32);
                BorderPane pane = ConfigCenter.createBorderPane(new Node[]{label, state}, view);
                setGraphic(pane);
            } else {
                setText(null);
                setGraphic(null);
            }
        }
    }

    public static class SceneListCell extends ListCell<MediaC> {
        @Override
        protected void updateItem(MediaC item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                Label label = new Label(item.name);
                label.setPadding(new Insets(0, 10, 0, 0));
                ImageView view = new ImageView(ConfigCenter.getScene(item.id));
                view.setFitHeight(32);
                view.setFitWidth(32);
                BorderPane pane = ConfigCenter.createBorderPane(new Node[]{label}, view);
                setGraphic(pane);
            } else {
                setText(null);
                setGraphic(null);
            }
        }
    }

    public static class ChapterPlayTreeCell extends TreeCell<StoryBody> {

        TextField field;

        @Override
        protected void updateItem(StoryBody item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (field != null) {
                        String word = item == null ? "" : item.name;
                        field.setText(word);
                    }
                    setText(null);
                    setGraphic(field);
                } else {
                    setText(item == null ? "" : item.name);
                    setGraphic(getTreeItem().getGraphic());
                }
            }
        }

        String _prefName;

        @Override
        public void startEdit() {
            super.startEdit();
            if (getItem().type == StoryBody.Type.GLOBAL) {
                return;
            }

            if (field == null) {
                field = new TextField();
                field.setOnKeyReleased(event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        StoryBody now=getItem();
                        StoryBody newB=new StoryBody(now.type,field.getText(),now.id,now.parent_name);
                        if(now.type== StoryBody.Type.CHAPTER){
                            ObservableList<StoryBody> playList=FormController.get().
                                    getPlayInChapterMap().get(_prefName);
                            TreeItem<StoryBody> item=FormController.get().getChapterTree().get(_prefName);
                            item.getValue().name=newB.name;
                            FormController.get().getChapterTree().remove(_prefName);
                            FormController.get().getChapterTree().put(newB.name,item);
                            FormController.get().getPlayInChapterMap().put(newB.name, playList);
                        }else if(now.type== StoryBody.Type.PLAY){
                            ObservableList<StoryBody> playList=FormController.get().
                                    getPlayInChapterMap().get(now.parent_name);
                            if(playList!=null){
                                playList.remove(now);
                                playList.add(newB);
                            }else {
                                playList= FXCollections.observableList(new ArrayList<>());
                                FormController.get().getPlayInChapterMap().put(now.parent_name,playList);
                                playList.add(newB);
                            }
                        }
                        commitEdit(newB);
                    } else if (event.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                });
                field.setText(getItem().name);
            }
            setText(null);
            setGraphic(field);
            field.selectAll();
            _prefName=getItem().name;
            System.out.println("_prefName:"+_prefName);
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText(getItem().name);
            setGraphic(getTreeItem().getGraphic());
        }
    }

    public static class StoryViewListCell extends ListCell<StoryView> {

        MediaView view;

        public StoryViewListCell(MediaView view) {
            this.view = view;
        }

        @Override
        protected void updateItem(StoryView item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            if (!empty) {
                if (item.name.equals("Word")) {
                    setWord();
                } else if (item.name.equals("Bgm.Play") || item.name.equals("Sound.Play")) {
                    setAudio();
                } else {
                    setNormalCommand();
                }
            } else {
                setGraphic(null);
            }
        }

        //Dialog Word XXXX
        //Dialog Word M XXXX
        //Dialog Word S XXXX
        //Dialog Word DataId XXXX
        private void setWord() {
            System.out.println("Set Word...");
            VBox box = new VBox();
            box.setAlignment(Pos.CENTER_LEFT);
            if (getItem().data.length != 1) {
                Label label = new Label();
                label.setFont(Font.font("System", FontWeight.BOLD,20));
                String id = getItem().data[0];
                if (id.equals("M")) {
                    label.setText("Me Say:");
                } else if (id.equals("S")) {
                    label.setText("Secret Say:");
                } else {
                    label.setText(id + "Say:");
                }
                box.getChildren().add(label);
            } else {
                Label label = new Label();
                label.setFont(Font.font("System", FontWeight.BOLD,20));
                label.setText("Pound Say:");
                box.getChildren().add(label);
            }
            String word = getItem().data[getItem().data.length - 1];
            StringBuilder builder = new StringBuilder();
            int cy = 0;
            int addCount = 0;
            for (int i = 0; i != word.length(); ++i) {
                char c = word.charAt(i);
                //超过一行的结尾,或者强行换行
                if (cy == ConfigCenter.WORD_MAX_COLUMN || c == '\\') {
                    Label line = new Label();
                    line.setFont(Font.font("System", FontWeight.BOLD,20));
                    line.setText(builder.toString());
                    box.getChildren().add(line);
                    builder = new StringBuilder();
                    cy = 0;
                    ++addCount;
                    System.out.println("Append:" + line.getText());
                    //否则就把当前的添加到builder里
                } else {
                    builder.append(c);
                    ++cy;
                }
            }

            //如果builder里还有剩下的文字，但是并没有到一行，并且还有空着的一行，则当作一行存入
            if (addCount != ConfigCenter.WORD_MAX_ROW && builder.length() > 0) {
                Label line = new Label(builder.toString());
                box.getChildren().add(line);
            }

            setGraphic(box);
        }

        private void setAudio() {
            String type = getItem().name;
            if (type.equals("Bgm.Play")) {
                Label label = new Label(getItem().data[0]);
                Button play = new Button("Play");
                play.setUserData(Boolean.TRUE);
                play.setOnAction(event -> {
                    MediaC m = FormController.get().getBgmMap().get(getItem().data[0]);
                    if ((Boolean) play.getUserData()) {
                        Media bgm = ConfigCenter.getBgm(m.id);
                        if (bgm != null) {
                            MediaPlayer player = view.getMediaPlayer();
                            if (player != null) {
                                player.stop();
                            }
                            player = new MediaPlayer(bgm);
                            view.setMediaPlayer(player);

                            player.play();
                            play.setText("Pause");
                            play.setUserData(Boolean.FALSE);
                        }
                    } else {
                        MediaPlayer player = view.getMediaPlayer();
                        if (player != null) {
                            player.stop();
                        }
                        view.setMediaPlayer(null);
                        play.setText("Play");
                        play.setUserData(Boolean.TRUE);
                    }
                });
                BorderPane pane = ConfigCenter.createBorderPane(new Node[]{label}, play);
                setGraphic(pane);
            } else {
                Label label = new Label(getItem().data[0]);
                Button play = new Button("Play");
                play.setOnAction(event -> {
                    MediaC m = FormController.get().getAudioMap().get(getItem().data[0]);
                    AudioClip audioClip = ConfigCenter.getClip(m.id);
                    if (audioClip != null) {
                        audioClip.play();
                    }
                });
                BorderPane pane = ConfigCenter.createBorderPane(new Node[]{label}, play);
                setGraphic(pane);
            }
        }

        private void setNormalCommand() {
            Label type = new Label(getItem().type);
            type.setPadding(new Insets(0, 10, 0, 0));
            Label name = new Label(getItem().name);
            name.setPadding(new Insets(0, 0, 0, 10));
            Label[] data = new Label[getItem().data.length + 2];
            data[0] = type;
            data[1] = name;
            if (getItem().data.length != 0) {
                for (int i = 0; i < data.length; ++i) {
                    data[i + 2] = new Label(getItem().data[i]);
                    data[i + 2].setPadding(new Insets(0, 0, 0, 10));
                }
            }
            BorderPane pane = ConfigCenter.createBorderPane(data, null);
            setGraphic(pane);
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
        uniqueId=dest;
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
    private final Map<String,TreeItem<StoryBody>> chapterTree;
    private final Map<String, ObservableList<StoryBody>> playInChapterMap;

    private Play.GlobalConfig globalConfig;
    private Play.Chapter nowChapter;
    private Play nowPlay;
    private Play.BodyStruck nowStruck;

    private StoryView nowEditExpression;
    private int nowEditIndex;

    private FormController() {
        audioMap = new HashMap<>();
        bgmMap = new HashMap<>();
        sceneMap = new HashMap<>();
        personMap = new HashMap<>();
        playInChapterMap = new HashMap<>();
        chapterTree=new HashMap<>();
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

    public int getNowEditIndex() {
        return nowEditIndex;
    }

    public void setNowEditIndex(int nowEditIndex) {
        this.nowEditIndex = nowEditIndex;
    }
}
