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

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

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

public class ScoreEvolution extends AppCompatActivity {

    GridLayout gridLayout;
    private GraphView graph;
    private GridLayout grid;
    private Context currentContext;
    private View.OnClickListener clickListener;
    private Spinner dropdownNoms;
    private ArrayList<String> idsProfils;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_evolution);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        graph = findViewById(R.id.graph);
        idsProfils = getIntent().getStringArrayListExtra("idProfils");
        currentContext = this;
        gridLayout = findViewById(R.id.gridLayout);
        grid = findViewById(R.id.gridLayout);
        dropdownNoms = findViewById((R.id.dropDownNoms));
        dropdownNoms.setAdapter(new ArrayAdapter<>(currentContext, android.R.layout.simple_spinner_dropdown_item, idsProfils));
        hideGraph();
        Button mButton = findViewById(R.id.search);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridLayout.removeAllViews();
                hideGraph();
                getResults();
            }
        });

    }

    /**
     * HTTP GET : Obtenir les scores d'une personne en particulier
     * Communiquer les résultats obtenus avec la méthode 'plot' afin de dessiner le graphe de l'évolution
     * du score de chaque quiz
     */
    public void getResults() {
        grid.setVisibility(View.VISIBLE);
        runOnUiThread(new Runnable() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run() {
                final String ident = dropdownNoms.getSelectedItem().toString().toLowerCase();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/api/results/" + ident)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String jsonData = Objects.requireNonNull(response.body()).string();
                    final JSONObject jsonObject = new JSONObject(jsonData);
                    int graphsNbr = jsonObject.length();
                    int column = 3;
                    int row = graphsNbr / column;
                    gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
                    gridLayout.setColumnCount(column);
                    gridLayout.setRowCount(row + 1);
                    clickListener = new View.OnClickListener() {
                        public void onClick(View v) {
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.nav_default_enter_anim);
                            v.startAnimation(animation);
                            String tag = v.getTag().toString();
                            Iterator<String> keys = jsonObject.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONArray scoreList = null;
                                try {
                                    scoreList = jsonObject.getJSONArray(key);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (key.equals(tag))
                                    plot(scoreList, tag);
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
     * Afficher un bouton pour chaque quiz pour avoir accès au graphique correspondant
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
     * Dessiner le graphique de l'évolution du score d'un quiz en fonction des tentatives
     */
    public void plot(JSONArray results, String label) {
        graph.setVisibility(View.VISIBLE);
        resetGraph();
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setPadding(32);
        gridLabel.setHorizontalAxisTitle("Tentative");
        gridLabel.setVerticalAxisTitle("Score");
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(5);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(4);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        try {
            DataPoint[] values = new DataPoint[results.length()];
            for (int i = 0; i < results.length(); i++) {
                DataPoint dp = new DataPoint(i + 1, results.getInt(i));
                values[i] = dp;
            }
            graph.setTitle(label + " - " + values.length + " tentatives");
            graph.setTitleColor(Color.BLUE);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(values);
            graph.addSeries(series);
        } catch (JSONException e) {
            e.getMessage();
        }
    }

    /**
     * Réinitialiser les données du graphe
     */
    public void resetGraph() {
        graph.removeAllSeries();
    }

    /**
     * Rendre le graphe invisible
     */
    public void hideGraph() {
        graph.setVisibility(View.INVISIBLE);
    }
}
