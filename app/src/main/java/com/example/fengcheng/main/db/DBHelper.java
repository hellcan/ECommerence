package com.example.fengcheng.main.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @Package com.example.fengcheng.main.db
 * @FileName DBHelper
 * @Date 4/15/18, 10:57 PM
 * @Author Created by fengchengding
 * @Description ECommerence
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String name = "cart.db";
    private static final int version = 1;

    public DBHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table shoppingCart(" +
                "id INTEGER primary key autoincrement," +
                "mobile varchar," +
                "pid varchar," +
                "pname varchar," +
                "quantity INTEGER," +
                "prize varchar," +
                "imageurl varchar)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
