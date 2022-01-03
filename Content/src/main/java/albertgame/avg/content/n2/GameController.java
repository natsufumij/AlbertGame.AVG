package albertgame.avg.content.n2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;

public class GameController {

    public record KeyInput(EventHandler<KeyEvent> press,
                           EventHandler<KeyEvent> release) {
    }

    private static class FaceDataIm implements FaceData {

        boolean animating;
        Map<String, StringProperty> stringPropertyMap;
        Map<String, BooleanProperty> booleanPropertyMap;
        Map<String, IntegerProperty> integerPropertyMap;
        Map<String, Properties> propertiesMap;
        Map<String, Object> objectMap;

        public FaceDataIm() {
            animating = false;
            stringPropertyMap = new HashMap<>();
            booleanPropertyMap = new HashMap<>();
            integerPropertyMap = new HashMap<>();
            propertiesMap = new HashMap<>();
            objectMap=new HashMap<>();
        }

        @Override
        public StringProperty strPro(String id) {
            if (stringPropertyMap.containsKey(id)) return stringPropertyMap.get(id);

            return stringPropertyMap.put(id, new SimpleStringProperty(""));
        }

        @Override
        public BooleanProperty boolPro(String id) {
            if (booleanPropertyMap.containsKey(id)) return booleanPropertyMap.get(id);

            return booleanPropertyMap.put(id, new SimpleBooleanProperty(false));
        }

        @Override
        public IntegerProperty intPro(String id) {
            if (integerPropertyMap.containsKey(id)) return integerPropertyMap.get(id);

            return integerPropertyMap.put(id, new SimpleIntegerProperty(0));
        }

        @Override
        public void setObj(String id, Object o) {
            objectMap.put(id,o);
        }

        @Override
        public Object getObj(String id) {
            return objectMap.get(id);
        }

        @Override
        public Properties property(String id) {
            if (propertiesMap.containsKey(id)) return propertiesMap.get(id);

            return propertiesMap.put(id, new Properties());
        }

        @Override
        public void animate(boolean set) {
            animating = set;
        }
    }

    private static class FaceHeadIm extends Parent implements FaceHead {

        Map<String, NodeC> nodeMap;

        static class NodeC {
            Node node;
            Group p;

            NodeC(Node node, Group p) {
                this.node = node;
                this.p = p;
            }
        }

        public FaceHeadIm() {
            nodeMap = new HashMap<>();
        }

        @Override
        public void attach(Node node) {
            super.getChildren().add(node);
        }

        @Override
        public void mark(String id, Group p, Node node) {
            nodeMap.put(id, new NodeC(node, p));
        }

        @Override
        public Node fetch(String id) {
            if (nodeMap.containsKey(id)) return nodeMap.get(id).node;
            return NONE_NODE;
        }

        @Override
        public void remove(String id) {
            if (!nodeMap.containsKey(id)) return;

            NodeC c = nodeMap.get(id);
            if (c.p != null) {
                if (c.p == FaceHead.THIS_FACE) {
                    super.getChildren().remove(c.node);
                    nodeMap.remove(id);
                } else {
                    c.p.getChildren().remove(c.node);
                }
            }
        }
    }

    private static class FaceC {
        FaceHeadIm head = new FaceHeadIm();
        FaceDataIm data = new FaceDataIm();
        Map<String, FaceHandler> handlerMap = new HashMap<>();
        KeyInput input;
        FaceLife life;
    }

    public static GameController gameController(double width, double height, Stage stage, FaceLife initFaceLife) {
        return new GameController(width, height, stage, initFaceLife);
    }

    private final Scene _scene;
    private final Group _allGroup;
    private final MediaView mediaView;
    private Parent _headNode;

    private final Map<String, String> data;
    private final Stack<FaceC> packs;

    private FaceLife faceLife;
    private FaceDataIm faceData;
    private FaceHeadIm faceHead;
    private Map<String, FaceHandler> handlerMap;
    private KeyInput keyInput;

    double _w,_h;
    FaceLife _life;
    Stage _stage;
    private GameController(double width, double height, Stage stage, FaceLife initialFace) {
        _w=width;
        _h=height;
        _life=initialFace;
        _stage=stage;
        _allGroup = new Group();

        Group _mediaGroup = new Group();
        mediaView = new MediaView();
        _allGroup.getChildren().add(_mediaGroup);

        data = new HashMap<>();
        data.put("index","0");
        packs = new Stack<>();

        _scene = new Scene(_allGroup, width, height);
        stage.setScene(_scene);
    }

    public void initController(){
        pushFace(_life);
        initControlLine();
        initKeys();
    }

    public void pushFace(FaceLife life) {

        if (!packs.empty()) {
            FaceC c = packs.peek();
            c.life.pause(c.data, c.head);
        }

        FaceC c = new FaceC();
        c.life = life;
        c.input = life.handlerKeys();

        if (_headNode != null) {
            _allGroup.getChildren().remove(_headNode);
        }
        life.init(c.data, c.head, c.handlerMap);

        this.faceLife = c.life;
        faceData = c.data;
        faceHead = c.head;
        keyInput = c.input;
        handlerMap = c.handlerMap;

        _headNode = c.head;
        _allGroup.getChildren().add(_headNode);
        packs.push(c);
    }

    public boolean popFace() {
        if (packs.size() > 1) {
            FaceC c = packs.pop();
            c.life.stop(c.data, c.head);

            c = packs.peek();
            c.life.resume(c.data, c.head);

            faceLife = c.life;
            faceData = c.data;
            faceHead = c.head;
            keyInput = c.input;
            handlerMap = c.handlerMap;

            _allGroup.getChildren().remove(_headNode);
            _headNode = faceHead;
            _allGroup.getChildren().add(_headNode);
            return true;
        } else return false;
    }

    public void replaceFace(FaceLife life){
        popFace();
        pushFace(life);
    }

    public void handleArguments(FaceHandler.Arg arg) {
        if (handlerMap.containsKey(arg.type())) {
            FaceHandler faceHandler = handlerMap.get(arg.type());
            faceHandler.handle(faceData, faceHead, arg);
        }
    }

    public Map<String, String> getData() {
        return data;
    }

    public MediaView getMediaView() {
        return mediaView;
    }

    private void initKeys() {
        _scene.setOnKeyReleased(event -> {
            if (keyInput != null && keyInput.release != null && !faceData.animating) {
                keyInput.release.handle(event);
            }
        });
        _scene.setOnKeyPressed(event -> {
            if (keyInput != null && keyInput.press != null && !faceData.animating) {
                keyInput.press.handle(event);
            }
        });
    }

    private void initControlLine() {
        Timeline controlLine = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(30), event -> {
            if (!faceData.animating) {
                faceLife.update(faceData, faceHead);
            }
        });
        controlLine.setCycleCount(Timeline.INDEFINITE);
        controlLine.getKeyFrames().add(keyFrame);
        controlLine.play();
    }
}
