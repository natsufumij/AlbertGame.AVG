package albertgame.avg.editor;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.*;

public class GraphHelper {

    static class Manager{
        Group group;
        private Map<String,ShapeNode> nodeIdMap;
        private Map<String,ShapeLine> lineIdMap;
        private Map<String,Group> lineShapeMap;

        public Manager(Group group) {
            this.group = group;
            nodeIdMap=new HashMap<>();
            lineIdMap=new HashMap<>();
            lineShapeMap=new HashMap<>();
        }

        public void addNode(ShapeNode node){
            if(!nodeIdMap.containsKey(node.id)){
                nodeIdMap.put(node.id,node);
                group.getChildren().add(node.text);
            }
        }

        public void removeNode(String nodeId){
            if(nodeIdMap.containsKey(nodeId)){
                ShapeNode node=nodeIdMap.get(nodeId);
                group.getChildren().remove(node.text);
                nodeIdMap.remove(nodeId);
            }
        }

        public ShapeNode findNode(String nodeId){
            return nodeIdMap.get(nodeId);
        }

        public void addLine(ShapeLine line){
            if(!lineIdMap.containsKey(line.id)){
                String sourceId=line.sourceId;
                if(nodeIdMap.containsKey(sourceId)){
                    ShapeNode node=nodeIdMap.get(sourceId);
                    node.lines.add(line);
                    lineIdMap.put(line.id,line);
                    Group ne=createArrow(sourceId,line.destId);
                    lineShapeMap.put(line.id,ne);
                    group.getChildren().add(ne);
                }
            }
        }

        public void removeLine(String lineId){

        }

        public ShapeLine findLine(String lineId){
            return null;
        }

        public List<ShapeNode> getAllNode(){
            return null;
        }

        public List<ShapeLine> getAllLine(){
            return null;
        }

        //sx,sy -> dx,dy
        Group createArrow(String sourceId,String destId){
            ShapeNode source=nodeIdMap.get(sourceId);
            if(source==null)return null;

            ShapeNode dest=nodeIdMap.get(destId);
            if(dest==null)return null;

            Group group=new Group();
            double dx=dest.x-source.x;
            double dy=dest.y-source.y;
            double sx=source.x+dx*0.01;
            double sy=source.y+dy*0.01;
            double sx2=dest.x-dx*0.01;
            double sy2=dest.y-dy*0.01;
            double rx=sx2-sx;
            double ry=sy2-sy;
            group.setTranslateX(sx);
            group.setTranslateY(sy);
            Circle circle=new Circle();
            circle.setRadius(5.0);
            circle.setFill(Color.AZURE);
            Line l=new Line();
            l.setEndX(rx);
            l.setEndY(ry);
            group.getChildren().addAll(circle,l);
            return group;
        }
    }

    public static class ShapeNode{
        double x,y;
        String id;
        String optionId;
        String name;
        Text text;
        List<ShapeLine> lines=new ArrayList<>();
        public ShapeNode(String id,String name, String optionId,double x,double y) {
            this.id = id;
            this.optionId = optionId;
            this.x=x;
            this.y=y;
            this.name=name;
            text=new Text(name);
            text.setFont(Font.font(20));
            text.setTranslateX(x);
            text.setTranslateY(y);
        }
    }

    public static class ShapeLine{
        String id;
        String sourceId;
        String destId;
        String expression;
        Group arrow;

        public ShapeLine(String id, String sourceId,
                         String destId, String expression) {
            this.id = id;
            this.sourceId = sourceId;
            this.destId = destId;
            this.expression = expression;
            arrow=new Group();
        }
    }

    public static List<ShapeNode> loadShapeNode(Properties properties){
        List<ShapeNode> nodes=new ArrayList<>();


        return nodes;
    }
}
