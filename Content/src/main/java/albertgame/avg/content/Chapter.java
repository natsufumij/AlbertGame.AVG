package albertgame.avg.content;

import albertgame.avg.content.Play.OptionStruck;

import java.util.Map;

public record Chapter
        (String id,
         String name,
         Play startPlay,
         OptionStruck nextChapter) {

    public String nextChapter(Map<String, Integer> data) {
        if (nextChapter == OptionStruck.NONE_OPTION)
            return OptionStruck.NONE_ID;

        return nextChapter.struckNextOption(data);
    }
}

