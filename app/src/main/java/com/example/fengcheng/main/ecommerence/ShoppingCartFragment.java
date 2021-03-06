package com.example.fengcheng.main.ecommerence;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.internal.HttpClient;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.example.fengcheng.main.adapter.CartAdapter;
import com.example.fengcheng.main.dataBean.CartInfo;
import com.example.fengcheng.main.dataBean.OrderHistory;
import com.example.fengcheng.main.db.DbManager;
import com.example.fengcheng.main.utils.SpUtil;
import com.example.fengcheng.main.utils.VolleyHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * shopping cart fragment
 */

public class ShoppingCartFragment extends Fragment {
    RecyclerView recyclerView;
    Button checkoutBtn;
    LinearLayout checkoutLl, summaryLl;
    Button payBtn, cancelBtn, applyBtn, trackBtn;
    EditText couponEdt;
    TextView taxTv, shipTv, totalTv, idTv, timeTv, addressTv, mobileTv, paymentTv;
    private DbManager dbManager;
    private List<CartInfo.OrderBean> itemList;
    private int REQUEST_CODE = 9090;
    private int count, itemSize, tax = 0, deliveyCharge = 23;
    private List<OrderHistory.Order> orderSummaryList;
    CartAdapter cartAdapter;

    private static final String TAG = "ShoppingCartFragment";

