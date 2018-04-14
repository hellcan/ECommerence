package com.example.fengcheng.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fengcheng.main.dataBean.Products;
import com.example.fengcheng.main.ecommerence.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * product recyclerview data adapter
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.mViewHolder> implements View.OnClickListener {
    private Context context;
    private List<Products.ProductBean> dataList;
    private MainCategoryAdapter.OnItemClickListener mItemClickListener;


    public ProductAdapter(Context context, List<Products.ProductBean> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_product_list, parent, false);
        v.setOnClickListener(this);
        return new mViewHolder(v);
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, int position) {
        Picasso.with(context).load(dataList.get(position).getImageurl()).placeholder(R.drawable.bt_ic_camera).fit().into(holder.productPicIv);
        holder.productTitle.setText(dataList.get(position).getPname());
        holder.productPrice.setText("$" + dataList.get(position).getPrize());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    class mViewHolder extends RecyclerView.ViewHolder{
        ImageView productPicIv;
        TextView productTitle, productPrice;
        public mViewHolder(View itemView) {
            super(itemView);
            productPicIv = itemView.findViewById(R.id.product_pic_iv);
            productTitle = itemView.findViewById(R.id.product_title_tv);
            productPrice = itemView.findViewById(R.id.product_price_tv);


        }
    }

    public void setMItemClickListener(MainCategoryAdapter.OnItemClickListener onItemClickListener){
        this.mItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
}
