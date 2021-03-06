package albertgame.avg.editor;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.*;

public class C {
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
            "Open", "Clear", "Close", "Word", "Pound"
    };
    public static final int OPEN = 0;
    public static final int CLEAR = 1;
    public static final int CLOSE = 2;
    public static final int WORD = 3;
    public static final int POUND = 4;
    public static final ObservableList<String> DIALOG_OB_LIST = FXCollections.observableList(
            List.of(DIALOG_COMMANDS)
    );
    public static final String[] SELECT_COMMANDS = new String[]{"Go"};
    public static final ObservableList<String> SELECT_OB_LIST = FXCollections.observableList(
            List.of(SELECT_COMMANDS)
    );

    private static String projectPath = "Assets";

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
            gridPane.add(list[i], cy, cx);
        }

        return gridPane;
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
        String pa = projectPath + "/" + lib + "/" + id + "." + format;
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
        return projectPath + "/" + lib + "/" + id + "." + format;
    }

    public static void moveFileTo(String sourcePath,String lib,String id){
        File file=new File(sourcePath);
        if(file.exists()&&file.isFile()){
            moveFileTo(file,lib,id);
        }
    }

    //?????????????????? /lib/id.format???
    public static void moveFileTo(File file, String lib, String id) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String[] ss = file.getAbsolutePath().split("\\.");
                String format = ss[ss.length - 1];
                String destPath = getRealPath(lib, id, format);
                File destF = new File(destPath);
                File p=destF.getParentFile();
                if(p.isDirectory()&&!p.exists()){
                    p.mkdirs();
                }

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
            System.out.println("Move File "+file.getPath() +" to "+ lib+"/"+id+" Success");
        });
        Platform.runLater(task);
    }

    public static void removeFile(String lib, String id, String format) {
        File file = new File(getRealPath(lib, id, format));
        if (file.exists()) {
            file.delete();
        }
    }

    public static void createFile(String lib, String id, String format) {
        File file = new File(getRealPath(lib, id, format));
        if (!file.exists()) {
            try {
                File p = file.getParentFile();
                p.mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static URL getStoryFileUrl(String id) {
        File file = new File(getRealPath("story", id, "avg"));
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setProjectPath(String projectPath) {
        C.projectPath = projectPath;
    }
}
