package com.example.fengcheng.main.ecommerence;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.fengcheng.main.utils.SpUtil;
import com.example.fengcheng.main.utils.VolleyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Package com.example.fengcheng.main.ecommerence
 * @FileName LoginFragment
 * @Date 4/9/18, 11:14 PM
 * @Author Created by fengchengding
 * @Description ECommerence
 */

public class LoginFragment extends Fragment {
    EditText mobileEdt, pwdEdt;
    CheckBox remeCbx;
    Button signBtn, regBtn, findPwdBtn;
    ImageButton closeBtn;
    private static final String TAG = "LoginFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        initView(v);

        clickListener();

        return v;
    }

    private void initView(View v) {
        closeBtn = v.findViewById(R.id.close_btn);
        mobileEdt = v.findViewById(R.id.mobile_edt);
        pwdEdt = v.findViewById(R.id.pwd_edt);
        remeCbx = v.findViewById(R.id.remember_cbx);
        signBtn = v.findViewById(R.id.signin_btn);
        regBtn = v.findViewById(R.id.signup_btn);
        findPwdBtn = v.findViewById(R.id.findpwd_btn);

        String remember = SpUtil.getRemember(getContext());

        if (remember != null) {
            mobileEdt.setText(remember);
            remeCbx.setChecked(true);
        } else {
            remeCbx.setChecked(false);
        }

    }

    private void clickListener() {
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRemember();

                if (mobileEdt.getText().toString().length() != 10) {
                    Toast.makeText(getActivity().getBaseContext(), "Mobile num must be 10 digits", Toast.LENGTH_SHORT).show();
                } else if (pwdEdt.getText().toString().length() < 6) {
                    Toast.makeText(getActivity().getBaseContext(), "Password must be larger than 6 digits", Toast.LENGTH_SHORT).show();
                } else {

                    Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            Toast.makeText(getActivity().getBaseContext(), response.toString(), Toast.LENGTH_SHORT).show();
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject user = (JSONObject) response.get(i);
                                    String msg = user.getString("msg");
                                    String id = user.getString("id");
                                    String firstname = user.getString("firstname");
                                    String lastname = user.getString("lastname");
                                    String email = user.getString("email");
                                    String mobile = user.getString("mobile");
                                    String apiKey = user.getString("appapikey ");

                                    SpUtil.setUserInfo(getContext(), firstname, lastname, email, mobile, apiKey, id);

                                    startActivity(new Intent(getActivity(), MainHomeActivity.class));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity().getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    };

                    JsonArrayRequest logRequest = VolleyHelper.getInstance().loginRequest(mobileEdt.getText().toString(), pwdEdt.getText().toString(), listener, errorListener);

                    AppController.getInstance().addToRequestQueue(logRequest, "reg");
                }
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRemember();

                getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.frame_container, new SignUpFragment(), "signfgt")
                        .addToBackStack(null)
                        .commit();

            }
        });

        findPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRemember();
            }
        });


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRemember();
                System.exit(0);
            }
        });
    }

    public void saveRemember() {
        if (remeCbx.isChecked()) {
            SpUtil.setRemember(getContext(), mobileEdt.getText().toString());
        }else {
            SpUtil.setRemember(getContext(), null);
        }
    }

}
