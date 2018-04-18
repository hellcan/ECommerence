package com.example.fengcheng.main.ecommerence;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.fengcheng.main.dataBean.Products;
import com.example.fengcheng.main.utils.SpUtil;
import com.example.fengcheng.main.utils.VolleyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * fragment for profile update
 */

public class ProfileFragment extends Fragment {
    EditText firstEdt, lastEdt, addressEdt, mobEdt, emailEdt, oldPwdEdt, newPwdEdt, resetMobileEdt;
    Button updateBtn, resetBtn;
    private static final String TAG = "ProfileFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        initView(v);

        return v;
    }

    private void initView(View v) {
        firstEdt = v.findViewById(R.id.pro_fname_edt);
        lastEdt = v.findViewById(R.id.pro_lname_edt);
        addressEdt = v.findViewById(R.id.pro_address_edt);
        mobEdt = v.findViewById(R.id.pro_mobile_edt);
        emailEdt = v.findViewById(R.id.pro_email_edt);
        oldPwdEdt = v.findViewById(R.id.pro_old_pwd_edt);
        newPwdEdt = v.findViewById(R.id.pro_new_pwd_edt);
        resetMobileEdt = v.findViewById(R.id.pro_reset_mobile_edt);
        updateBtn = v.findViewById(R.id.pro_update_btn);
        resetBtn = v.findViewById(R.id.pro_reset_pwd_btn);

        firstEdt.setText(SpUtil.getUserName(getContext()));
        lastEdt.setText(SpUtil.getLastName(getContext()));
        mobEdt.setText(SpUtil.getMobile(getContext()));
        emailEdt.setText(SpUtil.getEmail(getContext()));

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPwd();
            }
        });
    }

    private void resetPwd() {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = (JSONObject) response;
                    JSONArray jsonArray = jsonObject.getJSONArray("msg");
                    if (jsonArray == null) {
                        Toast.makeText(getContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
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

        JsonObjectRequest logRequest = VolleyHelper.getInstance().resetPwdRequest(resetMobileEdt.getText().toString(), oldPwdEdt.getText().toString(),
                newPwdEdt.getText().toString(), listener, errorListener);

        AppController.getInstance().addToRequestQueue(logRequest, "getProductList");
    }

    public void updateProfile() {
        Response.Listener<String> listener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();

                SpUtil.updateUserInfo(getContext(),firstEdt.getText().toString(), lastEdt.getText().toString(),
                        emailEdt.getText().toString(), mobEdt.getText().toString());

                firstEdt.setText("");
                lastEdt.setText("");
                addressEdt.setText("");
                mobEdt.setText("");
                emailEdt.setText("");
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };


        StringRequest updateRequest = VolleyHelper.getInstance().updateProfile(firstEdt.getText().toString(), lastEdt.getText().toString(), addressEdt.getText().toString(),
                emailEdt.getText().toString(), mobEdt.getText().toString(), listener, errorListener);

        AppController.getInstance().addToRequestQueue(updateRequest, "update");
    }

}
