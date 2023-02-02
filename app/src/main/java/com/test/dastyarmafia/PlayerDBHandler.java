package com.test.dastyarmafia;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class PlayerDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PlayerManager";
    private static final String TABLE_PLAYER = "player";
    private static final String KEY_ID = "id";
    private static final String KEY_name = "name";

    public PlayerDBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_QUERY = "CREATE TABLE PlayerManager(id INTEGER ,name TEXT, PRIMARY KEY('id' AUTOINCREMENT));";
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS PlayerManager");
    }

    void addPlayer(Player player){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", player.getName());

        db.insert("PlayerManager", null, values);
        db.close();
    }

    Player getPlayer(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = (SQLiteCursor) db.query("PlayerManager", new String[]{"id", "name"}, "id=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        return new Player(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
    }

    public List<Player> getAllPlayers(){
        List<Player> players = new ArrayList<>();


        String SELECT_QUERY = "SELECT * FROM PlayerManager";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        if (cursor.moveToFirst()){
            do{
                Player player = new Player();
                player.setId(Integer.parseInt(cursor.getString(0)));
                player.setName(cursor.getString(1));
                players.add(player);
            } while (cursor.moveToNext());
            return players;
        }

        return players;
    }

    public List<String> getAllPlayersName(){
        List<String> players = new ArrayList<>();

        String SELECT_QUERY = "SELECT name FROM PlayerManager";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        if (cursor.moveToFirst()){
            do{
                players.add(cursor.getString(0));
            } while (cursor.moveToNext());
            return players;
        }

        return players;
    }

    public int updatePlayer(String beforeName, Player player){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", player.getName());

        int result = db.update("PlayerManager", values, "name=?", new String[]{String.valueOf(beforeName)});
        db.close();
        return result;
    }

    public void deletePlayer(Player player){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("PlayerManager", "name=?", new String[]{String.valueOf(player.getName())});
        db.close();
    }
}
