package com.example.fengcheng.main.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fengcheng.main.dataBean.CartInfo;
import com.example.fengcheng.main.db.DbManager;
import com.example.fengcheng.main.ecommerence.R;
import com.example.fengcheng.main.utils.SpUtil;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * @Package com.example.fengcheng.main.adapter
 * @FileName CartAdapter
 * @Date 4/14/18, 12:51 AM
 * @Author Created by fengchengding
 * @Description ECommerence
 */

public class WishAdapter extends RecyclerView.Adapter<WishAdapter.mViewHolder> {
    Context context;
    List<CartInfo.OrderBean> dataList;
    DbManager dbManager;

    public WishAdapter(Context context, List<CartInfo.OrderBean> dataList, DbManager db) {
        this.context = context;
        this.dataList = dataList;
        this.dbManager = db;
    }

    @Override
    public WishAdapter.mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mViewHolder mVH;

        if (viewType == -1) {//if empty

            View v = LayoutInflater.from(context).inflate(R.layout.item_empty, parent, false);

            mVH = new mViewHolder(v);


        } else {

            View v = LayoutInflater.from(context).inflate(R.layout.item_wishcart, parent, false);

            mVH = new mViewHolder(v);
        }

        return mVH;
    }

    @Override
    public void onBindViewHolder(WishAdapter.mViewHolder holder, int position) {
        if (dataList.size() > 0) {
            Picasso.with(context).load(dataList.get(position).getImageurl()).into(holder.itemPic);

            holder.nameTv.setText(dataList.get(position).getPname());
            holder.priceTv.setText("$" + dataList.get(position).getPrize());
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() > 0 ? dataList.size() : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList.size() <= 0) {

            return -1;

        }

        return super.getItemViewType(position);
    }

    class mViewHolder extends RecyclerView.ViewHolder {
        ImageView itemPic;
        TextView nameTv, priceTv;
        ImageButton removeBtn;

        public mViewHolder(View itemView) {
            super(itemView);
            itemPic = itemView.findViewById(R.id.wish_item_pic);
            nameTv = itemView.findViewById(R.id.wish_name_tv);
            priceTv = itemView.findViewById(R.id.wish_price_tv);
            removeBtn = itemView.findViewById(R.id.remove_btn);

            if (dataList.size() > 0){
            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbManager.openDatabase();
                    dbManager.deleteItemWish(SpUtil.getUserId(context), dataList.get(getLayoutPosition()).getPid());
                    dataList.remove(getLayoutPosition());
                    notifyDataSetChanged();

                }
            });
        }}

    }
}
