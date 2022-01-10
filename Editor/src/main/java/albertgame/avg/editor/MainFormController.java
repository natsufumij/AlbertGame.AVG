package albertgame.avg.editor;

import albertgame.avg.editor.FormController.MediaC;
import albertgame.avg.editor.FormController.PersonC;
import albertgame.avg.editor.FormController.StoryBody;
import albertgame.avg.editor.FormController.StoryView;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;

import java.util.*;

public class MainFormController {

    Map<String, StoryViewEdit> editMap;

    @FXML
    private MediaView mediaView;

    @FXML
    private Button graphButton;

    @FXML
    private ListView<StoryView> playsView;

    private Group shapeGroup;

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

    int selectPlayViewIndex=-1;

    @FXML
    void initialize() {
        editMap = new HashMap<>();
        for (int i = 0; i != StoryViewEdit.names.length; ++i) {
            String name = StoryViewEdit.names[i];
            StoryViewEdit edit = StoryViewEdit.views[i];
            editMap.put(name, edit);
        }

        storyAudioListV.setCellFactory(l -> new CellHelp.AudioListCell());
        storyBgmListV.setCellFactory(l -> new CellHelp.BgmListCell(mediaView));
        storyPersonListV.setCellFactory(l -> new CellHelp.PersonListCell());
        storySceneListV.setCellFactory(l -> new CellHelp.SceneListCell());
        storyTreeV.setCellFactory(l -> new CellHelp.ChapterPlayTreeCell());
        playsView.setCellFactory(l -> new CellHelp.StoryViewListCell(mediaView));
        storyTreeV.setEditable(true);
        initStoryTree();

        initCommandTypes();
        addListener();
        testListView();
    }

