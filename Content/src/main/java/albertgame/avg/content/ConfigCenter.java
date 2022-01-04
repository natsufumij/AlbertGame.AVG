package albertgame.avg.content;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ConfigCenter {

    private static final Map<String, Font> fontMap = new HashMap<>();
    private static final Map<String, Image> imageMap = new HashMap<>();
    private static final Map<String, Color> colorMap = new HashMap<>();
    private static final Map<String, Media> bgmMap = new HashMap<>();

    static {
        Map<String, Double> systemFontSizeConfig = new HashMap<>();
        systemFontSizeConfig.put("Name", 22.0);
        systemFontSizeConfig.put("Word", 18.0);
        systemFontSizeConfig.put("Select", 24.0);
        systemFontSizeConfig.put("CacheDate", 14.0);

        Play.SystemConfig config = Play.loadSystemConfig(loadFileInClasspath("config/system.avg"));
        assert config != null;
        colorMap.putAll(config.colorMap());
        loadFont(config.fontMap(), systemFontSizeConfig);
        loadImage(config.imageMap());
        loadBgm(config.bgmMap());
    }

    public static Color getSystemColor(String id) {
        return colorMap.get(id);
    }

    public static Image getSystemImage(String id) {
        return imageMap.get(id);
    }

    public static Font getSystemFont(String id) {
        return fontMap.get(id);
    }

    public static Media getSystemBgm(String id) {
        return bgmMap.get(id);
    }

    private static void loadFont(Map<String, String> fontM, Map<String, Double> systemFontSizeConfig) {
        for (Map.Entry<String, String> e : fontM.entrySet()) {
            String k = e.getKey();
            String v = e.getValue();
            Double size = systemFontSizeConfig.get(k);
            if (v.startsWith("@")) {
                String n = v.substring(1);
                fontMap.put(k, Font.loadFont(ConfigCenter.class.getClassLoader().
                        getResourceAsStream("config/fonts/" + n + ".ttf"), size));
            } else {
                fontMap.put(k, Font.font(v, size));
            }
        }
    }

    private static void loadImage(Map<String, Play.ImageC> imageMapMap) {
        for (Map.Entry<String, Play.ImageC> e : imageMapMap.entrySet()) {
            String k = e.getKey();
            String v = e.getValue().path();
            String format = e.getValue().format();
            imageMap.put(k, loadImageSystem("config/images/" + v, format));
        }
    }

    private static void loadBgm(Map<String, String> bgmMapMap) {
        for (Map.Entry<String, String> e : bgmMapMap.entrySet()) {
            String k = e.getKey();
            String v = e.getValue();
            bgmMap.put(k, new Media(Objects.requireNonNull(
                    ConfigCenter.class.getClassLoader().getResource("config/bgms/" + v + ".mp3")).toExternalForm()));
        }
    }

    public static final int WINDOW_HEIGHT = 600;
    public static final int WINDOW_WIDTH = 1000;

    public static final int PERSON_HEIGHT = WINDOW_HEIGHT;
    public static final int PERSON_WIDTH = WINDOW_WIDTH / 3;

    public static final int WORD_LINE_COLUMN = 30;
    public static final int WORD_LINE_ROW = 3;
    public static final int WORD_MAX_SIZE = WORD_LINE_COLUMN * WORD_LINE_ROW;

    public static final double WORD_FONT_SIZE = 18.0;
    public static final double NAME_FONT_SIZE = 22.0;

    public static final Font WORD_FONT = new Font(18.0);
    public static final Font NAME_FONT = new Font(22.0);

    public static final Font SELECT_FONT = new Font(24.0);
    public static final Font CACHE_DATE_FONT = new Font(14.0);

    public static final int SELECT_WIDTH = 600;
    public static final int SELECT_HEIGHT = 36;

    public static final int WORD_TAP = 3;
    public static final int WORD_LINE_TAP = 10;
    public static final int WORD_PANEL_DISPLAY_X = 0;
    public static final int WORD_PANEL_DISPLAY_Y = WINDOW_HEIGHT / 4 * 3 - 60;
    public static final int WORD_PANEL_WIDTH = WINDOW_WIDTH;
    public static final int WORD_PANEL_HEIGHT = 220;

    public static final int WORD_START_X = WINDOW_WIDTH / 5;
    public static final int WORD_START_Y = WINDOW_HEIGHT / 4 * 3 + 10;

    public static final int NAME_DISPLAY_X = WINDOW_WIDTH / 5 - 20;
    public static final int NAME_DISPLAY_Y = WINDOW_HEIGHT / 4 * 3 - 20;

    public static final int TOOL_DISPLAY_X_R = 840;
    public static final int TOOL_DISPLAY_Y = WINDOW_HEIGHT / 4 * 3 - 20;

    public static final int SELECT_MAX_SIZE = 5;
    public static final int SELECT_Y = 200;
    public static final int SELECT_X = 220;

    public static final int CACHE_COLUMN = 2;
    public static final int CACHE_ROW = 4;
    public static final int CACHE_RECT_WIDTH = 240;
    public static final int CACHE_RECT_HEIGHT = 50;
    public static final int CACHE_RECT_TAP = 10;
    public static final int CACHE_ALL_WIDTH = CACHE_RECT_WIDTH * CACHE_COLUMN + CACHE_RECT_TAP * (CACHE_COLUMN + 1);
    public static final int CACHE_ALL_HEIGHT = CACHE_RECT_HEIGHT * CACHE_ROW + CACHE_RECT_TAP * (CACHE_ROW + 1);
    public static final int CACHE_X = WINDOW_WIDTH / 2 - CACHE_ALL_WIDTH / 2;
    public static final int CACHE_Y = WINDOW_HEIGHT / 2 - CACHE_ALL_HEIGHT / 2;


    public static final String WINDOW_TITLE = "AlbertGame.AVG";

    public static File loadFileInClasspath(String path) {
        return new File(Objects.requireNonNull(ConfigCenter.class.getClassLoader().getResource(path)).getFile());
    }

    private static final String CACHE_PATH = System.getProperty("user.home") + "/.Cache/AlbertGame.AVG/Store_";

    public static boolean isCacheExist(int index) {
        String dest = CACHE_PATH + index + ".properties";
        File file = new File(dest);
        return file.exists();
    }

    public static boolean clearCache(int index) {
        String dest = CACHE_PATH + index + ".properties";
        File file = new File(dest);
        Path path = Path.of(file.toURI());
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean clearCacheData(int index) {
        String dest = CACHE_PATH + index + ".data.properties";
        File file = new File(dest);
        Path path = Path.of(file.toURI());
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static Properties loadCache(int index) {
        String dest = CACHE_PATH + index + ".properties";
        return loadP(dest);
    }

    private static Properties loadP(String dest) {
        File file = new File(dest);
        Properties properties = new Properties();
        if (file.exists() && file.isFile()) {
            try (FileInputStream stream = new FileInputStream(file)) {
                properties.load(new InputStreamReader(stream, StandardCharsets.UTF_8));
                return properties;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }

    public static Properties loadSelects(int index) {
        String dest = CACHE_PATH + index + ".data.properties";
        return loadP(dest);
    }

    public static Map<String, String> loadCacheData(int index) {
        String dest = CACHE_PATH + index + ".data.properties";
        Map<String, String> map = new HashMap<>();
        Properties p = loadP(dest);
        for (Map.Entry<Object, Object> s : p.entrySet()) {
            map.put((String) s.getKey(), (String) s.getValue());
        }
        return map;
    }


    public static void saveCache(int index, Properties properties) {
        String dest = CACHE_PATH + index + ".properties";
        Calendar calendar = Calendar.getInstance();
        properties.setProperty("year", String.valueOf(calendar.get(Calendar.YEAR) - 2000));
        properties.setProperty("month", String.valueOf(calendar.get(Calendar.MONTH)));
        properties.setProperty("day", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        properties.setProperty("hour", String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
        properties.setProperty("minute", String.valueOf(calendar.get(Calendar.MINUTE)));
        properties.setProperty("second", String.valueOf(calendar.get(Calendar.SECOND)));

        saveC(dest, "游戏存档", properties);
    }


    private static void saveC(String dest, String comment, Properties properties) {
        File file = new File(dest);
        try (FileOutputStream stream = new FileOutputStream(file)) {
            properties.store(new OutputStreamWriter(stream, StandardCharsets.UTF_8), "存档");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveSelects(int index, Properties properties) {
        String dest = CACHE_PATH + index + ".data.properties";
        saveC(dest, "游戏选择分支存档", properties);
    }

    public static void saveCacheData(int index, Map<String, String> map) {
        Properties p = new Properties();
        p.putAll(map);

        String dest = CACHE_PATH + index + ".data.properties";
        saveC(dest, "游戏选择分支存档", p);
    }

    static final String PLAY_PATH = "../Play/";

    private static File loadFileInPath(String path) {
        return new File(PLAY_PATH + path);
    }

    public static Play loadPlay(String chapterId, String name) {
        File file = loadFileInPath("story/" + chapterId + "/" + name + ".avg");
        return Play.loadPlay(file);
    }

    public static Play.Chapter loadChapter(String chapterId) {
        File file = loadFileInPath("story/" + chapterId + ".avg");
        return Play.loadChapter(file);
    }

    public static Play.GlobalConfig loadGlobalConfig() {
        File file = loadFileInPath("story/global.avg");
        return Play.loadGlobalConfig(file);
    }

    public static Image loadScene(String name) {
        return loadImage("scene/" + name, "jpg");
    }

    public static Image loadPersonState(String id, String state) {
        return loadImage("person/" + id + "_" + state, "png");
    }

    public static Image loadImageSystem(String path, String format) {
        return new Image(Objects.requireNonNull(ConfigCenter.class.getClassLoader().getResourceAsStream(path + "." + format)));
    }

    public static Image loadImage(String path, String format) {
        return loadImageFromPath(path, format);
//        return new Image(Objects.requireNonNull(ConfigCenter.class.getClassLoader().getResourceAsStream(path + "." + format)));
    }

    private static Image loadImageFromPath(String path, String format) {
        try {
            return new Image(loadFileInPath(PLAY_PATH + path + "." + format).toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Media loadBgm(String name) {
        File file = loadFileInPath(PLAY_PATH + "bgm/" + name + ".mp3");
        try {
            return new Media(file.toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
//        return new Media(Objects.requireNonNull(ConfigCenter.class.getClassLoader().getResource("play/bgm/" + name + ".mp3")).toExternalForm());
    }

    public static AudioClip loadAudio(String name) {
        File file = loadFileInPath(PLAY_PATH + "audio/" + name + ".wav");
        try {
            return new AudioClip(file.toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
//        return new AudioClip(Objects.requireNonNull(ConfigCenter.class.getClassLoader().getResource("play/audio/" + name + ".wav")).toExternalForm());
    }
}
