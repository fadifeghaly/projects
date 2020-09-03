package com.example.quizwhiz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewResident extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 2342;
    private static final int PICK_IMAGE_REQUEST = 22;
    private Button buttonChoose;
    private Button buttonCreate;
    private ImageView imageView;
    private String name;
    private String identifier;
    private ArrayList<CheckBox> arrayOfCheckBox, arrayOfAccCheckBox;
    private Uri filePath;
    private String realPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_resident);
        requestStoragePermission();
        showLabels();
        buttonChoose = findViewById(R.id.buttonChoose);
        buttonCreate = findViewById(R.id.buttonCreate);
        buttonCreate.setEnabled(false);
        imageView = findViewById(R.id.image);
        final EditText nameField = findViewById(R.id.nom);
        final EditText idField = findViewById(R.id.identifier);
        name = nameField.getText().toString();
        identifier = idField.getText().toString();
        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChosen();
            }
        });
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameField.getText().toString();
                identifier = idField.getText().toString();
                ArrayList<String> pointsofInterest = new ArrayList<>();
                if (residentFieldsAreFilled()) {
                    for (int i = 0; i < arrayOfCheckBox.size(); i++) {
                        if (arrayOfCheckBox.get(i).isChecked()) {
                            pointsofInterest.add(arrayOfCheckBox.get(i).getTag().toString());
                        }
                    }
                    JSONArray jsonPointInterest = new JSONArray(pointsofInterest);
                    uploadResidentInfo(name, identifier, String.valueOf(jsonPointInterest));

                }
            }
        });

    }

    /**
     * Demande la permission d'accéder à la gallerie de l'appareil.
     */
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Lance l'action de choisir une image dans la gallerie de l'appareil
     */
    private void showFileChosen() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    /**
     * Affiche l'image choisie
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            imageView.setImageURI(filePath);
            buttonCreate.setEnabled(true);
            realPath = getRealPath(data.getData());
        }
    }

    /**
     * Retourne l'emplacement absolu de l'image dans l'appareil
     */
    public String getRealPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        } else {
            cursor.moveToFirst();
            int id = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(id);
        }
    }


    /**
     * Affiche les choix de points d'intérêts selon les quizs disponibles
     */
    private void showLabels() {
        GridLayout checkBoxContainer = findViewById(R.id.checkbox_container1);
        CheckBox checkBox;

        //Récupère la liste des quizs disponibles
        ArrayList<String> listLabels = getQuizzLabels();

        arrayOfCheckBox = new ArrayList<CheckBox>();

        for (int i = 0; i < listLabels.size(); i++) {
            checkBox = new CheckBox(getApplicationContext());
            checkBox.setId(i);
            checkBox.setText(listLabels.get(i));
            checkBox.setTextSize(20);
            checkBox.setPadding(10, 10, 10, 10);
            checkBox.setTag(listLabels.get(i));
            arrayOfCheckBox.add(checkBox);
            checkBoxContainer.addView(checkBox);
        }
    }

    /**
     * HTTP GET : Retourne la liste des quizs disponibles
     */
    public ArrayList<String> getQuizzLabels() {
        ArrayList<String> listLabels = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8080/api/quizzes")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String jsonData = Objects.requireNonNull(response.body()).string();
            JSONArray quizzs = new JSONArray(jsonData);
            String label;
            for (int i = 0; i < quizzs.length(); i++) {
                JSONObject quiz = quizzs.getJSONObject(i);
                label = quiz.getString("label");
                if (quiz.getJSONArray("questionsList").length() == 3)
                    listLabels.add(label);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return listLabels;
    }

    /**
     * Vérifie que tous les champs du formulaire d'inscription sont remplis
     */
    public boolean residentFieldsAreFilled() {
        if (name.isEmpty() && identifier.isEmpty()) {
            Toast.makeText(this,
                    "Les deux champs sont requis!",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (!name.isEmpty() && identifier.isEmpty()) {
            Toast.makeText(this,
                    "L'identifiant est requis!",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (name.isEmpty() && !identifier.isEmpty()) {
            Toast.makeText(this,
                    "Le nom du résident est requis!",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (filePath == null) {
            Toast.makeText(this,
                    "L'image est requise!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * HTTP POST : Enregistre le nouveau résident
     */
    public void uploadResidentInfo(String name, String identifier, String pointsInterest) {
        try {
            File test = new File(realPath);
            RequestBody req = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", identifier + ".png", RequestBody.create(test, MediaType.parse("image/*")))
                    .addFormDataPart("name", name)
                    .addFormDataPart("identifier", identifier)
                    .addFormDataPart("pointsIntereset", "pointsofInterest", RequestBody.create(pointsInterest, MediaType.parse("application/json")))
                    .build();
            Request requestBody = new Request.Builder()
                    .url("http://10.0.2.2:8080/api/accounts/resident")
                    .post(req)
                    .build();
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(requestBody).execute();
            if (response.code() == 200) {
                Toast.makeText(getApplicationContext(), "Enregistrement réussi!", Toast.LENGTH_SHORT).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                }, 1500);
            } else {
                Toast.makeText(getApplicationContext(), "L'identifiant existe déjà!", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
