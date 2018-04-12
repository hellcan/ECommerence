package com.example.fengcheng.main.utils;

import com.android.volley.Request;
import com.android.volley.Response;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @Package com.example.fengcheng.main.ecommerence
 * @FileName VolleyHelper
 * @Date 4/11/18, 11:30 PM
 * @Author Created by fengchengding
 * @Description ECommerence
 */

public class VolleyHelper {
    private final String BASE_URL = "http://rjtmobile.com/aamir/e-commerce/android-app/";
    private final String BASE_URL_CATEGORY = "http://rjtmobile.com/ansari/shopingcart/androidapp/";

    private static VolleyHelper instance;

    private VolleyHelper() {

    }

    public static VolleyHelper getInstance() {
        if (instance == null) {
            synchronized (VolleyHelper.class) {
                if (instance == null) {
                    instance = new VolleyHelper();
                }
            }
        }
        return instance;
    }

    public StringRequest registrationRequest(String first, String last, String address, String mob, String email, String pwd,
                                             Response.Listener<String> listener, Response.ErrorListener errorListener) {

        String regUrl = BASE_URL + "shop_reg.php?" + "fname=" + first + "&" + "lname=" + last + "&" + "address=" + address + "&" + "email=" + email
                + "&" + "mobile=" + mob + "&" + "password=" + pwd;

        return new StringRequest(Request.Method.GET, regUrl, listener, errorListener);
    }

    public JsonArrayRequest loginRequest(String mob, String pwd, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {

        String loginUrl = BASE_URL + "shop_login.php?" + "mobile=" + mob + "&password=" + pwd;

        return new JsonArrayRequest(loginUrl, listener, errorListener);
    }

    public StringRequest updateProfile(String fname, String lname, String address, String email, String mobile, Response.Listener<String> listener, Response.ErrorListener errorListener) {

        String url = BASE_URL + "edit_profile.php?" + "fname=" + fname + "&lanme=" + lname + "&address=" + address + "&email=" + email + "&mobile=" + mobile;

        return new StringRequest(Request.Method.GET, url, listener, errorListener);
    }

    public JsonObjectRequest getProductRequest(String mobile, String pwd, String newPwd, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {

        String url = BASE_URL + "shop_reset_pass.php?" + "&mobile=" + mobile + "&password=" + pwd + "&newpassword=" + newPwd;

        return new JsonObjectRequest(url, null, listener, errorListener);
    }

    public JsonObjectRequest getProductRequest(String id, String apikey, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {

        String url = BASE_URL_CATEGORY + "cust_category.php?" + "api_key=" + apikey + "&user_id=" + id;

        return new JsonObjectRequest(url, null, listener, errorListener);
    }


}
