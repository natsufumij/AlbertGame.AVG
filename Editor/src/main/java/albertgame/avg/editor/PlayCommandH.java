package albertgame.avg.editor;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;

public class PlayCommandH implements PlayCommand.Handler {

    private final List<PlayCommand> playCommands;

    private final ListView<PlayCommand> list;
    private final TextArea area;
    private final Button enterBu;

    private PlayCommand selectItem;
    private int selectIndex;

    private boolean editMode;

    private String playId;
    public PlayCommandH(ListView<PlayCommand> list, TextArea area, Button enterBu) {
        this.list = list;
        this.area = area;
        this.enterBu = enterBu;
        playCommands = new ArrayList<>();
        init();
    }

    void init() {
        list.getSelectionModel().selectedIndexProperty().addListener((v, o, n) -> {
            if (n != null && n.intValue() != -1) {
                selectIndex = n.intValue();
                selectItem = list.getItems().get(selectIndex);
            }else {
                selectIndex=-1;
                selectItem=null;
            }
        });
        enterBu.setDisable(true);
        area.setDisable(true);
    }

    @Override
    public void loadCommands(String playId,List<String> expressions) {
        playCommands.clear();
        list.getItems().clear();
        for (String s : expressions) {
            PlayCommand c = PlayCommand.transfer(s);
            if (c != null) {
                playCommands.add(c);
                list.getItems().add(c);
            }
        }
        this.playId=playId;
    }

    @Override
    public void create() {
        area.setDisable(false);
        enterBu.setDisable(false);
        editMode = false;
    }

    @Override
    public void edit() {
        area.setText(selectItem.toWord());
        editMode = true;
        area.setDisable(false);
        enterBu.setDisable(false);
    }

    @Override
    public void enter() {
        PlayCommand command = PlayCommand.transfer(area.getText());
        if(command!=null){
            if (editMode) {
                if(selectIndex!=-1){
                    list.getItems().remove(selectIndex);
                    list.getItems().add(selectIndex + 1, command);
                }else {
                    list.getItems().add(command);
                }
            } else {
                if(selectIndex!=-1){
                    list.getItems().add(selectIndex+1,command);
                }else{
                    list.getItems().add(command);
                }
            }
            selectItem = null;
            area.setDisable(true);
            area.setText("");
            enterBu.setDisable(true);
        }
    }

    @Override
    public void remove() {
        if (selectItem != null) {
            list.getItems().remove(selectItem);
            playCommands.remove(selectItem);
            selectItem = null;
        }
    }

    @Override
    public void save() {
        //TODO: 保存当前的剧本命令列表
    }

    @Override
    public String playId() {
        return playId;
    }

    @Override
    public List<PlayCommand> allCommands() {
        return playCommands;
    }
}
