package albertgame.avg.content;

public interface FaceHandler {

    class Arg{

        public static final Arg NONE_ARG=null;

        String type,name;
        String[] data;

        public Arg(String type, String name, String[] data) {
            this.type = type;
            this.name = name;
            this.data = data;
        }

        public String type() {
            return type;
        }

        public String name() {
            return name;
        }

        public String[] data() {
            return data;
        }
    }

    void handle(FaceData data, FaceHead head, Arg arg);
}
