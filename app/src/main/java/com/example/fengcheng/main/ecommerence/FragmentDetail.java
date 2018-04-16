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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fengcheng.main.dataBean.CartInfo;
import com.example.fengcheng.main.dataBean.Products;
import com.example.fengcheng.main.db.DbManager;
import com.example.fengcheng.main.utils.SpUtil;
import com.example.fengcheng.main.utils.VolleyHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * fragment to display a single product detail information
 */


public class FragmentDetail extends Fragment {
    ImageView productPicIv;
    TextView titleTv, priceTv, descTv;
    Button addCartBtn;
    String id, name, price, imgUrl;
    DbManager dbManager;
    private static final String TAG = "FragmentDetail";

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

            Picasso.with(getContext()).load(imgUrl).fit().error(R.drawable.bt_ic_camera).into(productPicIv);

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

                dbManager = new DbManager(getContext());
                dbManager.openDatabase();
                int quantity = dbManager.verifyItemInCart(name, SpUtil.getUserId(getContext()));
                Log.i("数量", quantity + "");
                //means this user do not have this item in his cart
                if (quantity == -1) {
                    dbManager.addItemToCart(SpUtil.getUserId(getContext()), id, name, 1, price, imgUrl);
                } else {
                    dbManager.updateShoppingCartQty(quantity + 1, name, SpUtil.getUserId(getContext()));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dbManager != null) {
            dbManager.closeDatabase();
        }
    }
}
