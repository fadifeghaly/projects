package com.example.quizwhiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WelcomePage extends AppCompatActivity {

    List<Profil> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        fetchProfils();

        if (!mData.isEmpty()) {
            RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
            RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, mData);
            myrv.setLayoutManager(new GridLayoutManager(this, 3));
            myrv.setAdapter(myAdapter);
        }
    }

    public void helperHome(View v) {
        final HelperAuthentication pageConnexion = new HelperAuthentication();
        pageConnexion.show(getSupportFragmentManager(), "Page Connexion");
    }

    public void onBackPressed() {
        Intent exit = new Intent(Intent.ACTION_MAIN);
        exit.addCategory(Intent.CATEGORY_HOME);
        exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
                    mData = new ArrayList<>();
                    for (int i = 0; i < profils.length(); i++) {
                        JSONObject profil = profils.getJSONObject(i);
                        JSONArray accessJson = profil.getJSONArray("accessibilities");
                        ArrayList<String> accessArray = new ArrayList<>();
                        for (int j = 0; j < accessJson.length(); j++) {
                            accessArray.add(accessJson.get(j).toString());
                        }
                        Profil p = new Profil(profil.getString("identifier"),
                                profil.getString("name"),
                                accessArray);
                        mData.add(p);
                    }
                } catch (IOException | JSONException e) {
                    System.out.println(e);
                }
            }
        });
    }

}
