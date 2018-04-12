package com.example.fengcheng.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fengcheng.main.utils.MyBanner;
import com.example.fengcheng.main.ecommerence.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package com.example.fengcheng.main.adapter
 * @FileName MainCategoryAdapter
 * @Date 4/11/18, 9:55 PM
 * @Author Created by fengchengding
 * @Description ECommerence
 */

public class MainCategoryAdapter extends RecyclerView.Adapter {
    private Context context;
    private SparseIntArray dataArray;
    //item类型
    private static final int ITEM_TYPE_HEADER = 0;
    private static final int ITEM_TYPE_NORMAL = 1;

    public MainCategoryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == ITEM_TYPE_HEADER) {
            View v = LayoutInflater.from(context).inflate(R.layout.header_category_main, parent, false);
            vh = new HeaderViewHolder(v);

            return vh;

        }
        View v = LayoutInflater.from(context).inflate(R.layout.item_category_main, parent, false);
        vh = new NormalViewHolder(v);

        return vh;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).banner.initBanner(getImageUrlData(), getContentData());

            //设置图片加载器
            ((HeaderViewHolder) holder).banner.setImageLoader(new MyBanner.ImageLoader() {
                @Override
                public void loadImage(ImageView imageView, String url) {
                    Picasso.with(context).load(url).into(imageView);
                }
            });

            ((HeaderViewHolder) holder).banner.start();

        } else if (holder instanceof NormalViewHolder) {
            ((NormalViewHolder) holder).tv.setText(position + "12312312312312sdlkns");

        }

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_HEADER;
        }

        return ITEM_TYPE_NORMAL;

    }

    //header vh
    class HeaderViewHolder extends RecyclerView.ViewHolder {
        MyBanner banner;

        public HeaderViewHolder(View v) {
            super(v);
            banner = v.findViewById(R.id.banner);
        }
    }

    //item vh
    class NormalViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public NormalViewHolder(View v) {
            super(v);
            tv = v.findViewById(R.id.tv);
        }
    }

    private List<String> getImageUrlData() {
        List<String> imageList = new ArrayList<>();
        imageList.add("https://pic3.zhimg.com/v2-e885c8acf8ca274cda11dd8ce7760d26.jpg");
        imageList.add("https://pic3.zhimg.com/v2-ecfc08ebca9f42e29144d09050fb619e.jpg");
        imageList.add("https://pic1.zhimg.com/v2-0d3e0c7d778083003e6f8029cf7cf570.jpg");
        imageList.add("https://pic1.zhimg.com/v2-5d0f06845b6cdd5e32aed0a960aede98.jpg");
        imageList.add("https://pic2.zhimg.com/v2-4d873d82642d347aa0e709b2e2f5be81.jpg");
        return imageList;
    }

    private List<String> getContentData() {
        List<String> contentList = new ArrayList<>();
        contentList.add("");
        contentList.add("");
        contentList.add("");
        contentList.add("");
        contentList.add("");
        return contentList;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams){
            StaggeredGridLayoutManager.LayoutParams slp = (StaggeredGridLayoutManager.LayoutParams) lp;
            slp.setFullSpan(holder.getLayoutPosition() == 0);
        }
    }
}
