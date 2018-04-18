package com.example.fengcheng.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fengcheng.main.dataBean.OrderHistory;
import com.example.fengcheng.main.ecommerence.R;

import java.util.List;

/**
 * product recyclerView data adapter
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.mViewHolder> implements View.OnClickListener {
    private Context context;
    private List<OrderHistory.Order> dataList;
    private MainCategoryAdapter.OnItemClickListener mItemClickListener;


    public HistoryAdapter(Context context, List<OrderHistory.Order> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_order_list, parent, false);
        v.setOnClickListener(this);
        return new mViewHolder(v);
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, int position) {
        holder.idTv.setText("# " + dataList.get(position).getOrderid());
        holder.timeTv.setText(dataList.get(position).getPlacedon());
        holder.priceTv.setText("$" + dataList.get(position).getTotalprice());

        switch (dataList.get(position).getOrderstatus()){
            case "1":
                holder.statusTv.setText(R.string.order_confirm);
                break;
            case "2":
                holder.statusTv.setText(R.string.order_dispatch);
            case "3":
                holder.statusTv.setText(R.string.order_on_way);
                break;
            case "4":
                holder.statusTv.setText(R.string.order_delivered);
                break;
        }
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

    class mViewHolder extends RecyclerView.ViewHolder {
        TextView idTv, timeTv, priceTv, statusTv;

        public mViewHolder(View itemView) {
            super(itemView);
            idTv = itemView.findViewById(R.id.his_id_tv);
            timeTv = itemView.findViewById(R.id.his_time_tv);
            priceTv = itemView.findViewById(R.id.his_price_tv);
            statusTv = itemView.findViewById(R.id.his_status_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(v, getLayoutPosition());
                }
            });
        }
    }

    public void setMItemClickListener(MainCategoryAdapter.OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
}
