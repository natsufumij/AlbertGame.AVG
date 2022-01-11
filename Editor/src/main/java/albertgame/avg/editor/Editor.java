package albertgame.avg.editor;

public class Editor {

    private static Editor _s;
    private static final Object lock=new Object();
    public static Editor get() {
        if(_s==null){
            synchronized (lock){
                _s=new Editor();
            }
        }
        return _s;
    }

    private MainFormController2 con;
    private Struck.Handler struckH;
    private PlayCommand.Handler playCommandH;
    private Progress.Handler progressH;

    private Editor() {

    }

    public void init(){
    }

    public MainFormController2 getController() {
        return con;
    }

    public void setController(MainFormController2 controller2) {
        this.con = controller2;
    }
}
