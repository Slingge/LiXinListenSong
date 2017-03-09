package com.lixin.listen.bean;

/**
 * Created by admin on 2017/1/13.
 */

public class DaySignalVO {

    /**
     * dayCount : 0
     * mid : 0
     * result : 0
     * resultNote : 调整每日任务成功
     */

    private String dayCount;
    private int mid;
    private String result;
    private String resultNote;

    public String getDayCount() {
        return dayCount;
    }

    public void setDayCount(String dayCount) {
        this.dayCount = dayCount;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
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
