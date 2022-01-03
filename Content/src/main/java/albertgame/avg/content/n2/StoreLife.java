package albertgame.avg.content.n2;

import albertgame.avg.content.ConfigCenter;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Map;
import java.util.Properties;

public class StoreLife implements FaceLife{

    @Override
    public GameController.KeyInput handlerKeys() {
        return null;
    }

    @Override
    public void init(FaceData d, FaceHead h, Map<String, FaceHandler> p) {
        initParent(h);
    }

    private void initParent(FaceHead head) {
        Group group = new Group();
        ImageView back=new ImageView();
        back.setFitWidth(ConfigCenter.WINDOW_WIDTH);
        back.setFitHeight(ConfigCenter.WINDOW_HEIGHT);
        back.setImage(ConfigCenter.loadScene("demo2"));
        head.attach(back);
        Rectangle bigRect=new Rectangle();
        bigRect.setTranslateX(-ConfigCenter.CACHE_RECT_TAP);
        bigRect.setTranslateY(-ConfigCenter.CACHE_RECT_TAP);
        bigRect.setWidth(ConfigCenter.CACHE_ALL_WIDTH);
        bigRect.setHeight(ConfigCenter.CACHE_ALL_HEIGHT);
        bigRect.setFill(Color.color(0.1,0.3,0.4,0.2));
        group.getChildren().addAll(bigRect);

        int all = ConfigCenter.CACHE_COLUMN * ConfigCenter.CACHE_ROW;
        for (int i = 0; i != all; ++i) {
            if (ConfigCenter.isCacheExist(i)) {
                Node n=createNodeOne(i);
                group.getChildren().add(n);
            } else {
                Node n=createEmptyOne(i);
                group.getChildren().add(n);
            }
        }

        group.setTranslateX(ConfigCenter.CACHE_RECT_TAP);
        group.setTranslateY(ConfigCenter.CACHE_RECT_TAP);
        group.setTranslateX(ConfigCenter.CACHE_X);
        group.setTranslateY(ConfigCenter.CACHE_Y);
        head.attach(group);
    }

    private Node createEmptyOne(int index) {
        Group group = new Group();
        Text text = new Text("空文档");
        text.setFont(ConfigCenter.NAME_FONT);
        text.setFill(Color.AZURE);
        group.getChildren().add(text);
        text.setTranslateY(36);

        Rectangle rect = new Rectangle();
        rect.setWidth(ConfigCenter.CACHE_RECT_WIDTH);
        rect.setHeight(ConfigCenter.CACHE_RECT_HEIGHT);
        final Color c1=Color.color(0.3,0.2,0.4,0.3);
        final Color c2=Color.color(0.1,0.1,0.6,0.3);
        rect.setFill(c1);
        rect.setOnMouseEntered(event -> {
            rect.setFill(c2);
        });
        rect.setOnMouseExited(event -> {
            rect.setFill(c1);
        });
        rect.setOnMouseClicked(event -> {
            System.out.println("You Select Store "+index);
            MainEntry.Controller().getData().put("index",index+"");
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

    private Node createNodeOne(int index) {

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
        text1.setFont(ConfigCenter.NAME_FONT);
        text2.setFont(ConfigCenter.CACHE_DATE_FONT);
        text1.setFill(Color.AZURE);
        text2.setFill(Color.BURLYWOOD);

        Rectangle rect = new Rectangle();
        rect.setWidth(ConfigCenter.CACHE_RECT_WIDTH);
        rect.setHeight(ConfigCenter.CACHE_RECT_HEIGHT);
        final Color c1=Color.color(0.3,0.2,0.4,0.3);
        final Color c2=Color.color(0.1,0.1,0.6,0.3);
        final Color c3=Color.color(0.3,0.4,0.2,0.3);
        rect.setFill(c1);
        rect.setOnMouseEntered(event -> {
            rect.setFill(c2);
        });
        rect.setOnMouseExited(event -> {
            rect.setFill(c1);
        });
        rect.setOnMouseClicked(event -> {
            System.out.println("You Select Store "+index);
            MainEntry.Controller().getData().put("index",index+"");
        });

        group.getChildren().addAll(text1, text2, rect);

        int rowIndex = index / ConfigCenter.CACHE_COLUMN;
        int columnIndex = index % ConfigCenter.CACHE_ROW;

        text1.setTranslateY(26);
        text2.setTranslateY(46);

        final int taph = ConfigCenter.CACHE_RECT_HEIGHT + 10;
        final int tapw = ConfigCenter.CACHE_RECT_WIDTH + 10;
        group.setTranslateX(columnIndex * tapw);
        group.setTranslateY(rowIndex * taph);

        text1.setTranslateX(ConfigCenter.CACHE_RECT_TAP);
        text2.setTranslateX(ConfigCenter.CACHE_RECT_TAP);
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

    }
}
