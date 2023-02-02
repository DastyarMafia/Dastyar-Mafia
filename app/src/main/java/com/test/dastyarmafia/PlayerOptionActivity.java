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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlayerOptionActivity extends AppCompatActivity {
    ArrayList<String> players_name;
    String player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_option);

        TextView textView = findViewById(R.id.player_name);
        players_name = (ArrayList<String>) getIntent().getSerializableExtra("players");
        player = (String) getIntent().getStringExtra("player");
        textView.setText(player);

        Button deleteButton = findViewById(R.id.delete_player);
        deleteButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("mode", "delete");
            ((ResultReceiver)getIntent().getParcelableExtra("receiver")).send(1, bundle);
            finish();
        });

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
}