    private String mToken = "http://rjtmobile.com/aamir/braintree-paypal-payment/main.php?";
    String token, amount = "1", paymentType, cardDesc;
    HashMap<String, String> paramHash;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

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
        //get shopping cart item list from database
        itemList = dbManager.getCartItemList(SpUtil.getUserId(getContext()));
    }

    private void initClickListener() {
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutLl.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.window_bot_in));
                checkoutLl.setVisibility(View.VISIBLE);

                //count total price need to pay
                countTotal(false);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutLl.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.window_bot_out));
                checkoutLl.setVisibility(View.GONE);
            }
        });

        //make payment -> ask braintree
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call braintree
                itemSize = itemList.size();
                onBraintreeSubmit();
            }
        });

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //apply coupon here
                applyCoupon(couponEdt.getText().toString());
            }
        });

        trackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentOrder(), "historyFgt").commit();
            }
        });
    }

    //init brainttree payment system
    private void onBraintreeSubmit() {
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(token);
        startActivityForResult(dropInRequest.getIntent(getActivity()), REQUEST_CODE);
    }

    //braintree pop up menu callback
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                //this is braintree call back data
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                //this is payment method i choose (credit card)
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();

                paymentType = nonce.getDescription();
                cardDesc = nonce.getTypeLabel();

                String stringNonce = nonce.getNonce();
                Log.d("mylog", "Result: " + stringNonce);
                // Send payment price with the nonce
                // use the result to update your UI and send the payment method nonce to your server
                if (!totalTv.getText().toString().isEmpty()) {
                    amount = totalTv.getText().toString();
                    paramHash = new HashMap<>();
                    paramHash.put("amount", amount);
                    paramHash.put("nonce", stringNonce);
                    //send payment total and payment nonce to rjt server
                    sendPaymentDetails();
                } else
                    Toast.makeText(getContext(), "Please enter a valid amount.", Toast.LENGTH_SHORT).show();

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // the user canceled
                Log.d("mylog", "user canceled");
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("mylog", "Error : " + error.toString());
            }
        }
    }

    private void initRecyclerView() {
        cartAdapter = new CartAdapter(getContext(), itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(cartAdapter);
    }

    private void initView(View v) {
        //retrieve braintree token
        new HttpRequest().execute();
        recyclerView = v.findViewById(R.id.my_item_list);
        checkoutBtn = v.findViewById(R.id.checkout_btn);
        checkoutLl = v.findViewById(R.id.checkout_layout);
        payBtn = v.findViewById(R.id.checkout_confirm_btn);
        cancelBtn = v.findViewById(R.id.checkout_cancel_btn);
        couponEdt = v.findViewById(R.id.coupon_edt);
        applyBtn = v.findViewById(R.id.coupon_btn);
        taxTv = v.findViewById(R.id.tax_total_tv);
        shipTv = v.findViewById(R.id.deli_total_tv);
        totalTv = v.findViewById(R.id.total_tv);
        summaryLl = v.findViewById(R.id.ordersum_ll);
        idTv = v.findViewById(R.id.orderid_tv);
        timeTv = v.findViewById(R.id.time_tv);
        addressTv = v.findViewById(R.id.address_tv);
        mobileTv = v.findViewById(R.id.mobile_tv);
        paymentTv = v.findViewById(R.id.payment_tv);
        trackBtn = v.findViewById(R.id.track_btn);

        couponEdt.setText("2147483648");
    }

    public void countTotal(boolean isTaxed) {
        shipTv.setText("$" + deliveyCharge);
        int total = 0;
        for (CartInfo.OrderBean orderBean : itemList) {
            total = total + Integer.parseInt(orderBean.getPrize()) * orderBean.getQuantity();
        }
        tax = (int) (total * 0.08);
        total = total + tax + deliveyCharge;
        totalTv.setText("$" + total);
        taxTv.setText(" * 8% = $" + tax);
    }

    @Subscribe
    public void onEvent(Boolean quantityChanged) {
        if (quantityChanged) {
            countTotal(quantityChanged);
        }
        
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (dbManager != null) {
            dbManager.closeDatabase();
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

                        int totalBefore = Integer.valueOf(totalTv.getText().toString().substring(1, totalTv.getText().toString().length())) - tax - deliveyCharge;
                        int totolAfter = totalBefore - totalBefore * Integer.valueOf(discount) / 100 + tax + deliveyCharge;

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //save temp list to db here
    }

    private void sendPaymentDetails() {
        orderSummaryList = new ArrayList<>();

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response;
                    JSONArray jsonArray = jsonObject.getJSONArray("Order confirmed");
                    if (jsonArray == null) {
                        Toast.makeText(getContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        //each time request call back will count once
                        count++;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            orderSummaryList.add(new OrderHistory.Order(jsonObj.getString("orderid"),
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
                    //clear shopping cart
                    if (count == itemSize) {
                        //visible summary layout
                        summaryLl.setVisibility(View.VISIBLE);
                        int total = 0;
                        StringBuilder sb = new StringBuilder("Order ID: ");
                        for (OrderHistory.Order order : orderSummaryList) {
                            total = total + Integer.valueOf(order.getTotalprice());
                        }
                        for (int i = 0; i < orderSummaryList.size(); i++){
                            if (i != orderSummaryList.size() - 1){
                                sb.append(orderSummaryList.get(i).getOrderid() + ",");
                            }else {
                                sb.append(orderSummaryList.get(i).getOrderid());
                            }
                        }

                        idTv.setText(sb.toString());
                        timeTv.setText(getString(R.string.pay_time) + orderSummaryList.get(0).getPlacedon());
                        addressTv.setText(getString(R.string.pay_address) + orderSummaryList.get(0).getDeliveryadd());
                        mobileTv.setText(getString(R.string.pay_mobile) + orderSummaryList.get(0).getMobile());


                        paymentTv.setText(getString(R.string.pay_summary) + "\n" + getString(R.string.pay_total) + String.valueOf(total) + "\n"
                                + getString(R.string.pay_paid) + String.valueOf(total) + "\n" + getString(R.string.pay_paymenttype) + cardDesc + "\n" + getString(R.string.pay_card) + paymentType);

                        dbManager.deleteItem(SpUtil.getUserId(getContext()));
                        itemList.clear();
                        cartAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Transaction successful", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getContext(), "Transaction failed", Toast.LENGTH_SHORT).show();
            }
        };

        for (int i = 0; i < itemList.size(); i++) {
            //do rjt order web request
            JsonObjectRequest orderRequest = VolleyHelper.getInstance().paymentRequest(itemList.get(i).getPid(), itemList.get(i).getPname(),
                    String.valueOf(itemList.get(i).getQuantity()), itemList.get(i).getPrize(), SpUtil.getUserId(getContext()), SpUtil.getUserName(getContext()),
                    SpUtil.getMobile(getContext()), SpUtil.getEmail(getContext()), SpUtil.getApiKey(getContext()), listener, errorListener, paramHash);

            AppController.getInstance().addToRequestQueue(orderRequest);
        }
    }


    private class HttpRequest extends AsyncTask {
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getActivity(), android.R.style.Theme_DeviceDefault_Dialog);
            progress.setCancelable(false);
            progress.setMessage("We are contacting our servers for token, Please wait");
            progress.setTitle("Getting token");
            progress.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient client = new HttpClient();
            client.get(mToken, new HttpResponseCallback() {
                @Override
                public void success(String responseBody) {
                    //get token here
                    token = responseBody;
                }

                @Override
                public void failure(Exception exception) {
                    final Exception ex = exception;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Failed to get token: " + ex.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progress.dismiss();
        }
    }

}
