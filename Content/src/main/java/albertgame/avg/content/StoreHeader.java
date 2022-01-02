package albertgame.avg.content;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Properties;

public class StoreHeader extends Parent {

    private static final Node NO_NODE = null;

    public StoreHeader() {
        initParent();
    }

    private void initParent() {
        Group group = new Group();
        Rectangle bigRect=new Rectangle();
        bigRect.setWidth(ConfigCenter.CACHE_ALL_WIDTH);
        bigRect.setHeight(ConfigCenter.CACHE_ALL_HEIGHT);
        bigRect.setFill(Color.color(0.1,0.3,0.4,0.2));
        super.getChildren().add(bigRect);

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

        super.getChildren().add(group);
    }

    private Node createEmptyOne(int index) {
        Group group = new Group();
        Text text = new Text("空文档");
        text.setFont(ConfigCenter.NAME_FONT);
        group.getChildren().add(text);
        text.setTranslateY(36);

        Rectangle rect = new Rectangle();
        rect.setWidth(ConfigCenter.CACHE_RECT_WIDTH);
        rect.setHeight(ConfigCenter.CACHE_RECT_HEIGHT);
        rect.setFill(Color.color(0.3, 0.2, 0.4, 0.3));

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
        rect.setFill(Color.color(0.3, 0.2, 0.4, 0.3));

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
}
