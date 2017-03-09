package com.lixin.listen.bean;

import java.io.Serializable;

/**
 * Created by admin on 2017/1/12.
 */

public class StartRecordVo implements Serializable{

    /**
     * dayCount : 0
     * fileTimeLength : 0
     * mid : 0
     * result : 0
     * resultNote : 获取录制时间成功
     * timeLength : 0
     */

    private String dayCount;
    private String fileTimeLength;
    private int mid;
    private String result;
    private String resultNote;
    private String timeLength;

    public String getDayCount() {
        return dayCount;
    }

    public void setDayCount(String dayCount) {
        this.dayCount = dayCount;
    }

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

    public String getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(String timeLength) {
        this.timeLength = timeLength;
    }
}
