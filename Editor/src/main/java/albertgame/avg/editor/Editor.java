package albertgame.avg.editor;

import java.util.HashMap;
import java.util.Map;

public class Editor {

    private static Editor _s;
    private static final Object lock = new Object();

    public static Editor get() {
        if (_s == null) {
            synchronized (lock) {
                _s = new Editor();
            }
        }
        return _s;
    }

    private MainFormController2 con;
    private Struck.Handler struckH;
    private PlayCommand.Handler playCommandH;
    private Progress.Handler progressH;
    private Map<String, LibAsset.Person> persons;

    private Editor() {
        persons =new HashMap<>();
    }

    private void init() {
        LibAsset.addHandler(LibAsset.TYPE.AUDIO, new LibAssetH.ListH("audio", "wav",
                con.getAudioLib()));
        LibAsset.addHandler(LibAsset.TYPE.BGM, new LibAssetH.ListH("bgm", "mp3",
                con.getBgmLib()));
        LibAsset.addHandler(LibAsset.TYPE.PERSON, new LibAssetH.ListH("person", "png",
                con.getPersonLib()));
        LibAsset.addHandler(LibAsset.TYPE.SCENE, new LibAssetH.ListH("scene", "jpg",
                con.getSceneLib()));
        LibAsset.addHandler(LibAsset.TYPE.STORY, new LibAssetH.TreeH("story", "avg",
                con.getStoryLib()));
        struckH = new StruckH(con.getStruckFlow(), con.getStartStruckChoice());
        playCommandH = new PlayCommandH(con.getCmdViews(), con.getCommandArea(), con.getCmdEnterBu());
        progressH = new ProgressH(con.getProgresses());
    }

    public MainFormController2 getController() {
        return con;
    }

    public void setController(MainFormController2 controller2) {
        this.con = controller2;
        init();
    }

    public Struck.Handler getStruckH() {
        return struckH;
    }

    public PlayCommand.Handler getPlayCommandH() {
        return playCommandH;
    }

    public Progress.Handler getProgressH() {
        return progressH;
    }

    public Map<String, LibAsset.Person> getPersons() {
        return persons;
    }
}
