package albertgame.avg.content;

public interface FaceHandler {

    record Arg(String type, String name, String[] data) {
        public static Arg NONE_ARG = null;
    }

    void handle(FaceData data, FaceHead head, Arg arg);
}
