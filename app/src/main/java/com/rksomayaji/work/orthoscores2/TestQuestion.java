package com.rksomayaji.work.orthoscores2;

/**
 * Created by sushanth on 10/15/16.
 */

public class TestQuestion {
    private String question;
    private String[] response;
    private int[] value;
    private int selectedValue;

    public TestQuestion (){}

    TestQuestion(String q, String[] r, int[] v){
        question = q;
        response = r;
        value = v;
    }

    public int[] getValue() {
        return value;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getResponse() {
        return response;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setResponse(String[] response) {
        this.response = response;
    }

    public void setValue(int[] value) {
        this.value = value;
    }

    public int getSelectedValue(){
        return value[selectedValue];
    }

    public void setSelectedValue(int selectedValue) {
        this.selectedValue = value[selectedValue];
    }
}
