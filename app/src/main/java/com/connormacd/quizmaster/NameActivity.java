package com.connormacd.quizmaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NameActivity extends Activity {

    int REQUEST_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        REQUEST_CODE = 101;
        final Intent i = new Intent(this, QuizActivity.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        Button submitButton = (Button) findViewById(R.id.btnAnswer4);
        final EditText nameText = (EditText) findViewById(R.id.editText);


        View.OnClickListener listen = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nameText.getText().toString().matches("")) {
                    i.putExtra("name", nameText.getText().toString());
                    startActivity(i);
                } else {
                    Toast t = Toast.makeText(getApplicationContext(), "Don't have a name? Be creative!", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        };

        submitButton.setOnClickListener(listen);
    }
}
