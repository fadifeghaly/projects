package com.example.quizwhiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AccessibilityFeatures extends AppCompatActivity {

    private Spinner dropdown;
    private Button saveBtn;
    private Context currentContext;
    private GridLayout checkBoxContainer;
    private View.OnClickListener onClickListener;
    private Spinner dropdownNoms;
    private ArrayList<String> idsProfils, profilAccessibilities;
    private ArrayList<CheckBox> arrayOfAccCheckBox;
    private String ident, acc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessibility_features);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        idsProfils = getIntent().getStringArrayListExtra("idProfils");
        currentContext = this;
        dropdownNoms = findViewById((R.id.dropDownNoms));
        dropdownNoms.setAdapter(new ArrayAdapter<>(currentContext, android.R.layout.simple_spinner_dropdown_item, idsProfils));
        checkBoxContainer = findViewById(R.id.checkbox_container2);
        saveBtn = findViewById(R.id.save);
        Button mButton = findViewById(R.id.search);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxContainer.removeAllViews();
                fetchProfilAccessbilities();
                showAccessibilities();
                saveBtn.setVisibility(View.VISIBLE);
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAccessibilities();
            }
        });

    }


    /**
     * Affiche les adaptations disponibles
     */
    @SuppressLint("SetTextI18n")
    private void showAccessibilities() {
        runOnUiThread(new Runnable() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run() {
                try {
                    arrayOfAccCheckBox = new ArrayList<CheckBox>();
                    CheckBox checkBox;
                    OkHttpClient client = new OkHttpClient();
                    Request scdRequest = new Request.Builder()
                            .url("http://10.0.2.2:8080/api/accessibility")
                            .build();
                    Response res = client.newCall(scdRequest).execute();
                    String jsonData = Objects.requireNonNull(res.body()).string();
                    JSONArray accessibilities = new JSONArray(jsonData);
                    for (int i = 0; i < accessibilities.length(); i++) {
                        JSONObject jsonobject = accessibilities.getJSONObject(i);
                        checkBox = new CheckBox(getApplicationContext());
                        checkBox.setId(i);
                        checkBox.setText(jsonobject.getString("description"));
                        checkBox.setTextSize(20);
                        checkBox.setPadding(10, 10, 10, 10);
                        checkBox.setTag(jsonobject.getString("description"));
                        if (profilAccessibilities.contains(jsonobject.getString("description")))
                            checkBox.setChecked(true);
                        arrayOfAccCheckBox.add(checkBox);
                        TextView identifier = (TextView) findViewById(R.id.identifier);
                        identifier.setText(ident);
                        identifier.setVisibility(View.VISIBLE);
                        checkBoxContainer.addView(checkBox);
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void fetchProfilAccessbilities() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ident = dropdownNoms.getSelectedItem().toString().toLowerCase();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .get()
                        .url("http://10.0.2.2:8080/api/accessibility/" + ident)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONArray access = new JSONArray(response.body().string());
                    profilAccessibilities = new ArrayList<>();
                    for (int i = 0; i < access.length(); i++) {
                        profilAccessibilities.add(access.get(i).toString());
                    }
                } catch (IOException | JSONException e) {
                    System.out.println(e);
                }
            }
        });
    }


    public void updateAccessibilities() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> newAccessibilities = new ArrayList<>();

                for (int i = 0; i < arrayOfAccCheckBox.size(); i++) {
                    if (arrayOfAccCheckBox.get(i).isChecked())
                        newAccessibilities.add(arrayOfAccCheckBox.get(i).getTag().toString());
                }
                JSONArray jsonNewAccessibilities = new JSONArray(newAccessibilities);

                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("identifier", ident)
                        .add("accessibility", String.valueOf(jsonNewAccessibilities))
                        .build();

                Request request = new Request.Builder()
                        .put(formBody)
                        .url("http://10.0.2.2:8080/api/accessibility/update")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.code() == 202) {
                        Toast.makeText(getApplicationContext(), "Enregistrement réussi!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Erreur lors de l'enregistrement!", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}