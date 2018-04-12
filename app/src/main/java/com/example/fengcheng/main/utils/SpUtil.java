package com.example.fengcheng.main.utils;

import android.content.Context;
import android.content.SharedPreferences;

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

        editor.apply();
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

        editor.apply();
    }

    public static String getRemember(Context context){
        return getSp(context).getString("remember", null);
    }

}
