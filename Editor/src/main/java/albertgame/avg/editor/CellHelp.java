package albertgame.avg.editor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;

public class CellHelp {
    public static class AudioListCell extends ListCell<FormController.MediaC> {
        @Override
        protected void updateItem(FormController.MediaC item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                Label label = new Label(item.name);
                Button play = new Button("Play");
                play.setOnAction(event -> {
                    AudioClip audioClip = ConfigCenter.getClip(item.id);
                    if (audioClip != null) {
                        audioClip.play();
                    }
                });
                BorderPane pane = ConfigCenter.createBorderPane(new Node[]{label}, play);
                setGraphic(pane);
            } else {
                setText(null);
                setGraphic(null);
            }
        }
    }

    public static class BgmListCell extends ListCell<FormController.MediaC> {

        MediaView view;

        public BgmListCell(MediaView mediaView) {
            view = mediaView;
        }

        @Override
        protected void updateItem(FormController.MediaC item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                Label label = new Label(item.name);
                Button play = new Button("Play");
                play.setUserData(Boolean.TRUE);
                play.setOnAction(event -> {
                    if ((Boolean) play.getUserData()) {
                        Media bgm = ConfigCenter.getBgm(item.id);
                        if (bgm != null) {
                            MediaPlayer player = view.getMediaPlayer();
                            if (player != null) {
                                player.stop();
                            }
                            player = new MediaPlayer(bgm);
                            view.setMediaPlayer(player);

                            player.play();
                            play.setText("Pause");
                            play.setUserData(Boolean.FALSE);
                        }
                    } else {
                        MediaPlayer player = view.getMediaPlayer();
                        if (player != null) {
                            player.stop();
                        }
                        view.setMediaPlayer(null);
                        play.setText("Play");
                        play.setUserData(Boolean.TRUE);
                    }
                });
                BorderPane pane = ConfigCenter.createBorderPane(new Node[]{label}, play);
                setGraphic(pane);
            } else {
                setText(null);
                setGraphic(null);
            }
        }
    }

    public static class PersonListCell extends ListCell<FormController.PersonC> {
        @Override
        protected void updateItem(FormController.PersonC item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                Label label = new Label(item.name);
                label.setPadding(new Insets(0, 10, 0, 0));
                Label state = new Label(item.stateName);
                state.setPadding(new Insets(0, 0, 0, 10));
                ImageView view = new ImageView(ConfigCenter.getPersonState(item.id, item.stateId));
                view.setFitHeight(32);
                view.setFitWidth(32);
                BorderPane pane = ConfigCenter.createBorderPane(new Node[]{label, state}, view);
                setGraphic(pane);
            } else {
                setText(null);
                setGraphic(null);
            }
        }
    }

    public static class SceneListCell extends ListCell<FormController.MediaC> {
        @Override
        protected void updateItem(FormController.MediaC item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                Label label = new Label(item.name);
                label.setPadding(new Insets(0, 10, 0, 0));
                ImageView view = new ImageView(ConfigCenter.getScene(item.id));
                view.setFitHeight(32);
                view.setFitWidth(32);
                BorderPane pane = ConfigCenter.createBorderPane(new Node[]{label}, view);
                setGraphic(pane);
            } else {
                setText(null);
                setGraphic(null);
            }
        }
    }

    public static class ChapterPlayTreeCell extends TreeCell<FormController.StoryBody> {

        TextField field;

        @Override
        protected void updateItem(FormController.StoryBody item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (field != null) {
                        String word = item == null ? "" : item.name;
                        field.setText(word);
                    }
                    setText(null);
                    setGraphic(field);
                } else {
                    setText(item == null ? "" : item.name);
                    setGraphic(getTreeItem().getGraphic());
                }
            }
        }

        String _prefName;

        @Override
        public void startEdit() {
            super.startEdit();
            if (getItem().type == FormController.StoryBody.Type.GLOBAL) {
                return;
            }

            if (field == null) {
                field = new TextField();
                field.setOnKeyReleased(event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        FormController.StoryBody now = getItem();
                        FormController.StoryBody newB = new FormController.StoryBody(now.type, field.getText(), now.id, now.parent_name);
                        if (now.type == FormController.StoryBody.Type.CHAPTER) {
                            ObservableList<FormController.StoryBody> playList = FormController.get().
                                    getPlayInChapterMap().get(_prefName);
                            TreeItem<FormController.StoryBody> item = FormController.get().getChapterTree().get(_prefName);
                            item.getValue().name = newB.name;
                            FormController.get().getChapterTree().remove(_prefName);
                            FormController.get().getChapterTree().put(newB.name, item);
                            FormController.get().getPlayInChapterMap().put(newB.name, playList);
                        } else if (now.type == FormController.StoryBody.Type.PLAY) {
                            ObservableList<FormController.StoryBody> playList = FormController.get().
                                    getPlayInChapterMap().get(now.parent_name);
                            if (playList != null) {
                                playList.remove(now);
                                playList.add(newB);
                            } else {
                                playList = FXCollections.observableList(new ArrayList<>());
                                FormController.get().getPlayInChapterMap().put(now.parent_name, playList);
                                playList.add(newB);
                            }
                        }
                        commitEdit(newB);
                    } else if (event.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                });
                field.setText(getItem().name);
            }
            setText(null);
            setGraphic(field);
            field.selectAll();
            _prefName = getItem().name;
            System.out.println("_prefName:" + _prefName);
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText(getItem().name);
            setGraphic(getTreeItem().getGraphic());
        }
    }

    public static class StoryViewListCell extends ListCell<FormController.StoryView> {

        MediaView view;

        public StoryViewListCell(MediaView view) {
            this.view = view;
        }

        @Override
        protected void updateItem(FormController.StoryView item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            if (!empty) {
                if (item.name.equals("Pound") || item.name.equals("Word")) {
                    setWord();
                } else if (item.type.equals("Audio") &&
                        (item.name.equals("Bgm.Play") || item.name.equals("Sound.Play"))) {
                    setAudio();
                } else {
                    setNormalCommand(this);
                }
            } else {
                setGraphic(null);
            }
        }

        //Dialog Pound XXXX
        //Dialog Word M XXXX
        //Dialog Word S XXXX
        //Dialog Word DataId XXXX
        private void setWord() {
            System.out.println("Set Word...");
            VBox box = new VBox();
            box.setAlignment(Pos.CENTER_LEFT);
            if (getItem().name.equals("Word")) {
                Label label = new Label();
                label.setFont(Font.font("System", FontWeight.BOLD, 20));
                String id = getItem().data[0];
                if (id.equals("M")) {
                    label.setText("Me Say:");
                } else if (id.equals("S")) {
                    label.setText("Secret Say:");
                } else {
                    label.setText(id + "Say:");
                }
                box.getChildren().add(label);
            } else if(getItem().name.equals("Pound")) {
                Label label = new Label();
                label.setFont(Font.font("System", FontWeight.BOLD, 20));
                label.setText("Pound Say:");
                box.getChildren().add(label);
            }
            String word;
            if(getItem().name.equals("Pound")){
                word=getItem().data[0];
            }else {
                word=getItem().data[1];
            }
            StringBuilder builder = new StringBuilder();
            int cy = 0;
            int addCount = 0;
            for (int i = 0; i != word.length(); ++i) {
                char c = word.charAt(i);
                //超过一行的结尾,或者强行换行
                if (cy == ConfigCenter.WORD_MAX_COLUMN || c == '\\') {
                    Label line = new Label();
                    line.setFont(Font.font("System", FontWeight.BOLD, 20));
                    line.setText(builder.toString());
                    box.getChildren().add(line);
                    builder = new StringBuilder();
                    cy = 0;
                    ++addCount;
                    //否则就把当前的添加到builder里
                } else {
                    builder.append(c);
                    ++cy;
                }
            }

            //如果builder里还有剩下的文字，但是并没有到一行，并且还有空着的一行，则当作一行存入
            if (addCount != ConfigCenter.WORD_MAX_ROW && builder.length() > 0) {
                Label line = new Label(builder.toString());
                line.setFont(Font.font("System", FontWeight.BOLD, 20));
                box.getChildren().add(line);
            }

            setGraphic(box);
        }

        private void setAudio() {
            String type = getItem().name;
            if (type.equals("Bgm.Play")) {
                Label label = new Label(getItem().data[0]);
                Button play = new Button("Play");
                play.setUserData(Boolean.TRUE);
                play.setOnAction(event -> {
                    FormController.MediaC m = FormController.get().getBgmMap().get(getItem().data[0]);
                    if ((Boolean) play.getUserData()) {
                        Media bgm = ConfigCenter.getBgm(m.id);
                        if (bgm != null) {
                            MediaPlayer player = view.getMediaPlayer();
                            if (player != null) {
                                player.stop();
                            }
                            player = new MediaPlayer(bgm);
                            view.setMediaPlayer(player);

                            player.play();
                            play.setText("Pause");
                            play.setUserData(Boolean.FALSE);
                        }
                    } else {
                        MediaPlayer player = view.getMediaPlayer();
                        if (player != null) {
                            player.stop();
                        }
                        view.setMediaPlayer(null);
                        play.setText("Play");
                        play.setUserData(Boolean.TRUE);
                    }
                });
                BorderPane pane = ConfigCenter.createBorderPane(new Node[]{label}, play);
                setGraphic(pane);
            } else {
                Label label = new Label(getItem().data[0]);
                Button play = new Button("Play");
                play.setOnAction(event -> {
                    FormController.MediaC m = FormController.get().getAudioMap().get(getItem().data[0]);
                    AudioClip audioClip = ConfigCenter.getClip(m.id);
                    if (audioClip != null) {
                        audioClip.play();
                    }
                });
                BorderPane pane = ConfigCenter.createBorderPane(new Node[]{label}, play);
                setGraphic(pane);
            }
        }

        private static void setNormalCommand(StoryViewListCell storyViewListCell) {
            Label type = new Label(storyViewListCell.getItem().type);
            type.setPadding(new Insets(0, 10, 0, 0));
            Label name = new Label(storyViewListCell.getItem().name);
            name.setPadding(new Insets(0, 0, 0, 10));
            FormController.StoryView item = storyViewListCell.getItem();
            Label[] data = new Label[item.data.length + 2];
            data[0] = type;
            data[1] = name;
            if (item.data.length != 0) {
                for (int i = 0; i < item.data.length; ++i) {
                    data[i + 2] = new Label(item.data[i]);
                    data[i + 2].setPadding(new Insets(0, 0, 0, 10));
                }
            }
            BorderPane pane = ConfigCenter.createBorderPane(data, null);
            storyViewListCell.setGraphic(pane);
        }
    }
}
