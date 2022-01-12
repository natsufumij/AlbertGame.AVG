package albertgame.avg.editor;

import java.util.List;

public class PlayCommand {

    interface Handler {
        void loadCommands(String playId,List<String> expressions);

        void create();

        void edit();

        void remove();

        void enter();

        void save();

        String playId();

        List<PlayCommand> allCommands();
    }

    String type;
    String name;
    String[] data;

    public PlayCommand(String type,
                       String name, String[] data) {
        this.type = type;
        this.name = name;
        this.data = data;
    }

    public String toWord(){
        String s=type+"  "+name+"\n";
        if(data!=null){
            for(String x:data){
                s+=x+"  ";
            }
        }
        s=s.strip();
        return s;
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

    public static PlayCommand transfer(String expression){
        expression=expression.replace("\n","  ");
        String[] strings=expression.split(" {2}");
        if(strings.length==2){
            return new PlayCommand(strings[0],strings[1],new String[0]);
        }
        else if(strings.length>2){
            String t=strings[0];
            String n=strings[1];
            String[] data=new String[strings.length-2];
            System.arraycopy(strings,2,data,0,strings.length-2);
            return new PlayCommand(t,n,data);
        }else {
            System.err.println("Invalid Expression: "+expression);
            return null;
        }
    }
}
