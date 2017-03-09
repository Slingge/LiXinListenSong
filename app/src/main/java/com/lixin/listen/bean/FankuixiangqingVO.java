package com.lixin.listen.bean;

/**
 * Created by admin on 2017/1/18.
 */

public class FankuixiangqingVO {


    /**
     * mid : 0
     * questionAnswer : http://116.255.239.201:8080/musicing/回答2
     * result : 0
     * resultNote : 获取常见问题详情URL成功
     */

    private int mid;
    private String questionAnswer;
    private String result;
    private String resultNote;

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
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
}
