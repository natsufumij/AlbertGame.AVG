package albertgame.avg.editor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainEntry extends Application {

    private static Stage _stage;
    public static Stage stage() {
        return _stage;
    }

    @Override
    public void start(Stage primaryStage) {
        _stage=primaryStage;

        try {
            Parent parent=FXMLLoader.load(
                    Objects.requireNonNull(
                            MainEntry.class.getResource(
                                    "form.fxml")));
            Scene scene = new Scene(parent);
            primaryStage.setScene(scene);
            Editor.get().setController(
                    MainFormController2.getController2());
            Editor.get().init();
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
