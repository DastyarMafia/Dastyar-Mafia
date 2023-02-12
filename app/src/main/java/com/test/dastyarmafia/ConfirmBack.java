package com.test.dastyarmafia;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.ResultReceiver;

public class ConfirmBack extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_back);

        findViewById(R.id.yes_button).setOnClickListener(view -> {finish(); ((ResultReceiver) getIntent().getParcelableExtra("receiver")).send(1, null);});
        findViewById(R.id.no_button).setOnClickListener(view -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}