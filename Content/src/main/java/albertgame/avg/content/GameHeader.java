package albertgame.avg.content;

import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameHeader extends Parent {

    public static final String[] buttonNames = {
            "SKI P", "AUTO", "SAVE", "LOAD", "BACK"
    };

    private ImageView background;

    private ImageView leftPerson;
    private ImageView centerPerson;
    private ImageView rightPerson;

    private MediaView mediaView;

    private Rectangle globalMask;

    private final Text[] buttonList = new Text[buttonNames.length];

    private final Text[] selectText = new Text[ConfigCenter.SELECT_MAX_SIZE];
    private final Rectangle[] selectMasks = new Rectangle[ConfigCenter.SELECT_MAX_SIZE];

    private Text name;
    GameData data;
    Rectangle wordPaneFrameV2_b;

    public GameHeader(GameData data) {
        initHead(data);
    }

    public void initHead(GameData data) {
        this.data = data;
        mediaView = new MediaView();
        super.getChildren().add(mediaView);

        initBackground();

        initPersons();

        initNameAndWordPanel();

        initPanelWord();

        initTool();

        initSelectPanel(data);

        initGlobalMask();
    }

    private void initGlobalMask() {
        globalMask = new Rectangle(ConfigCenter.WINDOW_WIDTH, ConfigCenter.WINDOW_HEIGHT);
        globalMask.setFill(Color.color(0, 0, 0, 0));
        globalMask.setTranslateX(0);
        globalMask.setTranslateY(0);
        globalMask.setVisible(false);

        super.getChildren().add(globalMask);
    }

    private void initSelectPanel(GameData data) {
        for (int i = 0; i != selectText.length; ++i) {

            double ty = ConfigCenter.SELECT_Y + i * ConfigCenter.SELECT_FONT.getSize() * 1.8;
            Rectangle rectangle = new Rectangle(ConfigCenter.SELECT_WIDTH, ConfigCenter.SELECT_HEIGHT);
            rectangle.setUserData((i + 1));
            Color selectC = Color.color(0.2, 0.3, 0.2, 0.3);
            Color noneC = Color.color(0.1, 0.1, 0.1, 0.2);
            rectangle.setArcWidth(4.0);
            rectangle.setArcHeight(3.0);
            rectangle.setFill(selectC);
            rectangle.setOnMouseEntered(event -> {
                rectangle.setFill(selectC);
                rectangle.setEffect(new Bloom(0.1));
            });
            rectangle.setOnMouseExited(event -> {
                rectangle.setFill(noneC);
                rectangle.setEffect(null);
            });
            rectangle.setTranslateX(ConfigCenter.SELECT_X - 10);
            rectangle.setTranslateY(ty - 27.0);
            rectangle.setOnMouseClicked(event -> {
                System.out.println("You Select " + (Integer) rectangle.getUserData());
                for (int j = 0; j != selectText.length; ++j) {
                    Text desist = selectText[j];
                    desist.visibleProperty().set(false);
                }
                data.getData().put(data.getNowSelectId(), (String) rectangle.getUserData());
                data.setGameState(GameData.GAME_STATE_WAIT_NEXT);
            });

            Text t = new Text("");
            t.setUserData(rectangle);
            t.setFont(ConfigCenter.SELECT_FONT);
            t.setTranslateY(ty);
            t.setTranslateX(ConfigCenter.SELECT_X);
            t.setFill(Color.RED);
            t.setOnMouseEntered(event -> {
                rectangle.setFill(selectC);
                rectangle.setEffect(new Bloom(0.1));
            });
            t.setOnMouseExited(event -> {
                rectangle.setFill(noneC);
                rectangle.setEffect(null);
            });
            t.setOnMousePressed(event -> {
                rectangle.setEffect(new DropShadow(5.0, Color.BLACK));
            });
            t.setOnMouseReleased(event -> {
                System.out.println("You Select " + rectangle.getUserData());
                for (int j = 0; j != selectText.length; ++j) {
                    Text desist = selectText[j];
                    desist.visibleProperty().set(false);
                }
                data.getData().put(data.getNowSelectId(), (String) rectangle.getUserData());
                data.setGameState(GameData.GAME_STATE_WAIT_NEXT);
            });

            selectText[i] = t;
            selectMasks[i] = rectangle;
            t.visibleProperty().bindBidirectional(rectangle.visibleProperty());
            t.visibleProperty().set(false);
            super.getChildren().addAll(rectangle, t);
        }
    }

    private void initBackground() {
        background = new ImageView(ConfigCenter.loadScene("back_demo1"));
        background.setFitWidth(ConfigCenter.WINDOW_WIDTH);
        background.setFitHeight(ConfigCenter.WINDOW_HEIGHT);
        background.imageProperty().set(null);
        super.getChildren().add(background);
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

    private void initPersons() {
        Image ona = ConfigCenter.loadPersonState("onna", "1");
        Image otoko = ConfigCenter.loadPersonState("otoko", "1");
        Image bishojo = ConfigCenter.loadPersonState("bishojo", "1");

        leftPerson = new ImageView(ona);
        leftPerson.setFitWidth(ConfigCenter.PERSON_WIDTH);
        leftPerson.setFitHeight(ConfigCenter.PERSON_HEIGHT);
        leftPerson.imageProperty().set(null);

        centerPerson = new ImageView(bishojo);
        centerPerson.setFitWidth(ConfigCenter.PERSON_WIDTH);
        centerPerson.setFitHeight(ConfigCenter.PERSON_HEIGHT);
        centerPerson.setTranslateX(ConfigCenter.PERSON_WIDTH);
        centerPerson.imageProperty().set(null);

        rightPerson = new ImageView(otoko);
        rightPerson.setFitWidth(ConfigCenter.PERSON_WIDTH);
        rightPerson.setFitHeight(ConfigCenter.PERSON_HEIGHT);
        rightPerson.setTranslateX(ConfigCenter.PERSON_WIDTH * 2);
        rightPerson.imageProperty().set(null);

        data.backgroundImageProperty().bindBidirectional(background.imageProperty());
        data.leftPersonImageProperty().bindBidirectional(leftPerson.imageProperty());
        data.rightPersonImageProperty().bindBidirectional(rightPerson.imageProperty());

        super.getChildren().addAll(leftPerson, centerPerson, rightPerson);
    }

    private void initNameAndWordPanel() {
        name = new Text("");
        name.setFont(ConfigCenter.NAME_FONT);
        name.setFill(Color.WHEAT);
        name.setTranslateX(ConfigCenter.NAME_DISPLAY_X);
        name.setTranslateY(ConfigCenter.NAME_DISPLAY_Y);
        name.setOnMouseEntered(event -> name.setEffect(new Bloom(0.1)));
        name.setOnMouseExited(event -> name.setEffect(null));

        wordPaneFrameV2_b = new Rectangle(ConfigCenter.WORD_PANEL_WIDTH,
                ConfigCenter.WORD_PANEL_HEIGHT, Color.color(0.2, 0.2, 0.2, 0.5));
        wordPaneFrameV2_b.setTranslateY(ConfigCenter.WORD_PANEL_DISPLAY_Y);
        wordPaneFrameV2_b.setTranslateX(ConfigCenter.WORD_PANEL_DISPLAY_X);
        super.getChildren().addAll(wordPaneFrameV2_b);
        super.getChildren().addAll(name);
        data.nameDisplayProperty().bindBidirectional(name.textProperty());
        data.wordLineShowProperty().bindBidirectional(wordPaneFrameV2_b.visibleProperty());
        name.visibleProperty().bindBidirectional(data.wordLineShowProperty());
        data.wordLineShowProperty().set(false);
    }

    private void initPanelWord() {
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
        data.nameDisplayProperty().bindBidirectional(name.textProperty());
        for (int i = 0; i != data.getDisplayWords().length; ++i) {
            StringProperty text = data.getDisplayWords()[i];
            text.bindBidirectional(words[i].textProperty());
            words[i].visibleProperty().bindBidirectional(data.wordLineShowProperty());
        }
    }

    private void initTool() {
        List<EventHandler<MouseEvent>> eventHandlers = new ArrayList<>(Arrays.asList(
                new SkipEventHandler(),
                new AutoEventHandler(),
                new SaveEventHandler(),
                new LoadEventHandler(),
                new BackEventHandler()
        ));

        double prefX = ConfigCenter.TOOL_DISPLAY_X_R - 55;
        for (int i = buttonList.length - 1; i != -1; --i) {
            Text button = new Text(buttonNames[i]);
            button.setOnMouseClicked(eventHandlers.get(i));
            button.setOnMouseEntered(event -> button.setEffect(new Bloom(0.1)));
            button.setOnMouseExited(event -> button.setEffect(null));
            button.setLayoutX(prefX);
            button.setLayoutY(ConfigCenter.TOOL_DISPLAY_Y);
            button.setFont(ConfigCenter.WORD_FONT);
            button.setStroke(Color.WHITE);
            button.visibleProperty().bindBidirectional(data.wordLineShowProperty());
            super.getChildren().add(button);
            prefX -= 55;
        }
    }


    public MediaView getMediaView() {
        return mediaView;
    }

    public void addGlobalMask() {
        globalMask.setVisible(true);
    }

    public void removeGlobalMask() {
        globalMask.setVisible(false);
    }

    public Rectangle getGlobalMask() {
        return globalMask;
    }

    public Text[] getSelectText() {
        return selectText;
    }

    static class SkipEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            System.out.println("Skip");
        }
    }

    static class AutoEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            System.out.println("Auto");
            GameData d = GameCenter.getCenter().getGameData();
            boolean aut = d.isAuto();
            d.setAuto(!aut);
        }
    }

    static class SaveEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            //TODO: 调出存档界面
        }
    }

    static class LoadEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            //TODO: 调出存档界面
        }
    }

    static class BackEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            //TODO: 返回主界面
        }
    }
}
