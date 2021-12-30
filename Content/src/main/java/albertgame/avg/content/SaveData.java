package albertgame.avg.content;


import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.*;

/**
 * 一份游戏存档
 */
@XmlRootElement
public class SaveData {

    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hour;
    private Integer minute;
    private Integer second;

    private String playName;
    private Character struckOfPlay;
    private Integer playIndex;

    @XmlElementWrapper(name = "savedAttributes")
    @XmlElement(name = "attribute")
    private List<String> savedAttributes;

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
        savedAttributes=new ArrayList<>();
        displayWordLine=new ArrayList<>();
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
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

    public List<String> getSavedAttributes() {
        return savedAttributes;
    }

    public void setSavedAttributes(List<String> savedAttributes) {
        this.savedAttributes = savedAttributes;
    }

    public List<String> getDisplayWordLine() {
        return displayWordLine;
    }

    public void setDisplayWordLine(List<String> displayWordLine) {
        this.displayWordLine = displayWordLine;
    }

    public Character getStruckOfPlay() {
        return struckOfPlay;
    }

    public void setStruckOfPlay(Character struckOfPlay) {
        this.struckOfPlay = struckOfPlay;
    }
}
