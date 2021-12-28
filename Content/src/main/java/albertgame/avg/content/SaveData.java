package albertgame.avg.content;


import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 一份游戏存档
 */
@XmlRootElement
public class SaveData {

    private Date savedDate;

    private String playName;
    private Integer playIndex;

    private Map<String, String> savedAttributes;

    private String backMusicName;
    private String backImage;

    private String leftPersonName;
    private String centerPersonName;
    private String rightPersonName;

    private String leftPersonState;
    private String centerPersonState;
    private String rightPersonState;

    private String titleText;

    public SaveData() {
        savedAttributes=new HashMap<>();
    }

    public Date getSavedDate() {
        return savedDate;
    }

    public void setSavedDate(Date savedDate) {
        this.savedDate = savedDate;
    }

    public String getPlayName() {
        return playName;
    }

    public void setPlayName(String playName) {
        this.playName = playName;
    }

    public int getPlayIndex() {
        return playIndex;
    }

    public void setPlayIndex(int playIndex) {
        this.playIndex = playIndex;
    }

    public String getBackMusicName() {
        return backMusicName;
    }

    public void setBackMusicName(String backMusicName) {
        this.backMusicName = backMusicName;
    }

    public String getBackImage() {
        return backImage;
    }

    public void setBackImage(String backImage) {
        this.backImage = backImage;
    }

    public String getLeftPersonName() {
        return leftPersonName;
    }

    public void setLeftPersonName(String leftPersonName) {
        this.leftPersonName = leftPersonName;
    }

    public String getCenterPersonName() {
        return centerPersonName;
    }

    public void setCenterPersonName(String centerPersonName) {
        this.centerPersonName = centerPersonName;
    }

    public String getRightPersonName() {
        return rightPersonName;
    }

    public void setRightPersonName(String rightPersonName) {
        this.rightPersonName = rightPersonName;
    }

    public String getLeftPersonState() {
        return leftPersonState;
    }

    public void setLeftPersonState(String leftPersonState) {
        this.leftPersonState = leftPersonState;
    }

    public String getCenterPersonState() {
        return centerPersonState;
    }

    public void setCenterPersonState(String centerPersonState) {
        this.centerPersonState = centerPersonState;
    }

    public String getRightPersonState() {
        return rightPersonState;
    }

    public void setRightPersonState(String rightPersonState) {
        this.rightPersonState = rightPersonState;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public Map<String, String> getSavedAttributes() {
        return savedAttributes;
    }
}
