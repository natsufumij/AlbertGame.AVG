package albertgame.avg.content;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigCenter {

    private static final boolean loadOnClassPath = true;
    private static final String ASSET_PATH = "Assets";

    private static final Map<String, Font> fontMap = new HashMap<>();
    private static final Map<String, Image> imageMap = new HashMap<>();
    private static final Map<String, Color> colorMap = new HashMap<>();
    private static final Map<String, Media> bgmMap = new HashMap<>();

    public static Play.SystemConfig config;

    static {
        Map<String, Double> systemFontSizeConfig = new HashMap<>();
        systemFontSizeConfig.put("Name", 22.0);
        systemFontSizeConfig.put("Word", 18.0);
        systemFontSizeConfig.put("Select", 24.0);
        systemFontSizeConfig.put("CacheDate", 14.0);

        config = Play.loadSystemConfig(loadUrl("config", "system", "avg"));
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
                fontMap.put(k, loadSystemFont(n, size));
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
            imageMap.put(k, loadImageSystem(v, format));
        }
    }

    private static void loadBgm(Map<String, String> bgmMapMap) {
        for (Map.Entry<String, String> e : bgmMapMap.entrySet()) {
            String k = e.getKey();
            String v = e.getValue();
            bgmMap.put(k, loadSystemBgm(v));
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

    private static final String CACHE_PATH = System.getProperty("user.home") + "/.Cache/AlbertGame/AVG/Store_";

    public static boolean isCacheExist(int index) {
        String dest = CACHE_PATH + index + ".properties";
        File file = new File(dest);
        return file.exists();
    }

    public static boolean clearCache(int index) {
        String dest = CACHE_PATH + index + ".properties";
        File file = new File(dest);
        if (file.exists()) {
            Path path = Path.of(file.toURI());
            try {
                Files.delete(path);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean clearCacheData(int index) {
        String dest = CACHE_PATH + index + "_data.properties";
        File file = new File(dest);
        if (file.exists()) {
            Path path = Path.of(file.toURI());
            try {
                Files.delete(path);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        String dest = CACHE_PATH + index + "_data.properties";
        return loadP(dest);
    }

    public static Map<String, String> loadCacheData(int index) {
        String dest = CACHE_PATH + index + "_data.properties";
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
        String ys, mos, ds, hs, ms, ss;
        int year = calendar.get(Calendar.YEAR) - 2000;
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int second = calendar.get(Calendar.SECOND);
        int minute = calendar.get(Calendar.MINUTE);
        ys = String.valueOf(year);
        mos = month < 10 ? "0" + month : "" + month;
        ds = day < 10 ? "0" + day : "" + day;
        hs = hour < 10 ? "0" + hour : "" + hour;
        ms = minute < 10 ? "0" + minute : "" + minute;
        ss = second < 10 ? "0" + second : "" + second;

        properties.setProperty("year", ys);
        properties.setProperty("month", mos);
        properties.setProperty("day",ds);
        properties.setProperty("hour", hs);
        properties.setProperty("minute",ms);
        properties.setProperty("second", ss);

        saveC(dest, "游戏存档", properties);
    }

    private static void saveC(String dest, String comment, Properties properties) {
        File file = new File(dest);
        if (!file.exists()) {
            File f = file.getParentFile();
            f.mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileOutputStream stream = new FileOutputStream(file);
            OutputStreamWriter writer=new OutputStreamWriter(stream,StandardCharsets.UTF_8)) {
            properties.store(writer, comment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveSelects(int index, Properties properties) {
        String dest = CACHE_PATH + index + "_data.properties";
        saveC(dest, "游戏选择分支存档", properties);
    }

    public static Play loadPlay(String chapterId, String name) {
        return Play.loadPlay(loadUrl("play/story", chapterId + "/" + name, "avg"));
    }

    public static Play.Chapter loadChapter(String chapterId) {
        return Play.loadChapter(loadUrl("play/story", chapterId, "avg"));
    }

    public static Play.GlobalConfig loadGlobalConfig() {
        return Play.loadGlobalConfig(loadUrl("play/story", "global", "avg"));
    }

    public static Image loadScene(String name) {
        return new Image(loadFileUrl("play/scene", name, "jpg"));
    }

    public static Image loadPersonState(String id, String state) {
        return new Image(loadFileUrl("play/person", id + "_" + state, "png"));
    }

    private static Image loadImageSystem(String path, String format) {
        return new Image(loadFileUrl("config/images", path, format));
    }

    public static Media loadBgm(String name) {
        return new Media(loadFileUrl("play/bgm", name, "mp3"));
    }

    private static Media loadSystemBgm(String name) {
        return new Media(loadFileUrl("config/bgms", name, "mp3"));
    }

    private static Font loadSystemFont(String path, double size) {
        return Font.loadFont(loadFileUrl("config/fonts", path, "ttf"), size);
    }

    public static AudioClip loadAudio(String name) {
        return new AudioClip(loadFileUrl("play/audio", name, "wav"));
    }

    private static String loadFileUrl(String lib, String name, String format) {
        return loadUrl(lib, name, format).toExternalForm();
    }

    private static URL loadUrl(String lib, String name, String format) {
        if (!loadOnClassPath) {
            File f = new File(ASSET_PATH + "/" + lib + "/" + name + "." + format);
            try {
                return f.toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                System.err.println("File Not Found: " + f.getPath());
                return null;
            }
        }

        return ConfigCenter.class.getResource(lib + "/" + name + "." + format);
    }
}
