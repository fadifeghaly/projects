package com.example.quizwhiz;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MatchingCards extends AppCompatActivity {

    private List<String> cards = new ArrayList<>();
    private List<View> arrBtn = new ArrayList<>();
    private int lastClicked = -1;
    private int clickCounter = 0;
    private int tentative = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game);
        showTip();
        
        arrBtn.addAll(Arrays.asList(findViewById(R.id.card1),
                findViewById(R.id.card2),
                findViewById(R.id.card3),
                findViewById(R.id.card4),
                findViewById(R.id.card5),
                findViewById(R.id.card6),
                findViewById(R.id.card7),
                findViewById(R.id.card8),
                findViewById(R.id.card9),
                findViewById(R.id.card10),
                findViewById(R.id.card11),
                findViewById(R.id.card12)));
        cards.addAll(Arrays.asList("tomate", "cerise", "concombre", "mure", "framboise", "aubergine",
                "tomate", "cerise", "concombre", "mure", "framboise", "aubergine"));
        Collections.shuffle(arrBtn);

        for (int i = 0; i < 12; i++) {
            final int finalI = i;
            arrBtn.get(i).setTag("invisible");
            arrBtn.get(i).setOnClickListener(new View.OnClickListener() {
                public void onClick(final View v) {
                    if (v.getTag().equals("invisible")) {
                        v.setTag(cards.get(finalI));
                        int resID = getResources().getIdentifier(cards.get(finalI),
                                "drawable",
                                getPackageName());
                        v.setBackgroundResource(resID);
                        if (clickCounter == 0)
                            lastClicked = v.getId();
                        clickCounter++;
                    } else {
                        v.setBackgroundResource(R.drawable.card);
                        v.setTag("invisible");
                    }
                    if (lastClicked != -1 && clickCounter == 2 && v.getTag().equals(findViewById(lastClicked).getTag())) {
                        v.setEnabled(false);
                        findViewById(lastClicked).setEnabled(false);
                        tentative++;
                        clickCounter = 0;
                    } else if (lastClicked != -1 && clickCounter == 2) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                v.setBackgroundResource(R.drawable.card);
                                findViewById(lastClicked).setBackgroundResource(R.drawable.card);
                                v.setTag("invisible");
                                findViewById(lastClicked).setTag("invisible");
                                tentative++;
                            }
                        }, 1000);
                        clickCounter = 0;
                    }
                }
            });
        }
    }

    public void showTip() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AlertDialog);
        dialog.setCancelable(false);
        dialog.setTitle("Bonjour");
        dialog.setMessage("Chaque objet est représenté deux fois, retournez les cartes et trouvez toutes les paires");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog tip = dialog.create();
        tip.show();
    }
}


// TODO
// POST le nbr de tentatives de chaque indent


