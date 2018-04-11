package com.example.fengcheng.main.ecommerence;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * @Package com.example.fengcheng.main.ecommerence
 * @FileName DrawerAdapter
 * @Date 4/11/18, 12:42 AM
 * @Author Created by fengchengding
 * @Description ECommerence
 */

public class DrawerAdapter extends BaseAdapter {
    String[] dataArray = {"User Name", "Profile", "Shop", "My Order", "Logout"};
    private LayoutInflater mInflater;

    public DrawerAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataArray.length;
    }

    @Override
    public Object getItem(int position) {
        return dataArray[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        //initiate viewHolder to recyclely use the UI components
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_left_drawer, parent, false);
            holder.nameTv = convertView.findViewById(R.id.name_tv);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameTv.setText(dataArray[position]);

        return convertView;
    }

    public final class ViewHolder {
        TextView nameTv;
    }
}
