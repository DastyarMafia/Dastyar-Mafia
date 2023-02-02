package com.test.dastyarmafia;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

public class RoleActivity extends AppCompatActivity {
    Button citizen_button;
    Button mafia_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);
        RoleDBHandler roleDB = new RoleDBHandler(this);

        ImageButton back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(view -> onBackPressed());

        mafia_button = findViewById(R.id.mafia_button);
        mafia_button.setOnClickListener(view -> {
            findViewById(R.id.mafia_tab).setVisibility(View.VISIBLE);
            findViewById(R.id.citizen_tab).setVisibility(View.INVISIBLE);
            citizen_button.setEnabled(true);
            mafia_button.setEnabled(false);
        });

        citizen_button = findViewById(R.id.citizen_button);
        citizen_button.setOnClickListener(view -> {
            findViewById(R.id.mafia_tab).setVisibility(View.INVISIBLE);
            findViewById(R.id.citizen_tab).setVisibility(View.VISIBLE);
            mafia_button.setEnabled(true);
            citizen_button.setEnabled(false);
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RoleActivity.this, PlayerActivity.class);
        intent.putExtra("selected_players", (ArrayList<String>) getIntent().getSerializableExtra("players"));
        startActivity(intent);
        finish();
    }

}