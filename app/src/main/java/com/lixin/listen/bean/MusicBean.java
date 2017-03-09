package com.lixin.listen.bean;

import java.io.File;
import java.io.Serializable;

/**
 * Created by admin on 2017/1/14.
 */

public class MusicBean implements Serializable{

    private String musicName;

    private String time;

    private String type;

    private File file;

    private String filePath;

    private String showName;

    private String zhuanjiName;

    private String quziName;

    private String firstTypeId;

    private String secondTypeId;

    private String thirdTypeId;

    private boolean isUpload;

    private boolean isPlaying;

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }

    public String getFirstTypeId() {
        return firstTypeId;
    }

    public void setFirstTypeId(String firstTypeId) {
        this.firstTypeId = firstTypeId;
    }

    public String getSecondTypeId() {
        return secondTypeId;
    }

    public void setSecondTypeId(String secondTypeId) {
        this.secondTypeId = secondTypeId;
    }

    public String getThirdTypeId() {
        return thirdTypeId;
    }

    public void setThirdTypeId(String thirdTypeId) {
        this.thirdTypeId = thirdTypeId;
    }

    public String getZhuanjiName() {
        return zhuanjiName;
    }

    public void setZhuanjiName(String zhuanjiName) {
        this.zhuanjiName = zhuanjiName;
    }

    public String getQuziName() {
        return quziName;
    }

    public void setQuziName(String quziName) {
        this.quziName = quziName;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
