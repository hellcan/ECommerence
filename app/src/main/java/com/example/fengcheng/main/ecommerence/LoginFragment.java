package com.example.fengcheng.main.ecommerence;

import android.app.ProgressDialog;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.fengcheng.main.dialog.DialogFindPwd;
import com.example.fengcheng.main.utils.SpUtil;
import com.example.fengcheng.main.utils.VolleyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * log in fragment doing log in business execution
 */

public class LoginFragment extends Fragment {
    EditText mobileEdt, pwdEdt;
    CheckBox remeCbx;
    Button signBtn, regBtn, findPwdBtn;
    ImageButton closeBtn;
    private static final String TAG = "LoginFragment";
    ProgressDialog progress;

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

        //check if shared preference have remember me data
        String remember = SpUtil.getRemember(getContext());

        //if yes, fill the data in editText and check remember me checkbox
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
                progress = new ProgressDialog(getContext(), R.style.Base_AlertDialog);
                progress.setCancelable(false);
                progress.show();
                //everyTime we need check if the remember me data had been changed
                saveRemember();
                //check if mobile length equal to 10
                if (mobileEdt.getText().toString().length() != 10) {
                    Toast.makeText(getActivity().getBaseContext(), R.string.mobile_verificatin, Toast.LENGTH_SHORT).show();
                    progress.dismiss();

                    //password length must larger than 6
                } else if (pwdEdt.getText().toString().length() < 6) {
                    Toast.makeText(getActivity().getBaseContext(), R.string.pwd_verify, Toast.LENGTH_SHORT).show();
                    progress.dismiss();

                } else {
                    //go login and get response from server
                    Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
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
                                    progress.dismiss();

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
                            progress.dismiss();
                        }
                    };

                    //generate volley json array request
                    JsonArrayRequest logRequest = VolleyHelper.getInstance().loginRequest(mobileEdt.getText().toString(), pwdEdt.getText().toString(), listener, errorListener);

                    AppController.getInstance().addToRequestQueue(logRequest, "reg");
                }
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRemember();
                //registration screen
                getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.frame_container, new SignUpFragment(), "signfgt")
                        .addToBackStack(null)
                        .commit();

            }
        });

        //find password
        findPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRemember();

                DialogFindPwd.newInstance().showDialog(getActivity().getSupportFragmentManager(), "findpwd");
            }
        });


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRemember();
                //close the app
                System.exit(0);
            }
        });
    }

    public void saveRemember() {
        if (remeCbx.isChecked()) {
            SpUtil.setRemember(getContext(), mobileEdt.getText().toString());
        } else {
            SpUtil.setRemember(getContext(), null);
        }
    }

}
