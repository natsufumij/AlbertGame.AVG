package albertgame.avg.editor.n2;

import java.util.Map;

public class LibResource {
    final String id;
    String name;
    String path;
    String format;

    public LibResource(String id, String name, String path, String format) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.format = format;
    }

    interface Handler {

        void initHandler();

        void addResource(LibResource resource);

        void removeResource(String id);

        void removeSelect();

        void editResource(LibResource resource);

        LibResource copySelectItem();

        Map<String, LibResource> allResources();
    }
}
