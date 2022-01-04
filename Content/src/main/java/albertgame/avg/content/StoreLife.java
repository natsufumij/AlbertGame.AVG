package albertgame.avg.content;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class StoreLife implements FaceLife {

    @Override
    public GameController.KeyInput handlerKeys() {
        return null;
    }

    @Override
    public void init(FaceData d, FaceHead h, Map<String, FaceHandler> p) {
        initParent(d, h);
    }

    private void initParent(FaceData d, FaceHead head) {
        d.intPro("click").set(-1);
        _g = new Group();
        ImageView back = new ImageView();
        back.setFitWidth(ConfigCenter.WINDOW_WIDTH);
        back.setFitHeight(ConfigCenter.WINDOW_HEIGHT);
        back.setImage(ConfigCenter.getSystemImage("Store"));
        head.attach(back);
        Rectangle bigRect = new Rectangle();
        bigRect.setTranslateX(-ConfigCenter.CACHE_RECT_TAP);
        bigRect.setTranslateY(-ConfigCenter.CACHE_RECT_TAP);
        bigRect.setWidth(ConfigCenter.CACHE_ALL_WIDTH);
        bigRect.setHeight(ConfigCenter.CACHE_ALL_HEIGHT);
        bigRect.setFill(Color.color(0.1, 0.3, 0.4, 0.2));
        _g.getChildren().addAll(bigRect);

        int all = ConfigCenter.CACHE_COLUMN * ConfigCenter.CACHE_ROW;
        for (int i = 0; i != all; ++i) {
            if (ConfigCenter.isCacheExist(i)) {
                Node n = createNodeOne(d, i);
                _g.getChildren().add(n);
                head.mark("cache_" + i, _g, n);
            } else {
                Node n = createEmptyOne(d, i);
                _g.getChildren().add(n);
                head.mark("cache_" + i, _g, n);
            }
        }

        _g.getChildren().add(createButton(d, true, head));
        _g.getChildren().add(createButton(d, false, head));

        _g.setTranslateX(ConfigCenter.CACHE_X);
        _g.setTranslateY(ConfigCenter.CACHE_Y);
        head.attach(_g);
    }

    Group _g;

    private Node createButton(FaceData d, boolean isStart, FaceHead head) {
        Text Button = new Text(isStart ? "Start" : "Clear");
        Color c1 = ConfigCenter.getSystemColor("Button0");
        Color c2 = ConfigCenter.getSystemColor("Button1");
        final Effect bloom = new Bloom(0.1);
        Button.setFont(ConfigCenter.getSystemFont("Name"));
        Button.setFill(c1);
        Button.setOnMousePressed(event -> Button.setFill(c2));
        Button.setOnMouseReleased(event -> Button.setFill(c1));
        Button.setOnMouseEntered(event -> Button.setEffect(bloom));
        Button.setOnMouseExited(event -> Button.setEffect(null));
        if (isStart) {
            Button.setOnMouseClicked(event -> MainEntry.Controller().replaceFace(new GameFaceLife()));
        } else {
            Button.setOnMouseClicked(event -> clear(head, d));
        }

        if (isStart) {
            Button.setTranslateX(140.0);
            Button.setTranslateY(300.0);
        } else {
            Button.setTranslateX(260.0);
            Button.setTranslateY(300.0);
        }

        return Button;
    }

    private void clear(FaceHead head, FaceData d) {
        final Task<Boolean> clearTask = new Task<>() {
            @Override
            protected Boolean call(){
                int index = Integer.parseInt(MainEntry.Controller().getData().get("index"));
                boolean s = ConfigCenter.clearCache(index);
                boolean sa = ConfigCenter.clearCacheData(index);
                head.remove("cache_" + index);
                Node emptyOne = createEmptyOne(d, index);
                _g.getChildren().add(emptyOne);
                head.mark("cache_" + index, _g, emptyOne);
                return s && sa;
            }
        };
        clearTask.setOnSucceeded(event -> {
            try {
                System.out.println("Clear . "+clearTask.get().toString());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        Platform.runLater(clearTask);
    }

    private Node createEmptyOne(FaceData d, int index) {
        Group group = new Group();
        Text text = new Text("空文档");
        text.setFont(ConfigCenter.getSystemFont("Name"));
        text.setFill(Color.AZURE);
        group.getChildren().add(text);
        text.setTranslateY(36);

        Rectangle rect = new Rectangle();
        rect.setUserData(index);
        rect.setWidth(ConfigCenter.CACHE_RECT_WIDTH);
        rect.setHeight(ConfigCenter.CACHE_RECT_HEIGHT);
        final Color c1 = ConfigCenter.getSystemColor("Button0");
        final Color c2 = ConfigCenter.getSystemColor("Button1");
        rect.setFill(c1);

        d.intPro("click").addListener((v, o, n) -> {
            if (o == rect.getUserData()) {
                rect.setFill(c1);
            } else if (n == rect.getUserData()) {
                rect.setFill(c2);
            }
        });


        rect.setOnMouseClicked(event -> {
            System.out.println("You Select Store " + index);
            MainEntry.Controller().getData().put("index", index + "");
            d.intPro("click").set(index);
        });

        int rowIndex = index / ConfigCenter.CACHE_COLUMN;
        int columnIndex = index % ConfigCenter.CACHE_COLUMN;

        final int taph = ConfigCenter.CACHE_RECT_HEIGHT + 10;
        final int tapw = ConfigCenter.CACHE_RECT_WIDTH + 10;

        group.setTranslateX(columnIndex * tapw);
        group.setTranslateY(rowIndex * taph);

        group.getChildren().add(rect);

        text.setTranslateX(ConfigCenter.CACHE_RECT_TAP);
        return group;
    }

    private Node createNodeOne(FaceData d, int index) {

        Properties p = ConfigCenter.loadCache(index);
        String cname = p.getProperty("chaptername");
        String pname = p.getProperty("playname");
        String year = p.getProperty("year");
        String month = p.getProperty("month");
        String day = p.getProperty("day");
        String hour = p.getProperty("hour");
        String minute = p.getProperty("minute");
        String second = p.getProperty("second");

        Group group = new Group();
        Text text1 = new Text(cname + " / " + pname);
        Text text2 = new Text(year + "/" + month + "/" + day + " " + hour + ":" + minute + ":" + second);
        text1.setFont(ConfigCenter.getSystemFont("Name"));
        text2.setFont(ConfigCenter.getSystemFont("CacheDate"));
        text1.setFill(Color.AZURE);
        text2.setFill(Color.BURLYWOOD);

        Rectangle rect = new Rectangle();
        rect.setUserData(index);
        rect.setWidth(ConfigCenter.CACHE_RECT_WIDTH);
        rect.setHeight(ConfigCenter.CACHE_RECT_HEIGHT);
        final Color c1 = ConfigCenter.getSystemColor("Button0");
        final Color c2 = ConfigCenter.getSystemColor("Button1");
        rect.setFill(c1);

        d.intPro("click").addListener((v, o, n) -> {
            if (o == rect.getUserData()) {
                rect.setFill(c1);
            } else if (n == rect.getUserData()) {
                rect.setFill(c2);
            }
        });

        rect.setOnMouseClicked(event -> {
            System.out.println("You Select Store " + index);
            MainEntry.Controller().getData().put("index", index + "");
            d.intPro("click").set(index);
        });

        group.getChildren().addAll(text1, text2, rect);

        int rowIndex = index / ConfigCenter.CACHE_COLUMN;
        int columnIndex = index % ConfigCenter.CACHE_COLUMN;

        text1.setTranslateY(26);
        text2.setTranslateY(46);

        final int taph = ConfigCenter.CACHE_RECT_HEIGHT + 10;
        final int tapw = ConfigCenter.CACHE_RECT_WIDTH + 10;
        group.setTranslateX(columnIndex * tapw);
        group.setTranslateY(rowIndex * taph);
        return group;
    }

    @Override
    public void update(FaceData d, FaceHead h) {

    }

    @Override
    public void pause(FaceData d, FaceHead h) {
    }

    @Override
    public void resume(FaceData d, FaceHead h) {
    }

    @Override
    public void stop(FaceData d, FaceHead h) {
        MediaView mediaView = MainEntry.Controller().getMediaView();
        MediaPlayer player = mediaView.getMediaPlayer();
        if (player != null) {
            player.stop();
        }
    }
}
