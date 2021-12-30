package albertgame.avg.content;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.StringProperty;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.util.concurrent.Executors;

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
}