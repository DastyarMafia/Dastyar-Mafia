package com.test.dastyarmafia;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RoleActivity extends AppCompatActivity {
    Bundle data;
    Button citizen_button;
    Button mafia_button;
    List<String> selectedPlayersName;
    List<String> selectedRolesName = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);

        data = getIntent().getBundleExtra("data");
        selectedPlayersName = data.getStringArrayList("selected_players");
        List<String> _roles = data.getStringArrayList("selected_roles");

        if (_roles.size() > selectedPlayersName.size()){
            for (int i = _roles.size(); i != selectedPlayersName.size(); i--){
                _roles.remove(i-1);
            }
        }

        Button submit_button = findViewById(R.id.submit_button);
        submit_button.setOnClickListener(view -> {
            if (selectedRolesName.isEmpty()){ show_error("شما هیچ نقش انتخابی ندارید"); }
            else if (selectedPlayersName.size() != selectedRolesName.size()){ show_error("تعداد نقش ها با بازیکنان یکی نیست"); }
            else {
                Intent intent = new Intent(RoleActivity.this, RandomChoice.class);
                data.putStringArrayList("selected_roles", (ArrayList<String>) selectedRolesName);
                intent.putExtra("data", data);
                startActivity(intent);
                finish();
            }
        });

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
            role.setOnCheckedChangeListener((compoundButton, b) -> onRoleSelected(compoundButton, (String) role.getText(), b));
            role.setOnLongClickListener(view -> true);
            if (_roles.contains((String) role.getText())) { role.setChecked(true); }
        }

        for (int i = 0; i < mafia_child_count; i++) {
            CheckBox role = (CheckBox) mafia_tab.getChildAt(i);
            role.setOnCheckedChangeListener((compoundButton, b) -> onRoleSelected(compoundButton, (String) role.getText(), b));
            role.setOnLongClickListener(view -> true);
            if (_roles.contains((String) role.getText())) { role.setChecked(true); }
        }

        refresh_counter();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RoleActivity.this, PlayerActivity.class);
        data.putStringArrayList("selected_roles", (ArrayList<String>) selectedRolesName);
        intent.putExtra("data", data);
        startActivity(intent);
        finish();
    }

    void show_error(CharSequence text){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.warn_toast, findViewById(R.id.notify_layout));
        TextView tv = layout.findViewById(R.id.notify_message);
        tv.setText(text);
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }

    @SuppressLint("SetTextI18n")
    void refresh_counter(){
        TextView textView = findViewById(R.id.show_player_count);
        textView.setText( selectedRolesName.size() + "/" + selectedPlayersName.size());
    }

    public void onRoleSelected(CompoundButton checkBox, String roleName, boolean isChecked){
        if (selectedRolesName.size() + 1 > selectedPlayersName.size() && isChecked){ checkBox.setChecked(false); }
        else if (!isChecked && selectedRolesName.contains(roleName)){selectedRolesName.remove(roleName);}
        else if (isChecked && !selectedRolesName.contains(roleName)){selectedRolesName.add(roleName);}
        refresh_counter();
    }

}