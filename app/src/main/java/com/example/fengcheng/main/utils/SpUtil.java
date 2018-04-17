package com.example.fengcheng.main.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * @Package com.example.fengcheng.main.utils
 * @FileName SpUtil
 * @Date 4/12/18, 1:28 AM
 * @Author Created by fengchengding
 * @Description ECommerence
 */

public class SpUtil {

    public static SharedPreferences getSp(Context context) {
        return context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
    }



    public static void setUserInfo(Context context, String fname, String lname, String email, String mobile, String apikey, String id) {
        SharedPreferences.Editor editor = getSp(context).edit();
        editor.putString("fname", fname);
        editor.putString("lname", lname);
        editor.putString("email", email);
        editor.putString("mobile", mobile);
        editor.putString("apikey", apikey);
        editor.putString("id", id);
        editor.commit();
    }

    public static void clearUserInfo(Context context) {
        SharedPreferences.Editor editor = getSp(context).edit();
        editor.putString("fname", null);
        editor.putString("lname", null);
        editor.putString("email", null);
        editor.putString("mobile", null);
        editor.putString("apikey", null);
        editor.putString("id", null);
        editor.commit();
    }


    public static void updateUserInfo(Context context, String fname, String lname, String email, String mobile) {
        SharedPreferences.Editor editor = getSp(context).edit();
        editor.putString("fname", fname);
        editor.putString("lname", lname);
        editor.putString("email", email);
        editor.putString("mobile", mobile);
        editor.commit();
    }

    public static String getUserName(Context context){
        return getSp(context).getString("fname", null);
    }

    public static String getLastName(Context context){
        return getSp(context).getString("lname", null);
    }

    public static String getEmail(Context context){
        return getSp(context).getString("email", null);
    }

    public static String getMobile(Context context){
        return getSp(context).getString("mobile", null);
    }

    public static String getApiKey(Context context){
        return getSp(context).getString("apikey", null);
    }

    public static String getUserId(Context context){
        return getSp(context).getString("id", null);
    }

    public static void setRemember(Context context, String mobile){

        SharedPreferences.Editor editor = getSp(context).edit();

        editor.putString("remember", mobile);

        editor.commit();

    }

    public static String getRemember(Context context){
        return getSp(context).getString("remember", null);
    }

}
