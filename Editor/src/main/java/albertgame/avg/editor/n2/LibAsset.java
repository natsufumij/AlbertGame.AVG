package albertgame.avg.editor.n2;

import java.util.Map;

public class LibAsset {
    final String id;
    String name;
    String path;
    String format;

    public LibAsset(String id, String name, String path, String format) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.format = format;
    }

    interface Handler {

        void init();

        void create();

        void edit();

        void remove();

        LibAsset copySelectItem();

        Map<String, LibAsset> allResources();
    }
}
