package com.desperado.customerlib.view.infiniteviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.desperado.customerlib.R;
import com.desperado.customerlib.view.infiniteviewpager.indicator.CirclePageIndicator;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by desperado on 2016/1/12.
 * 首页简单的轮播图
 */
public class CarouseView extends LinearLayout {

    /**
     * 上下文
     */
    private Context mContext;
    /*
    * 无限录播的viewPager
    * */
    private InfiniteViewPager mViewPager;
    /*
    * 圆点指示器
    * */
    private CirclePageIndicator mIndicator;
    /*
    * 设置轮播图是否支持自动轮播
    * */
    private boolean isCarouseAutoPlay;
    /*
    * 轮播图片展示集合
    * */
    private ImageView[] mImageViews;
    /*
    * 轮播图片适配器
    * */
    private MockPagerAdapter mMPAdapter;
    /*
    * 轮播图滑动监听
    * */
    private ViewPager.OnPageChangeListener mListener;
    /*viewpager滑动的速度 默认是500*/
    private int mSlideSpeed = 400;
    /*轮播图轮播间隔的时间 默认是4秒*/
    private long mDelayTime = 4000;
    private ImageView mIvCarouseView;

    public CarouseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public CarouseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    public CarouseView(Context context) {
        super(context, null);
        this.mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.carouse_view, this);
        mViewPager = (InfiniteViewPager) findViewById(R.id.ivp_pager);
        mIvCarouseView = (ImageView) findViewById(R.id.iv_carouse_view);
        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
    }

    /**
     * 开始轮播(手动控制自动轮播与否，便于资源控制)
     */
    public void startImageCycle() {
        if (isCarouseAutoPlay)
            mViewPager.startAutoScroll();
    }

    /*
    * 设置轮播图轮动速度
    * */
    public void setCarouseCycleSpeed(int speed) {
        this.mSlideSpeed = speed;
    }

    /*
    * 设置轮播图切换动画
    * */
    public void setCarouseTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer) {
        mViewPager.setPageTransformer(reverseDrawingOrder, transformer);
    }

    /**
     * 开始轮播(手动控制自动轮播与否，便于资源控制)，并设置间隔时间
     */
    public void startImageCycle(long delayTime) {
        if (isCarouseAutoPlay)
            mViewPager.startAutoScroll(delayTime);
    }

    /**
     * 暂停轮播——用于节省资源
     */
    public void stopImageCycle() {
        if (isCarouseAutoPlay)
            mViewPager.stopAutoScroll();
    }

    /**
     * 设定轮播图是否能自动轮播
     */
    public void setIsCarouseAutoPlay(boolean isCarouseAutoPlay) {
        this.isCarouseAutoPlay = isCarouseAutoPlay;
    }

    /*
    * 设置轮播图轮播间隔的时间
    * */
    public void setCarouseDelayTime(long time) {
        this.mDelayTime = time;
    }


    /**
     * 设置轮播滑动监听
     *
     * @param listner 轮播滑动监听
     */

    public void setCarousePageChangeListner(ViewPager.OnPageChangeListener listner) {
        this.mListener = listner;
    }


    /**
     * 设置轮播滑动的速度
     */
    private void setScrollSpeed(int speed) {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            LinearInterpolator interpolator = new LinearInterpolator();
            FixedSpeedScroller myScroller = new FixedSpeedScroller(mContext, interpolator, speed);
            mScroller.set(mViewPager, myScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * 设置图片集合
    * */
    public void setImageResources(List<String> imageUrlList, ImageCycleViewListener imageCycleViewListener) {
        // 图片广告数量
        if (imageUrlList == null || imageUrlList.isEmpty()) {
            return;
        }
        final int imageCount = imageUrlList.size();
        mViewPager.setVisibility(View.VISIBLE);
          /*
        * */
        setScrollSpeed(mSlideSpeed);
          /*
        * 设置默认的viewpager切换动画
        * */
        mViewPager.setAutoScrollTime(mDelayTime);
        if (mListener != null) {
            mViewPager.setOnPageChangeListener(mListener);
        }
        if (imageCount > 0) {
            if (imageCount == 1) {
                /** 如果只有一张图片那么禁止LoopViewPager的滑动并且禁止自动轮播,不显示小圆点 */
                mIvCarouseView.setVisibility(View.VISIBLE);
                mIndicator.setVisibility(View.GONE);
                mViewPager.setVisibility(View.GONE);
                imageCycleViewListener.displayImage(imageUrlList.get(0), mIvCarouseView);
                imageCycleViewListener.onImageClick(0, mIvCarouseView);
                return;
            } else {
                mMPAdapter = new MockPagerAdapter(mContext, imageCycleViewListener, mViewPager, isCarouseAutoPlay);
                mMPAdapter.setDataList(imageUrlList);
                mViewPager.setAdapter(mMPAdapter);
                /** 大于一张图片,显示小圆点*/
                mIndicator.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.VISIBLE);
                mIvCarouseView.setVisibility(View.GONE);
                mIndicator.setViewPager(mViewPager);
            }
        }
    }

    /**
     * 图片适配器
     */
}

class MockPagerAdapter extends InfinitePagerAdapter {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final InfiniteViewPager viewpager;
    private final boolean isauto;
    private List<String> mList;

    /**
     * 广告图片点击监听器
     */
    private ImageCycleViewListener mImageCycleViewListener;


    public void setDataList(List<String> list) {
        if (list == null || list.size() == 0)
            throw new IllegalArgumentException("list can not be null or has an empty size");
        this.mList = list;
        this.notifyDataSetChanged();
    }


    public MockPagerAdapter(Context context, ImageCycleViewListener imageCycleViewListener, InfiniteViewPager viewPager, boolean isAuto) {
        mContext = context;
        this.mImageCycleViewListener = imageCycleViewListener;
        mInflater = LayoutInflater.from(mContext);
        this.viewpager = viewPager;
        this.isauto = isAuto;
    }


    @Override
    public View getView(final int position, View view, ViewGroup container) {
        final ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.item_viewpager, container, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        String item = mList.get(position);
        //holder.image.setOnClickListener(new MyClickListener(position,holder.image,mImageCycleViewListener));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageCycleViewListener.onImageClick(position, v);
            }
        });
        holder.image.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        // 开始图片滚动
                        if (isauto) {
                            viewpager.startAutoScroll();
                        }
                        break;
                    default:
                        // 停止图片滚动
                        if (isauto) {
                            viewpager.stopAutoScroll();
                        }
                        break;
                }
                return false;
            }
        });
        mImageCycleViewListener.displayImage(item, holder.image);
        return view;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    private class ViewHolder {
        public int position;
        ImageView image;

        public ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.item_image);
        }
    }
}
