package com.example.fengcheng.main.ecommerence;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fengcheng.main.dataBean.CartInfo;
import com.squareup.picasso.Picasso;


/**
 * fragment to display a single product detail information
 */


public class FragmentDetail extends Fragment {
    ImageView productPicIv;
    TextView titleTv, priceTv, descTv;
    Button addCartBtn;
    String id, name, price, imgUrl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_product_detail, container, false);

        initView(v);

        initClickListener();

        return v;
    }


    /**
     * @param v rootView: fragment_product
     *          get UI component in the root view
     */

    private void initView(View v) {
        productPicIv = v.findViewById(R.id.product_pic);
        titleTv = v.findViewById(R.id.product_title_tv);
        priceTv = v.findViewById(R.id.product_price_tv);
        descTv = v.findViewById(R.id.product_desc_tv);
        addCartBtn = v.findViewById(R.id.add_cart_tbn);

        Bundle bundle = getArguments();

        if (bundle != null) {
            id = bundle.getString("id");
            name = bundle.getString("name");
            price = bundle.getString("price");
            imgUrl = bundle.getString("imgurl");

            if (id.equals("308")) {
                Picasso.with(getContext()).load(R.drawable.i5_laptop).into(productPicIv);
            } else if (id.equals("315")) {
                Picasso.with(getContext()).load(R.drawable.hp).into(productPicIv);

            } else if (id.equals("316")) {
                Picasso.with(getContext()).load(R.drawable.macbook).into(productPicIv);
            }

            titleTv.setText(name);
            priceTv.setText("$" + price);
            descTv.setText(bundle.getString("desc"));
        }

    }

    /**
     * handle add to cart Button event here
     */


    private void initClickListener() {
        addCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CartInfo.getInstance().getOrderBeanList().size() > 0) {
                    for (CartInfo.OrderBean item : CartInfo.getInstance().getOrderBeanList()) {
                        if (item.getPid().equals(id)) {
                            item.setQuantity(item.getQuantity() + 1);
                        } else {
                            CartInfo.getInstance().getOrderBeanList().add(new CartInfo.OrderBean(id, name, 1, price, imgUrl));

                        }
                    }
                }else {
                    CartInfo.getInstance().getOrderBeanList().add(new CartInfo.OrderBean(id, name, 1, price, imgUrl));
                }
                setSnackBar(v);

            }
        });

    }

    /**
     * @param v root view for snack attach
     *          init an snackbar
     */

    public void setSnackBar(View v) {
        Snackbar snackbar = Snackbar.make(v, "Added to cart!", Toast.LENGTH_SHORT);
        snackbar.setActionTextColor(Color.parseColor("#DB5358"));
        TextView snackbrTv = snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        snackbrTv.setTextColor(Color.WHITE);
        snackbar.setAction("Ok", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });
        snackbar.show();
    }
}
