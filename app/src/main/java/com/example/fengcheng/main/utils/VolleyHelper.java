package com.example.fengcheng.main.utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.fengcheng.main.dataBean.Products;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final String BASE_URL_PAYMENT = "http://rjtmobile.com/aamir/e-commerce/android-app/";
    private static final String TAG = "VolleyHelper";
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

    public JsonObjectRequest resetPwdRequest(String mobile, String pwd, String newPwd, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {

        String url = BASE_URL + "shop_reset_pass.php?" + "&mobile=" + mobile + "&password=" + pwd + "&newpassword=" + newPwd;

        return new JsonObjectRequest(url, null, listener, errorListener);
    }

    //request to get main category
    public JsonObjectRequest getMainCategoryRequest(String id, String apikey, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {

        String url = BASE_URL_CATEGORY + "cust_category.php?" + "api_key=" + apikey + "&user_id=" + id;

        return new JsonObjectRequest(url, null, listener, errorListener);
    }

    //request to get sub category
    public JsonObjectRequest getSuCategoryRequest(String subId, String apikey, String userId, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {

        String url = BASE_URL_CATEGORY + "cust_sub_category.php?" + "Id=" + subId + "&api_key=" + apikey + "&user_id=" + userId;

        return new JsonObjectRequest(url, null, listener, errorListener);
    }

    //request to get productList
    public JsonObjectRequest getProductListRequest(String cid, String subId, String apikey, String userId, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {

        String url = BASE_URL_CATEGORY + "product_details.php?" + "cid=" + cid + "&scid=" + subId + "&api_key=" + apikey + "&user_id=" + userId;

        Log.i("url", url);
        return new JsonObjectRequest(url, null, listener, errorListener);
    }

    //request to get sub category
    public JsonObjectRequest paymentRequest(String pid, String pname, String quantity, String price,
                                            String userId, String userName, String mobile, String email, String apikey,
                                            Response.Listener<JSONObject> listener, Response.ErrorListener errorListener,
                                            final Map<String, String> paramHash) {

        String url = BASE_URL_PAYMENT + "orders.php?" + "item_id=" + pid + "&item_name=" + pname
                + "&item_quantity=" + quantity + "&final_price=" + price + "&api_key=" + apikey
                + "&user_id=" + userId + "&user_name=" + userName + "&billingadd=Noida&deliveryadd=Noida" +
                "&mobile=" + mobile + "&email=" + email;

        Log.i("url", url);
        return new JsonObjectRequest(url, null, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                if (paramHash == null)
                    return null;
                Map<String, String> params = new HashMap<>();
                for (String key : paramHash.keySet()) {
                    params.put(key, paramHash.get(key));
                    Log.d(TAG, "Key : " + key + " Value : " + paramHash.get(key));
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
    }

    //request to get productList
    public JsonObjectRequest couponRequest(String apikey, String userId, String coupon, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {

        String url = BASE_URL + "coupon.php?" + "api_key=" + apikey + "&user_id=" + userId + "&couponno=" + coupon;

        Log.i("url", url);
        return new JsonObjectRequest(url, null, listener, errorListener);
    }


    //order history
    public JsonObjectRequest orderHistory(String apikey, String userId, String mobile, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {

        String url = BASE_URL + "order_history.php?" + "api_key=" + apikey + "&user_id=" + userId + "&mobile=" + mobile;

        Log.i("url", url);
        return new JsonObjectRequest(url, null, listener, errorListener);
    }
}
