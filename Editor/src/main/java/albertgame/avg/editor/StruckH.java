package albertgame.avg.editor;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.Map;

public class StruckH implements Struck.Handler{

    private final FlowPane struckPane;
    private final ChoiceBox<String> startChoice;
    Map<String, Struck> struckMap;
    Struck nowSelect;

    private ChangeListener<String> startChangeListener;

    public StruckH(FlowPane struckPane,
                   ChoiceBox<String> startChoice) {
        this.struckPane = struckPane;
        this.startChoice = startChoice;
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
    public void save() {
        //TODO: 保存当前的剧本
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
