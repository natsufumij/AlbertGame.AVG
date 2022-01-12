package albertgame.avg.content;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public interface FaceHandlers {

    /**
     * - Dialog  Pound  #Text  旁白文字
     * - Dialog  Word  S  #Text  对话文字，使用名称？？？，以后都如此
     * - Dialog  Word  M  #Text  对话文字，以‘我’为名称，以后都如此
     * - Dialog  Word  DataId  #Text  对话文字,以id的人物名称，以后都如此
     * - Dialog  Open  打开对话框
     * - Dialog  Clear  清空对话框及名称
     * - Dialog  Close  关闭对话框
     */
    class DialogHandler implements FaceHandler {
        @Override
        public void handle(FaceData data, FaceHead head, Arg arg) {
            switch (arg.name()) {
                case "Word":
                    if (arg.data()[0].equals("S")) {
                        type = "S";
                        _setName(data, "???");
                    } else if (arg.data()[0].equals("M")) {
                        type = "M";
                        _setName(data, "我");
                    } else {
                        String pid = arg.data()[0];
                        type = "@" + pid;
                        Person p = GameFaceLife.playedPersons.get(pid);
                        if (p != null) {
                            _setName(data, p.getName());
                        }
                    }
                    Clear(data);
                    Word(data, arg.data()[1]);
                    break;
                case "Pound":
                    _setName(data, "");
                    type = "P";
                    Clear(data);
                    Word(data, arg.data()[0]);
                    break;
                case "Open":
                    Open(data);
                    break;
                case "Clear":
                    Clear(data);
                    break;
                case "Close":
                    Close(data);
                    break;
            }
        }

        private static Timeline line;
        private static String destWords;

        int index = 0, dest;
        int ix, iy;
        String type;
        static FaceData _da;

        private void skipWord(FaceData d, String text) {
            Clear(d);
            int ix=0,iy=0;
            for(int i=0;i!=text.length();++i){
                //到尽头了
                if(iy==ConfigCenter.WORD_LINE_COLUMN){
                    ++ix;
                    iy=0;
                    char c=text.charAt(i);
                    _da.strPro(GameFaceLife.findWordAt(ix,iy)).set(c+"");
                }else{
                    char c=text.charAt(i);
                    if(c=='\\'){
                        ++ix;
                        iy=0;
                    }else {
                        _da.strPro(GameFaceLife.findWordAt(ix,iy)).set(c+"");
                        ++iy;
                    }
                }
            }
            d.property("cache").put("word", text);
            d.property("cache").put("wordtype", type);
        }


        //在Word显示中，取消动画，一下子全部显示
        public static void shiftWord(){
            if(line!=null && !_da.boolPro("auto").get()){
                line.stop();
                int ix=0,iy=0;
                for(int i=0;i!=destWords.length();++i){
                    //到尽头了
                    if(iy==ConfigCenter.WORD_LINE_COLUMN){
                        ++ix;
                        iy=0;
                        char c=destWords.charAt(i);
                        _da.strPro(GameFaceLife.findWordAt(ix,iy)).set(c+"");
                    }else{
                        char c=destWords.charAt(i);
                        if(c=='\\'){
                            ++ix;
                            iy=0;
                        }else {
                            _da.strPro(GameFaceLife.findWordAt(ix,iy)).set(c+"");
                            ++iy;
                        }
                    }
                }
                _da.intPro("gameState").set(GameFaceLife.GAME_STATE_WAIT_PRESS);
            }
        }

        //如果有\号，表示下面的需要换一行显示.
        private void Word(FaceData d, String text) {
            _da=d;
            if (d.boolPro("skip").get()) {
                skipWord(d, text);
                return;
            }

            index = 0;
            destWords = text;
            dest = destWords.length();
            ix = iy = 0;

            if (line == null) {
                line = new Timeline();
                line.setCycleCount(Timeline.INDEFINITE);
                Duration period=Duration.millis(100);
                KeyFrame keyFrame = new KeyFrame(period, "WordDisplaying", event -> {
                    if (index == dest || ix >= ConfigCenter.WORD_LINE_ROW) {
                        //如果是普通文字显示状态，则切换为等待输入状态
                        //否则为自动响应状态，无需更改状态,无需等待用户反应文字显示，直接等待下一个命令
                        if (!d.boolPro("auto").get()) {
                            d.intPro("gameState").set(GameFaceLife.GAME_STATE_WAIT_PRESS);
                        } else {
                            d.intPro("gameState").set(GameFaceLife.GAME_STATE_WAIT_NEXT);
                        }
                        d.property("cache").put("word", text);
                        d.property("cache").put("wordtype", type);
                        line.stop();
                        line=null;
                    } else {
                        //继续贴字
                        char c = destWords.charAt(index);
                        if (c == '\\') {
                            ++ix;
                            iy = 0;
                            ++index;
                        } else if (iy == ConfigCenter.WORD_LINE_COLUMN) {
                            ++ix;
                            iy=0;
                        } else {
                            d.strPro(GameFaceLife.findWordAt(ix, iy)).setValue(String.valueOf(c));
                            ++iy;
                            ++index;
                        }
                    }
                });
                line.getKeyFrames().add(keyFrame);
            } else {
                line.stop();
            }

            d.intPro("gameState").set(GameFaceLife.GAME_STATE_WORD_DISPLAYING);
            line.play();
        }

        void _setName(FaceData data, String name) {
            data.strPro("nameDisplay").setValue(name);
        }

        private void Open(FaceData _d) {
            _d.boolPro("wordPanelShow").set(true);
        }

        private void Clear(FaceData data) {
            for (int i = 0; i != ConfigCenter.WORD_MAX_SIZE; ++i) {
                int cx = i / ConfigCenter.WORD_LINE_COLUMN;
                int cy = i % ConfigCenter.WORD_LINE_COLUMN;
                data.strPro(GameFaceLife.findWordAt(cx, cy)).setValue("");
            }
        }

        private void Close(FaceData _d) {
            _d.boolPro("wordPanelShow").set(false);
        }
    }

    //  - Person  In  #DataId 人物进入场景缓存
    //  - Person  Out  #DataId 人物换出场景缓存
    //  - Person  Show  #Position(L/R)  #DataId 在左边、右边显示人物
    //  - Person  No.Show  #Position(L/C/R)  去掉显示的人物位置
    //  - Person  Hide  #Position(L/R) 隐藏左边、右边
    //  - Person  Change.State  #Position(L/R)  #newState 改变左边、右边人物的状态
    class PersonHandle implements FaceHandler {

        @Override
        public void handle(FaceData data, FaceHead head, Arg arg) {
            switch (arg.name()) {
                case "In":
                    In(data, arg.data()[0]);
                    break;
                case "Out":
                    Out(data, arg.data()[0]);
                    break;
                case "Show":
                    Show(data, head, arg.data()[0], arg.data()[1]);
                    break;
                case "No.Show":
                    NoShow(data, head, arg.data()[0]);
                    break;
                case "Hide":
                    Hide(data, head, arg.data()[0]);
                    break;
                case "Change.State":
                    ChangeState(data, head, arg.data()[0], arg.data()[1]);
                    break;
            }
        }

        //导入一个人物到
        private void In(FaceData d, String pid) {
            Map<String, Person> playedPersons = GameFaceLife.playedPersons;
            if (playedPersons.containsKey(pid)) return;

            Map<String, Person.PersonData> personDataMap = GameFaceLife.personDataMap;
            Person.PersonData pd = personDataMap.get(pid);
            String[] states = pd.state().toArray(new String[0]);
            if (states.length == 0) return;

            Map<String, Image> img = new HashMap<>();
            for (String s : states) {
                Image i = ConfigCenter.loadPersonState(pid, s);
                img.put(s, i);
            }

            Person p = new Person(pd.id(), pd.name(), pd.defaultState(), img);
            playedPersons.put(p.getId(), p);
            refreshPersonIn(d);
        }

        private void refreshPersonIn(FaceData d) {
            StringBuilder builder = new StringBuilder();
            builder.append(",");
            Map<String, Person> playedPersons = GameFaceLife.playedPersons;
            playedPersons.keySet().forEach(id -> builder.append(id).append(","));
            builder.deleteCharAt(0);
            builder.deleteCharAt(builder.length() - 1);
            d.property("cache").put("personin", builder.toString());
        }

        private void Out(FaceData d, String pid) {
            Map<String, Person> playedPersons = GameFaceLife.playedPersons;

            if (!playedPersons.containsKey(pid)) return;

            playedPersons.remove(pid);
            refreshPersonIn(d);
        }

        private void NoShow(FaceData d, FaceHead h, String pos) {
            switch (pos) {
                case "L":
                    d.strPro("leftPerson").set("");
                    ((ImageView) h.fetch("leftPerson")).setImage(null);
                    d.property("cache").put("leftp", "");
                    break;
                case "C":
                    d.strPro("centerPerson").set("");
                    ((ImageView) h.fetch("centerPerson")).setImage(null);
                    d.property("cache").put("centerp", "");
                    break;
                case "R":
                    d.strPro("rightPerson").set("");
                    ((ImageView) h.fetch("rightPerson")).setImage(null);
                    d.property("cache").put("rightp", "");
                    break;
            }
        }

        private void Show(FaceData d, FaceHead h, String pos, String pid) {
            Map<String, Person> playedPersons = GameFaceLife.playedPersons;
            Person p = playedPersons.get(pid);
            if (p == null) return;
            ImageView v;

            switch (pos) {
                case "L":
                    d.strPro("leftPerson").set(p.getId());
                    v = (ImageView) h.fetch("leftPerson");
                    v.setImage(p.getNowStateImage());
                    v.setVisible(true);
                    d.property("cache").put("leftp", p.getId());
                    break;
                case "C":
                    d.strPro("centerPerson").set(p.getId());
                    v = (ImageView) h.fetch("centerPerson");
                    v.setImage(p.getNowStateImage());
                    v.setVisible(true);
                    d.property("cache").put("centerp", p.getId());
                    break;
                case "R":
                    d.strPro("rightPerson").set(p.getId());
                    v = (ImageView) h.fetch("rightPerson");
                    v.setImage(p.getNowStateImage());
                    v.setVisible(true);
                    d.property("cache").put("rightp", p.getId());
                    break;
            }
        }

        private void Hide(FaceData d, FaceHead h, String pos) {
            String v = "L";
            switch (pos) {
                case "L":
                    v = "leftPerson";
                    break;
                case "C":
                    v = "centerPerson";
                    break;
                case "R":
                    v = "rightPerson";
                    break;
            }
            ImageView view = (ImageView) h.fetch(v);
            view.setVisible(false);
        }

        private void ChangeState(FaceData d, FaceHead head, String pos, String newState) {
            Person p;
            switch (pos) {
                case "L":
                    p = GameFaceLife.leftPerson;
                    if (p == null) {
                        return;
                    }
                    p.changeStateTo(newState);
                    d.property("cache").put("leftstate", p.getNowState());
                    ((ImageView) head.fetch("leftPerson")).setImage(p.getNowStateImage());
                    break;
                case "C":
                    p = GameFaceLife.centerPerson;
                    if (p == null) {
                        return;
                    }
                    p.changeStateTo(newState);
                    d.property("cache").put("centerstate", p.getNowState());
                    ((ImageView) head.fetch("centerPerson")).setImage(p.getNowStateImage());
                    break;
                case "R":
                    p = GameFaceLife.rightPerson;
                    if (p == null) {
                        return;
                    }
                    p.changeStateTo(newState);
                    d.property("cache").put("rightstate", p.getNowState());
                    ((ImageView) head.fetch("rightPerson")).setImage(p.getNowStateImage());
                    break;
            }
        }
    }

    //  - Storage  Save#Name  #Value 保存为某个值
    //  - Storage  Plus#Name  #Value 增加一个值
    //  - Storage  Minus#Name  #Value 减少一个值
    class StoreHandler implements FaceHandler {

        @Override
        public void handle(FaceData data, FaceHead head, Arg arg) {
            switch (arg.name()) {
                case "Save":
                    Save(data, arg.name(), arg.data()[0]);
                    break;
                case "Plus":
                    Plus(data, arg.name(), Integer.parseInt(arg.data()[0]));
                    break;
                case "Minus":
                    Minus(data, arg.name(), Integer.parseInt(arg.data()[0]));
                    break;
            }
        }

        private void Save(FaceData data, String name, String v) {
            data.property("selects").put(name, v);
        }

        private void Plus(FaceData data, String name, Integer v) {
            String de = (String) data.property("selects").get(name);
            if (de != null) {
                int dx = Integer.parseInt(de);
                int dd = dx + v;
                data.property("selects").put(name, dd + "");
            }
        }

        private void Minus(FaceData data, String name, Integer v) {
            String de = (String) data.property("selects").get(name);
            if (de != null) {
                int dx = Integer.parseInt(de);
                data.property("selects").put(name, (dx - v) + "");
            }
        }
    }

    //  - Audio  Bgm.Play  #BgmName
    //  - Audio  Bgm.Pause
    //  - Audio  Bgm.Resume
    //  - Audio  Bgm.Stop
    //
    //  - Audio  Sound.Play #SoundName
    class AudioHandler implements FaceHandler {

        @Override
        public void handle(FaceData data, FaceHead head, Arg arg) {
            switch (arg.name()) {
                case "Bgm.Play":
                    BgmPlay(data, arg.data()[0]);
                    break;
                case "Bgm.Pause":
                    BgmPause();
                    break;
                case "Bgm.Resume":
                    BgmResume();
                    break;
                case "Bgm.Stop":
                    BgmStop(data);
                    break;
                case "Sound.Play":
                    AudioPlay(arg.data()[0]);
                    break;
            }
        }

        private void BgmPlay(FaceData data, String name) {
            MediaView mediaView = MainEntry.Controller().getMediaView();
            MediaPlayer mediaPlayer;
            if (mediaView.getMediaPlayer() != null) {
                mediaPlayer = mediaView.getMediaPlayer();
                mediaPlayer.stop();
            }
            data.property("cache").put("bgm", name);
            Media media = ConfigCenter.loadBgm(name);
            assert media != null;
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
        }

        private void BgmPause() {
            MediaView mediaView = MainEntry.Controller().getMediaView();
            MediaPlayer mediaPlayer;
            if (mediaView.getMediaPlayer() != null) {
                mediaPlayer = mediaView.getMediaPlayer();
                mediaPlayer.pause();
            }
        }

        private void BgmResume() {
            MediaView mediaView = MainEntry.Controller().getMediaView();
            MediaPlayer mediaPlayer;
            if (mediaView.getMediaPlayer() != null) {
                mediaPlayer = mediaView.getMediaPlayer();
                mediaPlayer.play();
            }
        }

        private void BgmStop(FaceData data) {
            data.property("cache").put("bgm", "");
            MediaView mediaView = MainEntry.Controller().getMediaView();
            MediaPlayer mediaPlayer;
            if (mediaView.getMediaPlayer() != null) {
                mediaPlayer = mediaView.getMediaPlayer();
                mediaPlayer.stop();
            }
            mediaView.setMediaPlayer(null);
        }

        private void AudioPlay(String name) {
            AudioClip clip = ConfigCenter.loadAudio(name);
            assert clip != null;
            clip.play();
        }
    }

    //  - Select  Go  #SelectId  Option1  Option2  Option3  选择问题保存Id
    class SelectHandler implements FaceHandler {

        @Override
        public void handle(FaceData data, FaceHead head, Arg arg) {
            data.strPro("nowSelectId").set(arg.data()[0]);
            for (int i = 1; i != arg.data().length; ++i) {
                String s = arg.data()[i];
                int dest = i - 1;
                String dex = GameFaceLife.findSelectAt(dest);
                data.strPro(dex).set(s);
                data.boolPro(dex).set(Boolean.TRUE);
            }
            data.intPro("gameState").set(GameFaceLife.GAME_STATE_SELECTING);
        }
    }

    //- 界面
    //  - View  Scene  #Name 更换场景图片
    //  - View  Shake  #L/C/R 抖动人物
    //  - View  Darking  #3000(ms)  渐黑
    //  - View  Lighting  #3000(s)  渐亮
    class ViewHandler implements FaceHandler {

        @Override
        public void handle(FaceData data, FaceHead head, Arg arg) {
            switch (arg.name()) {
                case "Scene":
                    Scene(data, head, arg.data()[0]);
                    break;
                case "Shake":
                    Shake(data, head, arg.data()[0]);
                    break;
                case "Darking":
                    Darking(data, head, Integer.parseInt(arg.data()[0]));
                    break;
                case "Lighting":
                    Lighting(data, head, Integer.parseInt(arg.data()[0]));
                    break;
            }
        }

        private void Scene(FaceData data, FaceHead head, String newSceneName) {
            Image scene = ConfigCenter.loadScene(newSceneName);
            ImageView img = (ImageView) head.fetch("scene");
            img.setImage(scene);
            data.property("cache").put("scene", newSceneName);
        }

        int leftCount;
        ImageView view;
        Timeline shakeLine;
        double rb, lb;
        double px;
        boolean right;

        private void Shake(FaceData data, FaceHead head, String pos) {

            switch (pos) {
                case "L":
                    view = (ImageView) head.fetch("leftPerson");
                    break;
                case "C":
                    view = (ImageView) head.fetch("centerPerson");
                    break;
                case "R":
                    view = (ImageView) head.fetch("rightPerson");
                    break;
                default:
                    return;
            }

            px = view.getTranslateX();
            rb = px + view.getFitWidth() / 20;
            lb = px - view.getFitWidth() / 20;
            right = true;
            shakeLine = new Timeline();
            shakeLine.setCycleCount(Timeline.INDEFINITE);
            leftCount = 0;
            KeyFrame frame = new KeyFrame(Duration.millis(10), event -> {
                double nowX = view.getTranslateX();
                //最后返回原位的动作
                if (leftCount == 3) {
                    if (nowX < px) {
                        nowX += 2.5;
                    } else {
                        //全部动作结束
                        shakeLine.stop();
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

            shakeLine.getKeyFrames().add(frame);
            shakeLine.setOnFinished(event -> {
                data.boolPro("maskShow").set(false);
                data.intPro("gameState").set(GameFaceLife.GAME_STATE_WAIT_NEXT);
                data.property("cache").put("maskShow", "false");
                data.animate(false);
            });

            data.animate(true);
            data.boolPro("globalMask").set(true);
            shakeLine.play();
        }

        int dest;
        int count;

        private void Darking(FaceData data, FaceHead head, Integer seconds) {
            dest = seconds / 50;
            count = 0;
            Timeline timeline = new Timeline();

            final Rectangle mask = (Rectangle) head.fetch("globalMask");
            mask.setFill(Color.color(0, 0, 0, 0.0));
            data.boolPro("maskShow").set(true);

            KeyFrame frame = new KeyFrame(Duration.millis(50), event -> {
                ++count;
                mask.setFill(Color.color(0, 0, 0, (count / (double) dest)));
            });
            timeline.getKeyFrames().add(frame);
            timeline.setCycleCount(dest);
            timeline.setOnFinished(event -> {
                data.intPro("gameState").set(GameFaceLife.GAME_STATE_WAIT_NEXT);
                data.animate(false);
                data.property("cache").put("maskShow", "true");
            });

            data.animate(true);
            timeline.play();
        }

        private void Lighting(FaceData data, FaceHead head, Integer seconds) {
            dest = seconds / 50;
            count = 0;
            Timeline timeline = new Timeline();

            final Rectangle mask = (Rectangle) head.fetch("globalMask");
            mask.setFill(Color.color(0, 0, 0, 1.0));
            data.boolPro("maskShow").set(true);

            KeyFrame frame = new KeyFrame(Duration.millis(50), event -> {
                ++count;
                mask.setFill(Color.color(0, 0, 0, (1 - (double) count / (double) dest)));
            });
            timeline.getKeyFrames().add(frame);
            timeline.setCycleCount(dest);
            timeline.setOnFinished(event -> {
                data.intPro("gameState").set(GameFaceLife.GAME_STATE_WAIT_NEXT);
                data.animate(false);
                data.boolPro("maskShow").set(false);
            });

            data.animate(true);
            timeline.play();
        }
    }
}
