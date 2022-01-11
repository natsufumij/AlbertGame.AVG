package albertgame.avg.content;

import java.util.Map;

public interface FaceLife {
    GameController.MouseInput handlerKeys();

    void init(FaceData d, FaceHead h, Map<String,FaceHandler> p);
    void update(FaceData d, FaceHead h);
    void pause(FaceData d, FaceHead h);
    void resume(FaceData d, FaceHead h);
    void stop(FaceData d, FaceHead h);
}
