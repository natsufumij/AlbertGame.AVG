package albertgame.avg.content.n2;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

import java.util.Properties;

public interface FaceData {

    StringProperty strPro(String id);
    BooleanProperty boolPro(String id);
    IntegerProperty intPro(String id);

    void setObj(String id,Object o);
    Object getObj(String id);

    Properties property(String id);

    void animate(boolean set);
}
