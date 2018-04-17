package com.example.fengcheng.main.ecommerence;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fengcheng.main.db.DbManager;
import com.example.fengcheng.main.utils.SpUtil;
import com.squareup.picasso.Picasso;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;


/**
 * fragment to display a single product detail information
 */


public class FragmentDetail extends Fragment {
    ImageView productPicIv;
    TextView titleTv, priceTv, descTv, rateTv;
    Button addCartBtn;
    String id, name, price, imgUrl;
    MaterialRatingBar ratingBar;
    DbManager dbManager;
    ImageButton likeBtn;
    String unwish = "unwish", wished = "wish";
    private static final String TAG = "FragmentDetail";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_product_detail, container, false);

        //open data base
        dbManager = new DbManager(getContext());
        dbManager.openDatabase();

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
        ratingBar = v.findViewById(R.id.rating_rb);
        rateTv = v.findViewById(R.id.rating_tv);
        likeBtn = v.findViewById(R.id.like_btn);

        //grab data from intent
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

        //set up rating bar score
        ratingBar.setRating(1.5f);
        rateTv.setText(String.valueOf(1.5));

        //set up wish icon
        if (dbManager.verifyItemWish(id, SpUtil.getUserId(getContext())) == -1) {
            likeBtn.setImageResource(R.drawable.ic_like_un);
            likeBtn.setTag(R.id.tag_iswish, unwish);
        } else {
            likeBtn.setImageResource(R.drawable.ic_like);
            likeBtn.setTag(R.id.tag_iswish, wished);
        }
    }

    /**
     * handle add to cart Button event here
     */

    private void initClickListener() {
        addCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //order quantity
                int quantity = dbManager.verifyItemInCart(name, SpUtil.getUserId(getContext()));
                //means this user do not have this item in his cart
                if (quantity == -1) {
                    dbManager.addItemToCart(SpUtil.getUserId(getContext()), id, name, 1, price, imgUrl);
                } else {
                    dbManager.updateShoppingCartQty(quantity + 1, name, SpUtil.getUserId(getContext()));
                }
                setSnackBar(v);
            }
        });

        //rating bar listener
        ratingBar.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                rateTv.setText(String.valueOf(rating));
            }
        });

        //wish listener
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (likeBtn.getTag(R.id.tag_iswish).equals(unwish)){
                    likeBtn.setImageResource(R.drawable.ic_like);
                    dbManager.addItemToWish(SpUtil.getUserId(getContext()), id, name, 1, price, imgUrl);
                    likeBtn.setTag(R.id.tag_iswish, wished);
                }else if (likeBtn.getTag(R.id.tag_iswish).equals(wished)){
                    likeBtn.setImageResource(R.drawable.ic_like_un);
                    dbManager.deleteItemWish(SpUtil.getUserId(getContext()), id);
                    likeBtn.setTag(R.id.tag_iswish, unwish);
                }
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
