package com.example.fengcheng.main.ecommerence;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fengcheng.main.dataBean.Products;
import com.example.fengcheng.main.utils.SpUtil;
import com.example.fengcheng.main.utils.VolleyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @Package com.example.fengcheng.main.ecommerence
 * @FileName FragmentOrder
 * @Date 4/11/18, 12:57 PM
 * @Author Created by fengchengding
 * @Description ECommerence
 */

public class FragmentOrder extends Fragment {
    private static final String TAG = "FragmentOrder";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, container, false);

        pullData();
        return v;
    }

    private void pullData() {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject =  response;
                    JSONArray jsonArray = jsonObject.getJSONArray("Order history");
                    if (jsonArray == null) {
                        Toast.makeText(getContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            jsonObj.getString("orderid");
                            jsonObj.getString("orderstatus");
                            jsonObj.getString("name");
                            jsonObj.getString("billingadd");
                            jsonObj.getString("deliveryadd");
                            jsonObj.getString("mobile");
                            jsonObj.getString("itemid");
                            jsonObj.getString("itemname");
                            jsonObj.getString("itemquantity");
                            jsonObj.getString("totalprice");
                            jsonObj.getString("paidprice");
                            jsonObj.getString("placedon");

                        }
                    }

//                    initRecyclerView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, response.toString());
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        JsonObjectRequest history = VolleyHelper.getInstance().orderHistory(SpUtil.getApiKey(getContext()), SpUtil.getUserId(getContext()),
                SpUtil.getMobile(getContext()), listener, errorListener);

        AppController.getInstance().addToRequestQueue(history, "history");

    }
}
