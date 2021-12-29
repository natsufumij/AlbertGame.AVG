package albertgame.avg.content;

import javafx.scene.image.Image;
import javafx.scene.text.Font;

import java.util.Objects;

public class ConfigCenter {

    public static final int WINDOW_HEIGHT = 600;
    public static final int WINDOW_WIDTH = 1000;

    public static final int PERSON_HEIGHT = WINDOW_HEIGHT;
    public static final int PERSON_WIDTH = WINDOW_WIDTH / 3;

    public static final int WORD_LINE_COLUMN=30;
    public static final int WORD_LINE_ROW=3;

    public static final Font WORD_FONT=new Font(16.0);

    public static final int WORD_TAP=3;

    public static final String WINDOW_TITLE = "AlbertGame.AVG";

    public static final Image WINDOW_ICON = loadImage("config/icon", "jpeg");

    public static Image loadScene(String name, String format) {
        return loadImage("play/scene/" + name, format);
    }

    public static Image loadPersonState(String name, String state, String format) {
        return loadImage("play/person/" + name + "_" + state, format);
    }

    public static Image loadImage(String path, String format) {
        return new Image(Objects.requireNonNull(ConfigCenter.class.getClassLoader().getResourceAsStream(path + "." + format)));
    }
}
