package albertgame.avg.content;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public interface GameFunction {

    String[] NONE_EXTRA = null;

    record FunctionArg(String type, String name, String value, String[] extra) {
    }

    void fun(GameData data, GameHeader header, FunctionArg arg);

    /**
     * - Dialog  Word  #Text  旁白文字
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
                        Clear();
                        Word(arg.extra[0]);
                    } else {
                        SetName("");
                        Clear();
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
                    period = Duration.millis(30);
                } else {
                    period = Duration.millis(100);
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

            this._d.setGameState(GameData.GAME_STATE_WORD_DISPLAYING);
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
    //  - Person  No.Show  #Position(L/C/R)  去掉显示的人物位置
    //  - Person  Hide  #Position(L/R) 隐藏左边、右边
    //  - Person  Change.State  #Position(L/R)  #newState 改变左边、右边人物的状态
    class PersonFunction implements GameFunction {

        @Override
        public void fun(GameData data, GameHeader header, FunctionArg arg) {
            switch (arg.name) {
                case "In" -> In(data, arg.value);
                case "Out" -> Out(data, arg.value);
                case "Show" -> Show(data, header, arg.value, arg.extra[0]);
                case "No.Show"->NoShow(data,header,arg.value);
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
                Image i = ConfigCenter.loadPersonState(pid, s);
                img.put(s, i);
            }

            Person p = new Person(pd.id(), pd.name(), pd.defaultState(), img);
            d.getPlayedPersons().put(p.getId(), p);
        }

        private void Out(GameData d, String pid) {

            if (!d.getPlayedPersons().containsKey(pid)) return;

            d.getPlayedPersons().remove(pid);
        }

        private void NoShow(GameData d, GameHeader h, String pos){
            switch (pos) {
                case "L" -> {
                    d.setLeftPerson(null);
                    d.leftPersonImageProperty().set(null);
                    h.getLeftPerson().setVisible(true);
                }
                case "C" -> {
                    d.setCenterPerson(null);
                    d.centerPersonImageProperty().set(null);
                    h.getCenterPerson().setVisible(true);
                }
                case "R" -> {
                    d.setRightPerson(null);
                    d.rightPersonImageProperty().set(null);
                    h.getRightPerson().setVisible(true);
                }
            }
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
            final String FAILED="ChangeState Failed: Don't Change On A Null Person";
            switch (pos) {
                case "L" -> {
                    p = d.getLeftPerson();
                    if(p==null){
                        System.out.println(FAILED);
                        return;
                    }
                    p.changeStateTo(newState);
                    d.leftPersonImageProperty().set(p.getNowStateImage());
                }
                case "C" -> {
                    p = d.getCenterPerson();
                    if(p==null){
                        System.out.println(FAILED);
                        return;
                    }
                    p.changeStateTo(newState);
                    d.centerPersonImageProperty().set(p.getNowStateImage());
                }
                case "R" -> {
                    p = d.getRightPerson();
                    if(p==null){
                        System.out.println(FAILED);
                        return;
                    }
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

    //  - Select  Go  #SelectId  #[Option1,Option2,Option3]  选择问题保存Id
    class SelectFunction implements GameFunction {
        @Override
        public void fun(GameData data, GameHeader header, FunctionArg arg) {
            data.setNowSelectId(arg.value);
            for (int i = 0; i != arg.extra.length; ++i) {
                String s = arg.extra[i];
                Text t=header.getSelectText()[i];
                t.textProperty().set((i+1)+". "+s);
                t.setVisible(true);
            }
            data.setGameState(GameData.GAME_STATE_SELECTING);
        }
    }

    //- 界面
    //  - View  Scene  #Name 更换场景图片
    //  - View  Shake  #L/C/R 抖动人物
    //  - View  Darking  #3000(ms)  渐黑
    //  - View  Lighting  #3000(s)  渐亮
    class ViewFunction implements GameFunction {

        GameHeader header;
        GameData data;

        @Override
        public void fun(GameData data, GameHeader header, FunctionArg arg) {
            this.header = header;
            this.data = data;
            switch (arg.name) {
                case "Scene" -> Scene(arg.value);
                case "Shake" -> Shake(arg.value);
                case "Darking" -> Darking(Integer.parseInt(arg.value));
                case "Lighting" -> Lighting(Integer.parseInt(arg.value));
            }
        }

        private void Scene(String newSceneName) {
            Image scene = ConfigCenter.loadScene(newSceneName);
            data.backgroundImageProperty().set(scene);
        }

        int leftCount;
        ImageView view;
        Timeline shakeline;
        double rb, lb;
        double px;
        boolean right;

        private void Shake(String pos) {

            switch (pos) {
                case "L":
                    view = header.getLeftPerson();
                    break;
                case "C":
                    view = header.getCenterPerson();
                    break;
                case "R":
                    view = header.getRightPerson();
                    break;
                default:
                    return;
            }

            px = view.getTranslateX();
            rb = px + view.getFitWidth() / 20;
            lb = px - view.getFitWidth() / 20;
            right = true;
            shakeline = new Timeline();
            shakeline.setCycleCount(Timeline.INDEFINITE);
            leftCount = 0;
            KeyFrame frame = new KeyFrame(Duration.millis(10), event -> {
                double nowX = view.getTranslateX();
                //最后返回原位的动作
                if (leftCount == 3) {
                    if (nowX < px) {
                        nowX += 2.5;
                    } else {
                        //全部动作结束
                        shakeline.stop();
                        nowX = px;
                    }
                } else {
                    //来回移动的动作
                    if (right) {
                        if (nowX >= rb) {
                            right = false;
                        } else {
                            nowX += 2.5;
                        }
                    } else {
                        if (nowX <= lb) {
                            //碰到左边的壁了
                            right = true;
                            ++leftCount;
                        } else {
                            nowX -= 2.5;
                        }
                    }
                }
                view.setTranslateX(nowX);
            });
            shakeline.getKeyFrames().add(frame);
            shakeline.setOnFinished(event -> {
                data.setGameState(GameData.GAME_STATE_WAIT_NEXT);
                header.removeGlobalMask();
            });

            data.setGameState(GameData.GAME_STATE_ANIMATING);
            header.addGlobalMask();
            shakeline.play();
        }

        int dest;
        int count;

        private void Darking(Integer seconds) {
            dest = seconds / 50;
            count = 0;
            Timeline timeline = new Timeline();
            KeyFrame frame = new KeyFrame(Duration.millis(50), event -> {
                ++count;
                header.getGlobalMask().setFill(Color.color(0, 0, 0, (count / (double) dest)));
            });
            timeline.getKeyFrames().add(frame);
            timeline.setCycleCount(dest);
            timeline.setOnFinished(event -> {
                data.setGameState(GameData.GAME_STATE_WAIT_NEXT);
            });

            data.setGameState(GameData.GAME_STATE_ANIMATING);
            header.addGlobalMask();

            timeline.play();
        }

        private void Lighting(Integer seconds) {
            dest = seconds / 50;
            count = 0;
            Timeline timeline = new Timeline();
            KeyFrame frame = new KeyFrame(Duration.millis(50), event -> {
                ++count;
                header.getGlobalMask().setFill(Color.color(0, 0, 0, (1 - (double) count / (double) dest)));
            });
            timeline.getKeyFrames().add(frame);
            timeline.setCycleCount(dest);
            timeline.setOnFinished(event -> {
                data.setGameState(GameData.GAME_STATE_WAIT_NEXT);
                header.removeGlobalMask();
            });

            data.setGameState(GameData.GAME_STATE_ANIMATING);
            header.addGlobalMask();
            timeline.play();
        }
    }
}