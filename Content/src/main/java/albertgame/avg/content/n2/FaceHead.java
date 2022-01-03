package albertgame.avg.content.n2;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;

public interface FaceHead {

    Node NONE_NODE = null;
    Group THIS_FACE = new Group();

    void attach(Node node);

    void mark(String id, Group parent, Node node);

    Node fetch(String id);

    void remove(String id);
}
