package com.example.fengcheng.main.ecommerence;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @Package com.example.fengcheng.main.ecommerence
 * @FileName SignUpFragment
 * @Date 4/9/18, 11:14 PM
 * @Author Created by fengchengding
 * @Description ECommerence
 */

public class SignUpFragment extends Fragment {
    EditText firstEdt, lastEdt, addressEdt, mobEdt, emailEdt, pwdEdt;
    Button signUpBtn, logBtn;
    ImageButton closeBtn;
    private final String BASE_URL = "http://rjtmobile.com/aamir/e-commerce/android-app/";
    private final String HEADER_REG_URL = "shop_reg.php?";
    private final String FNAME = "fname=";
    private final String LNAME = "lname=";
    private final String ADDRESS = "address=";
    private final String EMAIL = "email=";
    private final String MOBILE = "mobile=";
    private final String PWD = "password=";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup, container, false);

        initView(v);

        clickListener();

        return v;
    }

    private void initView(View v) {
        closeBtn = v.findViewById(R.id.close_btn);
        firstEdt = v.findViewById(R.id.fname_edt);
        lastEdt = v.findViewById(R.id.lname_edt);
        addressEdt = v.findViewById(R.id.address_edt);
        mobEdt = v.findViewById(R.id.mobile_edt);
        emailEdt = v.findViewById(R.id.email_edt);
        pwdEdt = v.findViewById(R.id.pwd_edt);
        signUpBtn = v.findViewById(R.id.signup_btn);
        logBtn = v.findViewById(R.id.login_btn);

    }

    private void clickListener() {
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSignUpVolley(firstEdt.getText().toString(), lastEdt.getText().toString(), addressEdt.getText().toString(),
                        mobEdt.getText().toString(), emailEdt.getText().toString(), pwdEdt.getText().toString());
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getSupportFragmentManager() != null){
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

    }

    private void initSignUpVolley(final String first, final String last, final String address, final String mob, final String email, final String pwd) {
        String regUrl = BASE_URL + HEADER_REG_URL + FNAME + first + "&" + LNAME + last + "&" + ADDRESS + address + "&" + EMAIL + email
                + "&" + MOBILE + mob + "&" + PWD + pwd;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, regUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                Log.i("测试", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("测试", error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(FNAME, first);
                map.put(LNAME, last);
                map.put(ADDRESS, address);
                map.put(EMAIL, email);
                map.put(MOBILE, mob);
                map.put(PWD, pwd);


                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, "reg");
    }


}
