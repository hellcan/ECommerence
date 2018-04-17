package com.example.fengcheng.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fengcheng.main.dataBean.MainCategories;
import com.example.fengcheng.main.ecommerence.R;
import com.example.fengcheng.main.utils.MyBanner;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Sub category list adapter
 */

public class SubCategoryAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private Context context;
    private List<MainCategories.CategoryBean> dataList, mainCatList;
    //item类型
    private static final int ITEM_TYPE_HEADER = 0;
    private static final int ITEM_TYPE_NORMAL = 1;

    private OnItemClickListener mItemClickListener;

//    int[] titleBg = {R.color.blue0, R.color.orange0, R.color.purple0, R.color.blue1, R.color.orange1, R.color.pink0};

    public SubCategoryAdapter(Context context, List<MainCategories.CategoryBean> dataList, List<MainCategories.CategoryBean> mainCatList) {
        this.context = context;
        this.dataList = dataList;
        this.mainCatList = mainCatList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == ITEM_TYPE_HEADER) {
            View v = LayoutInflater.from(context).inflate(R.layout.header_category_main, parent, false);
            vh = new HeaderViewHolder(v);

            return vh;

        }
        View v = LayoutInflater.from(context).inflate(R.layout.item_sub_category_main, parent, false);
        vh = new NormalViewHolder(v);
        v.setOnClickListener(this);
        return vh;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).banner.initBanner(getImageUrlData(), getContentData());

            //this is viewpager banner
            ((HeaderViewHolder) holder).banner.setImageLoader(new MyBanner.ImageLoader() {
                @Override
                public void loadImage(ImageView imageView, String url) {
                    Picasso.with(context).load(url).into(imageView);
                }
            });

            ((HeaderViewHolder) holder).banner.start();

        } else if (holder instanceof NormalViewHolder) {
            int dataPos = position - 1;

//            ((NormalViewHolder) holder).mainCategoryTv.setBackground(context.getDrawable(titleBg[dataPos % 6]));
            ((NormalViewHolder) holder).mainCategoryTv.setText(dataList.get(dataPos).getCname());

            Picasso.with(context).load(dataList.get(dataPos).getCimagerl()).fit().error(R.drawable.ic_error).into(((NormalViewHolder) holder).mainCategoryIv);

            ((NormalViewHolder) holder).itemView.setTag(position - 1);

        }

    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_HEADER;
        }

        return ITEM_TYPE_NORMAL;

    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
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
        TextView mainCategoryTv;
        ImageView mainCategoryIv;

        public NormalViewHolder(View v) {
            super(v);
            mainCategoryTv = v.findViewById(R.id.item_sub_category_tv);
            mainCategoryIv = v.findViewById(R.id.item_sub_category_iv);
        }
    }

    private List<String> getImageUrlData() {
        List<String> imageList = new ArrayList<>();
        for (MainCategories.CategoryBean categoryBean : mainCatList) {
            imageList.add(categoryBean.getCimagerl());
        }
        return imageList;
    }

    private List<String> getContentData() {
        List<String> contentList = new ArrayList<>();
        for (MainCategories.CategoryBean categoryBean : mainCatList) {
            contentList.add("");
        }
        return contentList;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams slp = (StaggeredGridLayoutManager.LayoutParams) lp;
            slp.setFullSpan(holder.getLayoutPosition() == 0);
        }
    }

    public void setMItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
}
