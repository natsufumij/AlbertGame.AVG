package albertgame.avg.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Progress {

    interface Handler{

        void loadProgresses(Play.GlobalConfig globalConfig);
        void loadProgresses(Play.Chapter chapter);
        void loadProgresses(Play play);
        void create();
        void edit();
        void remove();

        Map<String,Progress> allProgresses();
        Progress copySelectItem();
    }

    static class Pair{
        String destId;
        String expression;

        public Pair(String destId, String expression) {
            this.destId = destId;
            this.expression = expression;
        }
    }

    final String id;
    String name;
    String optionId;
    List<Pair> optionPairs;

    public Progress(String id, String name, String optionId) {
        this.id = id;
        this.name = name;
        this.optionId = optionId;
        optionPairs=new ArrayList<>();
    }

    public Play.OptionStruck convert(){
        String id=this.id;
        String[] destId=new String[optionPairs.size()];
        String[] expressions=new String[optionPairs.size()];
        for(int i=0;i!=optionPairs.size();++i){
            Pair pair=optionPairs.get(i);
            destId[i]=pair.destId;
            expressions[i]=pair.expression;
        }
        return new Play.OptionStruck(id,expressions,destId);
    }

    public Progress copy(){
        List<Pair> pairs=new ArrayList<>();
        for(Pair p:optionPairs){
            pairs.add(new Pair(p.destId,p.expression));
        }
        Progress pg=new Progress(id,name,optionId);
        pg.optionPairs=pairs;
        return pg;
    }
}
