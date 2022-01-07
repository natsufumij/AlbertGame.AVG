package albertgame.avg.editor;

import albertgame.avg.editor.FormController.StoryView;
import albertgame.avg.editor.FormController.MediaC;
import albertgame.avg.editor.FormController.StoryBody;
import albertgame.avg.editor.FormController.PersonC;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.media.MediaView;

public class MainFormController {

    @FXML
    private MediaView mediaView;

    @FXML
    private Button graphButton;

    @FXML
    private ChoiceBox<String> personChoiceBox;

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
    private TextArea wordArea;


    @FXML
    void initialize(){
        storyAudioListV.setCellFactory(l->new FormController.AudioListCell());
        storyBgmListV.setCellFactory(l->new FormController.BgmListCell(mediaView));
        storyPersonListV.setCellFactory(l->new FormController.PersonListCell());
        storySceneListV.setCellFactory(l->new FormController.SceneListCell());

        testListView();
    }

    void testListView(){

        MediaC c=new MediaC("audio1","Name1");
        storyAudioListV.getItems().addAll(c);

        MediaC c4=new MediaC("bgm1","第一个场景");
        MediaC c5=new MediaC("battle","战斗场景");
        storyBgmListV.getItems().addAll(c4,c5);

        PersonC personC=new PersonC("bishojo","美少女","1","默认状态");
        PersonC personC2=new PersonC("otoko","男人","1","默认状态");
        storyPersonListV.getItems().addAll(personC,personC2);

        MediaC mediaC=new MediaC("back_demo1","背景1");
        MediaC mediaC2=new MediaC("demo2","背景2");
        storySceneListV.getItems().addAll(mediaC,mediaC2);
    }

    @FXML
    void aboutEditor(ActionEvent event) {

    }

    @FXML
    void onAudioAct(ActionEvent event) {

    }

    @FXML
    void onAudioAdd(ActionEvent event) {

    }

    @FXML
    void onAudioEdit(ActionEvent event) {

    }

    @FXML
    void onAudioRemove(ActionEvent event) {

    }

    @FXML
    void onBgmAdd(ActionEvent event) {

    }

    @FXML
    void onBgmEdit(ActionEvent event) {

    }

    @FXML
    void onBgmRemove(ActionEvent event) {

    }

    @FXML
    void onPersonAct(ActionEvent event) {

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

    }

    @FXML
    void onSelectAct(ActionEvent event) {

    }

    @FXML
    void onStoreAct(ActionEvent event) {

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
    void onViewAct(ActionEvent event) {

    }

    @FXML
    void onWordClear(ActionEvent event) {

    }

    @FXML
    void onWordEnter(ActionEvent event) {

    }

    @FXML
    void onePersonRemove(ActionEvent event) {

    }

    @FXML
    void openProject(ActionEvent event) {

    }

    @FXML
    void saveProject(ActionEvent event) {

    }

}
