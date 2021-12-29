package albertgame.avg.content;


import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.*;

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

    private String leftPersonId;
    private String centerPersonId;
    private String rightPersonId;

    private String leftPersonState;
    private String centerPersonState;
    private String rightPersonState;

    private String displayName;
    private List<String> displayWordLine;

    public SaveData() {
        savedAttributes=new HashMap<>();
        displayWordLine=new ArrayList<>();
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

    public Integer getPlayIndex() {
        return playIndex;
    }

    public void setPlayIndex(Integer playIndex) {
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

    public String getLeftPersonId() {
        return leftPersonId;
    }

    public void setLeftPersonId(String leftPersonId) {
        this.leftPersonId = leftPersonId;
    }

    public String getCenterPersonId() {
        return centerPersonId;
    }

    public void setCenterPersonId(String centerPersonId) {
        this.centerPersonId = centerPersonId;
    }

    public String getRightPersonId() {
        return rightPersonId;
    }

    public void setRightPersonId(String rightPersonId) {
        this.rightPersonId = rightPersonId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public Map<String, String> getSavedAttributes() {
        return savedAttributes;
    }

    public void setSavedAttributes(Map<String, String> savedAttributes) {
        this.savedAttributes = savedAttributes;
    }

    public List<String> getDisplayWordLine() {
        return displayWordLine;
    }

    public void setDisplayWordLine(List<String> displayWordLine) {
        this.displayWordLine = displayWordLine;
    }
}
