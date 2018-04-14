package com.example.fengcheng.main.ecommerence;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fengcheng.main.adapter.MainCategoryAdapter;
import com.example.fengcheng.main.adapter.ProductAdapter;
import com.example.fengcheng.main.dataBean.Products;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * fragment to display product list
 */

public class ProductFragment extends Fragment {
    RecyclerView productList;
    List<Products.ProductBean> productBeanList;
    ProductAdapter productAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product, container, false);

        initView(v);

        pullData();

        initRecyclerView();

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

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragmentDetail, "detailFgt").commit();
            }
        });
    }

    /**
     * get product list data
     */

    private void pullData() {
        mockData();
    }

    /**
     * @param v rootView: fragment_product
     *          get UI component in the root view
     */

    private void initView(View v) {
        productList = v.findViewById(R.id.product_list_rv);
    }


    public void mockData() {
        productBeanList = new ArrayList<>();

        productBeanList.add(new Products.ProductBean("308", "i5-Laptop", "1", "60000",
                "Online directory of electrical goods manufacturers, electronic goodssuppliers and electronic product manufacturers. Get details of electronic products",
                "https://rjtmobile.com/ansari/shopingcart/admin/uploads/product_t_images/images.jpg"));

        productBeanList.add(new Products.ProductBean("315", "HP", "1", "40000",
                "Hp Laptops - Buy Hp Laptops at India's Best Online Shopping Store.Check Price in India and Buy Online. Free Shipping - Free Home Delivery atecom.com",
                "https://rjtmobile.com/ansari/shopingcart/admin/uploads/product_t_images/mylaptop1.jpg"));

        productBeanList.add(new Products.ProductBean("316", "Mac-Book", "11", "70000",
                "Online directory of electrical goods manufacturers, electronic goodssuppliers and electronic product manufacturers. Get details of electronic products",
                "https://rjtmobile.com/ansari/shopingcart/admin/uploads/product_t_images/mylaptop1.jpg"));
    }
}
