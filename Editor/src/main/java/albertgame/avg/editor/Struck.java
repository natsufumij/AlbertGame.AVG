package albertgame.avg.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Struck {
    private final String id;
    private String name;
    private final List<String> expressions;

    public Struck(String id, String name) {
        this.id = id;
        this.name = name;
        expressions = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getExpressions() {
        return expressions;
    }

    interface Handler {

        void loadAllStruck(Play play);

        void create();

        void remove();

        Map<String, Struck> allStruck();

        Struck copySelectItem();
    }
}
