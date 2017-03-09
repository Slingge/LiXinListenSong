package com.lixin.listen.bean;

/**
 * Created by admin on 2017/1/18.
 */

public class CurrentDayTimeVo {


    /**
     * fileTimeLength : 0分6秒
     * mid : 0
     * recTimeLength : 0分6秒
     * result : 0
     * resultNote : 获取当天录制信息成功
     */

    private String fileTimeLength;
    private int mid;
    private String recTimeLength;
    private String result;
    private String resultNote;

    public String getFileTimeLength() {
        return fileTimeLength;
    }

    public void setFileTimeLength(String fileTimeLength) {
        this.fileTimeLength = fileTimeLength;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getRecTimeLength() {
        return recTimeLength;
    }

    public void setRecTimeLength(String recTimeLength) {
        this.recTimeLength = recTimeLength;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultNote() {
        return resultNote;
    }

    public void setResultNote(String resultNote) {
        this.resultNote = resultNote;
    }
}
