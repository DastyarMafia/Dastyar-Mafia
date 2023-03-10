package com.test.dastyarmafia;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends AppCompatActivity {
    Bundle data;
    List<Player> playerList;
    List<String> playerNamesList;
    List<String> selectedPlayersName = new ArrayList<>();
    PlayerDBHandler playerDB;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        playerDB = new PlayerDBHandler(this);
        playerList = playerDB.getAllPlayers();
        playerNamesList = playerDB.getAllPlayersName();

        data = getIntent().getBundleExtra("data");

        Button submit_button = findViewById(R.id.submit_button);
        submit_button.setOnClickListener(view -> {
            if (playerList.isEmpty()){ show_error("شما هیچ بازیکنی ندارید"); }
            else if (selectedPlayersName.isEmpty() || ! (selectedPlayersName.size() >= 4)){ show_error("تعداد بازیکنان خیلی کم است"); }
            else if (selectedPlayersName.size() > 50){ show_error("تعداد بازیکنان بیش از حد مجاز است"); }
            else {
                Intent intent = new Intent(PlayerActivity.this, RoleActivity.class);
                data.putStringArrayList("selected_players", (ArrayList<String>) selectedPlayersName);
                intent.putExtra("data", data);
                startActivity(intent);
                finish();
            }
        });

        ImageButton toggle_all_checkbox = findViewById(R.id.toggle_all_checkbox);
        toggle_all_checkbox.setOnClickListener(view -> {
            LinearLayout players_layouts = findViewById(R.id.players);
            int index = players_layouts.getChildCount();
            if (index == 0){ show_error("شما هیچ بازیکنی ندارید"); }
            else{
                boolean state = selectedPlayersName.size() != index;
                for (int i = 0; i < index; i++) {
                    CheckBox player = (CheckBox) players_layouts.getChildAt(i);
                    player.setChecked(state);
                }
            }
        });

        ImageButton add_button = findViewById(R.id.add_player_button);
        add_button.setOnClickListener(view -> {
            Intent intent = new Intent(PlayerActivity.this, CreatePlayer.class);
            intent.putExtra("players", (ArrayList<String>) playerNamesList);
            intent.putExtra("receiver", new ResultReceiver(null) {
                @Override
                protected void onReceiveResult(int resultCode, Bundle resultData) {
                    changePlayersMod(1);
                    show_success("بازیکن با موفقیت اضافه شد");
                    Player player = new Player(resultData.getString("name"));
                    playerDB.addPlayer(player);
                    AddPlayerTo(player, findViewById(R.id.players), false);
                    playerList = playerDB.getAllPlayers();
                    playerNamesList = playerDB.getAllPlayersName();
                }
            });
            startActivity(intent);
        });

        ImageButton back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(view -> onBackPressed());

        if (!playerList.isEmpty()){
            changePlayersMod(1);
            ArrayList<String> _players = data.getStringArrayList("selected_players");
            for (Player player : playerList) {
                AddPlayerTo(player, findViewById(R.id.players), _players.contains(player.getName()));
            }
        }
        else{
            changePlayersMod(0);
        }

        refresh_counter();

    }

    void onCheckedChanged(String playerName, boolean isChecked){
        if (!isChecked && selectedPlayersName.contains(playerName)){selectedPlayersName.remove(playerName);}
        else if (isChecked && !selectedPlayersName.contains(playerName)){selectedPlayersName.add(playerName);}
        refresh_counter();
    }

    void AddPlayerTo(Player player, LinearLayout mainLinear, boolean checked){
        CheckBox player_check = new CheckBox(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics()));
        String playerName = player.getName();
        player_check.setLayoutParams(params);
        player_check.setBackgroundColor(Color.parseColor("#1BFFFFFF"));
        player_check.setTextColor(Color.parseColor("#FF03DAC5"));
        player_check.setText(playerName);
        player_check.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        player_check.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        player_check.setOnCheckedChangeListener((compoundButton, b) -> onCheckedChanged(playerName, b));
        player_check.setOnLongClickListener(view -> {
            Intent intent = new Intent(PlayerActivity.this, PlayerOptionActivity.class);
            Bundle playerBundle = new Bundle();
            playerBundle.putStringArrayList("players", (ArrayList<String>) playerNamesList);
            playerBundle.putString("player", playerName);
            intent.putExtra("data", playerBundle);
            intent.putExtra("receiver", new ResultReceiver(null) {
                @Override
                protected void onReceiveResult(int resultCode, Bundle resultData) {
                    LinearLayout players_layouts = findViewById(R.id.players);
                    int index = players_layouts.getChildCount();
                    if (resultData.get("mode") == "edit"){
                        edit_selected(playerName, (String) resultData.get("after_name"));
                        playerDB.updatePlayer(playerName, new Player(player.getId(), (String) resultData.get("after_name")));
                        if (index == 1 && ((CheckBox) players_layouts.getChildAt(0)).getText() == playerName ){ ((CheckBox) players_layouts.getChildAt(0)).setText(resultData.getString("after_name"));}
                        else{
                            for (int i = 0; i < index; i++){
                                CheckBox player = (CheckBox) players_layouts.getChildAt(i);
                                if (player.getText() == playerName){ player.setText(resultData.getString("after_name")); break;}
                            }
                        }
                        show_success("نام بازیکن با موفقیت ویرایش شد");
                    }
                    else if (resultData.get("mode") == "delete"){
                        delete_selected(playerName);
                        playerDB.deletePlayer(player);
                        if (index == 1 && ((CheckBox) players_layouts.getChildAt(0)).getText() == playerName ){
                            players_layouts.removeViewAt(0);
                            changePlayersMod(0);
                        }
                        else{
                            for (int i = 0; i < index; i++){
                                CheckBox player = (CheckBox) players_layouts.getChildAt(i);
                                if (player.getText() == playerName){ players_layouts.removeViewAt(i); break;}
                            }
                        }
                        show_success("بازیکن با موفقیت پاک شد");
                    }
                    playerList = playerDB.getAllPlayers();
                    playerNamesList = playerDB.getAllPlayersName();
                    refresh_counter();
                }
            });
            startActivity(intent);
            return true;
        });
        if (checked){player_check.setChecked(true);}
        player_check.setTypeface(Typeface.createFromAsset(getAssets(), "font/soraya.ttf"), Typeface.BOLD);
        mainLinear.addView(player_check);
    }

    void show_success(CharSequence text){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.success_toast, findViewById(R.id.notify_layout));
        TextView tv = layout.findViewById(R.id.notify_message);
        tv.setText(text);
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
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
        textView.setText( selectedPlayersName.size() + " نفر");
    }

    void changePlayersMod(int integer){
        if (integer == 0){
            RelativeLayout layout_no_players = findViewById(R.id.no_players);
            layout_no_players.setVisibility(View.VISIBLE);
            ScrollView layout_sidebar = findViewById(R.id.main);
            layout_sidebar.setVisibility(View.INVISIBLE);
        }
        else if (integer == 1){
            RelativeLayout layout_no_players = findViewById(R.id.no_players);
            layout_no_players.setVisibility(View.INVISIBLE);
            ScrollView layout_sidebar = findViewById(R.id.main);
            layout_sidebar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PlayerActivity.this, MainActivity.class));
        finish();
    }

    void delete_selected(String playerName){
        selectedPlayersName.remove(playerName);
    }

    void edit_selected(String beforePlayerName, String afterPlayerName){
        if (selectedPlayersName.contains(beforePlayerName)) { selectedPlayersName.remove(beforePlayerName); selectedPlayersName.add(afterPlayerName);}
    }
}