package albertgame.avg.content;

import javafx.scene.paint.Color;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Play {

    String id, name, startStruck;
    Map<String, BodyStruck> bodyStruckMap;

    public Play(String id, String name, String startStruck, Map<String, BodyStruck> bodyStruckMap) {
        this.id = id;
        this.name = name;
        this.startStruck = startStruck;
        this.bodyStruckMap = bodyStruckMap;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String startStruck() {
        return startStruck;
    }

    public Map<String, BodyStruck> bodyStruckMap() {
        return bodyStruckMap;
    }

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


    public static class BodyStruck {
        public static final BodyStruck NONE_BODY = null;

        String id;
        List<String> expressions;
        OptionStruck optionStruck;

        public BodyStruck(String id, List<String> expressions, OptionStruck optionStruck) {
            this.id = id;
            this.expressions = expressions;
            this.optionStruck = optionStruck;
        }

        public String id() {
            return id;
        }

        public List<String> expressions() {
            return expressions;
        }

        public OptionStruck optionStruck() {
            return optionStruck;
        }
    }

    public static class OptionStruck {
        String id;
        String[] selectExpression, destIds;

        public static final String NONE_ID = "00";

        public static final OptionStruck NONE_OPTION = new OptionStruck(NONE_ID, null, null);


        public OptionStruck(String id, String[] selectExpression, String[] destIds) {
            this.id = id;
            this.selectExpression = selectExpression;
            this.destIds = destIds;
        }

        public String id() {
            return id;
        }

        public String[] selectExpression() {
            return selectExpression;
        }

        public String[] destIds() {
            return destIds;
        }

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
                String s = data.get(v);
                if (s == null) return -1;
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
        if (s.startsWith("[") && s.endsWith("]")) {
            String f = s.substring(1, s.length() - 1);
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
                String[] cmd = new String[]{"Dialog", "Pound", w};
                arrayList.add(cmd);
            }
            String lastPage = f.substring(i * ConfigCenter.WORD_MAX_SIZE);
            String[] cmd = new String[]{"Dialog", "Pound", lastPage};
            arrayList.add(cmd);
            return arrayList;
        } else return Collections.emptyList();
    }

    private static List<BodyNodeH> loadBodyNodeH(URL url) {
        List<BodyNodeH> bodyNodeHS = new ArrayList<>();
        try {
            Stack<BodyNodeH> stack = new Stack<>();

            InputStreamReader reader;
            reader = new InputStreamReader(url.openStream(),
                    StandardCharsets.UTF_8);
            BufferedReader r = new BufferedReader(reader);
            String line;
            while (true) {
                if (!((line = r.readLine()) != null)) break;
                line = line.strip();
                if (!line.isBlank()) {
                    parseFile(bodyNodeHS, stack, line);
                }
            }
            parseFile(bodyNodeHS, stack, END_SIGN);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bodyNodeHS;
    }

    public static Play loadPlay(URL url) {
        return loadPlay(loadBodyNodeH(url));
    }

    private static Play loadPlay(List<BodyNodeH> hs) {
        Map<String, OptionStruck> op;
        Map<String, BodyStruck> bodyStruckMap = new HashMap<>();
        Map<String, String[]> optionMap = new HashMap<>();
        BodyNodeH bodyN = null;
        BodyNodeH progressN = null;
        String id = "00", name = "00", startStruck = "00";
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
        op = loadOptionStruckM(progressN, optionMap);

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

        return new Play(id, name, startStruck, bodyStruckMap);
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

    public static Chapter loadChapter(URL url) {
        Chapter chapter;
        String id = null, name = null, startName = null;

        List<BodyNodeH> list = loadBodyNodeH(url);
        BodyNodeH progressH = null;
        Map<String, String[]> optionExMap = new HashMap<>();

        for (BodyNodeH h : list) {
            switch (h.name) {
                case "Info":
                    if (h.texts.size() < 1 ||
                            !h.texts.get(h.texts.size() - 1).equals("Chapter")) {
                        return Chapter.NONE_CHAPTER;
                    }

                    id = h.texts.get(0);
                    name = h.texts.get(1);
                    startName = h.texts.get(2);
                    break;
                case "Progress":
                    progressH = h;
                    break;
                case "Options":
                    for (BodyNodeH b : h.children) {
                        optionExMap.put(b.name, b.texts.toArray(new String[0]));
                    }
                    break;
            }
        }

        if (id == null || name == null) {
            return Chapter.NONE_CHAPTER;
        }

        Map<String, OptionStruck> optionStruckMap = loadOptionStruckM(progressH, optionExMap);

        chapter = new Chapter(id, name, startName, optionStruckMap);
        return chapter;
    }

    public static GlobalConfig loadGlobalConfig(URL file) {

        List<BodyNodeH> bodyNodeHS = loadBodyNodeH(file);
        Map<String, String[]> optionExpressionMap = new HashMap<>();
        BodyNodeH progressH = null;
        Map<String, GlobalConfig.PersonConfig> personConfigMap = new HashMap<>();
        Map<String, OptionStruck> optionStruckMap;
        String startName = "";
        for (BodyNodeH h : bodyNodeHS) {
            switch (h.name) {
                case "Info":
                    if (h.texts.size() != 2 || !h.texts.get(h.texts.size() - 1).equals("Global")) {
                        return GlobalConfig.NONE_GLOBAL_CONFIG;
                    } else {
                        startName = h.texts.get(0);
                    }
                    break;
                case "Options":
                    for (BodyNodeH b : h.children) {
                        optionExpressionMap.put(b.name, b.texts.toArray(new String[0]));
                    }
                    break;
                case "Progress":
                    progressH = h;
                    break;
                case "PersonData":
                    for (BodyNodeH b : h.children) {
                        String id = b.name;
                        String name = b.texts.get(0);
                        List<String> states = new ArrayList<>();
                        String[] s = b.texts.get(1).split(",");
                        Collections.addAll(states, s);
                        GlobalConfig.PersonConfig personConfig = new GlobalConfig.PersonConfig(id, name, states);
                        personConfigMap.put(id, personConfig);
                    }
                    break;
            }
        }

        optionStruckMap = loadOptionStruckM(progressH, optionExpressionMap);

        return new GlobalConfig(startName, optionStruckMap, personConfigMap);
    }

    public static SystemConfig loadSystemConfig(URL url) {
        List<BodyNodeH> bodyNodeHS = loadBodyNodeH(url);
        Map<String, String> fontMap = new HashMap<>();
        Map<String, Color> colorMap = new HashMap<>();
        Map<String, ImageC> imageMap = new HashMap<>();
        Map<String, String> bgmMap = new HashMap<>();
        String title = "AlbertGame.AVG";
        for (BodyNodeH b : bodyNodeHS) {
            switch (b.name) {
                case "Info":
                    if (b.texts.size() < 2) {
                        System.err.println("系统配置导入失败");
                        return null;
                    }
                    if (!b.texts.get(1).equals("System")) {
                        System.err.println("系统配置导入失败");
                        return null;
                    }
                    title = b.texts.get(0);
                    break;
                case "Fonts":
                    for (String s : b.texts) {
                        String[] sx = s.split(" {2}");
                        String name = sx[0];
                        String fontName = sx[1];
                        fontMap.put(name, fontName);
                    }
                    break;
                case "Colors":
                    for (String s : b.texts) {
                        String[] sx = s.split(" {2}");
                        String name = sx[0];
                        String[] colors = sx[1].split(",");
                        double r = Double.parseDouble(colors[0]);
                        double g = Double.parseDouble(colors[1]);
                        double bl = Double.parseDouble(colors[2]);
                        double o = Double.parseDouble(colors[3]);
                        colorMap.put(name, Color.color(r, g, bl, o));
                    }
                    break;
                case "Images":
                    for (String s : b.texts) {
                        String[] sx = s.split(" {2}");
                        String name = sx[0];
                        String path = sx[1];
                        String format = sx[2];
                        imageMap.put(name, new ImageC(path, format));
                    }
                    break;
                case "Bgms":
                    for (String s : b.texts) {
                        String[] sx = s.split(" {2}");
                        bgmMap.put(sx[0], sx[1]);
                    }
                    break;
            }
        }

        return new SystemConfig(fontMap, colorMap, imageMap, bgmMap, title);
    }

    private static Map<String, OptionStruck> loadOptionStruckM(BodyNodeH progressH, Map<String, String[]> expressions) {
        Map<String, OptionStruck> struck = new HashMap<>();
        if (progressH != null) {
            for (String s : progressH.texts) {
                String x = s.replaceAll(" {2}", "");
                String[] xl = x.split("->");
                String sourceId = xl[0];
                String optionName = xl[1];
                String[] select = xl[2].split(",");
                struck.put(sourceId, new OptionStruck(optionName, expressions.get(optionName), select));
            }
        }
        return struck;
    }


    public static class Chapter {
        String id, name, startPlayId;
        Map<String, OptionStruck> playOptionMap;
        public static final Chapter NONE_CHAPTER = null;

        public Chapter(String id, String name, String startPlayId, Map<String, OptionStruck> playOptionMap) {
            this.id = id;
            this.name = name;
            this.startPlayId = startPlayId;
            this.playOptionMap = playOptionMap;
        }

        public String id() {
            return id;
        }

        public String name() {
            return name;
        }

        public String startPlayId() {
            return startPlayId;
        }

        public Map<String, OptionStruck> playOptionMap() {
            return playOptionMap;
        }

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

    public static class GlobalConfig {
        String startChapter;
        Map<String, OptionStruck> chapterOptionMap;
        Map<String, PersonConfig> personConfigs;

        public GlobalConfig(String startChapter, Map<String, OptionStruck> chapterOptionMap, Map<String, PersonConfig> personConfigs) {
            this.startChapter = startChapter;
            this.chapterOptionMap = chapterOptionMap;
            this.personConfigs = personConfigs;
        }

        public String startChapter() {
            return startChapter;
        }

        public Map<String, OptionStruck> chapterOptionMap() {
            return chapterOptionMap;
        }

        public Map<String, PersonConfig> personConfigs() {
            return personConfigs;
        }

        public static final GlobalConfig NONE_GLOBAL_CONFIG = null;

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


        public static class PersonConfig {
            String id, name;
            List<String> state;

            public PersonConfig(String id, String name, List<String> state) {
                this.id = id;
                this.name = name;
                this.state = state;
            }

            public String id() {
                return id;
            }

            public String name() {
                return name;
            }

            public List<String> state() {
                return state;
            }
        }
    }

    public static class SystemConfig {
        Map<String, String> fontMap;
        Map<String, Color> colorMap;
        Map<String, ImageC> imageMap;
        Map<String, String> bgmMap;
        String title;

        public SystemConfig(Map<String, String> fontMap,
                            Map<String, Color> colorMap,
                            Map<String, ImageC> imageMap,
                            Map<String, String> bgmMap, String title) {
            this.fontMap = fontMap;
            this.colorMap = colorMap;
            this.imageMap = imageMap;
            this.bgmMap = bgmMap;
            this.title = title;
        }

        public String title() {
            return title;
        }

        public Map<String, String> fontMap() {
            return fontMap;
        }

        public Map<String, Color> colorMap() {
            return colorMap;
        }

        public Map<String, ImageC> imageMap() {
            return imageMap;
        }

        public Map<String, String> bgmMap() {
            return bgmMap;
        }
    }

    public static class ImageC {
        String path, format;

        public ImageC(String path, String format) {
            this.path = path;
            this.format = format;
        }

        public String path() {
            return path;
        }

        public String format() {
            return format;
        }
    }
}
