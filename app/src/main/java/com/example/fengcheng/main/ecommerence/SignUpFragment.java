package com.example.fengcheng.main.ecommerence;

import android.os.Bundle;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.fengcheng.main.utils.VolleyHelper;

import java.net.UnknownHostException;
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
    private static final String EMAIL_FORMAT = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";


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
                if (TextUtils.isEmpty(firstEdt.getText().toString()) || TextUtils.isEmpty(lastEdt.getText().toString())
                        || TextUtils.isEmpty(addressEdt.getText().toString()) || TextUtils.isEmpty(mobEdt.getText().toString())
                        || TextUtils.isEmpty(emailEdt.getText().toString()) || TextUtils.isEmpty(pwdEdt.getText().toString())) {
                    Toast.makeText(getActivity().getBaseContext(), "Text field can not be null", Toast.LENGTH_SHORT).show();
                } else if (pwdEdt.getText().toString().length() < 6) {
                    Toast.makeText(getActivity().getBaseContext(), "Password can not less than 6 digits", Toast.LENGTH_SHORT).show();
                } else if (mobEdt.getText().toString().length() != 10) {
                    Toast.makeText(getActivity().getBaseContext(), "Mobile num must 10 digits", Toast.LENGTH_SHORT).show();
                } else if (!emailEdt.getText().toString().matches(EMAIL_FORMAT)){
                    Toast.makeText(getActivity().getBaseContext(), "Not a valid email format", Toast.LENGTH_SHORT).show();
                }else {

                    Response.Listener<String> listener = new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getActivity().getBaseContext(), response, Toast.LENGTH_SHORT).show();
                            firstEdt.setText("");
                            lastEdt.setText("");
                            addressEdt.setText("");
                            mobEdt.setText("");
                            emailEdt.setText("");
                            pwdEdt.setText("");
                        }
                    };

                    Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity().getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    };


                    StringRequest regRequest = VolleyHelper.getInstance().registrationRequest(firstEdt.getText().toString(), lastEdt.getText().toString(), addressEdt.getText().toString(),
                            mobEdt.getText().toString(), emailEdt.getText().toString(), pwdEdt.getText().toString(), listener, errorListener);

                    AppController.getInstance().addToRequestQueue(regRequest, "reg");
                }


            }
        });

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getSupportFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getSupportFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

    }



}
