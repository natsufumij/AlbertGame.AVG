package albertgame.avg.editor;

import albertgame.avg.editor.Play;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.Map;

public class StruckH implements Struck.Handler{

    GridPane pane;
    Map<String, Struck> struckMap;
    Struck nowSelect;

    public StruckH(GridPane pane) {
        this.pane = pane;
        struckMap=new HashMap<>();
    }

    @Override
    public void loadAllStruck(Play play) {
        //TODO: 从剧本中导入Struck
    }

    @Override
    public void create() {
        //TODO: 创建一个Struck
    }

    @Override
    public void remove() {
        //TODO: 移除选中的Struck
    }

    @Override
    public Map<String, Struck> allStruck() {
        return struckMap;
    }

    @Override
    public Struck copySelectItem() {
        if(nowSelect==null){
            return null;
        }else {
            Struck struck=new Struck(nowSelect.getId(),nowSelect.getName());
            for(String s:nowSelect.getExpressions()){
                struck.getExpressions().add(s);
            }
            return struck;
        }
    }
}
