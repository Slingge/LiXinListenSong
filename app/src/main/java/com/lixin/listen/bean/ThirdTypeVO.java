package com.lixin.listen.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/1/15.
 */

public class ThirdTypeVO {


    /**
     * mid : 0
     * result : 0
     * resultNote : 获取三级分类成功
     * thirdTypeList : [{"thirdTypeId":"67","thirdTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","thirdTypeName":"三级1"},{"thirdTypeId":"68","thirdTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","thirdTypeName":"三级1"},{"thirdTypeId":"69","thirdTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","thirdTypeName":"三级1"},{"thirdTypeId":"70","thirdTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","thirdTypeName":"三级1"},{"thirdTypeId":"71","thirdTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","thirdTypeName":"三级1"},{"thirdTypeId":"72","thirdTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","thirdTypeName":"三级1"},{"thirdTypeId":"73","thirdTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","thirdTypeName":"三级1"},{"thirdTypeId":"74","thirdTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","thirdTypeName":"三级1"},{"thirdTypeId":"75","thirdTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","thirdTypeName":"三级1"},{"thirdTypeId":"76","thirdTypeImg":"http://116.255.239.201:8080/musicing/789.jpg","thirdTypeName":"三级1"}]
     */

    private int mid;
    private String result;
    private String resultNote;
    private List<ThirdTypeListBean> thirdTypeList;

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

    public List<ThirdTypeListBean> getThirdTypeList() {
        return thirdTypeList;
    }

    public void setThirdTypeList(List<ThirdTypeListBean> thirdTypeList) {
        this.thirdTypeList = thirdTypeList;
    }

    public static class ThirdTypeListBean implements Serializable{
        /**
         * thirdTypeId : 67
         * thirdTypeImg : http://116.255.239.201:8080/musicing/789.jpg
         * thirdTypeName : 三级1
         */

        private String thirdTypeId;
        private String thirdTypeImg;
        private String thirdTypeName;

        public String getThirdTypeId() {
            return thirdTypeId;
        }

        public void setThirdTypeId(String thirdTypeId) {
            this.thirdTypeId = thirdTypeId;
        }

        public String getThirdTypeImg() {
            return thirdTypeImg;
        }

        public void setThirdTypeImg(String thirdTypeImg) {
            this.thirdTypeImg = thirdTypeImg;
        }

        public String getThirdTypeName() {
            return thirdTypeName;
        }

        public void setThirdTypeName(String thirdTypeName) {
            this.thirdTypeName = thirdTypeName;
        }
    }
}
