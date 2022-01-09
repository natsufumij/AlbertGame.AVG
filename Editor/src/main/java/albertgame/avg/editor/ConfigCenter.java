package albertgame.avg.editor;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Set;

public class ConfigCenter {
    public static final int WORD_MAX_COLUMN = 30;
    public static final int WORD_MAX_ROW = 3;
    public static final int WORD_MAX_SIZE = WORD_MAX_COLUMN * WORD_MAX_ROW;

    public static final String[] AUDIO_COMMANDS = new String[]{
            "Bgm.Play", "Bgm.Pause", "Bgm.Resume",
            "Bgm.Stop", "Sound.Play"
    };
    public static final int BGM_PLAY = 0;
    public static final int BGM_PAUSE = 1;
    public static final int BGM_RESUME = 2;
    public static final int BGM_STOP = 3;
    public static final int SOUND_PLAY = 4;
    public static final ObservableList<String> AUDIO_OB_LIST = FXCollections.observableList(
            List.of(AUDIO_COMMANDS));

    public static final String[] STORAGE_COMMANDS = new String[]{
            "Save", "Plus", "Minus"
    };
    public static final int SAVE = 0;
    public static final int PLUS = 1;
    public static final int MINUS = 2;
    public static final ObservableList<String> STORAGE_OB_LIST = FXCollections.observableList(
            List.of(STORAGE_COMMANDS));

    public static final String[] VIEW_COMMANDS = new String[]{
            "Scene", "Shake", "Darking", "Lighting"
    };
    public static final int SCENE = 0;
    public static final int SHAKE = 1;
    public static final int DARKING = 2;
    public static final int LIGHTING = 3;
    public static final ObservableList<String> VIEW_OB_LIST = FXCollections.observableList(
            List.of(VIEW_COMMANDS)
    );

    public static final String[] PERSON_COMMANDS = new String[]{
            "In", "Out", "Show", "No.Show", "Hide", "Change.State"
    };
    public static final int IN = 0;
    public static final int OUT = 1;
    public static final int SHOW = 2;
    public static final int NO_SHOW = 3;
    public static final int HIDE = 4;
    public static final int CHANGE_STATE = 5;
    public static final ObservableList<String> PERSON_OB_LIST = FXCollections.observableList(
            List.of(PERSON_COMMANDS)
    );

    public static final String[] DIALOG_COMMANDS = new String[]{
            "Open", "Clear", "Close"
    };
    public static final int OPEN = 0;
    public static final int CLEAR = 1;
    public static final int CLOSE = 2;
    public static final ObservableList<String> DIALOG_OB_LIST = FXCollections.observableList(
            List.of(DIALOG_COMMANDS)
    );
    public static final String[] SELECT_COMMANDS = new String[]{"Go"};
    public static final ObservableList<String> SELECT_OB_LIST = FXCollections.observableList(
            List.of(SELECT_COMMANDS)
    );

    public static BorderPane createBorderPane(Node[] lefts, Node right) {
        BorderPane pane = new BorderPane();

        if (right != null) {
            BorderPane.setAlignment(right, Pos.CENTER_RIGHT);
            pane.setRight(right);
        }
        if (lefts.length > 1) {
            HBox box = new HBox();
            box.setAlignment(Pos.CENTER_LEFT);
            BorderPane.setAlignment(box, Pos.CENTER_LEFT);
            for (Node n : lefts) {
                box.getChildren().add(n);
            }
            pane.setLeft(box);
        } else {
            BorderPane.setAlignment(lefts[0], Pos.CENTER_LEFT);
            pane.setLeft(lefts[0]);
        }

        return pane;
    }

    public static Dialog<FormController.MediaC> createDialog(String headerText,
                                                             Callback<ButtonType, FormController.MediaC> value,
                                                             Node[] list, ButtonType[] buttonTypes) {
        Dialog<FormController.MediaC> mediaCDialog = new Dialog<>();
        for (ButtonType type : buttonTypes) {
            mediaCDialog.getDialogPane().getButtonTypes().add(type);
        }
        mediaCDialog.setResultConverter(value);
        GridPane pane = createDialogGrid(list);
        mediaCDialog.setHeaderText(headerText);
        mediaCDialog.getDialogPane().setContent(pane);
        return mediaCDialog;
    }

