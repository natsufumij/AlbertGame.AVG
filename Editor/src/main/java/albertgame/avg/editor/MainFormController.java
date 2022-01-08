package albertgame.avg.editor;

import albertgame.avg.editor.FormController.MediaC;
import albertgame.avg.editor.FormController.PersonC;
import albertgame.avg.editor.FormController.StoryBody;
import albertgame.avg.editor.FormController.StoryView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;

import java.io.File;

public class MainFormController {

    @FXML
    private MediaView mediaView;

    @FXML
    private Button graphButton;

    @FXML
    private ListView<StoryView> playsView;

    @FXML
    private Canvas shapeCanvas;

    @FXML
    private ListView<MediaC> storyAudioListV;

    @FXML
    private ListView<MediaC> storyBgmListV;

    @FXML
    private ListView<PersonC> storyPersonListV;

    @FXML
    private ListView<MediaC> storySceneListV;

    @FXML
    private TreeView<StoryBody> storyTreeV;

    @FXML
    private VBox commands;

    @FXML
    private ChoiceBox<String> nameChoice;

    @FXML
    private ChoiceBox<String> typeChoice;

    @FXML
    void initialize() {
        storyAudioListV.setCellFactory(l -> new FormController.AudioListCell());
        storyBgmListV.setCellFactory(l -> new FormController.BgmListCell(mediaView));
        storyPersonListV.setCellFactory(l -> new FormController.PersonListCell());
        storySceneListV.setCellFactory(l -> new FormController.SceneListCell());
        storyTreeV.setCellFactory(l -> new FormController.ChapterPlayTreeCell());
        playsView.setCellFactory(l->new FormController.StoryViewListCell(mediaView));

        storyTreeV.setEditable(true);

        addListener();
        testListView();
    }

