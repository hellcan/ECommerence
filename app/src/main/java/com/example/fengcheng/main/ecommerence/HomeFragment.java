package com.example.fengcheng.main.ecommerence;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.fengcheng.main.adapter.MainCategoryAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package com.example.fengcheng.main.ecommerence
 * @FileName HomeFragment
 * @Date 4/11/18, 1:06 AM
 * @Author Created by fengchengding
 * @Description ECommerence
 */

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        initview(v);
        
        initRecyclerView();

        return v;
    }

    private void initRecyclerView() {
        MainCategoryAdapter mainCategoryAdapter = new MainCategoryAdapter(getContext());
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mainCategoryAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void initview(View v) {
        recyclerView = v.findViewById(R.id.main_category_rv);
    }
}
