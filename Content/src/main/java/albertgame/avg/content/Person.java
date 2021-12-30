package albertgame.avg.content;

import javafx.scene.image.Image;

import java.util.Map;

public class Person {

    public record PersonData(String id, String name, String defaultState, String[] state) {
    }

    public static final Person NONE_PERSON = new Person();

    private final String id;
    private final String name;

    private String nowState;
    private Image nowStateImage;

    private final String defaultState;
    private final Map<String, Image> stateMap;

    private Person() {
        this.id = "00";
        this.name = "00";
        this.stateMap = null;
        this.nowState = this.defaultState = "00";
    }

    public Person(String id, String name, String defaultState, Map<String, Image> stateMap) {
        this.id = id;
        this.name = name;
        this.stateMap = stateMap;
        this.nowState = this.defaultState = defaultState;
        this.nowStateImage = stateMap.get(defaultState);
    }

    public void changeStateTo(String newState) {
        if (stateMap.containsKey(newState)) {
            nowStateImage = stateMap.get(newState);
            nowState = newState;
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNowState() {
        return nowState;
    }

    public String getDefaultState() {
        return defaultState;
    }

    public Map<String, Image> getStateMap() {
        return stateMap;
    }

    public Image getNowStateImage() {
        return nowStateImage;
    }
}
