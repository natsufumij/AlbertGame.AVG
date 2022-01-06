module avg.editor {
    requires javafx.media;
    requires javafx.controls;
    requires javafx.fxml;

    opens albertgame.avg.editor to javafx.fxml;

    exports albertgame.avg.editor;
}