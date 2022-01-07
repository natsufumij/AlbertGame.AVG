package albertgame.avg.editor;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

import java.io.File;
import java.net.MalformedURLException;

public class ConfigCenter {
    public static final int WORD_MAX_SIZE = 3 * 30;

    public static BorderPane createBorderPane(Node left1, Node left2, Node right) {
        BorderPane pane = new BorderPane();
        BorderPane.setAlignment(right, Pos.CENTER_RIGHT);
        pane.setRight(right);
        if (left2 != null) {
            HBox box = new HBox();
            box.setAlignment(Pos.CENTER_LEFT);
            BorderPane.setAlignment(box, Pos.CENTER_LEFT);
            box.getChildren().addAll(left1, left2);
            pane.setLeft(box);
        }else {
            BorderPane.setAlignment(left1,Pos.CENTER_LEFT);
            pane.setLeft(left1);
        }

        return pane;
    }

    public static AudioClip getClip(String id) {
        String url = getSourceUrl("audio", id, "wav");
        if (url == null) return null;
        return new AudioClip(url);
    }

    public static Media getBgm(String id) {
        String url = getSourceUrl("bgm", id, "mp3");
        System.out.println("url:"+url);
        if (url == null) return null;
        return new Media(url);
    }

    public static Image getScene(String id) {
        String url = getSourceUrl("scene", id, "jpg");
        if (url == null) return null;
        return new Image(url);
    }

    public static Image getPersonState(String personId,String stateId){
        String url=getSourceUrl("person",personId+"_"+stateId,"png");
        if(url==null)return null;
        return new Image(url);
    }

    private static String getSourceUrl(String lib, String id, String format) {
        String pa = FormController.getNowPath()+"/" + lib + "/" + id + "." + format;
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
}
