package com.lixin.listen.bean;

import java.io.File;

/**
 * Created by admin on 2017/1/11.
 */

public class RequestVO {

    private String cmd;

    private String uid;

    private String albumId;

    private String thirdUid;

    private String nickName;

    private String userIcon;

    private String dayCount;

    private String type;

    private String recTimeLength;

    private String fileTimeLength;

    private String firstTypeId;

    private String secondTypeId;

    private String thirdTypeId;

    private String albumLength;

    private String albumName;

    private File file;

    private String date;

    private String questionId;

    private String ComplainContent;

    private String tellphone;

    private String localId;

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getComplainContent() {
        return ComplainContent;
    }

    public void setComplainContent(String complainContent) {
        ComplainContent = complainContent;
    }

    public String getTellphone() {
        return tellphone;
    }

    public void setTellphone(String tellphone) {
        this.tellphone = tellphone;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public RequestVO(){

    }

    public RequestVO(String cmd) {
        this.cmd = cmd;
    }

    public RequestVO(String cmd, String uid){
        this.cmd = cmd;
        this.uid = uid;
    }

    public RequestVO(String cmd, String uid, String albumId){
        this.cmd = cmd;
        this.uid = uid;
        this.albumId = albumId;
    }


    public RequestVO(String cmd, String thirdUid, String nickName, String userIcon){
        this.cmd = cmd;
        this.thirdUid = thirdUid;
        this.nickName = nickName;
        this.userIcon = userIcon;
    }

    public String getThirdUid() {
        return thirdUid;
    }

    public void setThirdUid(String thirdUid) {
        this.thirdUid = thirdUid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getCmd() {
        return cmd;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getUid() {
        return uid;
    }

    public String getThirdTypeId() {
        return thirdTypeId;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getAlbumLength() {
        return albumLength;
    }

    public void setAlbumLength(String albumLength) {
        this.albumLength = albumLength;
    }

    public void setThirdTypeId(String thirdTypeId) {
        this.thirdTypeId = thirdTypeId;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDayCount() {
        return dayCount;
    }

    public void setDayCount(String dayCount) {
        this.dayCount = dayCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRecTimeLength() {
        return recTimeLength;
    }

    public void setRecTimeLength(String recTimeLength) {
        this.recTimeLength = recTimeLength;
    }

    public String getFileTimeLength() {
        return fileTimeLength;
    }

    public void setFileTimeLength(String fileTimeLength) {
        this.fileTimeLength = fileTimeLength;
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
}
