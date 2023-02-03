package com.test.dastyarmafia;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class RoleActivity extends AppCompatActivity {
    Button citizen_button;
    Button mafia_button;
    List<String> selectedRolesName = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);

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

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout citizen_tab = findViewById(R.id.citizen_tab_roles);
        int citizen_child_count = citizen_tab.getChildCount();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout mafia_tab = findViewById(R.id.mafia_tab_roles);
        int mafia_child_count = mafia_tab.getChildCount();

        for (int i = 0; i < citizen_child_count; i++) {
            CheckBox role = (CheckBox) citizen_tab.getChildAt(i);
            role.setOnCheckedChangeListener((compoundButton, b) -> onRoleSelected((String) role.getText(), b));
            role.setOnLongClickListener(view -> true);
        }

        for (int i = 0; i < mafia_child_count; i++) {
            CheckBox role = (CheckBox) mafia_tab.getChildAt(i);
            role.setOnCheckedChangeListener((compoundButton, b) -> onRoleSelected((String) role.getText(), b));
            role.setOnLongClickListener(view -> true);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RoleActivity.this, PlayerActivity.class);
        intent.putExtra("selected_players", getIntent().getSerializableExtra("players"));
        startActivity(intent);
        finish();
    }

    public void onRoleSelected(String roleName, boolean isChecked){
        if (!isChecked && selectedRolesName.contains(roleName)){selectedRolesName.remove(roleName);}
        else if (isChecked && !selectedRolesName.contains(roleName)){selectedRolesName.add(roleName);}
    }

}