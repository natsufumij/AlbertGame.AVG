package albertgame.avg.editor;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;

//帮助LibAssetHandler处理LibAsset的帮助类
public class LibAssetHelp {

    public static Dialog<LibAsset> createLibAsset(String lib, String format,boolean needFile) {
        return _createDialog(lib, format, null,needFile);
    }

    public static Dialog<LibAsset> editLibAsset(String lib, LibAsset asset,boolean needFile) {
        return _createDialog(lib, asset.format, asset,needFile);
    }

    private static Dialog<LibAsset> _createDialog(String lib, String format,
                                                  LibAsset asset, boolean needFile) {
        Label idL = new Label("ID:");
        TextField idF = new TextField();
        Label nameL = new Label("Name:");
        TextField nameF = new TextField();

        Label pathL = new Label("");
        Button button = _createSelectFileButton(lib, pathL, format);
        button.setDisable(!needFile);

        Callback<ButtonType, LibAsset> assetCall = (l) -> {
            if (l.getText().equals("OK")) {
                return new LibAsset(idF.getText(), nameF.getText(), pathL.getText(), format);
            } else {
                return null;
            }
        };

        Node[] list = new Node[]{idL, idF, nameL, nameF, button, pathL};

        Dialog<LibAsset> dialog = _createDialog(list, assetCall, lib + " Dialog");
        ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(ok, cancel);

        Button okB = (Button) dialog.getDialogPane().lookupButton(ok);

        //编辑资源库时，Id无法修改
        if (asset != null) {
            idF.setText(asset.id);
            idF.setDisable(true);
            nameF.setText(asset.name);
            pathL.setText(asset.path);
            nameF.textProperty().addListener((v, o, n) -> {
                okB.setDisable(n == null ||
                        n.isBlank() ||
                        (needFile && pathL.getText().isBlank()));
            });
            pathL.textProperty().addListener((v, o, n) -> {
                okB.setDisable(n == null ||
                        nameF.getText().isBlank() ||
                        (needFile && n.isBlank()));
            });
        } else {
            idF.textProperty().addListener((v, o, n) -> {
                okB.setDisable(n == null ||
                        n.isBlank() ||
                        nameF.getText().isBlank() ||
                        (needFile && pathL.getText().isBlank()));
            });
            nameF.textProperty().addListener((v, o, n) -> {
                okB.setDisable(n == null ||
                        idF.getText().isBlank() ||
                        n.isBlank() ||
                        (needFile && pathL.getText().isBlank()));
            });
            pathL.textProperty().addListener((v, o, n) -> {
                okB.setDisable(n == null ||
                        idF.getText().isBlank() ||
                        nameF.getText().isBlank() ||
                        (needFile && n.isBlank()));
            });
        }

        return dialog;
    }

    private static Button _createSelectFileButton(String lib, Label pathL, String format) {
        Button button = new Button("File");
        button.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle(lib + " Select");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(lib,"*." + format));
            File file = chooser.showOpenDialog(MainEntry.stage());
            if (file != null && file.exists() && file.isFile()) {
                pathL.setText(file.getPath());
            }
        });
        return button;
    }

    private static Dialog<LibAsset> _createDialog(Node[] list, Callback<ButtonType, LibAsset> callback,
                                                  String header) {
        Dialog<LibAsset> assetDialog = new Dialog<>();
        assetDialog.setResultConverter(callback);
        GridPane pane = _createGridPane(list);
        assetDialog.getDialogPane().setContent(pane);
        assetDialog.setHeaderText(header);
        return assetDialog;
    }

    private static GridPane _createGridPane(Node[] list) {
        GridPane pane = new GridPane();
        pane.setVgap(5.0);
        pane.setHgap(5.0);
        pane.setAlignment(Pos.CENTER);
        for (int i = 0; i != list.length; ++i) {
            pane.add(list[i], i % 2, i / 2);
        }

        return pane;
    }
}
