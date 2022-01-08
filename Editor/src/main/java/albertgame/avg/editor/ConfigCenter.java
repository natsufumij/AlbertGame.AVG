package albertgame.avg.editor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

public class ConfigCenter {
    public static final int WORD_MAX_COLUMN = 30;
    public static final int WORD_MAX_ROW = 3;
    public static final int WORD_MAX_SIZE = WORD_MAX_COLUMN * WORD_MAX_ROW;

    public static final String[] AUDIO_COMMANDS = new String[]{
            "Bgm.Play", "Bgm.Pause", "Bgm.Resume",
            "Bgm.Stop", "Sound.Play"
    };
    public static final int BGM_PLAY = 0;
    public static final int BGM_PAUSE = 1;
    public static final int BGM_RESUME = 2;
    public static final int BGM_STOP = 3;
    public static final int SOUND_PLAY = 4;
    public static final ObservableList<String> AUDIO_OB_LIST = FXCollections.observableList(
            List.of(AUDIO_COMMANDS));

    public static final String[] STORAGE_COMMANDS = new String[]{
            "Save", "Plus", "Minus"
    };
    public static final int SAVE = 0;
    public static final int PLUS = 1;
    public static final int MINUS = 2;
    public static final ObservableList<String> STORAGE_OB_LIST = FXCollections.observableList(
            List.of(STORAGE_COMMANDS));

    public static final String[] VIEW_COMMANDS = new String[]{
            "Scene", "Shake", "Darking", "Lighting"
    };
    public static final int SCENE = 0;
    public static final int SHAKE = 1;
    public static final int DARKING = 2;
    public static final int LIGHTING = 3;
    public static final ObservableList<String> VIEW_OB_LIST = FXCollections.observableList(
            List.of(VIEW_COMMANDS)
    );

    public static final String[] PERSON_COMMANDS = new String[]{
            "In", "Out", "Show", "No.Show", "Hide", "Change.State"
    };
    public static final int IN = 0;
    public static final int OUT = 1;
    public static final int SHOW = 2;
    public static final int NO_SHOW = 3;
    public static final int HIDE = 4;
    public static final int CHANGE_STATE = 5;
    public static final ObservableList<String> PERSON_OB_LIST = FXCollections.observableList(
            List.of(PERSON_COMMANDS)
    );

    public static final String[] DIALOG_COMMANDS = new String[]{
            "Open", "Clear", "Close"
    };
    public static final int OPEN = 0;
    public static final int CLEAR = 1;
    public static final int CLOSE = 2;
    public static final ObservableList<String> DIALOG_OB_LIST = FXCollections.observableList(
            List.of(DIALOG_COMMANDS)
    );

    public static BorderPane createBorderPane(Node[] lefts, Node right) {
        BorderPane pane = new BorderPane();

        if (right != null) {
            BorderPane.setAlignment(right, Pos.CENTER_RIGHT);
            pane.setRight(right);
        }
        if (lefts.length > 1) {
            HBox box = new HBox();
            box.setAlignment(Pos.CENTER_LEFT);
            BorderPane.setAlignment(box, Pos.CENTER_LEFT);
            for (Node n : lefts) {
                box.getChildren().add(n);
            }
            pane.setLeft(box);
        } else {
            BorderPane.setAlignment(lefts[0], Pos.CENTER_LEFT);
            pane.setLeft(lefts[0]);
        }

        return pane;
    }

    public static AudioClip getClip(String id) {
        String url = getSourceUrl("audio", id, "wav");
        if (url == null) return null;
        return new AudioClip(url);
    }

    public static Media getBgm(String id) {
        String url = getSourceUrl("bgm", id, "mp3");
        System.out.println("url:" + url);
        if (url == null) return null;
        return new Media(url);
    }

    public static Image getScene(String id) {
        String url = getSourceUrl("scene", id, "jpg");
        if (url == null) return null;
        return new Image(url);
    }

    public static Image getPersonState(String personId, String stateId) {
        String url = getSourceUrl("person", personId + "_" + stateId, "png");
        if (url == null) return null;
        return new Image(url);
    }

    private static String getSourceUrl(String lib, String id, String format) {
        String pa = FormController.getNowPath() + "/" + lib + "/" + id + "." + format;
        File file = new File(pa);
        if (file.exists()) {
            try {
                return file.toURI().toURL().toExternalForm();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        } else return null;
    }
}
