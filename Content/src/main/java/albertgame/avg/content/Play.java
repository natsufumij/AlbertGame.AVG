package albertgame.avg.content;

public record Play(String id,String name,
                   String[] begins, String[] ends,
                   BodyOptionUnion body) {

    public record OptionStruck(String optionName, String optionExpression,
                               String[] optionSelectExpression) {
    }

    public record BodyStruck(String name, String[] expression) {

    }

    public record BodyOptionUnion(BodyStruck bodyStruck,
                                  OptionStruck struck,
                                  BodyStruck[] optionStruck) {
    }
}