    void initCommandTypes() {
        String[] types = new String[]{"Audio", "Dialog", "Storage", "Select",
                "View"};
        List<ObservableList<String>> olist = new ArrayList<>();
        olist.add(ConfigCenter.AUDIO_OB_LIST);
        olist.add(ConfigCenter.DIALOG_OB_LIST);
        olist.add(ConfigCenter.STORAGE_OB_LIST);
        olist.add(ConfigCenter.SELECT_OB_LIST);
        olist.add(ConfigCenter.VIEW_OB_LIST);
        typeChoice.getItems().addAll(types);
        typeChoice.getSelectionModel().selectedIndexProperty().addListener((v, o, n) -> {
            if (n.intValue() >= 0 && n.intValue() < olist.size()) {
                System.out.println("n=" + n);
                ObservableList<String> de = olist.get(n.intValue());
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
        playsView.getSelectionModel().selectedIndexProperty().addListener((v, o, n) -> {
            if (n != null) {
                System.out.println("Select " + n.intValue());
                playsView.setUserData(playsView.getItems().get(n.intValue()));
                selectPlayViewIndex=n.intValue();
            }
        });
        typeChoice.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
            if (n != null) {
                FormController formController = FormController.get();
                if (formController.getNewExpression() != null && !formController.isEditMode()) {
                    formController.getNewExpression().type = n;
                    gotoStoryView(formController.getNewExpression());
                }
            }
        });
    }

    void testListView() {

        MediaC c = new MediaC("audio1", "Name1");
        storyAudioListV.getItems().addAll(c);
        FormController.get().getAudioMap().put(c.name, c);

        MediaC c4 = new MediaC("bgm1", "第一个场景");
        MediaC c5 = new MediaC("battle", "战斗场景");
        storyBgmListV.getItems().addAll(c4, c5);
        FormController.get().getBgmMap().put(c4.name, c4);
        FormController.get().getBgmMap().put(c5.name, c5);

        PersonC personC = new PersonC("bishojo", "美少女", "1", "默认状态");
        PersonC personC2 = new PersonC("otoko", "男人", "1", "默认状态");
        storyPersonListV.getItems().addAll(personC, personC2);
        FormController.get().getPersonMap().put(personC.name, personC);
        FormController.get().getPersonMap().put(personC2.name, personC2);


        MediaC mediaC = new MediaC("back_demo1", "背景1");
        MediaC mediaC2 = new MediaC("demo2", "背景2");
        storySceneListV.getItems().addAll(mediaC, mediaC2);
        FormController.get().getSceneMap().put(mediaC.name, mediaC);
        FormController.get().getSceneMap().put(mediaC2.name, mediaC2);

        TreeItem<StoryBody> globalItem = FormController.get().getGlobal();
        StoryBody global = globalItem.getValue();
        StoryBody b1 = new StoryBody(StoryBody.Type.CHAPTER, "第一章",
                FormController.getUniqueId(), global.id);
        StoryBody b2 = new StoryBody(StoryBody.Type.CHAPTER, "第二章",
                FormController.getUniqueId(), global.id);
        StoryBody b3 = new StoryBody(StoryBody.Type.PLAY, "第一节",
                FormController.getUniqueId(), b1.id);
        StoryBody b4 = new StoryBody(StoryBody.Type.PLAY, "第二节",
                FormController.getUniqueId(), b1.id);
        StoryBody b5 = new StoryBody(StoryBody.Type.PLAY, "第一节",
                FormController.getUniqueId(), b2.id);
        StoryBody b6 = new StoryBody(StoryBody.Type.PLAY, "第二节",
                FormController.getUniqueId(), b2.id);

        TreeItem<StoryBody> chapter1 = new TreeItem<>(b1);
        TreeItem<StoryBody> chapter2 = new TreeItem<>(b2);
        TreeItem<StoryBody> play1 = new TreeItem<>(b3);
        TreeItem<StoryBody> play2 = new TreeItem<>(b4);
        TreeItem<StoryBody> play3 = new TreeItem<>(b5);
        TreeItem<StoryBody> play4 = new TreeItem<>(b6);

        globalItem.getChildren().add(chapter1);
        globalItem.getChildren().add(chapter2);
        chapter1.getChildren().add(play1);
        chapter1.getChildren().add(play2);
        chapter2.getChildren().add(play3);
        chapter2.getChildren().add(play4);


        StoryView v1 = new StoryView(0, "Dialog", "Open", NONE_DATA);
        StoryView v2 = new StoryView(1, "Dialog", "Pound", new String[]{"这是一个很好的天气\\啊啊啊\\啊啊啊\\啊啊"});
        StoryView v3 = new StoryView(3, "Dialog", "Word", new String[]{
                "M", "这是我的话语，你不要说给别人听"
        });
        StoryView v4 = new StoryView(4, "Dialog", "Word", new String[]{
                "S", "这是我的话语，你不要说给别人听"
        });
        StoryView v5 = new StoryView(5, "Audio", "Bgm.Play", new String[]{"第一个场景"});
        StoryView v6 = new StoryView(6, "Select", "Go", new String[]{"Option1", "我选择", "我不选择", "让他选择"});
        StoryView v7 = new StoryView(7, "Storage", "Save", new String[]{"A", "10"});
        StoryView v8 = new StoryView(8, "View", "Scene", new String[]{"第一个场景"});
        playsView.getItems().addAll(v1, v2, v3, v4, v5, v6, v7, v8);
    }

    void initStoryTree() {
        StoryBody global = new StoryBody(StoryBody.Type.GLOBAL, "Global",
                FormController.getUniqueId(), null);
        TreeItem<StoryBody> rootNode = new TreeItem<>(global);
        storyTreeV.setRoot(rootNode);
        storyTreeV.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
            storyTreeV.setUserData(n);
        });
        FormController.get().setGlobal(rootNode);
    }

    public static final String[] NONE_DATA = new String[0];

    @FXML
    void aboutEditor(ActionEvent event) {

    }


    @FXML
    void onRemoveCommand(ActionEvent event) {
        StoryView storyView = (StoryView) playsView.getUserData();
        if (storyView != null) {

            //删除BodyStruck里的指定一行
            playsView.getItems().remove(storyView);
        } else {
            if (playsView.getItems().size() == 1) {

                //清空BodyStruck
                playsView.getItems().clear();
            }
        }
    }

    @FXML
    void onAddCommand(ActionEvent event) {
        StoryView view = new StoryView(10, "Audio", "", new String[]{"","","","","","",""});
        FormController.get().setEditMode(false);
        gotoStoryView(view);
    }

    @FXML
    void onAudioAdd(ActionEvent event) {
        Dialog<MediaC> mediaCDialog = ConfigCenter.createMediaDialog(
                "Audio Select", "Select Audio", "wav", null, null, null);
        Optional<MediaC> mediaC = mediaCDialog.showAndWait();
        mediaC.ifPresent(l -> {
            mediaCAdd(l, "audio", FormController.get().getAudioMap(), storyAudioListV);
        });
    }

    @FXML
    void onAudioEdit(ActionEvent event) {
        MediaC m = (MediaC) storyAudioListV.getUserData();
        if (m != null) {
            Dialog<MediaC> mediaCDialog = ConfigCenter.createMediaDialog(
                    "Audio Edit", "Select Audio", "wav", m.name, "audio", m.id);
            Optional<MediaC> mediaC = mediaCDialog.showAndWait();
            mediaC.ifPresent(l -> {
                storyAudioListV.setUserData(null);
                onAudioRemove(null);
                mediaCAdd(l, "audio", FormController.get().getAudioMap(), storyAudioListV);
            });
        }
    }

    @FXML
    void onAudioRemove(ActionEvent event) {
        MediaC m = (MediaC) storyAudioListV.getUserData();
        if (m != null) {
            storyAudioListV.setUserData(null);
            FormController.get().getAudioMap().remove(m.name);
            storyAudioListV.getItems().remove(m);
            ConfigCenter.removeFile("audio", m.id, "wav");
        }
    }

    @FXML
    void onBgmAdd(ActionEvent event) {
        Dialog<MediaC> mediaCDialog = ConfigCenter.createMediaDialog(
                "Bgm Select", "Bgm", "mp3", null, null, null);
        Optional<MediaC> mediaC = mediaCDialog.showAndWait();
        mediaC.ifPresent(l -> {
            mediaCAdd(l, "bgm", FormController.get().getBgmMap(), storyBgmListV);
        });
    }

    @FXML
    void onBgmEdit(ActionEvent event) {
        MediaC mediaC = (MediaC) storyBgmListV.getUserData();
        if (mediaC != null) {
            Dialog<MediaC> mediaCDialog = ConfigCenter.createMediaDialog(
                    "Bgm Select", "Bgm File", "mp3",
                    "bgm", mediaC.name, mediaC.id);
            Optional<MediaC> med = mediaCDialog.showAndWait();
            med.ifPresent(l -> {
                storyBgmListV.setUserData(null);
                onBgmRemove(null);
                mediaCAdd(l, "bgm", FormController.get().getBgmMap(), storyBgmListV);
            });
        }
    }

    private void mediaCAdd(MediaC l, String bgm, Map<String, MediaC> BgmMap, ListView<MediaC> storyBgmListV) {
        String id = l.id;
        ConfigCenter.moveFileTo(FormController.getSelectFile(), bgm, id);
        BgmMap.put(l.name, l);
        storyBgmListV.getItems().add(l);
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
        Dialog<PersonC> dialog = ConfigCenter.createPersonDialog(null, null, null);
        Optional<PersonC> result = dialog.showAndWait();
        result.ifPresent(l -> {
            personAdd_(l);
        });
    }

    @FXML
    void onPersonEdit(ActionEvent event) {
        PersonC personC = (PersonC) storyPersonListV.getUserData();
        if (personC != null) {
            Dialog<PersonC> dialog = ConfigCenter.createPersonDialog(
                    personC.name, personC.stateName, personC.id);
            Optional<PersonC> result = dialog.showAndWait();
            result.ifPresent(l -> {
                onePersonRemove(null);
                personAdd_(l);
            });
        }
    }

    private void personAdd_(PersonC l) {
        String id = l.id;
        String stateId = l.stateId;
        String person_id = id + "_" + stateId;
        FormController.get().getPersonMap().put(person_id, l);
        storyPersonListV.getItems().add(l);
        ConfigCenter.moveFileTo(FormController.getSelectFile(), "person", person_id);
    }

    @FXML
    void onSceneAdd(ActionEvent event) {
        Dialog<MediaC> mediaCDialog = ConfigCenter.createMediaDialog(
                "Scene Select", "Scene Image", "jpg", null, null, null);
        Optional<MediaC> mediaC = mediaCDialog.showAndWait();
        mediaC.ifPresent(l -> {
            mediaCAdd(l, "scene", FormController.get().getSceneMap(), storySceneListV);
        });
    }

    @FXML
    void onSceneEdit(ActionEvent event) {
        MediaC mediaC = (MediaC) storySceneListV.getUserData();
        if (mediaC != null) {
            Dialog<MediaC> mediaCDialog = ConfigCenter.createMediaDialog(
                    "Scene Select", "Scene Image", "jpg",
                    mediaC.name, "scene", mediaC.id);
            Optional<MediaC> med = mediaCDialog.showAndWait();
            med.ifPresent(l -> {
                storyAudioListV.setUserData(null);
                onSceneRemove(null);
                mediaCAdd(l, "scene", FormController.get().getSceneMap(), storySceneListV);
            });
        }
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
        Dialog<StoryBody> dialog = ConfigCenter.createStoryDialog();
        Optional<StoryBody> result = dialog.showAndWait();
        result.ifPresent(l -> {
            if (l.type == StoryBody.Type.CHAPTER) {
                TreeItem<StoryBody> chapter = new TreeItem<>(l);
                FormController.get().getGlobal().getChildren().add(chapter);
                FormController.get().getChapterTree().put(l.name, chapter);
                FormController.get().getPlayInChapterMap().put(l.name, FXCollections.observableList(new ArrayList<>()));
                ConfigCenter.createFile("story", l.id, "avg");
                System.out.println("Add A Chapter" + l.name);
            } else if (l.type == StoryBody.Type.PLAY) {
                TreeItem<StoryBody> play = new TreeItem<>(l);
                TreeItem<StoryBody> p = FormController.get().getChapterTree().get(l.parent_name);
                p.getChildren().add(play);
                FormController.get().getPlayInChapterMap().get(l.parent_name).add(l);
                ConfigCenter.createFile("story", l.parent_name + "/" + l.id, "avg");
            }
        });
    }

    @FXML
    void onStoryRemove(ActionEvent event) {
        TreeItem<StoryBody> item = (TreeItem<StoryBody>) storyTreeV.getUserData();
        if (item != null) {
            if (item.getParent() != null) {
                item.getParent().getChildren().remove(item);
            }
            StoryBody body = item.getValue();
            if (body.type == StoryBody.Type.CHAPTER) {
                FormController.get().getChapterTree().remove(body.name);
                FormController.get().getPlayInChapterMap().remove(body.name);
            } else if (body.type == StoryBody.Type.PLAY) {
                FormController.get().getPlayInChapterMap().
                        get(body.parent_name).remove(body);
            }
        }
    }

    @FXML
    void onWordEnter(ActionEvent event) {
        System.out.println("On Enter");
        FormController formController = FormController.get();
        StoryView editView = formController.getNewExpression();
        if(editView!=null){
            //将type、name重新写入StoryView
            if(FormController.get().isEditMode()){
                StoryView source = formController.getNowEditExpression();
                String[] newD=new String[editView.data.length];
                System.arraycopy(editView.data,0,newD,0,newD.length);
                source.data=newD;
            }else {
                playsView.getItems().add(selectPlayViewIndex+1,editView);
            }
        }
        FormController.get().setNowEditExpression(null);
        typeChoice.setDisable(true);
        nameChoice.setDisable(true);
        FormController.get().eraseNewExpression();
        FormController.get().setEditEnd(true);
        commands.getChildren().clear();
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


    @FXML
    void onCommandEdit(ActionEvent event) {
        StoryView storyView = playsView.getItems().get(selectPlayViewIndex);
        FormController.get().setEditMode(true);
        gotoStoryView(storyView);
    }

    void gotoStoryView(StoryView view) {
        FormController.get().createNewExpression(view);
        FormController.get().setNowEditExpression(view);
        StoryView newV = FormController.get().getNewExpression();

        StoryViewEdit edit = editMap.get(newV.type);
        commands.getChildren().clear();
        commands.setAlignment(Pos.CENTER);

        if (edit != null) {
            FormController.get().setEditEnd(false);

            typeChoice.setValue(newV.type);
            ReadOnlyObjectProperty<String> onlyObjectProperty=nameChoice.getSelectionModel().selectedItemProperty();
            onlyObjectProperty.addListener((v,o,n)->{
                if(n!=null){
                    StoryView e=FormController.get().getNewExpression();
                    if(e!=null&&!FormController.get().isEditMode()){
                        e.name=n;
                    }
                }
            });

            GridPane pane = edit.create(newV, nameChoice.getSelectionModel().selectedItemProperty());
            if (pane != null) {
                pane.setAlignment(Pos.CENTER);
                pane.setMinWidth(Region.USE_COMPUTED_SIZE);
                commands.getChildren().clear();
                commands.getChildren().add(pane);
                if (FormController.get().isEditMode()) {
                    typeChoice.setDisable(true);
                    nameChoice.setValue(newV.name);
                    nameChoice.setDisable(true);
                }else {
                    typeChoice.setDisable(false);
                    nameChoice.setDisable(false);
                }
            }
        }
    }
}
