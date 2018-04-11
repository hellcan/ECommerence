package com.example.fengcheng.main.ecommerence;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @Package com.example.fengcheng.main.ecommerence
 * @FileName ProfileFragment
 * @Date 4/11/18, 12:05 PM
 * @Author Created by fengchengding
 * @Description ECommerence
 */

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        return v;
    }
}
