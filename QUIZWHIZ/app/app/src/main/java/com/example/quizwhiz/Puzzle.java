package com.example.quizwhiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Puzzle extends AppCompatActivity {

    private GridLayout puzzle;
    private String lastPiece;
    private int clickCounter = 0;
    private Context currentContext;
    private String selectedPuzzle;
    private Spinner choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        currentContext = this;
        String puzzles[] = {"Animaux", "Villes"};

        choice = findViewById((R.id.choice));
        choice.setAdapter(new ArrayAdapter<>(currentContext, android.R.layout.simple_spinner_dropdown_item, puzzles));

        puzzle = findViewById(R.id.puzzleGrid);

        showTip();

        choice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPuzzle = choice.getSelectedItem().toString().toLowerCase();
                puzzle.removeAllViews();
                drawPuzzle();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void showTip() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AlertDialog);
        dialog.setCancelable(false);
        dialog.setTitle("Bonjour");
        dialog.setMessage("Cliquez sur deux images pour échanger leur position");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog tip = dialog.create();
        tip.show();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.nav_default_enter_anim);
            v.startAnimation(animation);
            clickCounter += 1;
            if (clickCounter == 2) {
                int fstID = getResources().getIdentifier(lastPiece,
                        "drawable",
                        getPackageName());
                int scdID = getResources().getIdentifier(v.getTag().toString(),
                        "drawable",
                        getPackageName());
                ((ImageView) v).setImageResource(fstID);
                ((ImageView) puzzle.findViewWithTag(lastPiece)).setImageResource(scdID);
                ((ImageView) puzzle.findViewWithTag(lastPiece)).setTag(v.getTag());
                ((ImageView) v).setTag(lastPiece);
                clickCounter = 0;
            }
            if (clickCounter == 1)
                lastPiece = v.getTag().toString();
        }
    };

    public void drawPuzzle() {
        ImageView preview = findViewById(R.id.preview);
        int resID = getResources().getIdentifier(selectedPuzzle + "_preview",
                "drawable",
                getPackageName());
        preview.setImageResource(resID);

        List<Integer> randomPieces = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25));
        Collections.shuffle(randomPieces);
        for (int i = 0; i < 25; i++) {
            int index = randomPieces.get(i);
            resID = getResources().getIdentifier(selectedPuzzle + index,
                    "drawable",
                    getPackageName());
            ImageView imageView = new ImageView(this);
            imageView.setTag(selectedPuzzle + index);
            imageView.setOnClickListener(onClickListener);
            imageView.setImageResource(resID);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                    222,
                    222);
            layoutParams2.setMargins(5, 5, 5, -50);
            imageView.setLayoutParams(layoutParams2);
            puzzle.addView(imageView);
        }
    }

}