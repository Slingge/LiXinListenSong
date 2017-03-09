package com.lixin.listen.bean;

import java.util.List;

/**
 * Created by admin on 2017/1/18.
 */

public class ZhuanjiQuziVO {

    /**
     * albumList : [{"albumId":"49","albumLength":"30秒","albumName":"不好","albumVoice":"http://116.255.239.201:8080/musicService/images/talkImgPath/HUO80wMJzprK$3级#不好%01-18_1484729132188.amr","commentNum":"0","praiseCount":"0"},{"albumId":"50","albumLength":"30秒","albumName":"不好","albumVoice":"http://116.255.239.201:8080/musicService/images/talkImgPath/AKn07bOFhOu6$3级#不好%01-18_1484729132188.amr","commentNum":"0","praiseCount":"0"},{"albumId":"51","albumLength":"30秒","albumName":"不好","albumVoice":"http://116.255.239.201:8080/musicService/images/talkImgPath/QMe5B4Ton24u$3级#不好%01-18_1484729132188.amr","commentNum":"0","praiseCount":"0"}]
     * mid : 0
     * result : 0
     * resultNote : 获取我的曲目列表成功
     */

    private int mid;
    private String result;
    private String resultNote;
    private List<AlbumListBean> albumList;

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

    public List<AlbumListBean> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(List<AlbumListBean> albumList) {
        this.albumList = albumList;
    }

    public static class AlbumListBean {
        /**
         * albumId : 49
         * albumLength : 30秒
         * albumName : 不好
         * albumVoice : http://116.255.239.201:8080/musicService/images/talkImgPath/HUO80wMJzprK$3级#不好%01-18_1484729132188.amr
         * commentNum : 0
         * praiseCount : 0
         */

        private String albumId;
        private String albumLength;
        private String albumName;
        private String albumVoice;
        private String commentNum;
        private String praiseCount;

        public String getAlbumId() {
            return albumId;
        }

        public void setAlbumId(String albumId) {
            this.albumId = albumId;
        }

        public String getAlbumLength() {
            return albumLength;
        }

        public void setAlbumLength(String albumLength) {
            this.albumLength = albumLength;
        }

        public String getAlbumName() {
            return albumName;
        }

        public void setAlbumName(String albumName) {
            this.albumName = albumName;
        }

        public String getAlbumVoice() {
            return albumVoice;
        }

        public void setAlbumVoice(String albumVoice) {
            this.albumVoice = albumVoice;
        }

        public String getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(String commentNum) {
            this.commentNum = commentNum;
        }

        public String getPraiseCount() {
            return praiseCount;
        }

        public void setPraiseCount(String praiseCount) {
            this.praiseCount = praiseCount;
        }
    }
}
