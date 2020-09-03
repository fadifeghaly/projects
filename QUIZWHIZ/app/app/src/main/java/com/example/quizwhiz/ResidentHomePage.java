package com.example.quizwhiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ResidentHomePage extends AppCompatActivity {

    private String identifier;
    private String name;
    private ArrayList<String> accessibilities;
    private Context currentContext;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_home_page);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        currentContext = this;

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            identifier = extra.getString("identifier");
            name = extra.getString("name");
            accessibilities = extra.getStringArrayList("accessibilities");
        }

        Button games = findViewById(R.id.zonejeux);
        games.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent gamesLobby = new Intent(ResidentHomePage.this, GamesLobby.class);
                gamesLobby.putExtra("identifier", identifier);
                startActivity(gamesLobby);
            }
        });


        ImageView userImage = findViewById(R.id.profil_img_id);
        TextView userName = findViewById(R.id.profil_nom_id);
        CardView cardView = findViewById(R.id.cardview);
        Picasso.get().load("http://10.0.2.2:8080/static/" + identifier + ".png")
                .into(userImage);
        userName.setText(name);
        cardView.setCardBackgroundColor(Color.TRANSPARENT);

        fetchUserQuizzes();
        showTip();
        getLatestScore();
    }

    /**
     * Afficher un message d'aide à l'ouverture de la session
     */
    public void showTip() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AlertDialog);
        dialog.setCancelable(false);
        dialog.setTitle("Bonjour");
        dialog.setMessage("Cliquez sur une des images pour démarrer un quiz");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog tip = dialog.create();
        tip.show();
    }

    /**
     * HTTP GET : Afficher les quiz de la personne aidante selon ses centres d'intérêt
     */
    public void fetchUserQuizzes() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/api/accounts/resident/" + identifier)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String jsonData = Objects.requireNonNull(response.body()).string();
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray pointsOfInterest = jsonObject.getJSONArray("pointOfInterest");

                    GridLayout gridLayout = findViewById(R.id.gridLayout);
                    int QuizzesNbr = pointsOfInterest.length();
                    int column = 3;
                    int row = QuizzesNbr / column;
                    gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
                    gridLayout.setColumnCount(column);
                    gridLayout.setRowCount(row + 1);

                    View.OnClickListener clickListener = new View.OnClickListener() {
                        public void onClick(View v) {
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.nav_default_enter_anim);
                            v.startAnimation(animation);
                            String tag = v.getTag().toString();
                            Intent startQuiz = new Intent(ResidentHomePage.this, QuizSample.class);
                            startQuiz.putExtra("label", tag);
                            startQuiz.putExtra("identifier", identifier);
                            startQuiz.putStringArrayListExtra("accessibilities", accessibilities);
                            startActivityForResult(startQuiz, 1);
                        }
                    };

                    for (int i = 0; i < pointsOfInterest.length(); i++) {
                        ImageView quizLogo = new ImageView(currentContext);
                        quizLogo.setTag(pointsOfInterest.getString(i));
                        quizLogo.setOnClickListener(clickListener);
                        String label = pointsOfInterest.getString(i);
                        label = label.substring(0, 1).toLowerCase() + label.substring(1).toLowerCase();
                        int resID = getResources().getIdentifier(label + "_quiz",
                                "drawable",
                                getPackageName());
                        quizLogo.setBackgroundResource(resID);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                330,
                                330);
                        layoutParams.setMarginEnd(45);
                        quizLogo.setLayoutParams(layoutParams);
                        gridLayout.addView(quizLogo);
                    }
                } catch (IOException | JSONException | NumberFormatException e) {
                    e.getMessage();
                }
            }
        });
    }

    /**
     * HTTP GET : Afficher le dernier score obtenu pour chaque quiz
     */
    public void getLatestScore() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/api/results/" + identifier)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String jsonData = Objects.requireNonNull(response.body()).string();
                    JSONObject quizzesScores = new JSONObject(jsonData);
                    Iterator<String> quizzesLabels = quizzesScores.keys();
                    while (quizzesLabels.hasNext()) {
                        String quizLabel = quizzesLabels.next();
                        JSONArray quizScores = quizzesScores.getJSONArray(quizLabel);
                        if (quizScores.length() > 0)
                            drawScore(Integer.parseInt(quizScores.get(quizScores.length() - 1).toString()),
                                    quizLabel);
                    }
                } catch (IOException | JSONException | NumberFormatException e) {
                    e.getMessage();
                }
            }
        });
    }

    /**
     * Afficher le score sur le logo du quiz correspondant
     */
    public void drawScore(int score, String quizLabel) {
        View currentView = this.findViewById(android.R.id.content);
        ImageView quizLogo = currentView.findViewWithTag(quizLabel);
        quizLabel = quizLabel.substring(0, 1).toLowerCase() + quizLabel.substring(1).toLowerCase();
        int resID = getResources().getIdentifier(quizLabel + "_score" + score,
                "drawable",
                getPackageName());
        quizLogo.setImageResource(resID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getLatestScore();
        TextToSpeech tts = QuizSample.tts;
        if (tts != null)
            tts.stop();
        Handler handler = QuizSample.handler;
        handler.removeCallbacksAndMessages(null);
    }
}
