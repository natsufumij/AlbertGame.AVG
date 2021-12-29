package albertgame.avg.content;

import javafx.beans.property.*;
import javafx.scene.image.Image;

public class GameData {

    static {
        synchronized (GameData.class){
            GameData.gameData=new GameData();
        }
    }

    public static GameData get(){
        return gameData;
    }

    private static GameData gameData;

    private SaveData saveData;

    private ObjectProperty<Image> backgroundImage;

    private ObjectProperty<Image> leftPersonImage;
    private ObjectProperty<Image> centerPersonImage;
    private ObjectProperty<Image> rightPersonImage;

    private StringProperty nameDisplay;
    private StringProperty[] displayWordLine;

    private BooleanProperty wordLineShow;

    private Person leftPerson;
    private Person centerPerson;
    private Person rightPerson;

    private GameData() {
        leftPerson=Person.NONE_PERSON;
        centerPerson=Person.NONE_PERSON;
        rightPerson=Person.NONE_PERSON;
        backgroundImage=new SimpleObjectProperty<>();
        leftPersonImage=new SimpleObjectProperty<>();
        centerPersonImage=new SimpleObjectProperty<>();
        rightPersonImage=new SimpleObjectProperty<>();
        nameDisplay=new SimpleStringProperty();
        displayWordLine=new StringProperty[ConfigCenter.WORD_LINE_ROW*ConfigCenter.WORD_LINE_COLUMN];
        for(int i=0;i!=displayWordLine.length;++i){
            StringProperty text=new SimpleStringProperty(" ");
            displayWordLine[i]=text;
        }
        wordLineShow=new SimpleBooleanProperty(Boolean.TRUE);
    }

    public SaveData getSaveData() {
        return saveData;
    }

    public void setSaveData(SaveData saveData) {
        this.saveData = saveData;
    }

    public Image getBackgroundImage() {
        return backgroundImage.get();
    }

    public ObjectProperty<Image> backgroundImageProperty() {
        return backgroundImage;
    }

    public Image getLeftPersonImage() {
        return leftPersonImage.get();
    }

    public ObjectProperty<Image> leftPersonImageProperty() {
        return leftPersonImage;
    }

    public Image getCenterPersonImage() {
        return centerPersonImage.get();
    }

    public ObjectProperty<Image> centerPersonImageProperty() {
        return centerPersonImage;
    }

    public Image getRightPersonImage() {
        return rightPersonImage.get();
    }

    public ObjectProperty<Image> rightPersonImageProperty() {
        return rightPersonImage;
    }

    public String getNameDisplay() {
        return nameDisplay.get();
    }

    public StringProperty nameDisplayProperty() {
        return nameDisplay;
    }

    public StringProperty[] getDisplayWordLine() {
        return displayWordLine;
    }

    public boolean isWordLineShow() {
        return wordLineShow.get();
    }

    public BooleanProperty wordLineShowProperty() {
        return wordLineShow;
    }

    public Person getLeftPerson() {
        return leftPerson;
    }

    public Person getCenterPerson() {
        return centerPerson;
    }

    public Person getRightPerson() {
        return rightPerson;
    }
}
