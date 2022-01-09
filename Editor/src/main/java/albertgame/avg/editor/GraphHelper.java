package albertgame.avg.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GraphHelper {

    public static class ShapeNode{
        double x,y;
        String id;
        String optionId;
        List<ShapeLine> lines=new ArrayList<>();
        public ShapeNode(String id, String optionId,double x,double y) {
            this.id = id;
            this.optionId = optionId;
            this.x=x;
            this.y=y;
        }
    }

    public static class ShapeLine{
        String destId;
        String expression;

        public ShapeLine(String destId,String expression) {
            this.destId = destId;
            this.expression=expression;
        }
    }

    public static List<ShapeNode> loadShapeNode(Properties properties){
        List<ShapeNode> nodes=new ArrayList<>();


        return nodes;
    }
}
