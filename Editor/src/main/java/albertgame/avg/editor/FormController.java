package albertgame.avg.editor;

import albertgame.avg.editor.Play.GlobalConfig.PersonConfig;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FormController {

    private static File nowSelectFile;
    private static String nowPath="Assets";

    public static String getNowPath() {
        return nowPath;
    }

    public static void setNowPath(String nowPath) {
        FormController.nowPath = nowPath;
    }

    public static File getNowSelectFile() {
        return nowSelectFile;
    }

    public static void setNowSelectFile(File nowSelectFile) {
        FormController.nowSelectFile = nowSelectFile;
    }

    public static class MediaC {
        String id;
        String name;

        public MediaC(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

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

    public static class PersonC{
        String id,name;
        String stateId,stateName;
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
                BorderPane pane = ConfigCenter.createBorderPane(label, null, play);
                setGraphic(pane);
            }
        }
    }

    public static class BgmListCell extends ListCell<MediaC> {

        MediaView view;

        public BgmListCell(MediaView mediaView) {
            view=mediaView;
        }

        @Override
        protected void updateItem(MediaC item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                Label label = new Label(item.name);
                Button play = new Button("Play");
                play.setUserData(Boolean.TRUE);
                play.setOnAction(event -> {
                    if((Boolean)play.getUserData()){
                        Media bgm = ConfigCenter.getBgm(item.id);
                        if(bgm!=null){
                            MediaPlayer player=view.getMediaPlayer();
                            if(player!=null){
                                player.stop();
                            }
                            player=new MediaPlayer(bgm);
                            view.setMediaPlayer(player);

                            player.play();
                            play.setText("Pause");
                            play.setUserData(Boolean.FALSE);
                        }
                    }else {
                        MediaPlayer player=view.getMediaPlayer();
                        if(player!=null){
                            player.stop();
                        }
                        view.setMediaPlayer(null);
                        play.setText("Play");
                        play.setUserData(Boolean.TRUE);
                    }
                });
                BorderPane pane = ConfigCenter.createBorderPane(label, null, play);
                setGraphic(pane);
            }
        }
    }

    public static class PersonListCell extends ListCell<PersonC>{
        @Override
        protected void updateItem(PersonC item, boolean empty) {
            super.updateItem(item, empty);
            if(item!=null){
                Label label=new Label(item.name);
                label.setPadding(new Insets(0,10,0,0));
                Label state=new Label(item.stateName);
                state.setPadding(new Insets(0,0,0,10));
                ImageView view=new ImageView(ConfigCenter.getPersonState(item.id,item.stateId));
                view.setFitHeight(32);
                view.setFitWidth(32);
                BorderPane pane=ConfigCenter.createBorderPane(label,state,view);
                setGraphic(pane);
            }
        }
    }

    public static class SceneListCell extends ListCell<MediaC>{
        @Override
        protected void updateItem(MediaC item, boolean empty) {
            super.updateItem(item, empty);
            if(item!=null){
                Label label=new Label(item.name);
                label.setPadding(new Insets(0,10,0,0));
                ImageView view=new ImageView(ConfigCenter.getScene(item.id));
                view.setFitHeight(32);
                view.setFitWidth(32);
                BorderPane pane=ConfigCenter.createBorderPane(label,null,view);
                setGraphic(pane);
            }
        }
    }

    //均为Name - Map的映射
    private final Map<String, MediaC> audioMap;
    private final Map<String, MediaC> bgmMap;
    private final Map<String, MediaC> sceneMap;
    private final Map<String, PersonConfig> personMap;

    public FormController() {
        audioMap = new HashMap<>();
        bgmMap = new HashMap<>();
        sceneMap = new HashMap<>();
        personMap = new HashMap<>();
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
}
