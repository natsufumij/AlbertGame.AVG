package albertgame.avg.content;

public record Chapter(String id, boolean end, ChapterOptionUnion chapterOptionUnion) {

    record ChapterStruck(String name, Play startPlay) {

    }

    record ChapterOptionUnion(ChapterStruck chapterStruck,
                              Play.OptionStruck[] optionStruck,
                              ChapterStruck[] destStruck) {
    }
}
