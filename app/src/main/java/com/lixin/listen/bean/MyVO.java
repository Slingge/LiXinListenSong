package com.lixin.listen.bean;

/**
 * Created by admin on 2017/1/12.
 */

public class MyVO {

    /**
     * mid : 0
     * result : 0
     * resultNote : 获取个人信息成功
     * userInfo : {"dayCount":"0","fileCount":"0","nickName":"李举鹏","uid":11,"userIcon":"http://wx.qlogo.cn/mmopen/PiajxSqBRaEKtmb2xhHKpBGBqkSaibpHoS8hQaFx7NcvjhVW8cPbzT6gJF8vkw2FmD2WW0CM8Ch6bFNnPNQYiceZA/0","version":"12"}
     */

    private int mid;
    private String result;
    private String resultNote;
    /**
     * dayCount : 0
     * fileCount : 0
     * nickName : 李举鹏
     * uid : 11
     * userIcon : http://wx.qlogo.cn/mmopen/PiajxSqBRaEKtmb2xhHKpBGBqkSaibpHoS8hQaFx7NcvjhVW8cPbzT6gJF8vkw2FmD2WW0CM8Ch6bFNnPNQYiceZA/0
     * version : 12
     */

    private UserInfoBean userInfo;

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

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public static class UserInfoBean {
        private String dayCount;
        private String fileCount;
        private String nickName;
        private int uid;
        private String userIcon;
        private String version;

        public String getDayCount() {
            return dayCount;
        }

        public void setDayCount(String dayCount) {
            this.dayCount = dayCount;
        }

        public String getFileCount() {
            return fileCount;
        }

        public void setFileCount(String fileCount) {
            this.fileCount = fileCount;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getUserIcon() {
            return userIcon;
        }

        public void setUserIcon(String userIcon) {
            this.userIcon = userIcon;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
