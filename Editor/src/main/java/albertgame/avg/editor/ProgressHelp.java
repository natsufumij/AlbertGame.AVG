package albertgame.avg.editor;

import javafx.scene.control.Dialog;
import javafx.scene.control.ListCell;

//帮助ProgressHelp
public class ProgressHelp {

    //Source_Id Option_id
    //1 Expression1
    //2 Expression2
    //3 Expression3
    //4 Expression4
    public static class ProgressListCell extends ListCell<Progress>{
        @Override
        protected void updateItem(Progress item, boolean empty) {
            super.updateItem(item, empty);
        }
    }
}
