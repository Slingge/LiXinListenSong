package com.lixin.listen.bean;

/**
 * Created by admin on 2017/2/21.
 */

public class EditUserInfoVO {


    /**
     * mid : 0
     * result : 0
     * resultNote : 修改昵称头像成功
     * userIcon : http://wx.qlogo.cn/mmopen/PiajxSqBRaEKtmb2xhHKpBGBqkSaibpHoS8hQaFx7NcvjhVW8cPbzT6gJF8vkw2FmD2WW0CM8Ch6bFNnPNQYiceZA/0
     * userName : 李举鹏
     */

    private int mid;
    private String result;
    private String resultNote;
    private String userIcon;
    private String userName;

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

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
