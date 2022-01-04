package albertgame.avg.content;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

import java.util.Properties;

public interface FaceData {

    StringProperty strPro(String id);
    BooleanProperty boolPro(String id);
    IntegerProperty intPro(String id);

    Properties property(String id);

    void animate(boolean set);
}
