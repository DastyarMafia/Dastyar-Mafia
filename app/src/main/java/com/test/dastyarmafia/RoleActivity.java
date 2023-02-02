package com.test.dastyarmafia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class RoleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);
        RoleDBHandler roleDB = new RoleDBHandler(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RoleActivity.this, PlayerActivity.class);
        intent.putExtra("players", (ArrayList<String>) getIntent().getSerializableExtra("players"));
        startActivity(intent);
        finish();
    }

}