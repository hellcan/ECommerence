package com.example.fengcheng.main.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fengcheng.main.adapter.HistoryDetailAdapter;
import com.example.fengcheng.main.ecommerence.AppController;
import com.example.fengcheng.main.ecommerence.R;
import com.example.fengcheng.main.utils.SpUtil;
import com.example.fengcheng.main.utils.VolleyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * dialog to show the order details
 */

public class DialogOrderDetail extends DialogFragment {
    private RecyclerView recyclerView;
    private static final String TAG = "DialogOrderDetail";
    String selectedOrderId;
    TextView shipTv;


    public static DialogOrderDetail newInstance(String orderId) {
        Bundle args = new Bundle();
        args.putString("orderId", orderId);
        DialogOrderDetail dialogOrderDetail = new DialogOrderDetail();
        dialogOrderDetail.setArguments(args);
        return dialogOrderDetail;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //remove dialog title
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog rootView
        View v = inflater.inflate(R.layout.dialog_fragment_order_detail, container, false);

        initView(v);

        pullData();

        return v;
    }

    private void initRecycler(String shipId, String statusId) {
        shipTv.setText(getString(R.string.track_order) + "\n#" +  shipId);
        HistoryDetailAdapter mTimeLineAdapter = new HistoryDetailAdapter(getContext(), statusId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mTimeLineAdapter);
    }

    private void pullData() {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = response;
                    JSONArray jsonArray = jsonObject.getJSONArray("Shipment track");
                    if (jsonArray == null) {
                        Toast.makeText(getContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);

                            initRecycler(jsonObj.getString("shipmentid"), jsonObj.getString("shipmentstatus"));

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

        JsonObjectRequest logRequest = VolleyHelper.getInstance().shipmentRequest(SpUtil.getApiKey(getContext()), SpUtil.getUserId(getContext()), selectedOrderId, listener, errorListener);

        AppController.getInstance().addToRequestQueue(logRequest, "trackshipment");
    }

    private void initView(View v) {
        recyclerView = v.findViewById(R.id.timeline);
        shipTv = v.findViewById(R.id.shipId);
        selectedOrderId = getArguments().getString("orderId");
    }

    public void showDialog(FragmentManager fragmentManager, String tag) {
        if (getDialog() == null || !getDialog().isShowing()) {
            show(fragmentManager, tag);
        }
    }
}
