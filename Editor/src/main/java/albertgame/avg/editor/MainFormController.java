package albertgame.avg.editor;

import albertgame.avg.editor.FormController.MediaC;
import albertgame.avg.editor.FormController.PersonC;
import albertgame.avg.editor.FormController.StoryBody;
import albertgame.avg.editor.FormController.StoryView;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        playsView.setCellFactory(l -> new FormController.StoryViewListCell(mediaView));

        storyTreeV.setEditable(true);

        initCommandTypes();
        addListener();
        testListView();
    }

    void initCommandTypes() {
        String[] types = new String[]{"Audio", "Dialog", "Store", "Select",
                "View"};
        List<ObservableList<String>> olist = new ArrayList<>();
        olist.add(ConfigCenter.AUDIO_OB_LIST);
        olist.add(ConfigCenter.DIALOG_OB_LIST);
        olist.add(ConfigCenter.STORAGE_OB_LIST);
        olist.add(ConfigCenter.SELECT_OB_LIST);
        olist.add(ConfigCenter.VIEW_OB_LIST);
        typeChoice.getItems().addAll(types);
        typeChoice.getSelectionModel().selectedIndexProperty().addListener((v,o,n)->{
            if(n.intValue()>=0&&n.intValue()<olist.size()){
                System.out.println("n="+n);
                ObservableList<String> de=olist.get(n.intValue());
                nameChoice.getItems().clear();
                nameChoice.getItems().addAll(de);
            }
        });
    }

    void addListener() {
        storyAudioListV.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
            if (n != null) {
                storyAudioListV.setUserData(n);
            }
        });
        storyBgmListV.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
            if (n != null) {
                System.out.println("Select Bgm :" + n.name);
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
        storyTreeV.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
            if (n != null) {
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

        StoryView v1 = new StoryView("Dialog", "Open", NONE_DATA);
        StoryView v2 = new StoryView("Dialog", "Word", new String[]{"这是一个很好的天气\\啊啊啊\\啊啊啊\\啊啊"});
        StoryView v3 = new StoryView("Audio", "Bgm.Play", new String[]{"第一个场景"});
        playsView.getItems().addAll(v1, v2, v3);
    }

    public static final String[] NONE_DATA = new String[0];

    @FXML
    void aboutEditor(ActionEvent event) {

    }


    @FXML
    void onRemoveCommand(ActionEvent event) {

    }

    @FXML
    void onAddCommand(ActionEvent event) {

    }

    @FXML
    void onEditCommand(ActionEvent event) {

    }

    @FXML
    void onAudioAdd(ActionEvent event) {
        Dialog<MediaC> mediaCDialog = ConfigCenter.createMediaDialog(
                "Audio Select", "Select Audio", "wav");
        Optional<MediaC> mediaC = mediaCDialog.showAndWait();
        mediaC.ifPresent(l -> {
            String id = l.id;
            ConfigCenter.moveFileTo(FormController.getSelectFile(), "audio", id);
            FormController.get().getAudioMap().put(l.name, l);
            storyAudioListV.getItems().add(l);
        });
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
            ConfigCenter.removeFile("audio", m.id, "wav");
        }
    }

    @FXML
    void onBgmAdd(ActionEvent event) {
        Dialog<MediaC> mediaCDialog = ConfigCenter.createMediaDialog(
                "Bgm Select", "Bgm", "mp3");
        Optional<MediaC> mediaC = mediaCDialog.showAndWait();
        mediaC.ifPresent(l -> {
            String id = l.id;
            ConfigCenter.moveFileTo(FormController.getSelectFile(), "bgm", id);
            FormController.get().getBgmMap().put(l.name, l);
            storyBgmListV.getItems().add(l);
        });
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
            ConfigCenter.removeFile("bgm", m.id, "mp3");
        }
    }

    @FXML
    void onPersonAdd(ActionEvent event) {
        Dialog<PersonC> dialog=ConfigCenter.createPersonDialog();
        Optional<PersonC> result=dialog.showAndWait();
        result.ifPresent(l->{
            String id=l.id;
            String stateId=l.stateId;
            String person_id=id+"_"+stateId;
            FormController.get().getPersonMap().put(person_id,l);
            storyPersonListV.getItems().add(l);
            ConfigCenter.moveFileTo(FormController.getSelectFile(),"person",person_id);
        });
    }

    @FXML
    void onPersonEdit(ActionEvent event) {

    }

    @FXML
    void onSceneAdd(ActionEvent event) {
        Dialog<MediaC> mediaCDialog = ConfigCenter.createMediaDialog(
                "Scene Select", "Scene Image", "jpg");
        Optional<MediaC> mediaC = mediaCDialog.showAndWait();
        mediaC.ifPresent(l -> {
            String id = l.id;
            ConfigCenter.moveFileTo(FormController.getSelectFile(), "scene", id);
            FormController.get().getSceneMap().put(l.name, l);
            storySceneListV.getItems().add(l);
        });
    }

    @FXML
    void onSceneEdit(ActionEvent event) {

    }

    @FXML
    void onSceneRemove(ActionEvent event) {
        MediaC mediaC = (MediaC) storySceneListV.getUserData();
        if (mediaC != null) {
            FormController.get().getSceneMap().remove(mediaC.name);
            storySceneListV.getItems().remove(mediaC);
            ConfigCenter.removeFile("scene", mediaC.id, "jpg");
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
            ConfigCenter.removeFile("person", m.id + "_" + m.stateId, "png");
        }
    }

    @FXML
    void openProject(ActionEvent event) {

    }

    @FXML
    void saveProject(ActionEvent event) {

    }
}
