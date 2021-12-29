package albertgame.avg.content;

import javafx.beans.property.*;
import javafx.scene.image.Image;

public class GameData {

    public static final char GAME_STATE_WAIT_PRESS=0;
    public static final char GAME_STATE_AUTO_WORDING=1;
    public static final char GAME_STATE_WORD_DISPLAYING=2;
    public static final char GAME_STATE_SELECTING=3;
    public static final char GAME_ANIMATING=4;

    private SaveData saveData;

    private final ObjectProperty<Image> backgroundImage;

    private final ObjectProperty<Image> leftPersonImage;
    private final ObjectProperty<Image> centerPersonImage;
    private final ObjectProperty<Image> rightPersonImage;

    private final StringProperty nameDisplay;
    private final StringProperty[] displayWords;

    private final BooleanProperty wordLineShow;

    private Person leftPerson;
    private Person centerPerson;
    private Person rightPerson;

    private char gameState;

    public GameData() {
        leftPerson=Person.NONE_PERSON;
        centerPerson=Person.NONE_PERSON;
        rightPerson=Person.NONE_PERSON;
        backgroundImage=new SimpleObjectProperty<>();
        leftPersonImage=new SimpleObjectProperty<>();
        centerPersonImage=new SimpleObjectProperty<>();
        rightPersonImage=new SimpleObjectProperty<>();
        nameDisplay=new SimpleStringProperty();
        displayWords =new StringProperty[ConfigCenter.WORD_LINE_ROW*ConfigCenter.WORD_LINE_COLUMN];
        for(int i = 0; i!= displayWords.length; ++i){
            StringProperty text=new SimpleStringProperty(" ");
            displayWords[i]=text;
        }
        wordLineShow=new SimpleBooleanProperty(Boolean.TRUE);
        gameState=GAME_STATE_WAIT_PRESS;
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

    public StringProperty[] getDisplayWords() {
        return displayWords;
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

    public void setLeftPerson(Person leftPerson) {
        this.leftPerson = leftPerson;
    }

    public void setCenterPerson(Person centerPerson) {
        this.centerPerson = centerPerson;
    }

    public void setRightPerson(Person rightPerson) {
        this.rightPerson = rightPerson;
    }

    public char getGameState() {
        return gameState;
    }

    public void setGameState(char gameState) {
        this.gameState = gameState;
    }
}
