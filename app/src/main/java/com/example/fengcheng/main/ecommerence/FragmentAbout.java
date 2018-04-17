package com.example.fengcheng.main.ecommerence;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * About Fragment
 */

public class FragmentAbout extends Fragment {
    TextView aboutTv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);

        initView(v);
        return v;
    }

    private void initView(View v) {
        aboutTv = v.findViewById(R.id.aboutus);
        aboutTv.setText(getString(R.string.about_us));


    }
}
