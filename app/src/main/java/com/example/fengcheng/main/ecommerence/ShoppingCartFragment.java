package com.example.fengcheng.main.ecommerence;

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
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fengcheng.main.adapter.CartAdapter;
import com.example.fengcheng.main.dataBean.CartInfo;
import com.example.fengcheng.main.db.DbManager;
import com.example.fengcheng.main.utils.SpUtil;
import com.example.fengcheng.main.utils.VolleyHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package com.example.fengcheng.main.ecommerence
 * @FileName ShoppingCartFragment
 * @Date 4/11/18, 12:05 PM
 * @Author Created by fengchengding
 * @Description ECommerence
 */

public class ShoppingCartFragment extends Fragment {
    RecyclerView recyclerView;
    Button checkoutBtn;
    LinearLayout checkoutLl;
    Button confirmBtn, cancelBtn, applyBtn;
    EditText couponEdt;
    TextView taxTv, shipTv, totalTv;
    private DbManager dbManager;
    private List<CartInfo.OrderBean> itemList;
    private static final String TAG = "ShoppingCartFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_shopping_cart, container, false);

        initView(v);

        pullData();

        initRecyclerView();

        if (itemList.size() > 0) {
            initClickListener();
        }

        return v;
    }

    private void pullData() {
        dbManager = new DbManager(getContext());
        dbManager.openDatabase();

        itemList = dbManager.getCartItemList(SpUtil.getUserId(getContext()));
    }

    private void initClickListener() {
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutLl.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.window_bot_in));
                checkoutLl.setVisibility(View.VISIBLE);

                countTotal();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutLl.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.window_bot_out));
                checkoutLl.setVisibility(View.GONE);
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putOrder();
            }
        });

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //apply coupon here
                applyCoupon(couponEdt.getText().toString());
            }
        });
    }

    private void initRecyclerView() {
        CartAdapter cartAdapter = new CartAdapter(getContext(), itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(cartAdapter);
    }


    private void initView(View v) {
        recyclerView = v.findViewById(R.id.my_item_list);
        checkoutBtn = v.findViewById(R.id.checkout_btn);
        checkoutLl = v.findViewById(R.id.checkout_layout);
        confirmBtn = v.findViewById(R.id.checkout_confirm_btn);
        cancelBtn = v.findViewById(R.id.checkout_cancel_btn);
        couponEdt = v.findViewById(R.id.coupon_edt);
        applyBtn = v.findViewById(R.id.coupon_btn);
        taxTv = v.findViewById(R.id.tax_total_tv);
        shipTv = v.findViewById(R.id.deli_total_tv);
        totalTv = v.findViewById(R.id.total_tv);

        couponEdt.setText("2147483648");
    }

    public void countTotal() {
        shipTv.setText("$23");
        int total = 0;
        for (CartInfo.OrderBean orderBean : itemList) {
            total = total + Integer.parseInt(orderBean.getPrize()) * orderBean.getQuantity();
        }
        int tax = (int) (total * 0.08);
        totalTv.setText("$" + total);
        taxTv.setText(" * 8% = $" + tax);
    }

    @Subscribe
    public void onEvent(Boolean quantityChanged) {
        if (quantityChanged) {
            countTotal();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (dbManager != null) {
            dbManager.closeDatabase();
        }
    }

    public void putOrder() {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                try {

                    JSONObject jsonObject = response;
                    JSONArray jsonArray = jsonObject.getJSONArray("Order confirmed");
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

        for (int i = 0; i < itemList.size(); i++) {
            JsonObjectRequest orderRequest = VolleyHelper.getInstance().makeOrder(itemList.get(i).getPid(), itemList.get(i).getPname(),
                    String.valueOf(itemList.get(i).getQuantity()), itemList.get(i).getPrize(), SpUtil.getUserId(getContext()), SpUtil.getUserName(getContext()),
                    SpUtil.getMobile(getContext()), SpUtil.getEmail(getContext()), SpUtil.getApiKey(getContext()), listener, errorListener);

            AppController.getInstance().addToRequestQueue(orderRequest);
        }
    }

    public void applyCoupon(String coupon) {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            String discount = "";

            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                try {

                    JSONObject jsonObject = response;
                    JSONArray jsonArray = jsonObject.getJSONArray("Coupon discount");
                    if (jsonArray == null) {
                        Toast.makeText(getContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            jsonObj.getString("couponno");
                            discount = jsonObj.getString("discount");
                        }
                    }

                    if (!totalTv.getText().toString().contains("*")) {
                        int totalBefore = Integer.valueOf(totalTv.getText().toString().substring(1, totalTv.getText().toString().length()));
                        int totolAfter = totalBefore - totalBefore * Integer.valueOf(discount) / 100;

                        totalTv.setText("* " + discount + "% = $" + totolAfter);
                    }

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

        JsonObjectRequest couponRequest = VolleyHelper.getInstance().couponRequest(SpUtil.getApiKey(getContext()),
                SpUtil.getUserId(getContext()), coupon, listener, errorListener);

        AppController.getInstance().addToRequestQueue(couponRequest);
    }
}
