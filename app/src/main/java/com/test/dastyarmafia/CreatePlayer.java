package com.test.dastyarmafia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CreatePlayer extends AppCompatActivity {
    ArrayList<String> players_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_player);
        players_name = (ArrayList<String>) getIntent().getSerializableExtra("players");
        Button submit_button = findViewById(R.id.submit_button);
        submit_button.setOnClickListener(view -> {
            EditText editText = findViewById(R.id.player_name_edit);
            if (editText.getText().toString().trim().equals("")){
                show_error("نام بازیکن وارد نشده است");
            }
            else if(players_name.contains(editText.getText().toString())) {
                show_error("نام بازیکن از قبل وارد شده است");
            }
            else{
                Bundle bundle = new Bundle();
                bundle.putString("name", editText.getText().toString());
                ((ResultReceiver)getIntent().getParcelableExtra("receiver")).send(1, bundle);
                finish();
            }
        });
    }

    void show_success(CharSequence text){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.success_toast, (ViewGroup) findViewById(R.id.notify_layout));
        TextView tv = (TextView) layout.findViewById(R.id.notify_message);
        tv.setText(text);
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }

    void show_error(CharSequence text){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.warn_toast, (ViewGroup) findViewById(R.id.notify_layout));
        TextView tv = (TextView) layout.findViewById(R.id.notify_message);
        tv.setText(text);
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }
}