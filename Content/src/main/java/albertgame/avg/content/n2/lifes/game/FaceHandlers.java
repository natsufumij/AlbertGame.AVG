package albertgame.avg.content.n2.lifes.game;

import albertgame.avg.content.ConfigCenter;
import albertgame.avg.content.Person;
import albertgame.avg.content.n2.FaceData;
import albertgame.avg.content.n2.FaceHandler;
import albertgame.avg.content.n2.FaceHead;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public interface FaceHandlers {

    /**
     * - Dialog  Word  #Text  旁白文字
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
                    if (arg.data().length==2) {
                        if (arg.data()[0].equals("S")) {
                            type="S";
                            _setName(data,"???");
                        } else if (arg.data()[0].equals("M")) {
                            type="M";
                            _setName(data,"我");
                        } else {
                            String pid = arg.data()[0];
                            type="@"+pid;
                            Person p = ((Map<String, Person>)
                                    (data.getObj("playedPersons"))).get(pid);
                            if (p != null) {
                                _setName(data,p.getName());
                            }
                        }
                        Clear(data);
                        Word(data,arg.data()[1]);
                    } else {
                        _setName(data,"");
                        type="P";
                        Clear(data);
                        Word(data,arg.data()[0]);
                    }
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

        Timeline line;
        String destWords;
        int index = 0, dest;
        String type;

        private void Word(FaceData d, String text) {

            index=0;
            destWords=text;
            dest=destWords.length();

            if (line == null) {
                line = new Timeline();
                line.setCycleCount(Timeline.INDEFINITE);
                Duration period;
                if (d.boolPro("auto").get()) {
                    period = Duration.millis(30);
                } else {
                    period = Duration.millis(100);
                }
                KeyFrame keyFrame = new KeyFrame(period, "WordDisplaying", event -> {
                    if (index == dest) {
                        //如果是普通文字显示状态，则切换为等待输入状态
                        //否则为自动响应状态，无需更改状态,无需等待用户反应文字显示，直接等待下一个命令
                        if (!d.boolPro("auto").get()) {
                            d.intPro("gameState").set(GameFaceLife.GAME_STATE_WAIT_PRESS);
                        } else {
                            d.intPro("gameState").set(GameFaceLife.GAME_STATE_WAIT_NEXT);
                        }
                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i != ConfigCenter.WORD_MAX_SIZE; ++i) {
                            int cx = i / ConfigCenter.WORD_LINE_COLUMN;
                            int cy = i % ConfigCenter.WORD_LINE_COLUMN;
                            builder.append(d.strPro(GameFaceLife.findWordAt(cx, cy)).get());
                        }
                        d.property("cache").put("word", builder.toString());
                        d.property("cache").put("wordtype", type);
                        line.stop();
                    } else {
                        //继续贴字
                        char c = destWords.charAt(index);
                        int cx = index / ConfigCenter.WORD_LINE_COLUMN;
                        int cy = index % ConfigCenter.WORD_LINE_COLUMN;
                        d.strPro(GameFaceLife.findWordAt(cx, cy)).setValue(String.valueOf(c));
                        ++index;
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
    class PersonHandle implements FaceHandler{

        @Override
        public void handle(FaceData data, FaceHead head, Arg arg) {

        }
        //导入一个人物到
        private void In(FaceData d, String pid) {
            Map<String,Person> playedPersons= (Map<String, Person>) d.getObj("playedPersons");
            if (playedPersons.containsKey(pid)) return;

            Map<String, Person.PersonData> personDataMap=
                    (Map<String, Person.PersonData>) d.getObj("personDataMap");
            Person.PersonData pd = personDataMap.get(pid);
            String[] states = pd.state().toArray(new String[0]);
            if (states == null) return;

            Map<String, Image> img = new HashMap<>();
            for (String s : states) {
                Image i = ConfigCenter.loadPersonState(pid, s);
                img.put(s, i);
            }

            Person p = new Person(pd.id(), pd.name(), pd.defaultState(), img);
            playedPersons.put(p.getId(), p);
            refreshPersonIn(d);
        }
        private void refreshPersonIn(FaceData d){
            StringBuilder builder=new StringBuilder();
            builder.append(",");
            Map<String,Person> playedPersons= (Map<String, Person>) d.getObj("playedPersons");
            playedPersons.keySet().forEach(id->builder.append(id).append(","));
            builder.deleteCharAt(0);
            builder.deleteCharAt(builder.length()-1);
            d.property("cache").put("personin",builder.toString());
        }

        private void Out(FaceData d, String pid) {
            Map<String,Person> playedPersons= (Map<String, Person>) d.getObj("playedPersons");

            if (!playedPersons.containsKey(pid)) return;

            playedPersons.remove(pid);
            refreshPersonIn(d);
        }
        private void NoShow(FaceData d, FaceHead h, String pos) {
            switch (pos) {
                case "L" -> {
                    d.strPro("leftPerson").set("");
                    ((ImageView)h.fetch("leftPerson")).setImage(null);
                    d.property("cache").put("leftp","");
                }
                case "C" -> {
                    d.strPro("centerPerson").set("");
                    ((ImageView)h.fetch("centerPerson")).setImage(null);
                    d.property("cache").put("centerp","");
                }
                case "R" -> {
                    d.strPro("rightPerson").set("");
                    ((ImageView)h.fetch("rightPerson")).setImage(null);
                    d.property("cache").put("rightp","");
                }
            }
        }

        private void Show(FaceData d, FaceHead h,  String pos, String pid) {
            Map<String,Person> playedPersons= (Map<String, Person>) d.getObj("playedPersons");
            Person p = playedPersons.get(pid);
            if (p == null) return;

            switch (pos) {
                case "L" -> {
                    d.strPro("leftPerson").set(p.getId());
                    ImageView v= (ImageView) h.fetch("leftPerson");
                    v.setImage(p.getNowStateImage());
                    v.setVisible(true);
                    d.property("cache").put("leftp",p.getId());
                }
                case "C" -> {
                    d.strPro("centerPerson").set(p.getId());
                    ImageView v= (ImageView) h.fetch("centerPerson");
                    v.setImage(p.getNowStateImage());
                    v.setVisible(true);
                    d.property("cache").put("centerp",p.getId());
                }
                case "R" -> {
                    d.strPro("rightPerson").set(p.getId());
                    ImageView v= (ImageView) h.fetch("rightPerson");
                    v.setImage(p.getNowStateImage());
                    v.setVisible(true);
                    d.property("cache").put("rightp",p.getId());
                }
            }
        }

        private void Hide(FaceData d, FaceHead h, String pos) {
            String v="L";
            switch (pos) {
                case "L" -> v="leftPerson";
                case "C" -> v="centerPerson";
                case "R" -> v="rightPerson";
            }
            ImageView view=(ImageView) h.fetch(v);
            view.setVisible(false);
        }
        private void ChangeState(FaceData d, FaceHead head,String pos, String newState) {
            Person p;
            final String FAILED = "ChangeState Failed: Don't Change On A Null Person";
            switch (pos) {
                case "L" -> {
                    p = (Person) d.getObj("leftPerson");
                    if (p == null) {
                        System.out.println(FAILED);
                        return;
                    }
                    p.changeStateTo(newState);
                    ((ImageView)head.fetch("leftPerson")).setImage(p.getNowStateImage());
                }
                case "C" -> {
                    p = (Person) d.getObj("centerPerson");
                    if (p == null) {
                        System.out.println(FAILED);
                        return;
                    }
                    p.changeStateTo(newState);
                    ((ImageView)head.fetch("centerPerson")).setImage(p.getNowStateImage());
                }
                case "R" -> {
                    p = (Person) d.getObj("rightPerson");
                    if (p == null) {
                        System.out.println(FAILED);
                        return;
                    }
                    p.changeStateTo(newState);
                    ((ImageView)head.fetch("rightPerson")).setImage(p.getNowStateImage());
                }
            }
        }
    }

    //TODO: 还有很多功能需要搬运
}
