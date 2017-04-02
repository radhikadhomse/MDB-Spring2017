package com.example.radhika.mdbmemberquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* your code here */
        Button startButton = (Button) findViewById(R.id.startButton);
        // R links Java to XML
        //Question 3: find a way for the Button to LISTEN ON the CLICKs. Use Google for guidance.

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Question 4: every time the button is clicked, create and execute an Intent to go to the
                // OtherClass.
                Intent quizIntent = new Intent(getApplicationContext(), QuizActivity.class);
                startActivity(quizIntent); // pauses current activity and starts the otheractivity
            }
        });
    }
}
