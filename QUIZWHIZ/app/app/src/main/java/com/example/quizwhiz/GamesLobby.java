package com.example.quizwhiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

public class GamesLobby extends AppCompatActivity {

    private String identifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_lobby);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Bundle extra = getIntent().getExtras();
        if (extra != null)
            identifier = extra.getString("identifier");

        Button matchCards = findViewById(R.id.matchcards);
        Button simon = findViewById(R.id.simon);
        Button puzzle = findViewById(R.id.puzzle);
        Button brainbox = findViewById(R.id.brainbox);

        matchCards.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent matchingCards = new Intent(GamesLobby.this, MatchingCards.class);
                startActivity(matchingCards);
            }
        });

        simon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent matchingCards = new Intent(GamesLobby.this, SimonSays.class);
                startActivity(matchingCards);
            }
        });

        puzzle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent matchingCards = new Intent(GamesLobby.this, Puzzle.class);
                startActivity(matchingCards);
            }
        });

        brainbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent matchingCards = new Intent(GamesLobby.this, BrainBox.class);
                startActivity(matchingCards);
            }
        });

    }
}