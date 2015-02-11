package com.connormacd.quizmaster;

/*
 *   Error: EGL_NOT_INITALIZED is related to Genymotion's virtual device. Reset v. device if this happens
 *   Because this macbook is really old, it loses connect sometimes at random if its been idle for so long
 *   if using AVD you will not have this issue. (cannot confirm this)
 *
 *   QuizActivity: to be called from intent by NameActivity.
 *   Receives bundled extras from NameActivity
 *   key of extra = "name", contains name from EditText of NameActivity
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class QuizActivity extends Activity {


    //A bunch of variables used throughout the QuizActivity.
    TextView questionNumText;
    TextView questionText;

    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;

    int currentQuestion;
    int numOfQuestions;
    ArrayList<Question> list;
    ArrayList<String> answers;

    String name;

    int currentAttempts = 0;
    boolean didYouWin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionNumText = (TextView) findViewById(R.id.nameText);
        questionText = (TextView) findViewById(R.id.textQuestion);

        answer1 = (Button) findViewById (R.id.btnAnswer1);
        answer2 = (Button) findViewById (R.id.btnAnswer2);
        answer3 = (Button) findViewById (R.id.btnAnswer3);
        answer4 = (Button) findViewById (R.id.btnAnswer4);

        //used to get current question
        currentQuestion = 0;
        list = QuizGenerate.generateQuiz(getApplicationContext());
        //randomize the list so they're not always the same
        list = QuizGenerate.randomizeQuestions(list);
        numOfQuestions = list.size();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = (String) extras.get("name");
            if (name != "") {
                Toast announceName = Toast.makeText(getApplicationContext(), "Welcome " + name +"!", Toast.LENGTH_LONG);
                announceName.show();
            }
        }

        prepareQuiz();

        View.OnClickListener listen = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnAnswer1:
                        checkAnswer(answer1);
                        break;
                    case R.id.btnAnswer2:
                        checkAnswer(answer2);
                        break;
                    case R.id.btnAnswer3:
                        checkAnswer(answer3);
                        break;
                    case R.id.btnAnswer4:
                        checkAnswer(answer4);
                        break;
                }
            }
        };
        answer1.setOnClickListener(listen);
        answer2.setOnClickListener(listen);
        answer3.setOnClickListener(listen);
        answer4.setOnClickListener(listen);

    }

    protected void prepareQuiz() {

        //Log.d("Preparing Quiz","Preparing question and answers");
        String question = list.get(currentQuestion).getQuestion();
        String cAnswer = list.get(currentQuestion).getCorrectAnswer();
        questionText.setText(question);

        //reinit answers list for 5% of points
        //otherwise not neccesary for this application as lists are shuffled on each question loaded
        if (answers != null && answers.size() != 0) {
            answers.clear();
        }

        //randomize the answers so they're not always the same.
        answers = QuizGenerate.generateAnswers(getApplicationContext());
        answers = QuizGenerate.randomizeAnswers(answers);

        //Log.d("Preparing Answers","Assigning text to buttons...");
        //max of 4 answers (starting at 0)
        int maxAnswers = 3;
        ArrayList<Integer> usedNumbers = new ArrayList<Integer>();
        //fix logic

        //put the right answer somewhere
        //Randomly puts the right answer in a button.
        int random = randInt(0, maxAnswers);
        switch (random) {
            case 0:
                answer1.setText(cAnswer);
                usedNumbers.add(random);
                break;
            case 1:
                answer2.setText(cAnswer);
                usedNumbers.add(random);
                break;
            case 2:
                answer3.setText(cAnswer);
                usedNumbers.add(random);
                break;
            case 3:
                answer4.setText(cAnswer);
                usedNumbers.add(random);
                break;
        }

        //assign text to the buttons
        //We use the text of the buttons to determine the answer.
        //Check to see if the usedNumbers contains the button (0-3 for specific buttons with random answers)
        //Then check if the button already has the correct answer. If not, check if the string that will be input is the answer
        //if not, then put it in the button. If it is, then put in an answer from a random index.
        //(If the correct answer is found in the answers ArrayList, then getting a random one confirms that the one it gets will not be the answer)
        //Loop this 4 times for each button. Would've been helpful if I used an Array full of the buttons to loop rather than write lots of code.
        while (usedNumbers.size() != 4) {
            if (!usedNumbers.contains(0)) {
                if (!answer1.getText().toString().equals(cAnswer)) {
                    if (!answers.get(0).equals(cAnswer)) {
                        answer1.setText(answers.get(0));
                        usedNumbers.add(0);
                    } else {
                        answer1.setText(answers.get(randInt(4, answers.size() - 1)));
                        usedNumbers.add(0);
                    }
                }
            } else if (!usedNumbers.contains(1)) {
                if (!answer2.getText().toString().equals(cAnswer)) {
                    if (!answers.get(1).equals(cAnswer)) {
                        answer2.setText(answers.get(1));
                        usedNumbers.add(1);
                    } else {
                        answer2.setText(answers.get(randInt(4, answers.size() - 1)));
                        usedNumbers.add(1);
                    }
                }
            } else if (!usedNumbers.contains(2)) {
                if (!answer3.getText().toString().equals(cAnswer)) {
                    if (!answers.get(2).equals(cAnswer)) {
                        answer3.setText(answers.get(2));
                        usedNumbers.add(2);
                    } else {
                        answer3.setText(answers.get(randInt(4, answers.size() - 1)));
                        usedNumbers.add(2);
                    }
                }
            } else if (!usedNumbers.contains(3)) {
                if (!answer4.getText().toString().equals(cAnswer)) {
                    if (!answers.get(3).equals(cAnswer)) {
                        answer4.setText(answers.get(3));
                        usedNumbers.add(3);
                    } else {
                        answer4.setText(answers.get(randInt(4, answers.size() - 1)));
                        usedNumbers.add(3);
                    }
                }
            }

            questionNumText.setText("Question "+(currentQuestion+1)+" of "+numOfQuestions);
        }
    }


    //random int generator
    //generates ints between and including min and max.
    public static int randInt(int min, int max) {
        long seed = System.nanoTime();
        Random rand = new Random(seed);
        //generate random int between min and max
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    //this method checks to see if the button that was pressed is the correct one.
    //it compares strings of the buttons and the correct answer to determine if its correct.
    protected void checkAnswer(Button pressedButton) {
        //get current correct answer
        String cAnswer = list.get(currentQuestion).getCorrectAnswer();
        String inputString = pressedButton.getText().toString();
        if (inputString == cAnswer) {
            Toast right = Toast.makeText(getApplicationContext(),"Correct",Toast.LENGTH_SHORT);
            right.show();
            currentAttempts++;
            currentQuestion++;
            //If we're at the end, then show them the AlertDialog
            if (currentQuestion == numOfQuestions) {
                showResults();
            } else {
                //else keep going until we're out of questions.
                resetButtons();
                prepareQuiz();
            }
        }

        else {
            Toast wrong = Toast.makeText(getApplicationContext(),"Incorrect",Toast.LENGTH_SHORT);
            wrong.show();
            currentAttempts++;
            pressedButton.setClickable(false);
            pressedButton.setEnabled(false);
        }

    }

    //This shows the results screen, shown at the very end to give users some statistics and a button to take them back.
    public void showResults() {
        float percent = (numOfQuestions * 100)/currentAttempts;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Quiz Completed!");
        alert.setCancelable(false);
        alert.setInverseBackgroundForced(true);
        alert.setMessage("Congrats, you beat it!" +
                "\nYour name: "+name+
                "\nYour attempts: "+currentAttempts +
                "\nPercentage: %"+percent);
        alert.setNeutralButton("Retake Quiz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();

            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    //This resets the changed properties of the buttons.
    //Because we disable some buttons as they're pressed, we want to change them back to how they originally were.
    public void resetButtons() {
        answer1.setEnabled(true);
        answer1.setClickable(true);
        answer1.setText(R.string.blank_answer);
        answer2.setEnabled(true);
        answer2.setClickable(true);
        answer2.setText(R.string.blank_answer);
        answer3.setEnabled(true);
        answer3.setClickable(true);
        answer3.setText(R.string.blank_answer);
        answer4.setEnabled(true);
        answer4.setClickable(true);
        answer4.setText(R.string.blank_answer);
    }

    //We override the onBackPressed() because we don't want the user to be able to back out of the quiz while it's in progress.
    //It could be expanded with an alertdialog asking them to confirm.
    @Override
    public void onBackPressed() {
    }


}
