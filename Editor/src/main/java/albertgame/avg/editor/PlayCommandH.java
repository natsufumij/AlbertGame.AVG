package albertgame.avg.editor;

import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;

public class PlayCommandH implements PlayCommand.Handler {

    private final List<PlayCommand> playCommands;
    private final ListView<PlayCommand> list;
    private PlayCommand selectItem;

    public PlayCommandH(ListView<PlayCommand> list) {
        this.list = list;
        playCommands = new ArrayList<>();
        init();
    }

    void init(){
        list.getSelectionModel().selectedItemProperty().addListener((v,o,n)->{
            selectItem= n;
        });
    }

    @Override
    public void loadCommands(List<String> expressions) {
        playCommands.clear();
        list.getItems().clear();
        for(String s:expressions){
            PlayCommand c=PlayCommand.transfer(s);
            if(c!=null){
                playCommands.add(c);
                list.getItems().add(c);
            }
        }
    }

    @Override
    public void create() {
        //TODO: 创建一个PlayCommand
    }

    @Override
    public void edit() {
        //TODO: 编辑选中的PlayCommand
    }

    @Override
    public void remove() {
        if(selectItem!=null){
            list.getItems().remove(selectItem);
            playCommands.remove(selectItem);
            selectItem=null;
        }
    }

    @Override
    public PlayCommand copySelectItem() {
        if (selectItem == null) return null;
        int length = 0;
        if (selectItem.data != null && selectItem.data.length > 0) {
            length = selectItem.data.length;
        }
        PlayCommand command = new PlayCommand(selectItem.type, selectItem.name,
                new String[length]);
        if (selectItem.data != null) {
            System.arraycopy(selectItem.data, 0, command.data, 0, length);
        }

        return command;
    }

    @Override
    public List<PlayCommand> allCommands() {
        return playCommands;
    }
}
