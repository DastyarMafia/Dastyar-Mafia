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


public class RoleDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "RoleManager";

    public RoleDBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_QUERY = "CREATE TABLE RoleManager(id INTEGER ,name TEXT, description TEXT, PRIMARY KEY('id' AUTOINCREMENT));";
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS RoleManager");
    }

    void addRole(Role role){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", role.getName());
        values.put("description", role.getDescription());

        db.insert("RoleManager", null, values);
        db.close();
    }

    Role getRole(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = (SQLiteCursor) db.query("RoleManager", new String[]{"id", "name", "description"}, "id=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        return new Role(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
    }

    public List<Role> getAllRoles(){
        List<Role> roles = new ArrayList<>();


        String SELECT_QUERY = "SELECT * FROM RoleManager";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        if (cursor.moveToFirst()){
            do{
                Role role = new Role();
                role.setId(Integer.parseInt(cursor.getString(0)));
                role.setName(cursor.getString(1));
                role.setDescription(cursor.getString(2));
                roles.add(role);
            } while (cursor.moveToNext());
            return roles;
        }

        return roles;
    }

    public int updateRole(Role role){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", role.getName());
        values.put("description", role.getDescription());

        int result = db.update("RoleManager", values, "id=?", new String[]{String.valueOf(role.getId())});
        db.close();
        return result;
    }

    public void deleteRole(Role role){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("RoleManager", "id=?", new String[]{String.valueOf(role.getId())});
        db.close();
    }
}
