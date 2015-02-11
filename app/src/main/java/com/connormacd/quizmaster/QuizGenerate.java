package com.connormacd.quizmaster;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by Connor MacD on 14-10-29.
 *
 * QuizGenerate.java contains the core methods for loading data from /src/main/assets/
 * It also contains methods for shuffling the order of the question and answer ArrayLists.
 *
 */
public class QuizGenerate {

    //generate all possible questions and their correct answers from questions.txt
    //This is where the question and answer keys are put into an ArrayList containing the Question class.
    //The Question class contains the hashmap of question and answer.
    //The current question will always use the current answer's text for generating correct options.
    public static ArrayList<Question> generateQuiz(Context context) {
        ArrayList<Question> questionArrayList = new ArrayList<Question>();
        BufferedReader reading = null;

        try {
            reading = new BufferedReader(new InputStreamReader(context.getAssets().open("questions.txt")));
            Log.d("try","makes it past buffer");
            String cLine = reading.readLine();
            Log.d("buffer",cLine);
            while (cLine != null) {

                StringTokenizer tokens = new StringTokenizer(cLine,";");
                String question = tokens.nextToken();
                String answer = tokens.nextToken();
                HashMap<String, String> questionMap = new HashMap<String, String>();
                questionMap.put("question",question);
                questionMap.put("answer", answer);
                questionArrayList.add(new Question(questionMap));

                //load the next line to loop
                cLine = reading.readLine();
            }

        } catch (Exception e) {
            Log.e("generateQuiz","Error parsing question: " + e.toString());
            return null;
        } finally {
            if (reading != null) {
                try {
                    reading.close();
                } catch (IOException e) {
                    //log the exception
                    Log.e("generateQuiz - BufferedReader","Failed to close buffered reader. Why?: "+e.getMessage());
                }
            }
        }

        return questionArrayList;
    }

    //generate all possible answers from terms.txt
    //This list contains any random answers. This contains correct answers, but not to the answer being currently asked.
    public static ArrayList<String> generateAnswers(Context context) {
        String answer;
        ArrayList<String> answers = new ArrayList<String>();
        BufferedReader reading = null;
        try {
            reading = new BufferedReader(new InputStreamReader(context.getAssets().open("terms.txt")));
            String cLine = reading.readLine();
            while (cLine != null) {
                StringTokenizer tokens = new StringTokenizer(cLine, ";");
                String tokenString = tokens.nextToken();
                boolean tokensLeft = true;
                while (tokensLeft) {
                    //Log.d("QuizGenerate.class","Adding new term to answer list");

                    answers.add(tokenString);
                    //Log.d("QuizGenerate","Added term "+tokenString+" to answer list.");
                    //if there's more: loop de loop
                    if (!tokens.hasMoreTokens()) {
                        tokensLeft = false;
                    } else {
                        tokenString = tokens.nextToken();
                    }
                    //System.out.println(tokenString);
                }
                cLine = reading.readLine();
            }

        } catch (Exception e) {
            Log.e("generateAnswers","Error parsing terms: " + e.getCause());
            return null;
        } finally {
            if (reading != null) {
                try {
                    reading.close();
                } catch (IOException e) {
                    Log.e("generateAnswers - BufferedReader","Failed to close buffered reader. Why?: "+e.getMessage());
                }
            }
        }

        return answers;
    }



    public static ArrayList<Question> randomizeQuestions(ArrayList<Question> list) {
        long seed = System.nanoTime();
        Collections.shuffle(list);
        return list;
    }


    public static ArrayList<String> randomizeAnswers(ArrayList<String> list) {
        long seed = System.nanoTime();
        Collections.shuffle(list);
        return list;
    }


}
