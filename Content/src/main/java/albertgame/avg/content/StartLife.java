package albertgame.avg.content;

import javafx.application.Platform;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.Map;

public class StartLife implements FaceLife {
    @Override
    public GameController.KeyInput handlerKeys() {
        return null;
    }

    @Override
    public void init(FaceData d, FaceHead h, Map<String, FaceHandler> p) {

        ImageView back = new ImageView();
        back.setFitHeight(ConfigCenter.WINDOW_HEIGHT);
        back.setFitWidth(ConfigCenter.WINDOW_WIDTH);
        back.setImage(ConfigCenter.getSystemImage("Start"));
        h.attach(back);

        final String[] bName = {"S T A R T", "E X I T"};

        final Effect bloom = new Bloom(0.2);
        final Color pc = ConfigCenter.getSystemColor("Start0");
        final Color pc2 = ConfigCenter.getSystemColor("Start1");

        double startY = ConfigCenter.WINDOW_HEIGHT - 200;
        double height = ConfigCenter.NAME_FONT.getSize()*2;
        double px = ConfigCenter.WINDOW_WIDTH - 200;
        for (int i = 0; i != bName.length; ++i) {
            Text t = new Text(bName[i]);
            t.setId(i + "");
            t.setFont(ConfigCenter.getSystemFont("Name"));
            t.setFill(pc);
            t.setOnMouseEntered(event -> t.setEffect(bloom));
            t.setOnMouseExited(event -> t.setEffect(null));
            t.setOnMousePressed(event -> t.setFill(pc2));
            t.setOnMouseReleased(event -> t.setFill(pc));
            t.setOnMouseClicked(event -> {
                if (t.getId().equals("0")) {
                    System.out.println("Go To Store.");
                    MainEntry.Controller().pushFace(new StoreLife());
                } else if (t.getId().equals("1")) {
                    System.out.println("Exit Game.");
                    Platform.exit();
                }
            });
            t.setTranslateY(startY + i * height);
            t.setTranslateX(px);
            h.attach(t);
        }

        _m=ConfigCenter.getSystemBgm("Start");
        MediaView mediaView=MainEntry.Controller().getMediaView();
        MediaPlayer player=mediaView.getMediaPlayer();
        if(player!=null){
            player.stop();
        }
        player=new MediaPlayer(_m);
        mediaView.setMediaPlayer(player);
        player.setAutoPlay(true);
        player.play();
    }

    @Override
    public void update(FaceData d, FaceHead h) {

    }

    @Override
    public void pause(FaceData d, FaceHead h) {
        MediaView mediaView=MainEntry.Controller().getMediaView();
        MediaPlayer player=mediaView.getMediaPlayer();
        if(player!=null){
            player.pause();
        }
    }

    @Override
    public void resume(FaceData d, FaceHead h) {
        MediaView mediaView=MainEntry.Controller().getMediaView();
        MediaPlayer player=mediaView.getMediaPlayer();
        if(player!=null){
            player.stop();
        }
        player=new MediaPlayer(_m);
        player.setAutoPlay(true);
        mediaView.setMediaPlayer(player);
        player.play();
    }

    Media _m;
    @Override
    public void stop(FaceData d, FaceHead h) {
        MediaView mediaView=MainEntry.Controller().getMediaView();
        MediaPlayer player=mediaView.getMediaPlayer();
        if(player!=null){
            player.stop();
        }
    }
}
