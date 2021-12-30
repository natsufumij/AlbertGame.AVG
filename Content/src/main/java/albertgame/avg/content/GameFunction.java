package albertgame.avg.content;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public interface GameFunction {

    String[] NONE_EXTRA = null;

    record FunctionArg(String type, String name, String value, String[] extra) {
    }

    void fun(GameData data, GameHeader header, FunctionArg arg);

    /**
     * - Dialog  Word  #Text  默认文字,延续上一次对话的名称
     * - Dialog  Word  S  #Text  对话文字，使用名称？？？，以后都如此
     * - Dialog  Word  M  #Text  对话文字，以‘我’为名称，以后都如此
     * - Dialog  Word  DataId  #Text  对话文字,以id的人物名称，以后都如此
     * - Dialog  Open  打开对话框
     * - Dialog  Clear  清空对话框及名称
     * - Dialog  Close  关闭对话框
     */
    class WordFunction implements GameFunction {

        GameData _d;
        GameHeader _h;

        @Override
        public void fun(GameData data, GameHeader header, FunctionArg arg) {
            _d = data;
            _h = header;
            switch (arg.name) {
                case "Word":
                    if (arg.extra != NONE_EXTRA) {
                        if (arg.value.equals("S")) {
                            SetName("???");
                        } else if (arg.value.equals("M")) {
                            SetName("我");
                        } else {
                            String pid = arg.value;
                            Person p = _d.getPlayedPersons().get(pid);
                            if (p != null) {
                                SetName(p.getName());
                            }
                        }
                        Word(arg.extra[0]);
                    } else {
                        Word(arg.value);
                    }
                    break;
                case "Open":
                    Open();
                    break;
                case "Clear":
                    Clear();
                    break;
                case "Close":
                    Close();
                    break;
            }
        }

        Timeline line;
        String destWords;
        int index = 0, dest;

        /**
         * 将需要显示的文字暂存，修改状态为Displaying，并开启一个定时任务将文字贴到WordPanel上，
         * 定时任务做的内容：
         * 判断文字显示是否完成
         * 如果未完成，则添加下一个文字
         * 如果完成，则将状态修改为WaitPress，并关闭定时任务
         */
        private void Word(String text) {

            index = 0;
            destWords = text;
            dest = destWords.length();

            if (line == null) {
                line = new Timeline();
                line.setCycleCount(Timeline.INDEFINITE);
                Duration period;
                if (_d.isAuto()) {
                    period = Duration.millis(80);
                } else {
                    period = Duration.millis(200);
                }
                KeyFrame keyFrame = new KeyFrame(period, "WordDisplaying", event -> {
                    if (index == dest) {
                        //如果是普通文字显示状态，则切换为等待输入状态
                        //否则为自动响应状态，无需更改状态,无需等待用户反应文字显示，直接等待下一个命令
                        if (!this._d.isAuto()) {
                            this._d.setGameState(GameData.GAME_STATE_WAIT_PRESS);
                        } else {
                            this._d.setGameState(GameData.GAME_STATE_WAIT_NEXT);
                        }
                        line.stop();
                    } else {
                        //继续贴字
                        char c = destWords.charAt(index);
                        this._d.getDisplayWords()[index].setValue(String.valueOf(c));
                        ++index;
                    }
                });
                line.getKeyFrames().add(keyFrame);
            } else {
                line.stop();
            }
            line.play();
        }

        private void SetName(String name) {
            _d.nameDisplayProperty().setValue(name);
        }

        private void Open() {
            _d.wordLineShowProperty().set(true);
            _d.nameShowProperty().set(true);
        }

        private void Clear() {
            _d.nameDisplayProperty().setValue(" ");
            for (StringProperty s : _d.getDisplayWords()) {
                s.setValue(" ");
            }
        }

        private void Close() {
            _d.wordLineShowProperty().set(false);
            _d.nameShowProperty().set(false);
        }
    }

    //  - Person  In  #DataId 人物进入场景缓存
    //  - Person  Out  #DataId 人物换出场景缓存
    //  - Person  Show  #Position(L/R)  #DataId 在左边、右边显示人物
    //  - Person  Hide  #Position(L/R) 隐藏左边、右边
    //  - Person  Change.State  #Position(L/R)  #newState 改变左边、右边人物的状态
    class PersonFunction implements GameFunction {

        @Override
        public void fun(GameData data, GameHeader header, FunctionArg arg) {
            switch (arg.name) {
                case "In" -> In(data, arg.value);
                case "Out" -> Out(data, arg.value);
                case "Show" -> Show(data, header, arg.value, arg.extra[0]);
                case "Hide" -> Hide(data, header, arg.value);
                case "Change.State" -> ChangeState(data, arg.value, arg.extra[0]);
            }
        }

        //导入一个人物到
        private void In(GameData d, String pid) {
            if (d.getPlayedPersons().containsKey(pid)) return;

            Person.PersonData pd = d.getPersonDataMap().get(pid);
            String[] states = pd.state();
            if (states == null) return;

            Map<String, Image> img = new HashMap<>();
            for (String s : states) {
                Image i = ConfigCenter.loadPersonState(pid, s, "png");
                img.put(s, i);
            }

            Person p = new Person(pd.id(), pd.name(), pd.defaultState(), img);
            d.getPlayedPersons().put(p.getId(), p);
        }

        private void Out(GameData d, String pid) {

            if (!d.getPlayedPersons().containsKey(pid)) return;

            d.getPlayedPersons().remove(pid);
        }

        private void Show(GameData d, GameHeader h, String pos, String pid) {
            Person p = d.getPlayedPersons().get(pid);
            if (p == null) return;

            switch (pos) {
                case "L" -> {
                    d.setLeftPerson(p);
                    d.leftPersonImageProperty().set(p.getNowStateImage());
                    h.getLeftPerson().setVisible(true);
                }
                case "C" -> {
                    d.setCenterPerson(p);
                    d.centerPersonImageProperty().set(p.getNowStateImage());
                    h.getCenterPerson().setVisible(true);
                }
                case "R" -> {
                    d.setRightPerson(p);
                    d.rightPersonImageProperty().set(p.getNowStateImage());
                    h.getRightPerson().setVisible(true);
                }
            }
        }

        private void Hide(GameData d, GameHeader h, String pos) {
            switch (pos) {
                case "L" -> h.getLeftPerson().setVisible(false);
                case "C" -> h.getCenterPerson().setVisible(false);
                case "R" -> h.getRightPerson().setVisible(false);
            }
        }

        private void ChangeState(GameData d, String pos, String newState) {
            Person p;
            switch (pos) {
                case "L" -> {
                    p = d.getLeftPerson();
                    p.changeStateTo(newState);
                    d.leftPersonImageProperty().set(p.getNowStateImage());
                }
                case "C" -> {
                    p = d.getCenterPerson();
                    p.changeStateTo(newState);
                    d.centerPersonImageProperty().set(p.getNowStateImage());
                }
                case "R" -> {
                    p = d.getRightPerson();
                    p.changeStateTo(newState);
                    d.rightPersonImageProperty().set(p.getNowStateImage());
                }
            }

        }
    }

    //  - Storage  Save#Name  #Value 保存为某个值
    //  - Storage  Plus#Name  #Value 增加一个值
    //  - Storage  Minus#Name  #Value 减少一个值
    class StoreFunction implements GameFunction {

        @Override
        public void fun(GameData data, GameHeader header, FunctionArg arg) {
            switch (arg.name) {
                case "Save" -> Save(data, arg.name, Integer.parseInt(arg.value));
                case "Plus" -> Plus(data, arg.name, Integer.parseInt(arg.value));
                case "Minus" -> Minus(data, arg.name, Integer.parseInt(arg.value));
            }
        }

        private void Save(GameData d, String name, Integer v) {
            d.getData().put(name, v);
        }

        private void Plus(GameData d, String name, Integer v) {
            Integer de = d.getData().get(name);
            if (de != null) {
                d.getData().put(name, de + v);
            }
        }

        private void Minus(GameData d, String name, Integer v) {
            Integer de = d.getData().get(name);
            if (de != null) {
                d.getData().put(name, de - v);
            }
        }
    }


    //  - Audio  Bgm.Play  #BgmName
    //  - Audio  Bgm.Pause
    //  - Audio  Bgm.Resume
    //  - Audio  Bgm.Stop
    //
    //  - Audio  Sound.Play #SoundName
    class AudioFunction implements GameFunction {

        @Override
        public void fun(GameData data, GameHeader header, FunctionArg arg) {
            switch (arg.name) {
                case "Bgm.Play" -> BgmPlay(header, arg.value);
                case "Bgm.Pause" -> BgmPause(header);
                case "Bgm.Resume" -> BgmResume(header);
                case "Bgm.Stop" -> BgmStop(header);
                case "Sound.Play" -> AudioPlay(arg.value);
            }
        }

        private void BgmPlay(GameHeader h, String name) {
            MediaView mediaView = h.getMediaView();
            MediaPlayer mediaPlayer;
            if (mediaView.getMediaPlayer() != null) {
                mediaPlayer = mediaView.getMediaPlayer();
                mediaPlayer.stop();
            }
            Media media = ConfigCenter.loadBgm(name);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
        }

        private void BgmPause(GameHeader h) {
            MediaView mediaView = h.getMediaView();
            MediaPlayer mediaPlayer;
            if (mediaView.getMediaPlayer() != null) {
                mediaPlayer = mediaView.getMediaPlayer();
                Duration lastTime = mediaPlayer.getCurrentTime();
                mediaView.setUserData(lastTime);
                mediaPlayer.stop();
            }
        }

        private void BgmResume(GameHeader h) {
            MediaView mediaView = h.getMediaView();
            MediaPlayer mediaPlayer;
            if (mediaView.getMediaPlayer() != null) {
                mediaPlayer = mediaView.getMediaPlayer();
                Duration lastTime = (Duration) mediaView.getUserData();
                mediaPlayer.seek(lastTime);
                mediaPlayer.play();
            }
        }

        private void BgmStop(GameHeader h) {
            MediaView mediaView = h.getMediaView();
            MediaPlayer mediaPlayer;
            if (mediaView.getMediaPlayer() != null) {
                mediaPlayer = mediaView.getMediaPlayer();
                mediaPlayer.stop();
            }
            mediaView.setMediaPlayer(null);
        }

        private void AudioPlay(String name) {
            AudioClip clip = ConfigCenter.loadAudio(name);
            clip.play();
        }
    }


    //  - Select  #QuestionId  #Question  #[Option1,Option2,Option3]  选择问题保存Id
    class SelectFunction implements GameFunction {
        @Override
        public void fun(GameData data, GameHeader header, FunctionArg arg) {

            //应当跳出选择弹窗在正中央
            System.out.println("QuestionId:" + arg.name);
            System.out.println("Question:" + arg.value);
            for (int i = 0; i != arg.extra.length; ++i) {
                String s = arg.extra[i];
                System.out.println("[" + i + "]:" + s);
            }
        }
    }

    class ViewFunction implements GameFunction {

        @Override
        public void fun(GameData data, GameHeader header, FunctionArg arg) {

        }
    }
}