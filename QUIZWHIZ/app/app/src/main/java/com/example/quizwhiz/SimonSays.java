package com.example.quizwhiz;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SimonSays extends AppCompatActivity {


    final Handler handler = new Handler();
    private List<Button> buttons = new ArrayList<>();
    private List<String> colors = new ArrayList<>();
    private List<Integer> expectedColors = new ArrayList<>();
    private List<Integer> chosenColors = new ArrayList<>();
    private Button green, red, yellow, blue;
    private int clickCounter = 0;
    private int difficulty = 1;
    private Thread t;
    private int playersScore;
    private TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simon_says);

        score = findViewById(R.id.score);

        green = findViewById(R.id.green);
        green.setTag(0);
        red = findViewById(R.id.red);
        red.setTag(1);
        yellow = findViewById(R.id.yellow);
        yellow.setTag(2);
        blue = findViewById(R.id.blue);
        blue.setTag(3);

        green.setOnClickListener(onClickListener);
        red.setOnClickListener(onClickListener);
        yellow.setOnClickListener(onClickListener);
        blue.setOnClickListener(onClickListener);

        disableButtons();

        buttons.addAll(Arrays.asList(green, red, yellow, blue));
        colors.addAll(Arrays.asList("green", "red", "yellow", "blue"));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                shuffle();
                enableButtons();
            }
        }, 3000);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {
            clickCounter++;
            chosenColors.add((Integer) v.getTag());
            if (clickCounter == difficulty) {
                if (answerIsValid()) {
                    ++playersScore;
                    popUp("Bravo!");
                    score.setText("Score : " + playersScore);
                    difficulty++;
                } else {
                    popUp("Vous êtes capable!");
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        shuffle();
                    }
                }, 2000);
            }
        }
    };

    public void shuffle() {
        clickCounter = 0;
        t = new Thread() {
            @Override
            public void run() {
                try {
                    Random rand = new Random();
                    for (int i = 0; i < difficulty; i++) {
                        final int n = rand.nextInt(4);
                        expectedColors.add(n);
                        final Button randomBtn = buttons.get(n);
                        int resID = getResources().getIdentifier(colors.get(n) + "on",
                                "drawable",
                                getPackageName());
                        randomBtn.setBackgroundResource(resID);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int resID = getResources().getIdentifier(colors.get(n) + "off",
                                        "drawable",
                                        getPackageName());
                                randomBtn.setBackgroundResource(resID);
                            }
                        }, 900);
                        sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.getCause();
                }
            }
        };
        t.start();
    }

    public void popUp(String msg) {
        final AlertDialog dialog = new AlertDialog.Builder(SimonSays.this).setMessage(msg).show();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DCF0FF")));
        TextView textView = dialog.findViewById(android.R.id.message);
        textView.setTextSize(40);
        textView.setTextColor(Color.BLACK);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                dialog.dismiss();
                t.cancel();
            }
        }, 1500);
    }

    public boolean answerIsValid() {
        boolean check = expectedColors.equals(chosenColors);
        expectedColors.clear();
        chosenColors.clear();
        return check;
    }

    public void enableButtons() {
        green.setEnabled(true);
        red.setEnabled(true);
        yellow.setEnabled(true);
        blue.setEnabled(true);
    }

    public void disableButtons() {
        green.setEnabled(false);
        red.setEnabled(false);
        yellow.setEnabled(false);
        blue.setEnabled(false);
    }

    // TODO
    // POST le score de chaque indent
}