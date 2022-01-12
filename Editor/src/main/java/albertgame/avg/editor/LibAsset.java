package albertgame.avg.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibAsset {

    public static class Person{
        String id;
        String name;
        List<String> states;

        public Person(String id, String name) {
            this.id = id;
            this.name = name;
            states=new ArrayList<>();
        }
    }

    public enum TYPE {
        AUDIO, BGM, PERSON, SCENE, STORY
    }

    private static final Map<TYPE, Handler> all = new HashMap<>();

    public static Handler findHandler(TYPE type) {
        return all.get(type);
    }

    public static void addHandler(TYPE type, Handler handler) {
        all.put(type, handler);
    }

    public static void removeHandler(TYPE type) {
        all.remove(type);
    }

    final String id;
    final String format;
    String name;
    String path;

    public LibAsset(String id, String name, String path, String format) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.format = format;
    }

    @Override
    public String toString() {
        return name;
    }

    interface Handler {

        String format();

        void create();

        void edit();

        void remove();

        LibAsset copySelectItem();

        Map<String, LibAsset> allResources();
    }
}
