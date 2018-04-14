package com.example.fengcheng.main.ecommerence;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fengcheng.main.adapter.CartAdapter;
import com.example.fengcheng.main.dataBean.CartInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * @Package com.example.fengcheng.main.ecommerence
 * @FileName ShoppingCartFragment
 * @Date 4/11/18, 12:05 PM
 * @Author Created by fengchengding
 * @Description ECommerence
 */

public class ShoppingCartFragment extends Fragment {
    RecyclerView recyclerView;
    Button checkoutBtn;
    LinearLayout checkoutLl;
    Button confirmBtn, cancelBtn, applyBtn;
    EditText couponEdt;
    TextView taxTv, shipTv, totalTv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_shopping_cart, container, false);

        initView(v);

        initRecyclerView();

        if (CartInfo.getInstance().getOrderBeanList().size() > 0) {
            initClickListener();
        }

        return v;
    }

    private void initClickListener() {
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutLl.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.window_bot_in));
                checkoutLl.setVisibility(View.VISIBLE);

                countTotal();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutLl.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.window_bot_out));
                checkoutLl.setVisibility(View.GONE);
            }
        });
    }

    private void initRecyclerView() {
        CartAdapter cartAdapter = new CartAdapter(getContext(), CartInfo.getInstance().getOrderBeanList());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(cartAdapter);

    }


    private void initView(View v) {
        recyclerView = v.findViewById(R.id.my_item_list);
        checkoutBtn = v.findViewById(R.id.checkout_btn);
        checkoutLl = v.findViewById(R.id.checkout_layout);
        confirmBtn = v.findViewById(R.id.checkout_confirm_btn);
        cancelBtn = v.findViewById(R.id.checkout_cancel_btn);
        couponEdt = v.findViewById(R.id.coupon_edt);
        applyBtn = v.findViewById(R.id.coupon_btn);
        taxTv = v.findViewById(R.id.tax_total_tv);
        shipTv = v.findViewById(R.id.deli_total_tv);
        totalTv = v.findViewById(R.id.total_tv);
    }

    public void countTotal(){
        shipTv.setText("$23");
        int total = 0;
        for (CartInfo.OrderBean orderBean : CartInfo.getInstance().getOrderBeanList()){
            total = total + Integer.parseInt(orderBean.getPrize()) * orderBean.getQuantity();
        }
        int tax = (int)(total * 0.08);
        totalTv.setText("$" + total);
        taxTv.setText(" * 8% = $" + tax);
    }

    @Subscribe
    public void onEvent(Boolean quantityChanged){
        if (quantityChanged){
            countTotal();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
