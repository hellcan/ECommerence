package com.example.fengcheng.main.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.fengcheng.main.dataBean.CartInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package com.example.fengcheng.main.db
 * @FileName DbManager
 * @Date 4/15/18, 11:20 PM
 * @Author Created by fengchengding
 * @Description ECommerence
 */

public class DbManager {
    private DBHelper helper;
    private SQLiteDatabase mdb;
    private String cartTable = "shoppingCart";

    public DbManager(Context context) {
        helper = new DBHelper(context);
    }

    public void openDatabase() {
        mdb = helper.getReadableDatabase();
    }

    public void closeDatabase() {
        mdb.close();
    }
    public void addItemToCart(String mobile, String pid, String pname, int quantity, String prize, String imageurl) {
        ContentValues mContentValue = new ContentValues();
        mContentValue.put("mobile", mobile);
        mContentValue.put("pid", pid);
        mContentValue.put("pname", pname);
        mContentValue.put("quantity", quantity);
        mContentValue.put("prize", prize);
        mContentValue.put("imageurl", imageurl);

        this.mdb.insertOrThrow(cartTable, null, mContentValue);
    }


    //check whether item already exist in cart
    public int verifyItemInCart(String productName, String mobile) {
        int qty = -1;
        String sql = "SELECT quantity FROM " + cartTable + " WHERE pname =" + "\"" + productName + "\"" + "AND mobile =" + "\"" + mobile + "\"";
        Cursor result = mdb.rawQuery(sql, null);
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            qty = result.getInt(0);
        }

        return qty;
    }

    //update qty
    public void updateShoppingCartQty(int newQty, String productName, String mobile) {
        String sql = "UPDATE " + cartTable + " "
                + "SET quantity =" + "\"" + newQty + "\"" + "WHERE pname=" + "\"" + productName + "\"" + "AND mobile =" + "\"" + mobile + "\"";
        mdb.execSQL(sql);
    }

    //get all the item in cart under specific userName
    public List<CartInfo.OrderBean> getCartItemList(String mobile) {
        List<CartInfo.OrderBean> list = new ArrayList<>();
        String sql = "SELECT pid, pname, quantity, prize, imageurl From " + cartTable + " WHERE mobile =" + "\"" + mobile + "\"";
        Cursor result = mdb.rawQuery(sql, null);
        for(result.moveToFirst();!result.isAfterLast();result.moveToNext()){

            Log.i("测试数据库", result.getString(0) + result.getString(1) + result.getString(2) + result.getString(3)+result.getString(4));
            list.add(new CartInfo.OrderBean(result.getString(0),
                    result.getString(1),
                    Integer.valueOf(result.getString(2)),
                    result.getString(3),
                    result.getString(4)));
        }

        return list;
    }


}
