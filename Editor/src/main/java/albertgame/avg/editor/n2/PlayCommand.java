package albertgame.avg.editor.n2;

import java.util.List;

public class PlayCommand {

    interface Handler {
        void loadCommands(List<String> expressions);

        void create();

        void edit();

        void remove();

        List<PlayCommand> allCommands();
    }

    final int index;
    String type;
    String name;
    String[] data;

    public PlayCommand(int index, String type,
                       String name, String[] data) {
        this.index = index;
        this.type = type;
        this.name = name;
        this.data = data;
    }

    //Dialog  Word  M  XXX -> @M  XXXX
    //Dialog  Word  DataId  XXX - > @DataId  XXXX
    //Dialog  Pound  XXX  ->  XXXX
    //Audio  Bgm.Play  XXX -> [Audio  Bgm.Play  XXX]
    public String convert() {
        if (type.equals("Dialog")) {
            if (name.equals("Word")) {
                return '@' + data[0] + "  " + data[1];
            } else if (name.equals("Pound")) {
                return data[0];
            }
        }

        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(type).append("  ");
        builder.append(name).append("  ");
        if (data != null) {
            for (String s : data) {
                builder.append(s).append("  ");
            }
        }
        builder.delete(builder.length() - 2, builder.length());
        builder.append("]");
        return builder.toString();
    }
}
