package albertgame.avg.editor;

import albertgame.avg.editor.Play.GlobalConfig.PersonConfig;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.util.HashMap;
import java.util.Map;

public class FormController {

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
        String type;
        String name;
        String[] data;

        public StoryView(String type, String name, String[] data) {
            this.type = type;
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

        public StoryBody(Type type, String name, String id) {
            this.type = type;
            this.name = name;
            this.id = id;
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
                        commitEdit(new StoryBody(getItem().type, field.getText(), getItem().id));
                    } else if (event.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                });
                field.setText(getItem().name);
            }
            setText(null);
            setGraphic(field);
            field.selectAll();
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
                label.setText("Pound Say:");
                box.getChildren().add(label);
            }
            String word = getItem().data[getItem().data.length - 1];
            StringBuilder builder = new StringBuilder();
            int cy = 0;
            int addCount=0;
            for (int i = 0; i != word.length(); ++i) {
                char c = word.charAt(i);
                //超过一行的结尾,或者强行换行
                if (cy == ConfigCenter.WORD_MAX_COLUMN || c == '\\') {
                    Label line = new Label();
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
            if(addCount!=ConfigCenter.WORD_MAX_ROW && builder.length() > 0){
                Label line=new Label(builder.toString());
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

    //均为Name - Map的映射
    private final Map<String, MediaC> audioMap;
    private final Map<String, MediaC> bgmMap;
    private final Map<String, MediaC> sceneMap;
    private final Map<String, PersonConfig> personMap;

    //均为Id - Map的映射
    private final Map<String, StoryBody> chapterMap;
    private final Map<String, ObservableList<StoryBody>> playInChapterMap;

    private FormController() {
        audioMap = new HashMap<>();
        bgmMap = new HashMap<>();
        sceneMap = new HashMap<>();
        personMap = new HashMap<>();
        chapterMap = new HashMap<>();
        playInChapterMap = new HashMap<>();
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

    public Map<String, PersonConfig> getPersonMap() {
        return personMap;
    }

    public Map<String, StoryBody> getChapterMap() {
        return chapterMap;
    }

    public Map<String, ObservableList<StoryBody>> getPlayInChapterMap() {
        return playInChapterMap;
    }
}
