package albertgame.avg.content;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public record Play
        (String id,
         String name,
         Map<String, BodyStruck> bodyStruckMap,
         OptionStruck nextPlay) {

    public String nextPlay(Map<String, Integer> data) {
        if (nextPlay == OptionStruck.NONE_OPTION) return OptionStruck.NONE_ID;

        return nextPlay.struckNextOption(data);
    }

    public BodyStruck nextBodyStruck(String id, Map<String, Integer> data) {
        BodyStruck struck=bodyStruckMap.get(id);
        String dis=struck.optionStruck.struckNextOption(data);
        if(Objects.equals(dis, OptionStruck.NONE_ID)){
            return BodyStruck.NONE_BODY;
        }else {
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
        public String struckNextOption(Map<String, Integer> data) {
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
        private boolean parseStruck1(String expression, Map<String, Integer> data) {
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

        private Integer getReal(String v, Map<String, Integer> data) {
            char v10 = v.charAt(0);
            if (v10 >= '0' && v10 <= '9') {
                return Integer.parseInt(v);
            } else {
                return data.getOrDefault(v, -1);
            }
        }

        // A & B & C | D & E | F
        private boolean parseSelectExpression(String expression, Map<String, Integer> data) {
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
        private boolean parseAndEx(String expression, Map<String, Integer> data) {
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

        List<BodyNodeH> bodyNodeHS=new ArrayList<>();
        Stack<BodyNodeH> stack=new Stack<>();

        try {
            File file = ConfigCenter.loadFileInClasspath("demo/demo2.avg");
            Play p=loadPlay(file);
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

    private static void parseFile(List<BodyNodeH> oneList,Stack<BodyNodeH> stack,String line) {
        if (line.isBlank()) return;
        line = line.strip();

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
        if (s.startsWith("[")) {
            int ind = s.indexOf("]");
            String f = s.substring(1, ind);
            f = f.strip();
            ArrayList<String[]> arrayList=new ArrayList<>();
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

    public static Play loadPlay(File file){
        List<BodyNodeH> bodyNodeHS=new ArrayList<>();
        Stack<BodyNodeH> stack=new Stack<>();

        try {
            if (file.exists() && file.isFile()) {
                InputStreamReader reader;
                reader = new InputStreamReader(new FileInputStream(file),
                        StandardCharsets.UTF_8);
                BufferedReader r = new BufferedReader(reader);
                String line;
                while ((line = r.readLine()) != null) {
                    line = line.strip();
                    line=line.strip();
                    if(!line.isBlank()){
                        parseFile(bodyNodeHS,stack,line);
                    }
                }
                parseFile(bodyNodeHS,stack,END_SIGN);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return loadPlay(bodyNodeHS);
    }

    private static Play loadPlay(List<BodyNodeH> hs) {
        Map<String, OptionStruck> op = new HashMap<>();
        Map<String, BodyStruck> bodyStruckMap = new HashMap<>();
        Map<String, String[]> optionMap = new HashMap<>();
        String[] nextPlayIds = null, nextOps = null;
        BodyNodeH bodyN = null;
        BodyNodeH progressN=null;
        String id = "00", name = "00";
        for (BodyNodeH b : hs) {
            switch (b.name) {
                case "Info":
                    id = b.texts.get(0);
                    name = b.texts.get(1);
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
                case "NextPlays":
                    nextPlayIds = b.texts.get(0).split(",");
                    break;
                case "NextOptions":
                    nextOps = b.texts.toArray(new String[0]);
                    break;
            }
        }

        //生成OptionStruck
        if(progressN!=null){
            for (String s : progressN.texts) {
                s = s.replaceAll(" ", "");
                String[] sp = s.split("->");
                OptionStruck optionStruck = new OptionStruck(sp[1], optionMap.get(sp[1]), sp[2].split(","));
                op.put(sp[0], optionStruck);
            }
        }

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

        //生成OptionStruck NextPlay
        OptionStruck nextOp;
        if(nextOps==null){
            nextOp=OptionStruck.NONE_OPTION;
        }else {
            nextOp=new OptionStruck("This",nextOps,nextPlayIds);
        }
        return new Play(id, name, bodyStruckMap, nextOp);
    }

    //拼接成:Type  Name  Word  EXTRA[0]  EXTRA[1] ...
    private static void refreshBodyStruck(BodyStruck struck){
        List<String> destList=new ArrayList<>();
        for(String s:struck.expressions){
            List<String[]> dest=parseCmd(s);
            for(String[] sr:dest){
                StringBuilder ax= new StringBuilder("  ");
                for(String sd:sr){
                    ax.append(sd).append("  ");
                }
                ax = new StringBuilder(ax.toString().strip());
                destList.add(ax.toString());
            }
        }
        struck.expressions.clear();
        struck.expressions.addAll(destList);
    }
}