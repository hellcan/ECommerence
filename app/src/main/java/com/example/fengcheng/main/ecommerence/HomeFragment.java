package com.example.fengcheng.main.ecommerence;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fengcheng.main.adapter.MainCategoryAdapter;
import com.example.fengcheng.main.adapter.SubCategoryAdapter;
import com.example.fengcheng.main.dataBean.MainCategories;
import com.example.fengcheng.main.utils.SpUtil;
import com.example.fengcheng.main.utils.VolleyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    List<MainCategories.CategoryBean> mainCategoryList;
    List<MainCategories.CategoryBean> subCategoryList;
    private static final String TAG = "HomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        initview(v);

        pullData();

        return v;
    }

    private void pullData() {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                mainCategoryList = new ArrayList<>();
                try {
                    JSONObject jsonObject = (JSONObject) response;
                    JSONArray jsonArray = jsonObject.getJSONArray("category");
                    if (jsonArray == null) {
                        Toast.makeText(getContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            MainCategories.CategoryBean categoryBean = new MainCategories.CategoryBean(
                                    jsonObj.getString("cid"),
                                    jsonObj.getString("cname"),
                                    jsonObj.getString("cdiscription"),
                                    jsonObj.getString("cimagerl"));

                            mainCategoryList.add(categoryBean);
                        }
                    }

                    initRecyclerView(mainCategoryList);

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

        JsonObjectRequest logRequest = VolleyHelper.getInstance().getProductRequest(SpUtil.getUserId(getContext()), SpUtil.getApiKey(getContext()), listener, errorListener);

        AppController.getInstance().addToRequestQueue(logRequest, "getProductList");

    }


    private void initRecyclerView(List<MainCategories.CategoryBean> dataList) {
        MainCategoryAdapter mainCategoryAdapter = new MainCategoryAdapter(getContext(), dataList);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mainCategoryAdapter);


        mainCategoryAdapter.setMItemClickListener(new MainCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
//                startActivity(new Intent(getActivity(), SubActivity.class));
                pullSubData(position);
                MainHomeActivity activity = (MainHomeActivity) getActivity();
                if (activity != null) {
                    activity.updateTitle(mainCategoryList.get(position).getCname());
                }
            }
        });
    }

    /**
     * @param dataList sub category data list
     * @param mainCategoryList  main category data list
     * we set up sub category list here
     */

    private void resetRecyclerView(List<MainCategories.CategoryBean> dataList, List<MainCategories.CategoryBean> mainCategoryList) {
        SubCategoryAdapter adapter = new SubCategoryAdapter(getContext(), dataList, mainCategoryList);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setMItemClickListener(new SubCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction().
                        replace(R.id.content_frame, new ProductFragment(), "productfgt")
                        .commit();
            }
        });
    }

    private void pullSubData(int position) {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                subCategoryList = new ArrayList<>();
                try {
                    JSONObject jsonObject = (JSONObject) response;
                    JSONArray jsonArray = jsonObject.getJSONArray("subcategory");
                    if (jsonArray == null) {
                        Toast.makeText(getContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            MainCategories.CategoryBean categoryBean = new MainCategories.CategoryBean(
                                    jsonObj.getString("scid"),
                                    jsonObj.getString("scname"),
                                    jsonObj.getString("scdiscription"),
                                    jsonObj.getString("scimageurl"));

                            subCategoryList.add(categoryBean);
                        }
                    }

                    resetRecyclerView(subCategoryList, mainCategoryList);

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

        JsonObjectRequest logRequest = VolleyHelper.getInstance().getSuCategoryRequest(mainCategoryList.get(position).getCid(), SpUtil.getApiKey(getContext()), SpUtil.getUserId(getContext()), listener, errorListener);

        AppController.getInstance().addToRequestQueue(logRequest, "getSubProduct");
    }

    private void initview(View v) {
        recyclerView = v.findViewById(R.id.main_category_rv);
    }
}
