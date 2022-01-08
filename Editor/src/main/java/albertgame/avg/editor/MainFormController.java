package albertgame.avg.editor;

import albertgame.avg.editor.FormController.StoryView;
import albertgame.avg.editor.FormController.MediaC;
import albertgame.avg.editor.FormController.StoryBody;
import albertgame.avg.editor.FormController.PersonC;

import javafx.collections.ObservableList;
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
        //TODO: 添加监听器，给Controller添加、修改、或者删除指定Item
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
        MediaC m = (MediaC) storyAudioListV.getUserData();
        if (m != null) {
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
            System.out.println("On Bgm Remove:" + m.name);
            storyBgmListV.getItems().remove(m);
        }
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
        MediaC mediaC=(MediaC) storySceneListV.getUserData();
        if(mediaC!=null){
            storySceneListV.getItems().remove(mediaC);
        }
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
        PersonC m = (PersonC) storyPersonListV.getUserData();
        if (m != null) {
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
