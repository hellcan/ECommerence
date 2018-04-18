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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.internal.HttpClient;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.example.fengcheng.main.adapter.CartAdapter;
import com.example.fengcheng.main.adapter.WishAdapter;
import com.example.fengcheng.main.dataBean.CartInfo;
import com.example.fengcheng.main.db.DbManager;
import com.example.fengcheng.main.utils.SpUtil;
import com.example.fengcheng.main.utils.VolleyHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * shopping cart fragment
 */

public class WishFragment extends Fragment {
    RecyclerView recyclerView;
    private DbManager dbManager;
    //wish item list
    private List<CartInfo.OrderBean> itemList;
    WishAdapter wishAdapter;

    private static final String TAG = "WishFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product, container, false);

        initView(v);

        pullData();

        initRecyclerView();

        return v;
    }

    private void pullData() {
        dbManager = new DbManager(getContext());
        dbManager.openDatabase();
        //get wish cart item list from database
        itemList = dbManager.getWishItemList(SpUtil.getUserId(getContext()));
    }


    private void initRecyclerView() {
        wishAdapter = new WishAdapter(getContext(), itemList, dbManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(wishAdapter);
    }

    private void initView(View v) {
        recyclerView = v.findViewById(R.id.product_list_rv);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (dbManager != null) {
            dbManager.closeDatabase();
        }
    }
}
