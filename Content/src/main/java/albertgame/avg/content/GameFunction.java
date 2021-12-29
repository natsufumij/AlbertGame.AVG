package albertgame.avg.content;

import javafx.beans.property.StringProperty;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public interface GameFunction {

    record FunctionArg(String type, String name, String value, String[] extra) {
    }

    void fun(GameData data, GameHeader header, FunctionArg arg);

    /**
     * - Dialog  Word  #Text  默认文字,延续上一次对话的名称
     * - Dialog  Word  [S]  #Text  对话文字，使用名称？？？，以后都如此
     * - Dialog  Word  [M]  #Text  对话文字，以‘我’为名称，以后都如此
     * - Dialog  Word  [DataId]  #Text  对话文字,以id的人物名称，以后都如此
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
        }


        private static class WordDisplayExecution extends ScheduledService<Void> {

            GameData data;
            String destWords;
            int index;
            int dest;

            public WordDisplayExecution(GameData data, String destWords) {
                reset(data, destWords);
            }

            void reset(GameData data, String destWords) {
                this.data = data;
                this.destWords = destWords;
                clearWord();
                index = 0;
                dest = destWords.length();
            }

            private void clearWord() {
                for (StringProperty s : data.getDisplayWords()) {
                    s.setValue(" ");
                }
            }

            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
                        if (index == dest) {

                            //如果是普通文字显示状态，则切换为等待输入状态
                            if (data.getGameState() == GameData.GAME_STATE_WORD_DISPLAYING) {
                                data.setGameState(GameData.GAME_STATE_WAIT_PRESS);
                            }
                            //否则为自动响应状态，无需更改状态,无需等待用户反应文字显示，直到出现选择分支

                            WordDisplayExecution.super.succeeded();
                        } else {
                            //继续贴字
                            char c = destWords.charAt(index);
                            data.getDisplayWords()[index].setValue(String.valueOf(c));
                            ++index;
                        }

                        super.succeeded();
                        return null;
                    }
                };
            }
        }


        //文字显示定时任务
        WordDisplayExecution execution;

        /**
         * 将需要显示的文字暂存，修改状态为Displaying，并开启一个定时任务将文字贴到WordPanel上，
         * 定时任务做的内容：
         * 判断文字显示是否完成
         * 如果未完成，则添加下一个文字
         * 如果完成，则将状态修改为WaitPress，并关闭定时任务
         *
         */
        private void Word(FunctionArg arg) {
            String text = arg.extra[0];

            if (execution == null) {
                execution = new WordDisplayExecution(this._d, text);
                execution.setDelay(Duration.ZERO);
                execution.setExecutor(Executors.newFixedThreadPool(1));
            } else if (execution.isRunning()) {
                execution.cancel();
            }

            Duration period;
            if (_d.getGameState() == GameData.GAME_STATE_AUTO_WORDING) {
                period = Duration.millis(80);
            } else {
                _d.setGameState(GameData.GAME_STATE_WORD_DISPLAYING);
                period = Duration.millis(200);
            }
            execution.setPeriod(period);
            execution.start();
        }

        private void Open() {

        }

        private void Clear() {

        }

        private void Close() {

        }
    }
}