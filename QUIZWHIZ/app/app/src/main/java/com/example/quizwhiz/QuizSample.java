package com.example.quizwhiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QuizSample extends AppCompatActivity implements SensorEventListener {

    public static TextToSpeech tts;
    public static Handler handler;
    private final int NBR_QUESTION = 3;
    Boolean correctorIsActivated = true;
    Sensor accelerometer;
    private int correctAnswer, playerScore, pageCounter;
    private Button nextBtn, ttsBtn;
    private String label, identifier, question, choix1, choix2, choix3;
    private ArrayList<String> accessibilities;
    private TextView quest, score, questionCounter;
    private RadioGroup radioGroup;
    private RadioButton r1, r2, r3;
    private JSONArray questionsList;
    private long timePerQuiz, time;
    private JSONArray contextData = new JSONArray();
    private Boolean anim = true;
    private SensorManager sensorManager;
    private Runnable unregisterListener = new Runnable() {
        @Override
        public void run() {
            sensorManager.unregisterListener(QuizSample.this, accelerometer);
            handler.postDelayed(registerListener, 900);
        }
    };
    /**
     * Active et désactive le capteur afin de capter une fois par seconde
     */
    private Runnable registerListener = new Runnable() {
        @Override
        public void run() {
            sensorManager.registerListener(QuizSample.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            handler.postDelayed(unregisterListener, 100);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_sample);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle extra = getIntent().getExtras();
        accessibilities = extra.getStringArrayList("accessibilities");

        checkDMLA();
        checkTTS();
        checkCorrector();

        handler = new Handler();

        handler.post(registerListener);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        timePerQuiz = System.currentTimeMillis();
        nextBtn = findViewById(R.id.nextQuestion);
        questionCounter = findViewById(R.id.questionCounter);
        score = findViewById(R.id.score);
        if (extra != null)
            label = extra.getString("label");
        fetchQuestions();
        nextQuestion(pageCounter);

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
                    if (null != tts && pageCounter < 3)
                        activateTTS();
                    if (pageCounter < NBR_QUESTION)
                        questionCounter.setText("Question : " + (pageCounter + 1) + "/" + NBR_QUESTION);
                }
                if (pageCounter == NBR_QUESTION - 1)
                    nextBtn.setBackground(getResources().getDrawable(R.drawable.home));
                if (pageCounter == NBR_QUESTION) {
                    time = Math.round(System.currentTimeMillis() - timePerQuiz) / 1000;
                    logTime();
                    postScore();
                    disableRadioBtn();
                    postContextData();
                    sensorManager.unregisterListener(QuizSample.this, accelerometer);
                    handler.removeCallbacks(registerListener);
                    handler.removeCallbacks(unregisterListener);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onBackPressed();
                        }
                    }, 2000);
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.nav_default_enter_anim);
                if (null != findViewById(checkedId) && anim)
                    findViewById(checkedId).startAnimation(animation);
                if (correctorIsActivated) {
                    showSolution();
                    disableRadioBtn();
                } else if (!answerIsValid(correctAnswer) && correctAnswer != -1) {
                    dontGiveUp("Vous êtes proche!");
                } else {
                    if (correctAnswer != -1) {
                        dontGiveUp("BRAVO!");
                        showSolution();
                        disableRadioBtn();
                    }
                    correctAnswer = -1;
                }
            }
        });
        ttsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateTTS();
            }
        });
    }

    private void checkDMLA() {
        LayoutInflater I = getLayoutInflater();
        FrameLayout f = (FrameLayout) findViewById(R.id.framelayout);
        View v;
        if (accessibilities.contains("DMLA")) {
            v = I.inflate(R.layout.quiz_dmla, null);
        } else {
            v = I.inflate(R.layout.quiz_default, null);
        }
        f.addView(v);
    }

    private void checkCorrector() {
        if (accessibilities.contains("Désactiver la correction")) {
            correctorIsActivated = false;
        } else {
            correctorIsActivated = true;
        }
    }

    private void checkTTS() {
        ttsBtn = findViewById(R.id.tts);

        if (accessibilities.contains("TTS")) {
            ttsBtn.setVisibility(View.VISIBLE);
            tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        tts.setLanguage(Locale.CANADA_FRENCH);
                        activateTTS();
                    }
                }
            });
        } else {
            ttsBtn.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Afficher un msg d'encouragement
     */
    public void dontGiveUp(String msg) {
        final AlertDialog dialog = new AlertDialog.Builder(QuizSample.this).setMessage(msg).show();
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

    /**
     * Démarrer le 'text to speech'
     */
    public void activateTTS() {
        tts.speak(question, TextToSpeech.QUEUE_ADD, null);
        tts.playSilence(1000, TextToSpeech.QUEUE_ADD, null);
        tts.speak("choix 1", TextToSpeech.QUEUE_ADD, null);
        tts.playSilence(300, TextToSpeech.QUEUE_ADD, null);
        tts.speak(choix1, TextToSpeech.QUEUE_ADD, null);
        tts.playSilence(1000, TextToSpeech.QUEUE_ADD, null);
        tts.speak("choix 2", TextToSpeech.QUEUE_ADD, null);
        tts.playSilence(300, TextToSpeech.QUEUE_ADD, null);
        tts.speak(choix2, TextToSpeech.QUEUE_ADD, null);
        tts.playSilence(1000, TextToSpeech.QUEUE_ADD, null);
        tts.speak("choix 3", TextToSpeech.QUEUE_ADD, null);
        tts.playSilence(300, TextToSpeech.QUEUE_ADD, null);
        tts.speak(choix3, TextToSpeech.QUEUE_ADD, null);
    }

    /**
     * HTTP POST : Sauvegarder la durée du quiz
     */
    public void logTime() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                String accFlag;
                if (accessibilities.size() != 0)
                    accFlag = "ACC_ON";
                else
                    accFlag = "ACC_OFF";
                RequestBody formBody = new FormBody.Builder()
                        .add("identifier", identifier)
                        .add("label", label)
                        .add("time", Long.toString(time))
                        .add("accFlag", accFlag)
                        .build();
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/api/results/timePerQuiz")
                        .post(formBody)
                        .build();
                try {
                    client.newCall(request).execute();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    /**
     * HTTP GET : Obtenir les questions d'un quiz donné à partir du serveur
     */
    public void fetchQuestions() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/api/quizzes/" + label)
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

    /**
     * Vérifier si au moins une réponse a été sélectionnée
     */
    public boolean isAnswered() {
        if (!r1.isChecked() && !r2.isChecked() && !r3.isChecked()) {
            Toast.makeText(getApplicationContext(),
                    "Aucune réponse sélectionnée", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Vérifier si la réponse choisie est la bonne
     */
    @SuppressLint("SetTextI18n")
    public Boolean answerIsValid(int answerID) {
        String reponse;
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        View radioButton = radioGroup.findViewById(radioButtonID);
        int idx = radioGroup.indexOfChild(radioButton);
        if (idx + 1 == 1) reponse = choix1;
        else if (idx + 1 == 2) reponse = choix2;
        else reponse = choix3;
        if (idx + 1 == answerID) {
            score.setText("Score : " + ++playerScore);
            if (null != tts)
                tts.stop();
            logAnswer("correct", reponse);
            return true;
        } else {
            if (correctAnswer != -1)
                logAnswer("wrong", reponse);
            return false;
        }
    }

    /**
     * Afficher la bonne réponse en vert lorsque le bouton suivant est appuyé
     */
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
        if (correctorIsActivated)
            answerIsValid(correctAnswer);
        if (null != tts)
            tts.stop();
        correctAnswer = -1;
    }

    /**
     * Remettre la couleur des réponses en noir pour la prochaine question
     */
    public void resetColor() {
        r1.setTextColor(Color.BLACK);
        r2.setTextColor(Color.BLACK);
        r3.setTextColor(Color.BLACK);
    }

    /**
     * Afficher la prochaine question avec les réponses correspondantes
     */
    public void nextQuestion(final int pageCounter) {
        if (pageCounter > 0)
            resetColor();
        try {
            if (pageCounter < questionsList.length()) {
                question = questionsList.getJSONObject(pageCounter).getString("question");
                choix1 = questionsList.getJSONObject(pageCounter).getString("choice1");
                choix2 = questionsList.getJSONObject(pageCounter).getString("choice2");
                choix3 = questionsList.getJSONObject(pageCounter).getString("choice3");
                correctAnswer = Integer.parseInt(questionsList.getJSONObject(pageCounter).getString("correctAnswer"));
                quest = findViewById(R.id.question);
                quest.setText(question);
                radioGroup = findViewById(R.id.choices);
                r1 = ((RadioButton) radioGroup.getChildAt(0));
                r1.setText(choix1);
                r2 = ((RadioButton) radioGroup.getChildAt(1));
                r2.setText(choix2);
                r3 = ((RadioButton) radioGroup.getChildAt(2));
                r3.setText(choix3);
            }
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * HTTP POST : Enregistrer le score de la personne lorsque le quiz est terminé
     */
    public void postScore() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bundle extra = getIntent().getExtras();
                if (extra != null)
                    identifier = extra.getString("identifier");
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("identifier", identifier)
                        .add("label", label)
                        .add("score", Integer.toString(playerScore))
                        .build();
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/api/results")
                        .post(formBody)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.getMessage();
                }
                if (response.code() == 200)
                    Toast.makeText(getApplicationContext(), "Mise à jour du score réussie!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Sauvegarder la réponse choisie
     */
    public void logAnswer(final String answerFlag, final String reponse) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bundle extra = getIntent().getExtras();
                if (extra != null)
                    identifier = extra.getString("identifier");
                OkHttpClient client = new OkHttpClient();
                String accFlag;
                if (accessibilities.size() != 0)
                    accFlag = "ACC_ON";
                else
                    accFlag = "ACC_OFF";
                RequestBody formBody = new FormBody.Builder()
                        .add("identifier", identifier)
                        .add("label", label)
                        .add("question", question + "\nRéponse : " + reponse)
                        .add("answerFlag", answerFlag)
                        .add("accFlag", accFlag)
                        .build();
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/api/results/logAnswers/" + identifier)
                        .post(formBody)
                        .build();
                try {
                    client.newCall(request).execute();
                } catch (IOException e) {
                    e.getMessage();
                }
            }
        });
    }

    /**
     * Augmenter la police de caractère pour une meilleure visibilité
     */
    public void increaseTextSize(View v) {
        quest.setTextSize(30);
        score.setTextSize(30);
        questionCounter.setTextSize(30);
        r1.setTextSize(30);
        r2.setTextSize(30);
        r3.setTextSize(30);
    }

    /**
     * Réduire la police
     */
    public void decreaseTextSize(View v) {
        quest.setTextSize(25);
        score.setTextSize(25);
        questionCounter.setTextSize(25);
        r1.setTextSize(50);
        r2.setTextSize(25);
        r3.setTextSize(25);
    }

    /**
     * Désactiver les boutons 'radio', une fois une réponse est choisie
     */
    public void disableRadioBtn() {
        r1.setClickable(false);
        r2.setClickable(false);
        r3.setClickable(false);
    }

    /**
     * Activer les boutons 'radio'
     */
    public void enableRadioBtn() {
        r1.setClickable(true);
        r2.setClickable(true);
        r3.setClickable(true);
    }

    /**
     * Mise en place des capteurs
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        JSONObject data = new JSONObject();
        try {
            data.put("Timestamp", System.currentTimeMillis());
            data.put("X", event.values[0]);
            data.put("Y", event.values[1]);
            data.put("Z", event.values[2]);
            contextData.put(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    private void postContextData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("identifier", identifier)
                        .add("data", String.valueOf(contextData))
                        .build();
                Request requestBody = new Request.Builder()
                        .url("http://10.0.2.2:8080/api/results/data")
                        .put(formBody)
                        .build();

                Response response = null;
                try {
                    response = client.newCall(requestBody).execute();
                    response.close();
                    Log.d("rep :", String.valueOf(response));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(QuizSample.this, accelerometer);
    }
}

