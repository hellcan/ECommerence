package com.example.fengcheng.main.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.mViewHolder> {
    private Context context;
    private List<CartInfo.OrderBean> dataList;
    private DbManager dbManager;

    public CartAdapter(Context context, List<CartInfo.OrderBean> dataList) {
        this.context = context;
        this.dataList = dataList;
        dbManager = new DbManager(context);
        dbManager.openDatabase();
    }

    @Override
    public CartAdapter.mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mViewHolder mVH;

        if (viewType == -1) {//if empty

            View v = LayoutInflater.from(context).inflate(R.layout.item_empty, parent, false);

            mVH = new mViewHolder(v);


        } else {

            View v = LayoutInflater.from(context).inflate(R.layout.item_shoppingcart, parent, false);

            mVH = new mViewHolder(v);
        }

        return mVH;
    }

    @Override
    public void onBindViewHolder(CartAdapter.mViewHolder holder, int position) {
        if (dataList.size() > 0) {
            Picasso.with(context).load(dataList.get(position).getImageurl()).into(holder.itemPic);

            holder.nameTv.setText(dataList.get(position).getPname());
            holder.priceTv.setText("$" + dataList.get(position).getPrize());
            holder.quantityTv.setText(String.valueOf(dataList.get(position).getQuantity()));
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

    class mViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView itemPic;
        TextView nameTv, priceTv, quantityTv;
        ImageButton addBtn, minusBtn;

        public mViewHolder(View itemView) {
            super(itemView);
            itemPic = itemView.findViewById(R.id.order_item_pic);
            nameTv = itemView.findViewById(R.id.order_name_tv);
            priceTv = itemView.findViewById(R.id.order_price_tv);
            quantityTv = itemView.findViewById(R.id.order_quan_tv);
            addBtn = itemView.findViewById(R.id.order_add_btn);
            minusBtn = itemView.findViewById(R.id.order_minus_btn);

            if (addBtn != null) {
                addBtn.setOnClickListener(this);
                minusBtn.setOnClickListener(this);
            }

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.order_add_btn:
                    quantityTv.setText(String.valueOf(Integer.parseInt(quantityTv.getText().toString()) + 1));
                    dataList.get(getLayoutPosition()).setQuantity(Integer.parseInt(quantityTv.getText().toString()));
                    //update database
                    dbManager.updateShoppingCartQty(dataList.get(getLayoutPosition()).getQuantity(),
                            dataList.get(getLayoutPosition()).getPname(), SpUtil.getUserId(context));
                    EventBus.getDefault().post(true);
                    break;
                case R.id.order_minus_btn:
                    quantityTv.setText(String.valueOf(Integer.parseInt(quantityTv.getText().toString()) - 1));
                    dataList.get(getLayoutPosition()).setQuantity(Integer.parseInt(quantityTv.getText().toString()));
                    //update database
                    dbManager.updateShoppingCartQty(dataList.get(getLayoutPosition()).getQuantity(),
                            dataList.get(getLayoutPosition()).getPname(), SpUtil.getUserId(context));

                    if (quantityTv.getText().toString().equals("0")) {
                        AlertDialog dialog = new AlertDialog.Builder(context)
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        quantityTv.setText("1");
                                        dataList.get(getLayoutPosition()).setQuantity(1);
                                        //update database
                                        dbManager.updateShoppingCartQty(dataList.get(getLayoutPosition()).getQuantity(),
                                                dataList.get(getLayoutPosition()).getPname(), SpUtil.getUserId(context));
                                    }
                                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbManager.deleteItemCart(SpUtil.getUserId(context), dataList.get(getLayoutPosition()).getPid());
                                        dataList.remove(getLayoutPosition());
                                        notifyDataSetChanged();
                                        EventBus.getDefault().post(-1);
                                    }
                                }).setMessage(R.string.alert_delete).create();
                        dialog.setCancelable(false);
                        dialog.show();
                    }

                    EventBus.getDefault().post(true);

                    break;
                default:
                    break;
            }

        }
    }
}
