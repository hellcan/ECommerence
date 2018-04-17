package com.example.fengcheng.main.utils;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fengcheng.main.ecommerence.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package com.example.fengcheng.main.ecommerence
 * @FileName MyBanner
 * @Date 4/11/18, 2:41 PM
 * @Author Created by fengchengding
 * @Description ECommerence
 */

public class MyBanner extends FrameLayout implements ViewPager.OnPageChangeListener {

    //image url list
    private List<String> imageUrlList;

    //content list
    private List<String> contentList;

    //banner container
    private ViewPager bannerHolder;

    //
    private TextView bannerContent;

    //for the bottom dot
    private LinearLayout dotLayout;

    //ImageView list
    private List<ImageView> imageViewList;

    //dot previous position
    private int lastPosition;

    //dot size
    private final static float POINT_DEFAULT_SIZE = 10f;

    //time interval for banner switch
    private final static int BANNER_SWITCH_DELAY_MILLIS = 3000;

    //image loader
    private ImageLoader imageLoader;

    //touch flag
    private boolean isTouch = false;
    private PollingHandler mHandler = new PollingHandler();

    //banner click listener
    private OnItemClickListener listener;

    //if polling flag
    private boolean pollingEnable = false;

    public MyBanner(@NonNull Context context) {
        super(context);
        initView();
    }

    public MyBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private static class PollingHandler extends Handler {

    }

    private void initView() {
        // child = 0 means no layout load yet
        if (getChildCount() == 0) {
            View.inflate(getContext(), R.layout.item_header_banner, this);
            bannerHolder = findViewById(R.id.vp_banner);
            bannerContent = findViewById(R.id.tv_banner_content);
            dotLayout = findViewById(R.id.ll_banner_point);
        }
    }

    public void initBanner(List<String> imageUrlList, List<String> contentList) {
        this.imageUrlList = imageUrlList;
        this.contentList = contentList;
        if (imageUrlList == null || contentList == null || imageUrlList.size() == 0 || contentList
                .size() == 0) {
            throw new IllegalArgumentException("null");
        }

        if (imageUrlList.size() != contentList.size()) {
            throw new IllegalArgumentException("no match");
        }

        initView();
        initData();
    }

    // this is data holder
    private void initData() {
        imageViewList = new ArrayList<>();
        View dotView;

        int bannerSize = imageUrlList.size();
        for (int i = 0; i < bannerSize; i++) {
            //init ImageView
            ImageView imageView = new ImageView(getContext());
            //specified how image fill the view
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //set up listener
            if (imageLoader != null) {
                imageLoader.loadImage(imageView, imageUrlList.get(i));
            }

            imageViewList.add(imageView);

            //dot layout
            dotView = new View(getContext());
            //background
            dotView.setBackgroundResource(R.drawable.selector_dot);
            //dot size
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DensityUtil
                    .dp2px(getContext(), POINT_DEFAULT_SIZE), DensityUtil.dp2px(getContext(),
                    POINT_DEFAULT_SIZE));

            if (i != 0) {
                layoutParams.leftMargin = DensityUtil.dp2px(getContext(), POINT_DEFAULT_SIZE / 2);
                //disable dotView
                dotView.setEnabled(false);
            }

            dotView.setLayoutParams(layoutParams);
            //add to dot layout
            if (dotLayout.getChildCount() > imageViewList.size()){
                Log.i("test bug", "addView");
                dotLayout.removeAllViews();
            }
            dotLayout.addView(dotView);
        }

        //viewpager adapter
        BannerAdapter bannerAdapter = new BannerAdapter();
        bannerHolder.setAdapter(bannerAdapter);

        //add page change listener
        bannerHolder.addOnPageChangeListener(this);

        //set up viewpager start position
        int remainder = (Integer.MAX_VALUE / 2) % imageUrlList.size();
        bannerHolder.setCurrentItem(Integer.MAX_VALUE / 2 - remainder);
        //set up banner content, default is 0
        bannerContent.setText(contentList.get(0));
        dotLayout.getChildAt(0).setEnabled(true);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    //this method will invoked after viewPager switched to a new screen
    @Override
    public void onPageSelected(int position) {
        int newPos = position % imageUrlList.size();

        //replace dot background, if dot banner changed
        dotLayout.getChildAt(newPos).setEnabled(true);
        dotLayout.getChildAt(lastPosition).setEnabled(false);

        //replace txt content
        bannerContent.setText(contentList.get(newPos));
        //save last position
        lastPosition = newPos;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                isTouch = false;
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    //delay switch banner
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //if it is not touched
            if (!isTouch) {
                //next banner
                bannerHolder.setCurrentItem(bannerHolder.getCurrentItem() + 1);
            }

            if (pollingEnable) {
                mHandler.postDelayed(runnable, BANNER_SWITCH_DELAY_MILLIS);
            }
        }
    };

    private class BannerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            final int newPos = position % imageUrlList.size();
            ImageView imageView = imageViewList.get(newPos);

            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //call back
                    if (listener != null) {
                        listener.onItemClick(newPos, contentList.get(position));
                    }
                }
            });

//            if (container.getChildCount() > 0){
//                container.removeAllViews();
//            }
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    //banner click listener
    public void setOnItemClickListener(@NonNull OnItemClickListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("item listener can not be null!");
        }
        this.listener = listener;
    }

    //set up image loader
    public void setImageLoader(@NonNull ImageLoader imageLoader) {
        if (imageLoader == null) {
            throw new IllegalArgumentException("图片加载器不能为空");
        }
        this.imageLoader = imageLoader;
        if (imageViewList != null) {
            int imageSize = imageViewList.size();
            for (int i = 0; i < imageSize; i++) {
                imageLoader.loadImage(imageViewList.get(i), imageUrlList.get(i));
            }
        }
    }

    //set up is polling
    public boolean isPollingEnable() {
        return pollingEnable;
    }

    //start polling
    public void start() {
        // 之前已经开启轮播  无需再开启
        if (pollingEnable) {
            return;
        }
        pollingEnable = true;
        mHandler.postDelayed(runnable, BANNER_SWITCH_DELAY_MILLIS);
    }

    //stop polling
    public void stop() {
        pollingEnable = false;
        isTouch = false;
        //remove handler
        mHandler.removeCallbacksAndMessages(null);
    }

    //reset polling data
    public void resetData(@NonNull List<String> imageUrlList, @NonNull List<String> contentList) {
        this.imageUrlList = imageUrlList;
        this.contentList = contentList;
        if (imageUrlList == null || contentList == null || imageUrlList.size() == 0 || contentList
                .size() == 0) {
            throw new IllegalArgumentException("not null");
        }

        if (imageUrlList.size() != contentList.size()) {
            throw new IllegalArgumentException("need match");
        }

        //判断是否之前在轮播
        if (pollingEnable) {
            stop();
        }
        start();
    }

    /**
     * interface that let user to customize image load
     */
    public interface ImageLoader {
        void loadImage(ImageView imageView, String url);
    }


    public interface OnItemClickListener {

        void onItemClick(int position, String title);
    }


}
