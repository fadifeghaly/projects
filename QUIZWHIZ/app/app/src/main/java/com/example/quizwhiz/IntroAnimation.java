package com.example.quizwhiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class IntroAnimation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_animation);
        MediaController mediaController = new MediaController(this);
        VideoView introVideo = findViewById(R.id.videoView);
        introVideo.setMediaController(mediaController);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.animation;
        introVideo.setVideoURI(Uri.parse(path));
        introVideo.start();
        introVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Intent start = new Intent(IntroAnimation.this, WelcomePage.class);
                startActivity(start);
            }
        });
    }
}