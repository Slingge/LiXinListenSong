package com.lixin.listen.bean;

/**
 * Created by admin on 2017/1/12.
 */

public class LoginVo {


    /**
     * mid : 0
     * result : 0
     * resultNote : 登录成功
     * uid : 11
     */

    private int mid;
    private String result;
    private String resultNote;
    private String uid;

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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
