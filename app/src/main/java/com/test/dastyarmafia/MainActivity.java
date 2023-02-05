package com.test.dastyarmafia;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    MediaPlayer musicPlayer;
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button go_btn = findViewById(R.id.go_btn);
        go_btn.setOnClickListener(view -> {
                Bundle data = new Bundle();
                data.putStringArrayList("selected_players", new ArrayList<>());
                data.putStringArrayList("selected_roles", new ArrayList<>());
                startActivity(new Intent(MainActivity.this, PlayerActivity.class)
                        .putExtra("data", data));
                finish();
            }
        );

        ImageButton info_btn = findViewById(R.id.info_button);
        info_btn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AboutUs.class)));

        musicPlayer = MediaPlayer.create(getApplicationContext(), R.raw.menu_music);
        musicPlayer.setLooping(true);
        musicPlayer.setVolume(100, 100);
        musicPlayer.start();
    }

    @Override
    protected void onDestroy() {
        musicPlayer.pause();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        musicPlayer.pause();
        super.onPause();
    }

    @Override
    protected void onStart() {
        musicPlayer.start();
        super.onStart();
    }
}