package com.example.quizwhiz;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BrainBox extends AppCompatActivity {

    private final int NBR_QUESTION = 8;
    private Spinner choice;
    private String selectedCountry;
    private TextView score;
    private TextView titre, quest, questionCounter;
    private int correctAnswer, playerScore, pageCounter;
    private Button nextBtn;
    private String choix1;
    private String choix2;
    private String choix3;
    private RadioGroup radioGroup;
    private RadioButton r1, r2, r3;
    private JSONArray questionsList;
    private Boolean anim = true;
    private GridLayout grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brain_box);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String countries[] = {"Canada", "Royaume-Uni", "États-Unis"};

        final ImageView country = findViewById(R.id.preview);

        choice = findViewById((R.id.choice));
        choice.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, countries));
        grid = findViewById(R.id.Questions);
        quest = findViewById(R.id.question);
        titre = findViewById(R.id.title);


        choice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountry = choice.getSelectedItem().toString().toLowerCase();
                selectedCountry = selectedCountry.replace("-", "_");
                selectedCountry = selectedCountry.replace("é", "e");
                showCard();
                country.setVisibility(View.VISIBLE);
                quest.setVisibility(View.GONE);
                grid.setVisibility(View.GONE);
                questionCounter.setVisibility(View.GONE);
                score.setVisibility(View.GONE);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        playerScore = 0;
                        score.setText("Score : " + playerScore);
                        country.setVisibility(View.GONE);
                        titre.setVisibility(View.GONE);
                        choice.setVisibility(View.GONE);
                        questionCounter.setVisibility(View.VISIBLE);
                        score.setVisibility(View.VISIBLE);
                        quest.setVisibility(View.VISIBLE);
                        grid.setVisibility(View.VISIBLE);
                        nextBtn.setBackground(getResources().getDrawable(R.drawable.suivant1));
                        fetchQuestions();
                        nextQuestion(pageCounter);
                    }
                }, 30000);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        nextBtn = findViewById(R.id.nextQuestion);
        questionCounter = findViewById(R.id.questionCounter);
        score = findViewById(R.id.score);
        radioGroup = findViewById(R.id.choices);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                enableRadioBtn();
                if (pageCounter < NBR_QUESTION && isAnswered()) {
                    anim = false;
                    radioGroup.clearCheck();
                    anim = true;
                    enableRadioBtn();
                    nextQuestion(++pageCounter);
                    if (pageCounter < NBR_QUESTION)
                        questionCounter.setText("Question : " + (pageCounter + 1) + "/" + NBR_QUESTION);
                }
                if (pageCounter == NBR_QUESTION - 1)
                    nextBtn.setBackground(getResources().getDrawable(R.drawable.home1));
                if (pageCounter == NBR_QUESTION) {
                    questionCounter.setVisibility(View.GONE);
                    score.setVisibility(View.GONE);
                    quest.setVisibility(View.GONE);
                    grid.setVisibility(View.GONE);
                    titre.setVisibility(View.VISIBLE);
                    choice.setVisibility(View.VISIBLE);
                    pageCounter = 0;
                    questionCounter.setText("Question : " + 1 + "/" + NBR_QUESTION);
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.nav_default_enter_anim);
                if (null != findViewById(checkedId) && anim) {
                    findViewById(checkedId).startAnimation(animation);
                    showSolution();
                    disableRadioBtn();
                } else {
                    if (correctAnswer != -1) {
                        showSolution();
                        disableRadioBtn();
                        answerIsValid(correctAnswer);
                    }
                    correctAnswer = -1;
                }
            }
        });
    }

    public void showCard() {
        final ImageView country = findViewById(R.id.preview);
        int resID = getResources().getIdentifier(selectedCountry,
                "drawable",
                getPackageName());
        country.setImageResource(resID);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                country.setVisibility(View.GONE);
            }
        }, 30000);
    }

    public void fetchQuestions() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/api/quizzes/" + selectedCountry)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String jsonData = Objects.requireNonNull(response.body()).string();
                    JSONObject Jobject = new JSONObject(jsonData);
                    questionsList = Jobject.getJSONArray("questionsList");
                } catch (IOException | JSONException e) {
                    System.out.println(e);
                }
            }
        });
    }

    public boolean isAnswered() {
        if (!r1.isChecked() && !r2.isChecked() && !r3.isChecked()) {
            Toast.makeText(getApplicationContext(),
                    "Aucune réponse sélectionnée", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @SuppressLint("SetTextI18n")
    public Boolean answerIsValid(int answerID) {
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        View radioButton = radioGroup.findViewById(radioButtonID);
        int idx = radioGroup.indexOfChild(radioButton);
        if (idx + 1 == answerID) {
            score.setText("Score : " + ++playerScore);
            return true;
        } else {
            return false;
        }
    }

    public void showSolution() {
        r1.setTextColor(Color.RED);
        r2.setTextColor(Color.RED);
        r3.setTextColor(Color.RED);
        if (correctAnswer == 1)
            r1.setTextColor(Color.parseColor("#3da157"));
        else if (correctAnswer == 2)
            r2.setTextColor(Color.parseColor("#3da157"));
        else
            r3.setTextColor(Color.parseColor("#3da157"));
        answerIsValid(correctAnswer);
        correctAnswer = -1;
    }

    public void resetColor() {
        r1.setTextColor(Color.BLACK);
        r2.setTextColor(Color.BLACK);
        r3.setTextColor(Color.BLACK);
    }

    public void nextQuestion(final int pageCounter) {
        if (pageCounter > 0)
            resetColor();
        try {
            if (pageCounter < questionsList.length()) {
                String question = questionsList.getJSONObject(pageCounter).getString("question");
                choix1 = questionsList.getJSONObject(pageCounter).getString("choice1");
                choix2 = questionsList.getJSONObject(pageCounter).getString("choice2");
                choix3 = questionsList.getJSONObject(pageCounter).getString("choice3");
                correctAnswer = Integer.parseInt(questionsList.getJSONObject(pageCounter).getString("correctAnswer"));
                TextView quest = findViewById(R.id.question);
                quest.setText(question);
                r1 = ((RadioButton) radioGroup.getChildAt(0));
                r1.setText(choix1);
                r2 = ((RadioButton) radioGroup.getChildAt(1));
                r2.setText(choix2);
                r3 = ((RadioButton) radioGroup.getChildAt(2));
                r3.setText(choix3);
                if (choix3.isEmpty())
                    r3.setVisibility(View.INVISIBLE);
                else
                    r3.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }
    }

    public void disableRadioBtn() {
        r1.setClickable(false);
        r2.setClickable(false);
        r3.setClickable(false);
    }

    public void enableRadioBtn() {
        r1.setClickable(true);
        r2.setClickable(true);
        r3.setClickable(true);
    }

}