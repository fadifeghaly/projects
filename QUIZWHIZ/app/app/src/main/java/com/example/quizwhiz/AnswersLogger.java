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

import org.apache.commons.math3.util.Precision;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AnswersLogger extends AppCompatActivity {

    TableLayout table;
    GridLayout gridLayout;
    private GridLayout grid;
    private Context currentContext;
    private View.OnClickListener clickListener;
    private Spinner dropdownNoms;
    private ArrayList<String> idProfils;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers_logger);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        idProfils = getIntent().getStringArrayListExtra("idProfils");
        currentContext = this;
        gridLayout = findViewById(R.id.gridLayout);
        grid = findViewById(R.id.gridLayout);
        dropdownNoms = findViewById((R.id.dropDownNoms));
        dropdownNoms.setAdapter(new ArrayAdapter<>(AnswersLogger.this, android.R.layout.simple_spinner_dropdown_item, idProfils));
        table = findViewById(R.id.table);
        table.setVisibility(View.INVISIBLE);
        Button mButton = findViewById(R.id.search);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table.setVisibility(View.INVISIBLE);
                gridLayout.removeAllViews();
                getAnswers();
            }
        });
    }

    /**
     * HTTP GET des réponses d'un(e) résident(e) du serveur
     */
    public void getAnswers() {
        grid.setVisibility(View.VISIBLE);
        runOnUiThread(new Runnable() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run() {
                final String ident = dropdownNoms.getSelectedItem().toString().toLowerCase();
                ;
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
                                        .url("http://10.0.2.2:8080/api/results/answers/" + ident + "/" + tag + "/correct/ACC_ON")
                                        .build();
                                Response response = client.newCall(request).execute();
                                String jsonData = Objects.requireNonNull(response.body()).string();
                                JSONObject jsonObject1 = new JSONObject(jsonData);

                                request = new Request.Builder()
                                        .url("http://10.0.2.2:8080/api/results/answers/" + ident + "/" + tag + "/correct/ACC_OFF")
                                        .build();
                                response = client.newCall(request).execute();
                                jsonData = Objects.requireNonNull(response.body()).string();
                                JSONObject jsonObject2 = new JSONObject(jsonData);

                                request = new Request.Builder()
                                        .url("http://10.0.2.2:8080/api/results/answers/" + ident + "/" + tag + "/wrong/ACC_ON")
                                        .build();
                                response = client.newCall(request).execute();
                                jsonData = Objects.requireNonNull(response.body()).string();
                                JSONObject jsonObject3 = new JSONObject(jsonData);

                                request = new Request.Builder()
                                        .url("http://10.0.2.2:8080/api/results/answers/" + ident + "/" + tag + "/wrong/ACC_OFF")
                                        .build();
                                response = client.newCall(request).execute();
                                jsonData = Objects.requireNonNull(response.body()).string();
                                JSONObject jsonObject4 = new JSONObject(jsonData);

                                double len1 = jsonObject1.getJSONArray(tag).length();
                                double len2 = jsonObject2.getJSONArray(tag).length();
                                double len3 = jsonObject3.getJSONArray(tag).length();
                                double len4 = jsonObject4.getJSONArray(tag).length();
                                if (len1 == 0 && len2 == 0 && len3 == 0 && len4 == 0) {
                                    table.setVisibility(View.INVISIBLE);
                                } else {
                                    addRow(Precision.round(((len1 / (len1 + len3)) * 100), 2),
                                            Precision.round(((len3 / (len1 + len3)) * 100), 2),
                                            Precision.round(((len2 / (len2 + len4)) * 100), 2),
                                            Precision.round(((len4 / (len2 + len4)) * 100), 2));
                                    table.setVisibility(View.VISIBLE);
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
     * Afficher la table de la durée moyenne par quiz avec et sans adaptation
     */
    @SuppressLint("SetTextI18n")
    public void addRow(double correctAnswerRatioAccON,
                       double wrongAnswerRatioAccON,
                       double correctAnswerRatioAccOFF,
                       double wrongAnswerRatioAccOFF) {

        TableRow row = new TableRow(this);
        TextView avgWithoutAF = new TextView(this);
        TextView avgWithAF = new TextView(this);
        avgWithoutAF.setTextSize(20);
        avgWithoutAF.setTextColor(Color.parseColor("#FFFFFF"));
        if (!Double.isNaN(correctAnswerRatioAccOFF))
            avgWithoutAF.setText("✅ " + correctAnswerRatioAccOFF + " %\n❌ " + wrongAnswerRatioAccOFF + " %");
        avgWithAF.setTextSize(20);
        avgWithAF.setTextColor(Color.parseColor("#FFFFFF"));
        if (!Double.isNaN(correctAnswerRatioAccON))
            avgWithAF.setText("✅ " + correctAnswerRatioAccON + " %\n❌ " + wrongAnswerRatioAccON + " %");
        row.addView(avgWithoutAF);
        row.addView(avgWithAF);
        row.setBackgroundResource(R.color.bgColor);
        table.addView(row, 1);
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
}