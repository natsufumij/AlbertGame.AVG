package albertgame.avg.editor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
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
    private ListView<Progress> progresses;

    @FXML
    private ListView<LibAsset> sceneLib;

    @FXML
    private TreeView<LibAsset> storyLib;

    @FXML
    private FlowPane struckFlow;

    @FXML
    private ChoiceBox<String> typeChoice;

    @FXML
    private ChoiceBox<String> startStruckChoice;

    @FXML
    void initialize() {
        MainFormController2.controller2 = this;
    }

    @FXML
    void aboutEditor(ActionEvent event) {

    }

    @FXML
    void audioAdd(ActionEvent event) {
        LibAsset.findHandler(LibAsset.TYPE.AUDIO).create();
    }

    @FXML
    void audioEdit(ActionEvent event) {
        LibAsset.findHandler(LibAsset.TYPE.AUDIO).edit();
    }

    @FXML
    void audioRemove(ActionEvent event) {
        LibAsset.findHandler(LibAsset.TYPE.AUDIO).remove();
    }

    @FXML
    void bgmAdd(ActionEvent event) {
        LibAsset.findHandler(LibAsset.TYPE.BGM).create();
    }

    @FXML
    void bgmEdit(ActionEvent event) {
        LibAsset.findHandler(LibAsset.TYPE.BGM).edit();
    }

    @FXML
    void bgmRemove(ActionEvent event) {
        LibAsset.findHandler(LibAsset.TYPE.BGM).remove();
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
        LibAsset.findHandler(LibAsset.TYPE.PERSON).create();
    }

    @FXML
    void personEdit(ActionEvent event) {
        LibAsset.findHandler(LibAsset.TYPE.PERSON).edit();
    }

    @FXML
    void personRemove(ActionEvent event) {
        LibAsset.findHandler(LibAsset.TYPE.PERSON).remove();
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
        LibAsset.findHandler(LibAsset.TYPE.SCENE).create();
    }

    @FXML
    void sceneEdit(ActionEvent event) {
        LibAsset.findHandler(LibAsset.TYPE.SCENE).edit();
    }

    @FXML
    void sceneRemove(ActionEvent event) {
        LibAsset.findHandler(LibAsset.TYPE.SCENE).remove();
    }

    @FXML
    void storyAdd(ActionEvent event) {
        LibAsset.findHandler(LibAsset.TYPE.STORY).create();
    }

    @FXML
    void storyRemove(ActionEvent event) {
        LibAsset.findHandler(LibAsset.TYPE.STORY).remove();
    }

    @FXML
    void storyEdit(ActionEvent event){
        LibAsset.findHandler(LibAsset.TYPE.STORY).edit();
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

    public ListView<Progress> getProgresses() {
        return progresses;
    }

    public ListView<LibAsset> getSceneLib() {
        return sceneLib;
    }

    public TreeView<LibAsset> getStoryLib() {
        return storyLib;
    }

    public FlowPane getStruckFlow() {
        return struckFlow;
    }

    public ChoiceBox<String> getTypeChoice() {
        return typeChoice;
    }

    public ChoiceBox<String> getStartStruckChoice() {
        return startStruckChoice;
    }
}
