package com.connormacd.quizmaster;

import android.util.Log;

import java.util.HashMap;
/**
 * Created by Connor MacD on 14-10-29.
 *
 * Holds each individual question in a hashmap<String,String>
 * with some simple methods to get a Question's question key value
 * and getting a Question's answer key value without writing more lines.
 *
 **/

public class Question {

    private HashMap<String, String> qAndA;

    public Question(HashMap<String, String> answers) {
        Log.d("Question.java", "Creating new object");
        this.qAndA = answers;
        Log.d("Question.java", "Question object created");
    }


    // Getters and setters

    public String getQuestion() {
        return qAndA.get("question");
    }

    public String getCorrectAnswer() {
        return qAndA.get("answer");
    }

    public HashMap<String,String> getQuestionsHashMap() {
        return qAndA;
    }



}
