package com.example.fengcheng.main.ecommerence;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fengcheng.main.adapter.HistoryAdapter;
import com.example.fengcheng.main.adapter.MainCategoryAdapter;
import com.example.fengcheng.main.dataBean.OrderHistory;
import com.example.fengcheng.main.dataBean.Products;
import com.example.fengcheng.main.dialog.DialogOrderDetail;
import com.example.fengcheng.main.utils.HistoryComparator;
import com.example.fengcheng.main.utils.SpUtil;
import com.example.fengcheng.main.utils.VolleyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Order history fragment
 */

public class FragmentOrder extends Fragment {
    private static final String TAG = "FragmentOrder";
    private List<OrderHistory.Order> orderList;
    private RecyclerView historyRv;
    ProgressDialog progress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, container, false);

        initView(v);
        pullData();
        return v;
    }

    private void initView(View v) {
        historyRv = v.findViewById(R.id.order_history_list);
    }

    private void pullData() {
        progress = new ProgressDialog(getContext(), R.style.Base_AlertDialog);
        progress.setCancelable(false);
        progress.show();

        orderList = new ArrayList<>();

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response;
                    JSONArray jsonArray = jsonObject.getJSONArray("Order history");
                    if (jsonArray == null) {
                        Toast.makeText(getContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);

                            orderList.add(new OrderHistory.Order(jsonObj.getString("orderid"),
                                    jsonObj.getString("orderstatus"),
                                    jsonObj.getString("name"),
                                    jsonObj.getString("billingadd"),
                                    jsonObj.getString("deliveryadd"),
                                    jsonObj.getString("mobile"),
                                    jsonObj.getString("email"),
                                    jsonObj.getString("itemid"),
                                    jsonObj.getString("itemname"),
                                    jsonObj.getString("itemquantity"),
                                    jsonObj.getString("totalprice"),
                                    jsonObj.getString("paidprice"),
                                    jsonObj.getString("placedon")));
                        }
                    }

                    initRecyclerView();

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

    private void initRecyclerView() {
        Collections.sort(orderList, new HistoryComparator());
        HistoryAdapter historyAdapter = new HistoryAdapter(getContext(), orderList);
        historyRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        historyRv.setAdapter(historyAdapter);

        progress.dismiss();

        historyAdapter.setMItemClickListener(new MainCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                DialogOrderDetail.newInstance(orderList.get(position).getOrderid()).showDialog(getActivity().getSupportFragmentManager(), "dlg");
            }
        });


    }
}
