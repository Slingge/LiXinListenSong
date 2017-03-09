package com.lixin.listen.bean;

import java.util.List;

/**
 * Created by admin on 2017/1/18.
 */

public class QuestionListvo {


    /**
     * mid : 0
     * questionList : [{"questionId":"1","questionName":"问题1"},{"questionId":"2","questionName":"问题2"},{"questionId":"3","questionName":"问题3"},{"questionId":"4","questionName":"问题4"}]
     * result : 0
     * resultNote : 获取常见问题列表成功
     */

    private int mid;
    private String result;
    private String resultNote;
    private List<QuestionListBean> questionList;

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

    public List<QuestionListBean> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<QuestionListBean> questionList) {
        this.questionList = questionList;
    }

    public static class QuestionListBean {
        /**
         * questionId : 1
         * questionName : 问题1
         */

        private String questionId;
        private String questionName;

        public String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        public String getQuestionName() {
            return questionName;
        }

        public void setQuestionName(String questionName) {
            this.questionName = questionName;
        }
    }
}
