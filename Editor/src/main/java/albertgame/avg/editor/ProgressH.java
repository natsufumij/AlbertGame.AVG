package albertgame.avg.editor;

import javafx.scene.control.ListView;

import java.util.HashMap;
import java.util.Map;

public class ProgressH implements Progress.Handler {

    private final Map<String, Progress> allProgress;
    final ListView<Progress> pros;
    private Progress selectItem;

    public ProgressH(ListView<Progress> pros) {
        this.pros = pros;
        allProgress = new HashMap<>();
        init();
    }

    void init() {
        pros.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
            selectItem = n;
        });
    }

    @Override
    public void loadProgresses(Play.GlobalConfig globalConfig) {
        //TODO: 从总设置里导出章节的分支选项
    }

    @Override
    public void loadProgresses(Play.Chapter chapter) {
        //TODO: 从章节里导出剧本的分支选项
    }

    @Override
    public void loadProgresses(Play play) {
        //TODO: 从剧本里导出块的分支选项
    }

    @Override
    public void create() {
        //TODO: 创建一个分支选项
    }

    @Override
    public void edit() {
        //TODO: 编辑选中的分支选项
    }

    @Override
    public void remove() {
        if(selectItem!=null){
            allProgress.remove(selectItem.id);
            pros.getItems().remove(selectItem);
        }
    }

    @Override
    public void save() {
        //TODO: 保存当前的选择分支
    }

    @Override
    public Map<String, Progress> allProgresses() {
        return null;
    }

    @Override
    public Progress copySelectItem() {
        if (selectItem == null) return null;
        return selectItem.copy();
    }
}
