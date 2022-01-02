package albertgame.avg.content;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public record Play
        (String id,
         String name,
         String startStruck,
         Map<String, BodyStruck> bodyStruckMap) {

    public static final Play NONE_PLAY = null;

    public BodyStruck nextBodyStruck(String id, Map<String, String> data) {
        BodyStruck struck = bodyStruckMap.get(id);
        String dis = struck.optionStruck.struckNextOption(data);
        if (Objects.equals(dis, OptionStruck.NONE_ID)) {
            return BodyStruck.NONE_BODY;
        } else {
            return bodyStruckMap.get(dis);
        }
    }

    public record BodyStruck
            (String id,
             List<String> expressions,
             OptionStruck optionStruck) {

        public static final BodyStruck NONE_BODY = null;
    }

    public record OptionStruck
            (String id,
             String[] selectExpression,
             String[] destIds) {

        public static final String NONE_ID = "00";

        public static final OptionStruck NONE_OPTION = new OptionStruck(NONE_ID, null, null);

        //A=2 | D=2 | S=3
        //A>2 & D<2>
        //S>3 & K=1
        //Else
        //检查有哪一个分支符合，如果没有一条符合的话就返回-1，报错
        public String struckNextOption(Map<String, String> data) {
            if (selectExpression == null) return NONE_ID;

            for (int i = 0; i != selectExpression.length; ++i) {
                String s = selectExpression[i];
                if (parseSelectExpression(s, data)) {
                    return destIds[i];
                } else if (s.equals("Else"))
                    return destIds[i];
            }

            return NONE_ID;
        }

        //A=2
        //A>2
        //A<2
        //A!=2
        //A>=2
        //A<=2
        private boolean parseStruck1(String expression, Map<String, String> data) {
            String[] vs;
            Integer d1, d2;
            if (expression.contains("!=")) {
                vs = expression.split("!=");
                d1 = getReal(vs[0], data);
                d2 = getReal(vs[1], data);
                if (Objects.equals(d1, NONE_DATA)
                        || Objects.equals(d2, NONE_DATA)) return false;
                return d1.compareTo(d2) != 0;
            } else if (expression.contains(">=")) {
                vs = expression.split(">=");
                d1 = getReal(vs[0], data);
                d2 = getReal(vs[1], data);
                if (Objects.equals(d1, NONE_DATA)
                        || Objects.equals(d2, NONE_DATA)) return false;
                return d1.compareTo(d2) >= 0;
            } else if (expression.contains("<=")) {
                vs = expression.split("<=");
                d1 = getReal(vs[0], data);
                d2 = getReal(vs[1], data);
                if (Objects.equals(d1, NONE_DATA)
                        || Objects.equals(d2, NONE_DATA)) return false;
                return d1.compareTo(d2) <= 0;
            } else if (expression.contains("=")) {
                vs = expression.split("=");
                d1 = getReal(vs[0], data);
                d2 = getReal(vs[1], data);
                if (Objects.equals(d1, NONE_DATA)
                        || Objects.equals(d2, NONE_DATA)) return false;
                return d1.compareTo(d2) == 0;
            } else if (expression.contains(">")) {
                vs = expression.split(">");
                d1 = getReal(vs[0], data);
                d2 = getReal(vs[1], data);
                if (Objects.equals(d1, NONE_DATA)
                        || Objects.equals(d2, NONE_DATA)) return false;
                return d1.compareTo(d2) > 0;
            } else if (expression.contains("<")) {
                vs = expression.split("<");
                d1 = getReal(vs[0], data);
                d2 = getReal(vs[1], data);
                if (Objects.equals(d1, NONE_DATA)
                        || Objects.equals(d2, NONE_DATA)) return false;
                return d1.compareTo(d2) < 0;
            }
            return false;
        }

        private static final Integer NONE_DATA = -1;

        private Integer getReal(String v, Map<String, String> data) {
            char v10 = v.charAt(0);
            if (v10 >= '0' && v10 <= '9') {
                return Integer.parseInt(v);
            } else {
                String s=data.get(v);
                if(s==null)return -1;
                else return Integer.parseInt(s);
            }
        }

        // A & B & C | D & E | F
        private boolean parseSelectExpression(String expression, Map<String, String> data) {
            String dest = expression.replaceAll("\\s*", "");
            String[] orSplit = dest.split("\\|");
            for (String s : orSplit) {
                if (parseAndEx(s, data)) {
                    return true;
                }
            }
            return false;
        }

        //A & B & C
        private boolean parseAndEx(String expression, Map<String, String> data) {
            String[] exArray = expression.split("&");
            for (String s : exArray) {
                if (!parseStruck1(s, data)) {
                    return false;
                }
            }
            return true;
        }

    }

    public static void main(String[] args) {

        List<BodyNodeH> bodyNodeHS = new ArrayList<>();
        Stack<BodyNodeH> stack = new Stack<>();

        try {
            File file = ConfigCenter.loadFileInClasspath("play/story/chapter0/demo2.avg");
            Play p = loadPlay(file);
            System.out.println("Ok.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static final String END_SIGN = "<<<end>>>";

    private static class BodyNodeH {
        String name;
        List<String> texts = new ArrayList<>();
        List<BodyNodeH> children = new ArrayList<>();
    }

    private static void parseFile(List<BodyNodeH> oneList, Stack<BodyNodeH> stack, String line) {
        if (line.isBlank()) return;

        if (line.startsWith("#")) {
            //#只在栈中存在一次
            while (stack.size() > 1) {
                stack.pop();
            }
            if (!stack.isEmpty()) {
                BodyNodeH b = stack.pop();
                oneList.add(b);
            }

            BodyNodeH one = new BodyNodeH();
            one.name = line.substring(1);
            stack.push(one);
        } else if (line.startsWith(">>")) {
            //维持只有一个#，最多只有一个>>
            while (stack.size() > 1) {
                stack.pop();
            }
            BodyNodeH second = new BodyNodeH();
            second.name = line.substring(2);
            BodyNodeH one = stack.peek();
            one.children.add(second);
            stack.push(second);
        } else if (line.equals(END_SIGN)) {
            while (stack.size() > 1) {
                stack.pop();
            }
            if (!stack.isEmpty()) {
                BodyNodeH b = stack.pop();
                oneList.add(b);
            }
        } else {
            stack.peek().texts.add(line);
        }
    }

    //我是和好的一个人
    //@S  你在说什么?
    //[Person  In  DataId]
    //,这是注释
    public static List<String[]> parseCmd(String s) {
        if (s.startsWith("[")&&s.endsWith("]")) {
            String f = s.substring(1, s.length()-1);
            f = f.strip();
            ArrayList<String[]> arrayList = new ArrayList<>();
            arrayList.add(f.split(" {2}"));
            return arrayList;
        } else if (s.startsWith("@")) {
            String f = s.substring(1);
            f = f.strip();
            String[] x = f.split(" {2}");
            if (x.length != 2) return Collections.emptyList();

            ArrayList<String[]> arrayList = new ArrayList<>();
            int page = x[1].length() / ConfigCenter.WORD_MAX_SIZE;
            ++page;
            int i;
            for (i = 0; i != page - 1; ++i) {
                String w = x[1].substring(
                        i * ConfigCenter.WORD_MAX_SIZE, (i + 1) * ConfigCenter.WORD_MAX_SIZE);
                String[] cmd = new String[]{"Dialog", "Word", x[0], w};
                arrayList.add(cmd);
            }
            String lastPage = x[1].substring(i * ConfigCenter.WORD_MAX_SIZE);
            String[] cmd = new String[]{"Dialog", "Word", x[0], lastPage};
            arrayList.add(cmd);
            return arrayList;
        } else if (!s.startsWith(",")) {
            String f = s.strip();
            ArrayList<String[]> arrayList = new ArrayList<>();
            int page = f.length() / ConfigCenter.WORD_MAX_SIZE;
            ++page;
            int i;
            for (i = 0; i != page - 1; ++i) {
                String w = f.substring(
                        i * ConfigCenter.WORD_MAX_SIZE, (i + 1) * ConfigCenter.WORD_MAX_SIZE);
                String[] cmd = new String[]{"Dialog", "Word", w};
                arrayList.add(cmd);
            }
            String lastPage = f.substring(i * ConfigCenter.WORD_MAX_SIZE);
            String[] cmd = new String[]{"Dialog", "Word", lastPage};
            arrayList.add(cmd);
            return arrayList;
        } else return Collections.emptyList();
    }

    private static List<BodyNodeH> loadBodyNodeH(File file) {
        List<BodyNodeH> bodyNodeHS = new ArrayList<>();
        Stack<BodyNodeH> stack = new Stack<>();

        try {
            if (file.exists() && file.isFile()) {
                InputStreamReader reader;
                reader = new InputStreamReader(new FileInputStream(file),
                        StandardCharsets.UTF_8);
                BufferedReader r = new BufferedReader(reader);
                String line;
                while ((line = r.readLine()) != null) {
                    line = line.strip();
                    if (!line.isBlank()) {
                        parseFile(bodyNodeHS, stack, line);
                    }
                }
                parseFile(bodyNodeHS, stack, END_SIGN);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bodyNodeHS;
    }

    public static Play loadPlay(File file) {
        return loadPlay(loadBodyNodeH(file));
    }

    private static Play loadPlay(List<BodyNodeH> hs) {
        Map<String, OptionStruck> op =null;
        Map<String, BodyStruck> bodyStruckMap = new HashMap<>();
        Map<String, String[]> optionMap = new HashMap<>();
        BodyNodeH bodyN = null;
        BodyNodeH progressN = null;
        String id = "00", name = "00",startStruck = "00";
        for (BodyNodeH b : hs) {
            switch (b.name) {
                case "Info":
                    if (b.texts.size() < 1 ||
                            !b.texts.get(b.texts.size() - 1).equals("Play")) {
                        return Play.NONE_PLAY;
                    }

                    id = b.texts.get(0);
                    name = b.texts.get(1);
                    startStruck = b.texts.get(2);
                    break;
                case "Body":
                    bodyN = b;
                    break;
                case "Options":
                    for (BodyNodeH h : b.children) {
                        optionMap.put(h.name, h.texts.toArray(new String[0]));
                    }
                    break;
                case "Progress":
                    progressN = b;
                    break;
            }
        }

        //生成OptionStruck
        op=loadOptionStruckM(progressN,optionMap);

        //生成Map<String, BodyStruck>
        assert bodyN != null;
        for (BodyNodeH h : bodyN.children) {
            String hid = h.name;
            List<String> exs = h.texts;
            OptionStruck o = op.get(hid);
            BodyStruck struck;
            struck = new BodyStruck(hid, exs, Objects.requireNonNullElse(o, OptionStruck.NONE_OPTION));
            refreshBodyStruck(struck);
            bodyStruckMap.put(hid, struck);
        }

        return new Play(id, name,startStruck, bodyStruckMap);
    }

    //拼接成:Type  Name  Word  EXTRA[0]  EXTRA[1] ...
    private static void refreshBodyStruck(BodyStruck struck) {
        List<String> destList = new ArrayList<>();
        for (String s : struck.expressions) {
            List<String[]> dest = parseCmd(s);
            for (String[] sr : dest) {
                StringBuilder ax = new StringBuilder("  ");
                for (String sd : sr) {
                    ax.append(sd).append("  ");
                }
                ax = new StringBuilder(ax.toString().strip());
                destList.add(ax.toString());
            }
        }
        struck.expressions.clear();
        struck.expressions.addAll(destList);
    }

    public static Chapter loadChapter(File file) {
        Chapter chapter;
        String id = null, name = null, startName = null;

        List<BodyNodeH> list = loadBodyNodeH(file);
        BodyNodeH progressH = null;
        Map<String, String[]> optionExMap = new HashMap<>();

        for (BodyNodeH h : list) {
            if (h.name.equals("Info")) {
                if (h.texts.size() < 1 ||
                        !h.texts.get(h.texts.size() - 1).equals("Chapter")) {
                    return Chapter.NONE_CHAPTER;
                }

                id = h.texts.get(0);
                name = h.texts.get(1);
                startName = h.texts.get(2);
            } else if (h.name.equals("Progress")) {
                progressH = h;
            } else if (h.name.equals("Options")) {
                for(BodyNodeH b:h.children){
                    optionExMap.put(b.name, b.texts.toArray(new String[0]));
                }
            }
        }

        if (id == null || name == null) {
            return Chapter.NONE_CHAPTER;
        }

        Map<String, OptionStruck> optionStruckMap = loadOptionStruckM(progressH,optionExMap);

        chapter = new Chapter(id, name, startName, optionStruckMap);
        return chapter;
    }

    public static GlobalConfig loadGlobalConfig(File file){

        List<BodyNodeH> bodyNodeHS=loadBodyNodeH(file);
        Map<String,String[]> optionExpressionMap=new HashMap<>();
        BodyNodeH progressH=null;
        Map<String,GlobalConfig.PersonConfig> personConfigMap=new HashMap<>();
        Map<String,OptionStruck> optionStruckMap;
        String startName="";
        for(BodyNodeH h:bodyNodeHS){
            if(h.name.equals("Info")){
                if(h.texts.size()!=2 || !h.texts.get(h.texts.size()-1).equals("Global")){
                    return GlobalConfig.NONE_GLOBAL_CONFIG;
                }else {
                    startName=h.texts.get(0);
                }
            }else if(h.name.equals("Options")){
                for(BodyNodeH b:h.children){
                    optionExpressionMap.put(b.name, b.texts.toArray(new String[0]));
                }
            }else if(h.name.equals("Progress")){
                progressH=h;
            }else if(h.name.equals("PersonData")){
                for(BodyNodeH b:h.children){
                    String id=b.name;
                    String name=b.texts.get(0);
                    List<String> states=new ArrayList<>();
                    String[] s=b.texts.get(1).split(",");
                    Collections.addAll(states, s);
                    GlobalConfig.PersonConfig personConfig=new GlobalConfig.PersonConfig(id,name,states);
                    personConfigMap.put(id,personConfig);
                }
            }
        }

        optionStruckMap=loadOptionStruckM(progressH,optionExpressionMap);

        return new GlobalConfig(startName,optionStruckMap,personConfigMap);
    }

    private static Map<String,OptionStruck> loadOptionStruckM(BodyNodeH progressH,Map<String,String[]> expressions){
        Map<String,OptionStruck> struck=new HashMap<>();
        if(progressH!=null){
            for (String s : progressH.texts) {
                String x = s.replaceAll(" {2}", "");
                String[] xl = x.split("->");
                String sourceId = xl[0];
                String optionName = xl[1];
                String[] select = xl[2].split(",");
                struck.put(sourceId, new OptionStruck(optionName, expressions.get(optionName),select));
            }
        }
        return struck;
    }

    public record Chapter
            (String id,
             String name,
             String startPlayId,
             Map<String, OptionStruck> playOptionMap) {

        public static final Chapter NONE_CHAPTER = null;

        public String nextPlay(String id, Map<String, String> data) {
            OptionStruck struck = playOptionMap.get(id);
            if (struck == null) {
                return OptionStruck.NONE_ID;
            } else {
                String nextId = struck.struckNextOption(data);
                if (Objects.equals(nextId, OptionStruck.NONE_ID)) {
                    return OptionStruck.NONE_ID;
                } else {
                    return nextId;
                }
            }
        }
    }

    public record GlobalConfig(String startChapter,
                               Map<String, OptionStruck> chapterOptionMap,
                               Map<String,PersonConfig> personConfigs) {

        public static final GlobalConfig NONE_GLOBAL_CONFIG=null;

        public String nextChapter(String id, Map<String, String> data) {
            OptionStruck struck = chapterOptionMap.get(id);
            if (struck == null) {
                return OptionStruck.NONE_ID;
            } else {
                String nextId = struck.struckNextOption(data);
                if (Objects.equals(nextId, OptionStruck.NONE_ID)) {
                    return OptionStruck.NONE_ID;
                } else {
                    return nextId;
                }
            }
        }

        public record PersonConfig(String id, String name, List<String> state) {
        }
    }
}