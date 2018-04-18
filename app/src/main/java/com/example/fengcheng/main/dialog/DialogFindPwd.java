package com.example.fengcheng.main.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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

public class DialogFindPwd extends DialogFragment {
    private EditText emailEdt;
    private Button submitBtn;
    private static final String TAG = "DialogFindPwd";
    private static final String EMAIL_FORMAT = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    public static DialogFindPwd newInstance() {
        Bundle args = new Bundle();
        DialogFindPwd dialogOrderDetail = new DialogFindPwd();
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
        getDialog().setTitle("Find Password");
        //dialog rootView
        View v = inflater.inflate(R.layout.dialog_fragment_find_pwd, container, false);

        initView(v);

        pullData();

        clickListener();

        return v;
    }

    private void clickListener() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(emailEdt.getText().toString())) {
                    Toast.makeText(getContext(), R.string.alert_not_null, Toast.LENGTH_SHORT).show();
                } else if (!emailEdt.getText().toString().matches(EMAIL_FORMAT)) {
                    Toast.makeText(getActivity().getBaseContext(), R.string.alert_email_format, Toast.LENGTH_SHORT).show();
                } else {
                    Response.Listener<String> listener = new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                        }
                    };

                    Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(TAG, error.getMessage());
                        }
                    };

                    Toast.makeText(getActivity().getBaseContext(), R.string.reset_pwd, Toast.LENGTH_SHORT).show();
                    emailEdt.setText("");

                    StringRequest forgetRequest = VolleyHelper.getInstance().forgetPwdRequest(emailEdt.getText().toString(), listener, errorListener);

                    AppController.getInstance().addToRequestQueue(forgetRequest, "forget");
                }
            }
        });
    }

    private void pullData() {
    }

    private void initView(View v) {
        emailEdt = v.findViewById(R.id.find_pwd_email_edt);
        submitBtn = v.findViewById(R.id.find_btn);
    }

    public void showDialog(FragmentManager fragmentManager, String tag) {
        if (getDialog() == null || !getDialog().isShowing()) {
            show(fragmentManager, tag);
        }
    }
}
