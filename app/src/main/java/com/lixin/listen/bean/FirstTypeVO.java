package com.lixin.listen.bean;

import java.util.List;

/**
 * Created by admin on 2017/1/15.
 */

public class FirstTypeVO {


    /**
     * firstTypeList : [{"firstTypeId":"1","firstTypeImg":"http://116.255.239.201:8080/musicing/123.jpg","firstTypeName":"英文"},{"firstTypeId":"2","firstTypeImg":"http://116.255.239.201:8080/musicing/456.jpg","firstTypeName":"日语"},{"firstTypeId":"3","firstTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","firstTypeName":"中文"},{"firstTypeId":"4","firstTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","firstTypeName":"法文"},{"firstTypeId":"5","firstTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","firstTypeName":"俄文"},{"firstTypeId":"6","firstTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","firstTypeName":"德语"}]
     * mid : 0
     * result : 0
     * resultNote : 获取一级分类成功
     */

    private int mid;
    private String result;
    private String resultNote;
    private List<FirstTypeListBean> firstTypeList;

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

    public List<FirstTypeListBean> getFirstTypeList() {
        return firstTypeList;
    }

    public void setFirstTypeList(List<FirstTypeListBean> firstTypeList) {
        this.firstTypeList = firstTypeList;
    }

    public static class FirstTypeListBean {
        /**
         * firstTypeId : 1
         * firstTypeImg : http://116.255.239.201:8080/musicing/123.jpg
         * firstTypeName : 英文
         */

        private String firstTypeId;
        private String firstTypeImg;
        private String firstTypeName;
        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getFirstTypeId() {
            return firstTypeId;
        }

        public void setFirstTypeId(String firstTypeId) {
            this.firstTypeId = firstTypeId;
        }

        public String getFirstTypeImg() {
            return firstTypeImg;
        }

        public void setFirstTypeImg(String firstTypeImg) {
            this.firstTypeImg = firstTypeImg;
        }

        public String getFirstTypeName() {
            return firstTypeName;
        }

        public void setFirstTypeName(String firstTypeName) {
            this.firstTypeName = firstTypeName;
        }
    }
}
