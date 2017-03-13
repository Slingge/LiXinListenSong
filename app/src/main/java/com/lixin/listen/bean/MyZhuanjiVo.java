package com.lixin.listen.bean;

import java.util.List;

/**
 * Created by admin on 2017/1/18.
 */

public class MyZhuanjiVo {


    /**
     * albumList : [{"albumId":"76","albumImg":"http://116.255.239.201:8080/musicing/789.jpg","albumName":"三级1"}]
     * mid : 0
     * result : 0
     * resultNote : 获取我的专辑成功
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
         * albumId : 76
         * albumImg : http://116.255.239.201:8080/musicing/789.jpg
         * albumName : 三级1
         */

        private String albumId;
        private String albumImg;
        private String albumName;


        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public String getAlbumId() {
            return albumId;
        }

        public void setAlbumId(String albumId) {
            this.albumId = albumId;
        }

        public String getAlbumImg() {
            return albumImg;
        }

        public void setAlbumImg(String albumImg) {
            this.albumImg = albumImg;
        }

        public String getAlbumName() {
            return albumName;
        }

        public void setAlbumName(String albumName) {
            this.albumName = albumName;
        }
    }
}
