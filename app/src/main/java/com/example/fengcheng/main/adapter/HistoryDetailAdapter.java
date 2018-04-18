package com.example.fengcheng.main.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.example.fengcheng.main.ecommerence.R;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Package com.example.fengcheng.main.adapter
 * @FileName HistoryDetailAdapter
 * @Date 4/17/18, 7:28 PM
 * @Author Created by fengchengding
 * @Description ECommerence
 */

public class HistoryDetailAdapter extends RecyclerView.Adapter<HistoryDetailAdapter.TimeLineViewHolder> {
    private Context context;
    private String statusId;
    private List<String> mDataList;
    private String[] statusCode = {"Order Confirm", "Order Dispatch", "Order On the Way", "Order Delivered"};


    public HistoryDetailAdapter(Context context, String statusId) {
        this.context = context;
        this.statusId = statusId;
        //init step list
        mDataList = new ArrayList<>();
        mDataList.addAll(Arrays.asList(statusCode).subList(0, 4));
    }

    @Override
    public HistoryDetailAdapter.TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_timeline, parent, false);
        return new TimeLineViewHolder(v, viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {

        if (Integer.valueOf(statusId) - 1 == position) {
            holder.titleTv.setText(mDataList.get(position));
            holder.titleTv.setTextColor(context.getResources().getColor(R.color.bt_error_red));

            holder.mTimelineView.setMarker(context.getDrawable(R.drawable.ic_dot_track));
        } else {
            holder.titleTv.setText(mDataList.get(position));
            holder.mTimelineView.setMarker(context.getDrawable(R.drawable.ic_dot_empty));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    class TimeLineViewHolder extends RecyclerView.ViewHolder {
        TimelineView mTimelineView;
        TextView titleTv;

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            mTimelineView = itemView.findViewById(R.id.time_marker);
            titleTv = itemView.findViewById(R.id.text_timeline_title);
            mTimelineView.initLine(viewType);
        }
    }
}