    void addListener() {
        storyAudioListV.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
            if (n != null) {
                storyAudioListV.setUserData(n);
            }
        });
        storyBgmListV.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
            if (n != null) {
                System.out.println("Select Bgm :"+n.name);
                storyBgmListV.setUserData(n);
            }
        });
        storyPersonListV.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
            if (n != null) {
                storyPersonListV.setUserData(n);
            }
        });
        storySceneListV.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
            if (n != null) {
                storySceneListV.setUserData(n);
            }
        });
        storyTreeV.getSelectionModel().selectedItemProperty().addListener((v,o,n)->{
            if(n!=null){
                storyTreeV.setUserData(n);
            }
        });
    }

    void testListView() {

        MediaC c = new MediaC("audio1", "Name1");
        storyAudioListV.getItems().addAll(c);

        MediaC c4 = new MediaC("bgm1", "第一个场景");
        MediaC c5 = new MediaC("battle", "战斗场景");
        storyBgmListV.getItems().addAll(c4, c5);

        PersonC personC = new PersonC("bishojo", "美少女", "1", "默认状态");
        PersonC personC2 = new PersonC("otoko", "男人", "1", "默认状态");
        storyPersonListV.getItems().addAll(personC, personC2);

        MediaC mediaC = new MediaC("back_demo1", "背景1");
        MediaC mediaC2 = new MediaC("demo2", "背景2");
        storySceneListV.getItems().addAll(mediaC, mediaC2);

        TreeItem<StoryBody> rootNode = new TreeItem<>(new StoryBody(StoryBody.Type.GLOBAL, "Global", "000"));
        TreeItem<StoryBody> chapter1 = new TreeItem<>(new StoryBody(StoryBody.Type.CHAPTER, "Chapter1", "AAA"));
        TreeItem<StoryBody> chapter2 = new TreeItem<>(new StoryBody(StoryBody.Type.CHAPTER, "Chapter2", "AAB"));
        TreeItem<StoryBody> play1 = new TreeItem<>(new StoryBody(StoryBody.Type.CHAPTER, "Play1", "AAC"));
        TreeItem<StoryBody> play2 = new TreeItem<>(new StoryBody(StoryBody.Type.CHAPTER, "Play2", "AAD"));
        TreeItem<StoryBody> play3 = new TreeItem<>(new StoryBody(StoryBody.Type.CHAPTER, "Play3", "AAE"));
        TreeItem<StoryBody> play4 = new TreeItem<>(new StoryBody(StoryBody.Type.CHAPTER, "Play4", "AAF"));

        rootNode.getChildren().add(chapter1);
        rootNode.getChildren().add(chapter2);
        chapter1.getChildren().add(play1);
        chapter1.getChildren().add(play2);
        chapter2.getChildren().add(play3);
        chapter2.getChildren().add(play4);
        storyTreeV.setRoot(rootNode);

        StoryView v1=new StoryView("Dialog","Open",NONE_DATA);
        StoryView v2=new StoryView("Dialog","Word",new String[]{"这是一个很好的天气\\啊啊啊\\啊啊啊\\啊啊"});
        StoryView v3=new StoryView("Audio","Bgm.Play",new String[]{"第一个场景"});
        playsView.getItems().addAll(v1,v2,v3);
    }

    public static final String[] NONE_DATA=new String[0];

    @FXML
    void aboutEditor(ActionEvent event) {

    }


    @FXML
    void onRemoveCommand(ActionEvent event) {

    }

    @FXML
    void onAddCommand(ActionEvent event){

    }

    @FXML
    void onEditCommand(ActionEvent event){

    }

    @FXML
    void onAudioAdd(ActionEvent event) {
        Dialog<MediaC> mediaCDialog=new Dialog<>();
        Label nameL=new Label("Name");
        TextField field=new TextField();
        Label pathl=new Label("Path");
        Button button=new Button("Select");
        button.setOnAction(e->{
            FileChooser fileChooser=new FileChooser();
            fileChooser.setSelectedExtensionFilter(
                    new FileChooser.ExtensionFilter(
                            "Audio Asset","*.wav"));
            fileChooser.setTitle("Select Audio Asset");
            FormController.setSelectFile(
                    fileChooser.showOpenDialog(MainEntry.stage()));
        });
        GridPane pane=ConfigCenter.createDialogGrid(
                new Node[]{nameL,field,pathl,button});
        ButtonType buttonType=new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel=new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        mediaCDialog.getDialogPane().getButtonTypes().addAll(buttonType,cancel);
        Button buttonOk=(Button)mediaCDialog.getDialogPane().lookupButton(buttonType);
        buttonOk.setDisable(true);
        field.textProperty().addListener((v,o,n)->{
            if(n!=null&&FormController.getSelectFile()!=null){
                buttonOk.setDisable(false);
            }
        });
        mediaCDialog.getDialogPane().setGraphic(pane);
        mediaCDialog.showAndWait();
    }

    @FXML
    void onAudioEdit(ActionEvent event) {

    }

    @FXML
    void onAudioRemove(ActionEvent event) {
        MediaC m = (MediaC) storyAudioListV.getUserData();
        if (m != null) {
            FormController.get().getAudioMap().remove(m.name);
            storyAudioListV.getItems().remove(m);
        }
    }

    @FXML
    void onBgmAdd(ActionEvent event) {

    }

    @FXML
    void onBgmEdit(ActionEvent event) {

    }

    @FXML
    void onBgmRemove(ActionEvent event) {
        MediaC m = (MediaC) storyBgmListV.getUserData();
        if (m != null) {
            FormController.get().getBgmMap().remove(m.name);
            System.out.println("On Bgm Remove:" + m.name);
            storyBgmListV.getItems().remove(m);
        }
    }

    @FXML
    void onPersonAdd(ActionEvent event) {

    }

    @FXML
    void onPersonEdit(ActionEvent event) {

    }

    @FXML
    void onSceneAdd(ActionEvent event) {

    }

    @FXML
    void onSceneEdit(ActionEvent event) {

    }

    @FXML
    void onSceneRemove(ActionEvent event) {
        MediaC mediaC=(MediaC) storySceneListV.getUserData();
        if(mediaC!=null){
            FormController.get().getSceneMap().remove(mediaC.name);
            storySceneListV.getItems().remove(mediaC);
        }
    }

    @FXML
    void onStoryAdd(ActionEvent event) {

    }

    @FXML
    void onStoryEdit(ActionEvent event) {

    }

    @FXML
    void onStoryRemove(ActionEvent event) {

    }

    @FXML
    void onWordClear(ActionEvent event) {

    }

    @FXML
    void onWordEnter(ActionEvent event) {

    }

    @FXML
    void onePersonRemove(ActionEvent event) {
        PersonC m = (PersonC) storyPersonListV.getUserData();
        if (m != null) {
            FormController.get().getPersonMap().remove(m.name);
            storyPersonListV.getItems().remove(m);
        }
    }

    @FXML
    void openProject(ActionEvent event) {

    }

    @FXML
    void saveProject(ActionEvent event) {

    }
}
