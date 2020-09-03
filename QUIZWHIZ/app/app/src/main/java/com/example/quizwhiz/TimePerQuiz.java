package com.example.quizwhiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

public class TimePerQuiz extends AppCompatActivity {

    private GridLayout grid;
    private Context currentContext;
    private View.OnClickListener clickListener;
    private Spinner dropdownNoms;
    private ArrayList<String> idProfils;
    TableLayout table;
    GridLayout gridLayout;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_per_quiz);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        idProfils = getIntent().getStringArrayListExtra("idProfils");
        currentContext = this;
        dropdownNoms = findViewById((R.id.dropDownNoms));
        dropdownNoms.setAdapter(new ArrayAdapter<>(currentContext, android.R.layout.simple_spinner_dropdown_item, idProfils));
        gridLayout = findViewById(R.id.gridLayout);
        grid = findViewById(R.id.gridLayout);
        table = findViewById(R.id.table);
        table.setVisibility(View.INVISIBLE);
        Button mButton = findViewById(R.id.search);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridLayout.removeAllViews();
                getDurationList();
            }
        });
    }

    /**
     * http GET : Obtenir la liste du temps passé pour chaque tentative par quiz
     */
    public void getDurationList() {
        grid.setVisibility(View.VISIBLE);
        runOnUiThread(new Runnable() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run() {
                final String ident = dropdownNoms.getSelectedItem().toString().toLowerCase();
                final OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/api/results/" + ident)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String jsonData = Objects.requireNonNull(response.body()).string();
                    final JSONObject jsonObject = new JSONObject(jsonData);
                    int column = 3;
                    int row = jsonObject.length() / column;
                    gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
                    gridLayout.setColumnCount(column);
                    gridLayout.setRowCount(row + 1);
                    clickListener = new View.OnClickListener() {
                        @SuppressLint("SetTextI18n")
                        public void onClick(View v) {
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.nav_default_enter_anim);
                            v.startAnimation(animation);
                            String tag = v.getTag().toString();
                            try {
                                if (null != table.getChildAt(1))
                                    table.removeViewAt(1);
                                Request request = new Request.Builder()
                                        .url("http://10.0.2.2:8080/api/results/timePerQuiz/" + tag + "/" + ident + "/ACC_ON")
                                        .build();
                                Response response = client.newCall(request).execute();
                                String jsonData = Objects.requireNonNull(response.body().string());
                                JSONArray jArray1 = new JSONArray(jsonData);
                                int timeAverageAccON = 0;
                                for (int i = 0; i < jArray1.length(); i++) {
                                    timeAverageAccON += jArray1.getInt(i);
                                }
                                request = new Request.Builder()
                                        .url("http://10.0.2.2:8080/api/results/timePerQuiz/" + tag + "/" + ident + "/ACC_OFF")
                                        .build();
                                response = client.newCall(request).execute();
                                jsonData = Objects.requireNonNull(response.body().string());
                                JSONArray jArray2 = new JSONArray(jsonData);
                                int timeAverageAccOFF = 0;
                                for (int i = 0; i < jArray2.length(); i++) {
                                    timeAverageAccOFF += jArray2.getInt(i);
                                }
                                if (jArray1.length() > 0)
                                    timeAverageAccON = timeAverageAccON / jArray1.length();
                                if (jArray2.length() > 0)
                                    timeAverageAccOFF = timeAverageAccOFF / jArray2.length();
                                if (timeAverageAccON > 0 || timeAverageAccOFF > 0) {
                                    table.setVisibility(View.VISIBLE);
                                    addRow(timeAverageAccON, timeAverageAccOFF, ident);
                                } else {
                                    table.setVisibility(View.INVISIBLE);
                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    drawButtons(jsonObject);
                } catch (IOException | JSONException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    /**
     * Afficher un bouton pour chaque quiz
     */
    public void drawButtons(JSONObject jsonObject) {
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Button button = new Button(currentContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    300,
                    100);
            layoutParams.setMarginEnd(15);
            button.setLayoutParams(layoutParams);
            button.setTag(key);
            button.setBackground(getResources().getDrawable(R.drawable.roundshape2));
            button.setOnClickListener(clickListener);
            button.setTextColor(Color.parseColor("#000000"));
            button.setText(key);
            gridLayout.addView(button);
        }
    }

    /**
     * Afficher la table de la durée moyenne par quiz avec et sans adaptation
     */
    @SuppressLint("SetTextI18n")
    public void addRow(int timeAverageAccON, int timeAverageAccOFF, String identifier) {
        TableRow row = new TableRow(this);
        TextView avgWithoutAF = new TextView(this);
        TextView avgWithAF = new TextView(this);
        if (timeAverageAccON != 0)
            avgWithAF.setText("  " + timeAverageAccON + " sec");
        if (timeAverageAccOFF != 0)
            avgWithoutAF.setText("  " + timeAverageAccOFF + " sec");
        avgWithoutAF.setTextSize(22);
        avgWithoutAF.setTextColor(Color.parseColor("#FFFFFF"));
        avgWithAF.setTextSize(22);
        avgWithAF.setTextColor(Color.parseColor("#FFFFFF"));
        row.addView(avgWithoutAF);
        row.addView(avgWithAF);
        row.setBackgroundResource(R.color.bgColor);
        table.addView(row, 1);
    }
}