package com.example.radhika.mdbmemberquiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.lang.Math;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {
    private String currMember; // member whose image is displayed
    private String[] members = {"Jessica Cherny", "Kevin Jiang",
            "Jared Gutierrez", "Kristin Ho", "Christine Munar",
            "Mudit Mittal", "Richard Hu", "Shaan Appel", "Edward Liu",
            "Wilbur Shi", "Young Lin", "Abhinav Koppu", "Abhishek Mangla",
            "Akkshay Khoslaa", "Andy Wang", "Aneesh Jindal", "Anisha Salunkhe",
            "Ashwin Vaidyanathan", "Cody Hsieh", "Justin Kim", "Krishnan Rajiyah",
            "Lisa Lee", "Peter Schafhalter", "Sahil Lamba", "Sirjan Kafle",
            "Tarun Khasnavis", "Billy Lu", "Aayush Tyagi", "Ben Goldberg",
            "Candice Ye", "Eliot Han", "Emaan Hariri", "Jessica Chen",
            "Katharine Jiang", "Kedar Thakkar", "Leon Kwak", "Mohit Katyal",
            "Rochelle Shen", "Sayan Paul", "Sharie Wang", "Shreya Reddy",
            "Shubham Goenka", "Victor Sun", "Vidya Ravikumar"};
    private Button button0, button1, button2, button3; // buttons to select member name
    private Button endGameButton;
    private TextView scoreText, countDownText;
    private CountDownTimer timer;
    private ImageView memberPic;
    private int score = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        scoreText = (TextView) (findViewById(R.id.scoreField));
        countDownText = (TextView) (findViewById(R.id.countdownText));
        memberPic = (ImageView) (findViewById(R.id.memberPicture));
        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        endGameButton = (Button) findViewById(R.id.endGameButton);
        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        endGameButton.setOnClickListener(this);
        memberPic.setOnClickListener(this);
        nextMember();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void nextMember() {
        String[] options = getOptions();

        // change text on buttons/textviews to reflect current status
        button0.setText(options[0]);
        button1.setText(options[1]);
        button2.setText(options[2]);
        button3.setText(options[3]);
        scoreText.setText("Score: " + score);

        // image
        int currQuizMember = (int) (Math.random() * options.length);
        currMember = options[currQuizMember];
        String imgName = options[currQuizMember].toLowerCase().replace(" ", "");
        int id = getResources().getIdentifier(imgName,
                "drawable", getPackageName());
        memberPic.setImageResource(id);

        // countdown
        timer = new CountDownTimer(6000, 1000) {
            public void onTick(long millisUntilFinished) {
                countDownText.setText("Countdown: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Toast.makeText(getApplicationContext(), "Time's Up!", Toast.LENGTH_SHORT).show();
                nextMember();
            }
        };
        timer.start();
    }

    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.button0:
            case R.id.button1:
            case R.id.button2:
            case R.id.button3:
                timer.cancel();
                if  (((Button)v).getText().equals(currMember)) {
                    score+=1;
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong!", Toast.LENGTH_SHORT).show();
                }
                nextMember();
                break;
            case R.id.endGameButton:
                timer.cancel();
                new AlertDialog.Builder(QuizActivity.this)
                        .setTitle("End Game")
                        .setMessage("Are you sure you want to end the game?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Whatever...
                                score = 0;
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                nextMember();
                            }
                        }).show();
                break;
            case R.id.memberPicture:
                timer.cancel();
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                intent.putExtra(ContactsContract.Intents.Insert.NAME, currMember);
                startActivity(intent);
                break;
        }

    }
    // get an array with 4 member names that will act as multiple choice options for user
    private String[] getOptions() {
        String[] options = new String[4];
        for (int i = 0; i < options.length; i++) {
            while (options[i] == null) {
                int rand = (int) (Math.random() * members.length);
                if (!contains(members[rand], options)) {
                    options[i] = members[rand];
                }
            }
        }
        return options;
    }

    // checks if String x is in array options
    private boolean contains(String x, String[] options) {
        for (int i = 0; i < options.length; i++) {
            if (options[i] != null && options[i].equals(x)) {
                return true;
            }
        }
        return false;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Quiz Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }
    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    //NOTE: should be onResume but that makes weird things do
    @Override
    public void onRestart() {
        super.onResume();
        nextMember();
    }
    @Override
    public void onStop() {
        super.onStop();
        timer.cancel();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
