package albertgame.avg.editor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

import java.util.ArrayList;
import java.util.List;

//帮助PlayCommandHandler处理
public class PlayCommandHelp {

    public static class PlayCommandListCell extends ListCell<PlayCommand> {
        @Override
        protected void updateItem(PlayCommand item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                Node n;
                if (item.name.equals("Word") || item.name.equals("Pound")) {
                    n = _createWords(item);
                } else {
                    n = _createCommands(item);
                }
                setGraphic(n);
            } else {
                setGraphic(null);
            }
        }

        private static Node _createCommands(PlayCommand command) {
            HBox hBox = new HBox();
            Label type = _label(command.type);
            Label name = _label(command.name);
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.getChildren().addAll(type, name);

            if (command.data != null) {
                for (String s : command.data) {
                    Label l = _label(s);
                    hBox.getChildren().add(l);
                }
            }
            return hBox;
        }

        private static final Font commandF = Font.font("arial", FontPosture.ITALIC, 9.0);
        private static final Color commandC = Color.color(0.4, 0.3, 0.7, 0.9);

        private static Label _label(String w) {
            Label label = new Label(w);
            label.setFont(commandF);
            label.setTextFill(commandC);
            label.setPadding(new Insets(0.5, 2.5, 0.5, 2.5));
            return label;
        }

        private static final Font wordF = Font.font("arial", 12.0);
        private static final Color wordC = Color.color(0.4, 0.3, 0, 0.8);

        private static Node _createWords(PlayCommand command) {
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER_LEFT);
            String id = null, word = "";
            if (command.name.equals("Pound")) {
                id = null;
                word = command.data[0];
            } else if (command.name.equals("Word")) {
                id = command.data[0];
                word = command.data[1];
            }
            if (id != null) {
                Label label = new Label(id + ":");
                label.setFont(wordF);
                label.setTextFill(wordC);
                vBox.getChildren().add(label);
            }
            List<String> words = _allString(word);
            for (String s : words) {
                Label label = new Label(s);
                label.setFont(wordF);
                label.setTextFill(wordC);
                vBox.getChildren().add(label);
            }
            return vBox;
        }
    }

    private static List<String> _allString(String a) {
        List<String> arr = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        int ix = 0, iy = 0;
        for (int i = 0; i != a.length() && ix < C.WORD_MAX_ROW; ++i) {
            if (iy == C.WORD_MAX_COLUMN) {
                //强行换行
                iy = 0;
                ++ix;
                char c = a.charAt(i);
                line.append(c);
                arr.add(line.toString());
                line = new StringBuilder();
            } else {
                char c = a.charAt(i);
                //换行
                if (c == '\\') {
                    ++ix;
                    arr.add(line.toString());
                    line = new StringBuilder();
                } else {
                    ++iy;
                    line.append(a.charAt(i));
                }
            }
        }
        if (line.length() > 0) {
            arr.add(line.toString());
        }
        return arr;
    }
}
