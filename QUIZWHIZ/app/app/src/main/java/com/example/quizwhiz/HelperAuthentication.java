package com.example.quizwhiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HelperAuthentication extends AppCompatDialogFragment {

    private String userName;
    private String passWord;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_admin, null);

        builder.setView(view);

        final EditText editTextUser = view.findViewById(R.id.username);
        final EditText editTextPassword = view.findViewById(R.id.password);

        Button cancel = view.findViewById(R.id.cancel);
        Button connection = view.findViewById(R.id.confirm);

        final Dialog dialog = builder.create();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        // HTTP POST : authentification de la personne aidante
        connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = editTextUser.getText().toString();
                passWord = editTextPassword.getText().toString();

                if (FieldsAreFilled()) {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody formBody = new FormBody.Builder()
                            .add("identifier", userName.toLowerCase())
                            .add("password", passWord)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://10.0.2.2:8080/api/login/helper")
                            .post(formBody)
                            .build();
                    Response response = null;
                    try {
                        response = client.newCall(request).execute();
                    } catch (IOException e) {
                        e.getMessage();
                    }
                    assert response != null;

                    // Profil vérifié -> redirection vers la page admin
                    if (response.code() == 200) {
                        Intent connect = new Intent(dialog.getContext(), HelpersHomePage.class);
                        startActivity(connect);
                    } else {
                        Toast.makeText(getContext(),
                                "Informations incorrectes. Veuillez vérifier vos informations et réessayer.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return dialog;
    }

    /**
     * Méthode qui vérifie que les champs 'nom utilisateur' et 'mot de passe' sont remplis
     */
    public boolean FieldsAreFilled() {
        if (userName.isEmpty() && passWord.isEmpty()) {
            Toast.makeText(getContext(),
                    "Les deux champs sont requis!",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (!userName.isEmpty() && passWord.isEmpty()) {
            Toast.makeText(getContext(),
                    "Le champ mot de passe est requis!",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (userName.isEmpty() && !passWord.isEmpty()) {
            Toast.makeText(getContext(),
                    "Le champ nom d'utilisateur est requis!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
