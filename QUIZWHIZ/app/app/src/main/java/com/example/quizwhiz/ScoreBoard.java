package com.example.quizwhiz;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ScoreBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // HTTP GET : Obtenir les résutats (score) de tous les résidents
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8080/api/results")
                .build();
        try {
            Response response = client.newCall(request).execute();
            fillTable(Objects.requireNonNull(response.body()).string());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Remplir la table ligne par ligne avec les résultats de tous les résidents
     */
    public void fillTable(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            Iterator<String> users = jsonObject.keys();
            int i = 1;
            while (users.hasNext()) {
                String user = users.next();
                JSONObject scoreList = jsonObject.getJSONObject(user);
                Iterator<String> quizzesLabel = scoreList.keys();
                while (quizzesLabel.hasNext()) {
                    String quizLabel = quizzesLabel.next();
                    JSONArray quizScores = scoreList.getJSONArray(quizLabel);
                    if (quizScores.length() > 0) {
                        System.out.println(quizScores);
                        addRow(quizScores.getInt(quizScores.length() - 1), user, quizLabel, i++);
                    }
                }
            }
        } catch (JSONException | NumberFormatException e) {
            e.getMessage();
        }
    }

    /**
     * Afficher la table finale des scores
     */
    @SuppressLint("SetTextI18n")
    public void addRow(int score, String user, String label, int rowNbr) {
        TableLayout table = findViewById(R.id.table);
        TableRow row = new TableRow(this);
        TextView userID = new TextView(this);
        TextView userScore = new TextView(this);
        userID.setText("  " + user);
        userScore.setText("             " + label + " : " + score + "/3");
        userID.setTextSize(22);
        userID.setTextColor(Color.parseColor("#FFFFFF"));
        userScore.setTextSize(22);
        userScore.setTextColor(Color.parseColor("#FFFFFF"));
        row.addView(userID);
        row.addView(userScore);
        row.setBackgroundResource(R.color.bgColor);
        table.addView(row, rowNbr);
    }
}
