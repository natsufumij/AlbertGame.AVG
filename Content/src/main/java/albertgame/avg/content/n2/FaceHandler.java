package albertgame.avg.content.n2;

public interface FaceHandler {

    public record Arg(String type, String name, String[] data) {
    }

    void handle(FaceData data, FaceHead head, Arg arg);
}