    public static Dialog<FormController.MediaC> createMediaDialog(String headerText, String description, String format,
                                                                  String name, String lib, String id) {
        Label nameL = new Label("Name");
        TextField field = new TextField();
        if (name != null) {
            field.setText(name);
        }
        Button button = new Button("Select");
        final Label pathl = new Label("Id");
        if (id != null) {
            pathl.setText(id);
            File destF = new File(getRealPath(lib, id, format));
            if (destF != null && destF.exists()) {
                FormController.setSelectFile(destF);
            }
        }
        button.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(description, "*." + format));
            fileChooser.setTitle(headerText);
            File file = fileChooser.showOpenDialog(MainEntry.stage());
            if (file != null) {
                String id_ = FormController.getUniqueId();
                pathl.setText(id_);
                FormController.setSelectFile(file);
            }
        });
        ButtonType buttonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Callback<ButtonType, FormController.MediaC> callback = new Callback<ButtonType, FormController.MediaC>() {
            @Override
            public FormController.MediaC call(ButtonType param) {
                if (param == buttonType) {
                    String id = pathl.getText();
                    String name = field.getText();
                    return new FormController.MediaC(id, name);
                }
                return null;
            }
        };

        Dialog<FormController.MediaC> mediaCDialog = ConfigCenter.createDialog(headerText, callback,
                new Node[]{nameL, field, button, pathl}, new ButtonType[]{buttonType, cancel});

        Button buttonOk = (Button) mediaCDialog.getDialogPane().lookupButton(buttonType);
        buttonOk.setDisable(true);
        field.textProperty().addListener((v, o, n) -> {
            if (n != null && FormController.getSelectFile() != null) {
                buttonOk.setDisable(false);
            }
        });

        return mediaCDialog;
    }

    public static Dialog<FormController.PersonC> createPersonDialog(String name, String stateName, String id) {
        Label nameL = new Label("Name");
        TextField field = new TextField();
        if (name != null) {
            field.setText(name);
        }
        Button button = new Button("Select");
        Label path = new Label("Path");
        if (id != null) {
            path.setText(id);
            File file = new File(getRealPath("scene", id, "jpg"));
            if (file != null && file.exists()) {
                FormController.setSelectFile(file);
            }
        }
        Label state = new Label("State");
        TextField field1 = new TextField();
        if (stateName != null) {
            field1.setText(stateName);
        }
        button.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Person Image", "*.png"));
            fileChooser.setTitle("Select Person State Image");
            File file = fileChooser.showOpenDialog(MainEntry.stage());
            if (file != null) {
                String uid = FormController.getUniqueId();
                path.setText(uid);
                FormController.setSelectFile(file);
            }
        });
        ButtonType buttonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Callback<ButtonType, FormController.PersonC> callback = param -> {
            if (param == buttonType) {
                String _id = path.getText();
                String stateId = FormController.getUniqueId();
                return new FormController.PersonC(_id, field.getText(), stateId, field1.getText());
            }
            return null;
        };

        Dialog<FormController.PersonC> personCDialog = new Dialog<>();
        GridPane pane = createDialogGrid(new Node[]{nameL, field, state, field1, button, path});
        personCDialog.getDialogPane().setContent(pane);
        personCDialog.getDialogPane().getButtonTypes().addAll(buttonType, cancel);
        personCDialog.setResultConverter(callback);

        personCDialog.setHeaderText("Select Person State Image");
        Button buttonOk = (Button) personCDialog.getDialogPane().lookupButton(buttonType);
        buttonOk.setDisable(true);
        field.textProperty().addListener((v, o, n) -> {
            if (n != null && !field1.textProperty().get().isBlank()
                    && FormController.getSelectFile() != null) {
                buttonOk.setDisable(false);
            }
        });
        field1.textProperty().addListener((v, o, n) -> {
            if (n != null && !field.textProperty().get().isBlank()
                    && FormController.getSelectFile() != null) {
                buttonOk.setDisable(false);
            }
        });

        return personCDialog;
    }

    public static Dialog<FormController.StoryBody> createStoryDialog() {
        Dialog<FormController.StoryBody> dialog = new Dialog<>();
        Label label = new Label("Type");
        ChoiceBox<String> stringChoiceBox = new ChoiceBox<>();
        Label name = new Label("Name");
        TextField field = new TextField("");
        Label chapter = new Label("Chapter");
        stringChoiceBox.getItems().addAll("Chapter", "Play");
        ChoiceBox<String> chapterSelect = new ChoiceBox<>();
        chapterSelect.setDisable(true);
        stringChoiceBox.getSelectionModel().selectedIndexProperty().addListener((v, o, n) -> {
            if (n.intValue() == 1) {
                chapterSelect.getItems().clear();
                Set<String> names = FormController.get().getChapterTree().keySet();
                chapterSelect.getItems().addAll(names);
                chapterSelect.setDisable(false);
            } else {
                chapterSelect.getItems().clear();
                chapterSelect.setDisable(true);
            }
        });
        GridPane gridPane = createDialogGrid(new Node[]{label, stringChoiceBox,
                name, field,
                chapter, chapterSelect});

        ButtonType buttonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.setResultConverter(l->{
            if(l==buttonType){
                if(stringChoiceBox.getSelectionModel().getSelectedIndex()==0){
                    String id=FormController.getUniqueId();
                    return new FormController.StoryBody(FormController.StoryBody.Type.CHAPTER,
                            field.getText(),id,null);
                }else if(stringChoiceBox.getSelectionModel().getSelectedIndex()==1){
                    String id=FormController.getUniqueId();
                    String pName=chapterSelect.getSelectionModel().getSelectedItem();
                    return new FormController.StoryBody(FormController.StoryBody.Type.PLAY,
                            field.getText(),id,pName);
                }
            }
            return null;
        });
        dialog.getDialogPane().getButtonTypes().addAll(buttonType,cancel);
        dialog.setHeaderText("Add A New Story");
        dialog.getDialogPane().setContent(gridPane);

        return dialog;
    }

    //node[0] node[1]
    //node[2] node[3]
    //node[4] node[5]
    //....
    public static GridPane createDialogGrid(Node[] list) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5.0);
        gridPane.setVgap(5.0);
        gridPane.setAlignment(Pos.CENTER);
        for (int i = 0; i != list.length; ++i) {
            int cx = i / 2;
            int cy = i % 2;
            gridPane.getChildren().add(list[i]);
            GridPane.setColumnIndex(list[i], cy);
            GridPane.setRowIndex(list[i], cx);
        }

        return gridPane;
    }

    //TODO: 完成每个种类StoryView修改页面
    public static GridPane createStoryViewEditPane(){

        return null;
    }

    public static AudioClip getClip(String id) {
        String url = getSourceUrl("audio", id, "wav");
        if (url == null) return null;
        return new AudioClip(url);
    }

    public static Media getBgm(String id) {
        String url = getSourceUrl("bgm", id, "mp3");
        System.out.println("url:" + url);
        if (url == null) return null;
        return new Media(url);
    }

    public static Image getScene(String id) {
        String url = getSourceUrl("scene", id, "jpg");
        if (url == null) return null;
        return new Image(url);
    }

    public static Image getPersonState(String personId, String stateId) {
        String url = getSourceUrl("person", personId + "_" + stateId, "png");
        if (url == null) return null;
        return new Image(url);
    }

    private static String getSourceUrl(String lib, String id, String format) {
        String pa = FormController.getNowPath() + "/" + lib + "/" + id + "." + format;
        File file = new File(pa);
        if (file.exists()) {
            try {
                return file.toURI().toURL().toExternalForm();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        } else return null;
    }

    public static String getRealPath(String lib, String id, String format) {
        return FormController.getNowPath() + "/" + lib + "/" + id + "." + format;
    }

    //把文件复制到 /lib/id.format里
    public static void moveFileTo(File file, String lib, String id) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String[] ss = file.getAbsolutePath().split("\\.");
                String format = ss[ss.length - 1];
                String destPath = getRealPath(lib, id, format);
                File destF = new File(destPath);

                FileChannel inputChannel = null;
                FileChannel outputChannel = null;
                boolean result;
                try {
                    inputChannel = new FileInputStream(file).getChannel();
                    outputChannel = new FileOutputStream(destF).getChannel();
                    outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
                    result = true;
                    succeeded();
                } catch (IOException e) {
                    e.printStackTrace();
                    result = false;
                } finally {
                    try {
                        if (inputChannel != null) {
                            inputChannel.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (outputChannel != null) {
                            outputChannel.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (!result) {
                    System.out.println("Move Failed.");
                }
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            System.out.println("Move File To Asset Success");
        });
        Platform.runLater(task);
    }

    public static void removeFile(String lib, String id, String format) {
        File file = new File(getRealPath(lib, id, format));
        if (file.exists()) {
            file.delete();
        }
    }

    public static void createFile(String lib,String id,String format){
        File file=new File(getRealPath(lib,id,format));
        if(!file.exists()){
            try {
                File p=file.getParentFile();
                p.mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static URL getStoryFileUrl(String id){
        File file=new File(getRealPath("story",id,"avg"));
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
