package albertgame.avg.content;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.text.Font;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Objects;

public class ConfigCenter {

    public static final int WINDOW_HEIGHT = 600;
    public static final int WINDOW_WIDTH = 1000;

    public static final int PERSON_HEIGHT = WINDOW_HEIGHT;
    public static final int PERSON_WIDTH = WINDOW_WIDTH / 3;

    public static final int WORD_LINE_COLUMN = 30;
    public static final int WORD_LINE_ROW = 3;
    public static final int WORD_MAX_SIZE=WORD_LINE_COLUMN*WORD_LINE_ROW;

    public static final Font WORD_FONT = new Font(18.0);
    public static final Font NAME_FONT=new Font(22.0);

    public static final Font SELECT_FONT = new Font(24.0);
    public static final int SELECT_WIDTH=600;
    public static final int SELECT_HEIGHT=36;

    public static final int WORD_TAP = 3;
    public static final int WORD_LINE_TAP = 10;
    public static final int WORD_PANEL_DISPLAY_X = 0;
    public static final int WORD_PANEL_DISPLAY_Y = WINDOW_HEIGHT / 4 * 3 - 60;
    public static final int WORD_PANEL_WIDTH = WINDOW_WIDTH;
    public static final int WORD_PANEL_HEIGHT = 220;

    public static final int WORD_START_X = WINDOW_WIDTH / 5;
    public static final int WORD_START_Y = WINDOW_HEIGHT / 4 * 3  + 10;

    public static final int NAME_DISPLAY_X = WINDOW_WIDTH / 5 - 20;
    public static final int NAME_DISPLAY_Y = WINDOW_HEIGHT / 4 * 3 - 20;

    public static final int TOOL_DISPLAY_X_R = 840;
    public static final int TOOL_DISPLAY_Y = WINDOW_HEIGHT / 4 * 3 - 20;

    public static final int SELECT_MAX_SIZE=5;
    public static final int SELECT_Y=200;
    public static final int SELECT_X=220;

    public static final String WINDOW_TITLE = "AlbertGame.AVG";

    public static final Image WINDOW_ICON = loadImage("config/icon", "jpeg");

    public static File loadFileInClasspath(String path){
        return new File(Objects.requireNonNull(ConfigCenter.class.getClassLoader().getResource(path)).getFile());
    }

    public static Image loadScene(String name) {
        return loadImage("play/scene/" + name,"jpg");
    }

    public static Image loadPersonState(String id, String state) {
        return loadImage("play/person/" + id + "_" + state, "png");
    }

    public static Image loadImage(String path, String format) {
        return new Image(Objects.requireNonNull(ConfigCenter.class.getClassLoader().getResourceAsStream(path + "." + format)));
    }

    public static Media loadBgm(String name) {
        return new Media(Objects.requireNonNull(ConfigCenter.class.getClassLoader().getResource("play/bgm/" + name + ".mp3")).toExternalForm());
    }

    public static AudioClip loadAudio(String name){
        return new AudioClip(Objects.requireNonNull(ConfigCenter.class.getClassLoader().getResource("play/audio/" + name + ".wav")).toExternalForm());
    }
}
