package albertgame.avg.content;

import java.util.Map;
import java.util.Objects;

public record Play
        (String id,
         String name,
         Map<String, BodyStruck> bodyStruckMap,
         OptionStruck nextPlay) {

    public String nextPlay(Map<String,Integer> data){
        if(nextPlay==OptionStruck.NONE_OPTION)return OptionStruck.NONE_ID;

        return nextPlay.struckNextOption(data);
    }

    public BodyStruck nextBodyStruck(String nowStruckId, Map<String, Integer> data) {
        BodyStruck bodyStruck = bodyStruckMap.get(nowStruckId);
        if (bodyStruck == null) return BodyStruck.NONE_BODY;

        String destId = bodyStruck.optionStruck.struckNextOption(data);
        if (Objects.equals(destId, OptionStruck.NONE_ID)) {
            return BodyStruck.NONE_BODY;
        } else {
            return bodyStruckMap.get(destId);
        }
    }

    public record BodyStruck
            (String id,
             String[] expressions,
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
            Integer d1,d2;
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
}