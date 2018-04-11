package com.example.fengcheng.main.ecommerence;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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
    Button signBtn, regBtn;
    ImageButton closeBtn;

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

    }

    private void clickListener(){
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainHomeActivity.class));
            }
        });


        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.frame_container, new SignUpFragment(), "signfgt")
                        .addToBackStack(null)
                        .commit();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
                Toast.makeText(getActivity().getApplicationContext(), "Exit App", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
