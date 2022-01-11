package albertgame.avg.editor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;

public class MainFormController2 {

    private static MainFormController2 controller2;

    public static MainFormController2 getController2() {
        return controller2;
    }

    @FXML
    private ListView<LibAsset> audioLib;

    @FXML
    private ListView<LibAsset> bgmLib;

    @FXML
    private VBox cmdSettings;

    @FXML
    private ListView<PlayCommand> cmdViews;

    @FXML
    private MediaView mediaView;

    @FXML
    private ChoiceBox<String> nameChoice;

    @FXML
    private ListView<LibAsset> personLib;

    @FXML
    private ListView<LibAsset> progresses;

    @FXML
    private ListView<LibAsset> sceneLib;

    @FXML
    private TreeView<LibAsset> storyLib;

    @FXML
    private GridPane struckGrid;

    @FXML
    private ChoiceBox<String> typeChoice;

    @FXML
    void initialize(){
        MainFormController2.controller2=this;
    }

    @FXML
    void aboutEditor(ActionEvent event) {

    }

    @FXML
    void audioAdd(ActionEvent event) {

    }

    @FXML
    void audioEdit(ActionEvent event) {

    }

    @FXML
    void audioRemove(ActionEvent event) {

    }

    @FXML
    void bgmAdd(ActionEvent event) {

    }

    @FXML
    void bgmEdit(ActionEvent event) {

    }

    @FXML
    void bgmRemove(ActionEvent event) {

    }

    @FXML
    void cmdAdd(ActionEvent event) {

    }

    @FXML
    void cmdCompelete(ActionEvent event) {

    }

    @FXML
    void cmdEdit(ActionEvent event) {

    }

    @FXML
    void cmdRemove(ActionEvent event) {

    }

    @FXML
    void openProject(ActionEvent event) {

    }

    @FXML
    void personAdd(ActionEvent event) {

    }

    @FXML
    void personEdit(ActionEvent event) {

    }

    @FXML
    void personRemove(ActionEvent event) {

    }

    @FXML
    void progressAdd(ActionEvent event) {

    }

    @FXML
    void progressEdit(ActionEvent event) {

    }

    @FXML
    void progressRemove(ActionEvent event) {

    }

    @FXML
    void saveCommands(ActionEvent event) {

    }

    @FXML
    void saveProgress(ActionEvent event) {

    }

    @FXML
    void saveStruck(ActionEvent event) {

    }

    @FXML
    void saveProject(ActionEvent event) {

    }

    @FXML
    void sceneAdd(ActionEvent event) {

    }

    @FXML
    void sceneEdit(ActionEvent event) {

    }

    @FXML
    void sceneRemove(ActionEvent event) {

    }

    @FXML
    void storyAdd(ActionEvent event) {

    }

    @FXML
    void storyRemove(ActionEvent event) {

    }

    @FXML
    void struckAdd(ActionEvent event) {

    }

    @FXML
    void struckRemove(ActionEvent event) {

    }

    public ListView<LibAsset> getAudioLib() {
        return audioLib;
    }

    public ListView<LibAsset> getBgmLib() {
        return bgmLib;
    }

    public VBox getCmdSettings() {
        return cmdSettings;
    }

    public ListView<PlayCommand> getCmdViews() {
        return cmdViews;
    }

    public MediaView getMediaView() {
        return mediaView;
    }

    public ChoiceBox<String> getNameChoice() {
        return nameChoice;
    }

    public ListView<LibAsset> getPersonLib() {
        return personLib;
    }

    public ListView<LibAsset> getProgresses() {
        return progresses;
    }

    public ListView<LibAsset> getSceneLib() {
        return sceneLib;
    }

    public TreeView<LibAsset> getStoryLib() {
        return storyLib;
    }

    public GridPane getStruckGrid() {
        return struckGrid;
    }

    public ChoiceBox<String> getTypeChoice() {
        return typeChoice;
    }
}
