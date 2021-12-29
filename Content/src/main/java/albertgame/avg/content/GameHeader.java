package albertgame.avg.content;

import javafx.beans.property.StringProperty;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameHeader extends Parent {

    private ImageView background;

    private ImageView leftPerson;
    private ImageView centerPerson;
    private ImageView rightPerson;

    private ImageView wordPaneFrame;

    private Text name;
    private Text[] words;

    public GameHeader() {
        initHead();
    }

    public void initHead(){
        background=new ImageView(ConfigCenter.loadScene("back_demo1","jpeg"));
        background.setFitWidth(ConfigCenter.WINDOW_WIDTH);
        background.setFitHeight(ConfigCenter.WINDOW_HEIGHT);

        Image ona=ConfigCenter.loadPersonState("onna","1","jpg");
        Image otoko=ConfigCenter.loadPersonState("otoko","1","jpg");
        Image bishojo=ConfigCenter.loadPersonState("bishojo","1","jpg");

        leftPerson=new ImageView(ona);
        leftPerson.setFitWidth(ConfigCenter.PERSON_WIDTH);
        leftPerson.setFitHeight(ConfigCenter.PERSON_HEIGHT);

        centerPerson=new ImageView(bishojo);
        centerPerson.setFitWidth(ConfigCenter.PERSON_WIDTH);
        centerPerson.setFitHeight(ConfigCenter.PERSON_HEIGHT);
        centerPerson.setTranslateX(ConfigCenter.PERSON_WIDTH);

        rightPerson=new ImageView(otoko);
        rightPerson.setFitWidth(ConfigCenter.PERSON_WIDTH);
        rightPerson.setFitHeight(ConfigCenter.PERSON_HEIGHT);

        rightPerson.setTranslateX(ConfigCenter.PERSON_WIDTH*2);

        name=new Text("SECRET PLAYER");
        name.setTranslateX(300);
        name.setTranslateY(700);

        Button button=new Button("SAVE");
        button.setLayoutY(ConfigCenter.WINDOW_HEIGHT-30);
        button.setLayoutX(ConfigCenter.PERSON_WIDTH / 2);

        super.getChildren().add(background);
        super.getChildren().addAll(leftPerson,centerPerson,rightPerson);

        super.getChildren().addAll(name);

        words=new Text[ConfigCenter.WORD_LINE_ROW*ConfigCenter.WORD_LINE_COLUMN];
        Font f;
        for (int i=0;i!=words.length;++i){
            int row=i/ConfigCenter.WORD_LINE_COLUMN;
            int column=i%ConfigCenter.WORD_LINE_COLUMN;
            Text t=new Text(" ");
            words[i]=t;
            t.setEffect(new DropShadow(1,1,1,Color.BLACK));
            t.setStroke(Color.WHITE);
            t.setFont(ConfigCenter.WORD_FONT);
            f=t.getFont();
            t.setTranslateX(ConfigCenter.WINDOW_WIDTH/5+
                    column*f.getSize()+
                    column*ConfigCenter.WORD_TAP);
            t.setTranslateY(ConfigCenter.WINDOW_HEIGHT / 4 * 3 +
                    (row+1)*f.getSize()+
                    (row-1)*ConfigCenter.WORD_TAP);
            super.getChildren().add(t);
        }

        super.getChildren().add(button);

        GameData data=GameData.get();
        data.backgroundImageProperty().bindBidirectional(background.imageProperty());
        data.leftPersonImageProperty().bindBidirectional(leftPerson.imageProperty());
        data.rightPersonImageProperty().bindBidirectional(rightPerson.imageProperty());
        data.nameDisplayProperty().bindBidirectional(name.textProperty());
        for(int i=0;i!=data.getDisplayWordLine().length;++i){
            StringProperty text=data.getDisplayWordLine()[i];
            text.bindBidirectional(words[i].textProperty());
            text.setValue("我");
        }
    }
}
