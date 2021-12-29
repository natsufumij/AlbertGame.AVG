package albertgame.avg.content;

import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameHeader extends Parent {

    public static final String[] buttonNames = {
            "SKIP", "AUTO", "SAVE", "LOAD", "BACK"
    };

    private ImageView background;

    private ImageView leftPerson;
    private ImageView centerPerson;
    private ImageView rightPerson;

    private final Button[] buttonList = new Button[buttonNames.length];

    public GameHeader(GameData data) {
        initHead(data);
    }

    public void initHead(GameData data) {
        background = new ImageView(ConfigCenter.loadScene("back_demo1", "jpeg"));
        background.setFitWidth(ConfigCenter.WINDOW_WIDTH);
        background.setFitHeight(ConfigCenter.WINDOW_HEIGHT);

        Image ona = ConfigCenter.loadPersonState("onna", "1", "jpg");
        Image otoko = ConfigCenter.loadPersonState("otoko", "1", "jpg");
        Image bishojo = ConfigCenter.loadPersonState("bishojo", "1", "jpg");

        leftPerson = new ImageView(ona);
        leftPerson.setFitWidth(ConfigCenter.PERSON_WIDTH);
        leftPerson.setFitHeight(ConfigCenter.PERSON_HEIGHT);

        centerPerson = new ImageView(bishojo);
        centerPerson.setFitWidth(ConfigCenter.PERSON_WIDTH);
        centerPerson.setFitHeight(ConfigCenter.PERSON_HEIGHT);
        centerPerson.setTranslateX(ConfigCenter.PERSON_WIDTH);

        rightPerson = new ImageView(otoko);
        rightPerson.setFitWidth(ConfigCenter.PERSON_WIDTH);
        rightPerson.setFitHeight(ConfigCenter.PERSON_HEIGHT);

        rightPerson.setTranslateX(ConfigCenter.PERSON_WIDTH * 2);

        Text name = new Text("SECRET PLAYER");
        name.setFont(ConfigCenter.WORD_FONT);
        name.setTranslateX(ConfigCenter.NAME_DISPLAY_X);
        name.setTranslateY(ConfigCenter.NAME_DISPLAY_Y);
        name.setStroke(Color.WHITE);
        name.setEffect(new Bloom(0.1));

        Rectangle wordPaneFrameV2_b = new Rectangle(ConfigCenter.WORD_PANEL_WIDTH,
                ConfigCenter.WORD_PANEL_HEIGHT, Color.color(0.2, 0.2, 0.2, 0.5));
        wordPaneFrameV2_b.setTranslateY(ConfigCenter.WORD_PANEL_DISPLAY_Y);
        wordPaneFrameV2_b.setTranslateX(ConfigCenter.WORD_PANEL_DISPLAY_X);


        super.getChildren().add(background);
        super.getChildren().addAll(leftPerson, centerPerson, rightPerson);
        super.getChildren().addAll(wordPaneFrameV2_b);
        super.getChildren().addAll(name);

        Text[] words = new Text[ConfigCenter.WORD_LINE_ROW * ConfigCenter.WORD_LINE_COLUMN];
        Font f;
        for (int i = 0; i != words.length; ++i) {
            int row = i / ConfigCenter.WORD_LINE_COLUMN;
            int column = i % ConfigCenter.WORD_LINE_COLUMN;
            Text t = new Text(" ");
            words[i] = t;
            t.setEffect(new DropShadow(1, 1, 1, Color.BLACK));
            t.setStroke(Color.WHITE);
            t.setFont(ConfigCenter.WORD_FONT);
            f = t.getFont();
            t.setTranslateX(ConfigCenter.WORD_START_X +
                    column * f.getSize() +
                    column * ConfigCenter.WORD_TAP);
            t.setTranslateY(ConfigCenter.WORD_START_Y +
                    (row + 1) * f.getSize() +
                    (row - 1) * ConfigCenter.WORD_LINE_TAP);
            super.getChildren().add(t);
        }

        List<EventHandler<ActionEvent>> eventHandlers = new ArrayList<>(Arrays.asList(
                new SkipEventHandler(),
                new AutoEventHandler(),
                new SaveEventHandler(),
                new LoadEventHandler(),
                new BackEventHandler()
        ));

        double prefX = ConfigCenter.TOOL_DISPLAY_X_R - 55;
        for (int i = buttonList.length - 1; i != -1; --i) {
            Button button = new Button(buttonNames[i]);
            button.setOnAction(eventHandlers.get(i));
            button.setLayoutX(prefX);
            button.setPrefSize(50, 28);
            button.setLayoutY(ConfigCenter.TOOL_DISPLAY_Y);
            super.getChildren().add(button);
            prefX -= 55;
        }

        data.backgroundImageProperty().bindBidirectional(background.imageProperty());
        data.leftPersonImageProperty().bindBidirectional(leftPerson.imageProperty());
        data.rightPersonImageProperty().bindBidirectional(rightPerson.imageProperty());
        data.nameDisplayProperty().bindBidirectional(name.textProperty());
        for (int i = 0; i != data.getDisplayWords().length; ++i) {
            StringProperty text = data.getDisplayWords()[i];
            text.bindBidirectional(words[i].textProperty());
            text.setValue("我");
        }

        data.wordLineShowProperty().bindBidirectional(wordPaneFrameV2_b.visibleProperty());
        data.nameShowProperty().bindBidirectional(name.visibleProperty());
    }

    public ImageView getBackground() {
        return background;
    }

    public ImageView getLeftPerson() {
        return leftPerson;
    }

    public ImageView getCenterPerson() {
        return centerPerson;
    }

    public ImageView getRightPerson() {
        return rightPerson;
    }

    static class SkipEventHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            System.out.println("Skip");

        }
    }

    static class AutoEventHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            System.out.println("Auto");
            GameData d=ManageCenter.getCenter().getGameData();
            boolean aut=d.isAuto();
            d.setAuto(!aut);
        }
    }

    static class SaveEventHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            //TODO: 调出存档界面
        }
    }

    static class LoadEventHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            //TODO: 调出存档界面
        }
    }

    static class BackEventHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            //TODO: 返回主界面
        }
    }
}
