package com.lixin.listen.bean;

/**
 * Created by admin on 2017/1/18.
 */

public class CommonVO {


    /**
     * mid : 46
     * result : 0
     * resultNote : 上传成功
     */

    private int mid;
    private String result;
    private String resultNote;
    private String localId;

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
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
