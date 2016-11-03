package com.rksomayaji.work.orthoscores2;

import java.util.List;

/**
 * Created by sushanth on 10/15/16.
 */

public class TestQuestion {
    private String question;
    private List<String> response;
    private List<Integer> value;
    private int selectedValue;

    public TestQuestion (){}

    public TestQuestion(String q, List<String> r, List<Integer> v){
        question = q;
        response = r;
        value = v;
    }

    public List<Integer> getValue() {
        return value;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getResponse() {
        return response;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setResponse(List<String> response) {
        this.response = response;
    }

    public void setValue(List<Integer> value) {
        this.value = value;
    }

    public int getSelectedValue(){
        return selectedValue;
    }

    public void setSelectedValue(int selectedValue) {
        this.selectedValue = value.get(selectedValue);
    }
}
