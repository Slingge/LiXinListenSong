package com.lixin.listen.bean;

/**
 * Created by admin on 2017/1/15.
 */

public class UpdateVO {


    /**
     * mid : 0
     * result : 0
     * resultNote : 检测新版本成功
     * updataAddress : http://116.255.239.201:8080/musicing/icon/ChinaLogistics_wuliu.apk
     * versionName : 1.2
     * versionNumber : 12
     */

    private int mid;
    private String result;
    private String resultNote;
    private String updataAddress;
    private String versionName;
    private String versionNumber;

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

    public String getUpdataAddress() {
        return updataAddress;
    }

    public void setUpdataAddress(String updataAddress) {
        this.updataAddress = updataAddress;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }
}
