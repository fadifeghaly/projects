package com.example.quizwhiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HelpersHomePage extends AppCompatActivity {

    private ArrayList<String> idProfils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper_home_page);
    }

    // redirection vers la page qui affiche les scores de tous les résidents
    public void viewAllScores(View v) {
        Intent resultIntent = new Intent(HelpersHomePage.this, ScoreBoard.class);
        startActivity(resultIntent);
    }

    // redirection vers la page qui affiche l'évolution du score d'une personne résidente
    public void viewScoreById(View v) {
        Intent resultIntent = new Intent(HelpersHomePage.this, ScoreEvolution.class);
        fetchProfils();
        resultIntent.putStringArrayListExtra("idProfils", idProfils);
        startActivity(resultIntent);
    }

    // redirection vers le formulaire nécesssaire pour inscrire des nouveaux utilisateurs
    public void subscribreResident(View v) {
        Intent resultIntent = new Intent(HelpersHomePage.this, NewResident.class);
        fetchProfils();
        startActivity(resultIntent);
    }

    // redirection vers la page qui affiche le taux de réussite/échec de chaque quiz
    public void answersLogger(View v) {
        Intent resultIntent = new Intent(HelpersHomePage.this, AnswersLogger.class);
        fetchProfils();
        resultIntent.putStringArrayListExtra("idProfils", idProfils);
        startActivity(resultIntent);
    }

    // redirection vers la page qui affiche le temps moyen par quiz
    public void durationLogger(View v) {
        Intent resultIntent = new Intent(HelpersHomePage.this, TimePerQuiz.class);
        fetchProfils();
        resultIntent.putStringArrayListExtra("idProfils", idProfils);
        startActivity(resultIntent);
    }

    // redirection vers la page responsable d'activer ou de désactiver une/des adaptation(s)
    public void accessibilityPicker(View v) {
        Intent resultIntent = new Intent(HelpersHomePage.this, AccessibilityFeatures.class);
        fetchProfils();
        resultIntent.putStringArrayListExtra("idProfils", idProfils);
        startActivity(resultIntent);
    }

    public void onBackPressed() {
        Intent exit = new Intent(HelpersHomePage.this, WelcomePage.class);
        startActivity(exit);
    }

    public void fetchProfils() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .get()
                        .url("http://10.0.2.2:8080/api/accounts/residents")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONArray profils = new JSONArray(response.body().string());
                    idProfils = new ArrayList<>();
                    for (int i = 0; i < profils.length(); i++) {
                        JSONObject profil = profils.getJSONObject(i);
                        idProfils.add(profil.getString("identifier"));
                    }
                } catch (IOException | JSONException e) {
                    System.out.println(e);
                }
            }
        });
    }
}
