package com.lixin.listen.bean;

import java.util.List;

/**
 * Created by admin on 2017/1/15.
 */

public class SecondTypeVo {


    /**
     * mid : 0
     * result : 0
     * resultNote : 获取二级分类成功
     * secondTypeList : [{"secondTypeId":"7","secondTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","secondTypeName":"英文二级1"},{"secondTypeId":"8","secondTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","secondTypeName":"英文二级2"},{"secondTypeId":"9","secondTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","secondTypeName":"英文二级3"},{"secondTypeId":"10","secondTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","secondTypeName":"英文二级4"},{"secondTypeId":"11","secondTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","secondTypeName":"英文二级5"},{"secondTypeId":"12","secondTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","secondTypeName":"英文二级6"},{"secondTypeId":"13","secondTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","secondTypeName":"英文二级7"},{"secondTypeId":"14","secondTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","secondTypeName":"英文二级8"},{"secondTypeId":"15","secondTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","secondTypeName":"英文二级9"},{"secondTypeId":"16","secondTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","secondTypeName":"英文二级10"}]
     */

    private int mid;
    private String result;
    private String resultNote;
    private List<SecondTypeListBean> secondTypeList;

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

    public List<SecondTypeListBean> getSecondTypeList() {
        return secondTypeList;
    }

    public void setSecondTypeList(List<SecondTypeListBean> secondTypeList) {
        this.secondTypeList = secondTypeList;
    }

    public static class SecondTypeListBean {
        /**
         * secondTypeId : 7
         * secondTypeImg : http://116.255.239.201:8080/musicing/789.jpg
         * secondTypeName : 英文二级1
         */

        private String secondTypeId;
        private String secondTypeImg;
        private String secondTypeName;

        public String getSecondTypeId() {
            return secondTypeId;
        }

        public void setSecondTypeId(String secondTypeId) {
            this.secondTypeId = secondTypeId;
        }

        public String getSecondTypeImg() {
            return secondTypeImg;
        }

        public void setSecondTypeImg(String secondTypeImg) {
            this.secondTypeImg = secondTypeImg;
        }

        public String getSecondTypeName() {
            return secondTypeName;
        }

        public void setSecondTypeName(String secondTypeName) {
            this.secondTypeName = secondTypeName;
        }
    }
}
