package com.example.fengcheng.main.ecommerence;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fengcheng.main.adapter.MainCategoryAdapter;
import com.example.fengcheng.main.adapter.ProductAdapter;
import com.example.fengcheng.main.dataBean.MainCategories;
import com.example.fengcheng.main.dataBean.Products;
import com.example.fengcheng.main.utils.SpUtil;
import com.example.fengcheng.main.utils.VolleyHelper;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * fragment to display product list
 */

public class ProductListFragment extends Fragment {
    RecyclerView productList;
    List<Products.ProductBean> productBeanList;
    ProductAdapter productAdapter;
    private static final String TAG = "ProductListFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product, container, false);

        initView(v);

        pullData();

        return v;
    }

    /**
     * create recyclerView (product list)
     */

    private void initRecyclerView() {
        productAdapter = new ProductAdapter(getActivity().getBaseContext(), productBeanList);
        productList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        productList.setAdapter(productAdapter);
        productList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        productAdapter.setMItemClickListener(new MainCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                FragmentDetail fragmentDetail = new FragmentDetail();
                Bundle bundle = new Bundle();
                bundle.putString("imgurl", productBeanList.get(position).getImageurl());
                bundle.putString("name", productBeanList.get(position).getPname());
                bundle.putString("price", productBeanList.get(position).getPrize());
                bundle.putString("desc", productBeanList.get(position).getDiscription());
                bundle.putString("id", productBeanList.get(position).getPid());

                fragmentDetail.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragmentDetail, "detailFgt").addToBackStack(null).commit();
            }
        });
    }

    /**
     * get product list data
     */

    private void pullData() {
        String cid = getArguments().getString("cid");
        String scid = getArguments().getString("scid");
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                productBeanList = new ArrayList<>();
                try {
                    JSONObject jsonObject = response;
                    JSONArray jsonArray = jsonObject.getJSONArray("products");
                    if (jsonArray == null) {
                        Toast.makeText(getContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            Products.ProductBean categoryBean = new Products.ProductBean(
                                    jsonObj.getString("id"),
                                    jsonObj.getString("pname"),
                                    jsonObj.getString("quantity"),
                                    jsonObj.getString("prize"),
                                    jsonObj.getString("discription"),
                                    jsonObj.getString("image"));

                            productBeanList.add(categoryBean);
                        }
                    }

                    initRecyclerView();

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

        JsonObjectRequest logRequest = VolleyHelper.getInstance().getProductListRequest(cid, scid, SpUtil.getApiKey(getContext()), SpUtil.getUserId(getContext()), listener, errorListener);

        AppController.getInstance().addToRequestQueue(logRequest, "getProductList");
    }

    /**
     * @param v rootView: fragment_product
     *          get UI component in the root view
     */

    private void initView(View v) {
        productList = v.findViewById(R.id.product_list_rv);
    }
}
