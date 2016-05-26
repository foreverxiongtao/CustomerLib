package com.desperado.customerlib.view.verticalnoticationscrollbar;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.desperado.customerlib.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.List;

/*
 *
 *
 * 版 权 :@Copyright 北京优多鲜道科技有限公司版权所有
 *
 * 作 者 :desperado
 *
 * 版 本 :1.0
 *
 * 创建日期 :2016/5/17  14:30
 *
 * 描 述 :垂直滚动消息提示条
 *
 * 修订日期 :
 */
public class VerticalNoticationScrollBar extends RelativeLayout {
    private Context mContext;
    /**
     * 垂直滚动广告容器
     **/
    private SwitchViewGroup mSwitchViewGroup;
    /***
     * 父容器
     **/
    private LinearLayout ll_vertical_notication_scroll_bar_container;
    /***
     * 关闭按钮
     **/
    private ImageView iv_layout_vertical_notication_scroll_bar_close;
    /**
     * 自动隐藏
     */
    private boolean mAutoHidden;
    /**
     * 延迟时间
     */
    private long mDelayTime;
    private Handler mMhandler;

    private static long DELAYTIME = 10000;

    private long count;
    private Runnable mMTask;
    private List<String> mDatas;

    /**
     * 是否已经开启轮训
     **/
    private boolean isRunning = false;
    private static ArgbEvaluator tempArgbEvaluator;

    public VerticalNoticationScrollBar(Context context) {
        this(context, null);
        this.mContext = context;
    }

    public VerticalNoticationScrollBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
    }

    public VerticalNoticationScrollBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    /**
     * 控件初始化
     **/
    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.layout_vertical_notication_scroll_bar, this);
        mSwitchViewGroup = (SwitchViewGroup) findViewById(R.id.switchViewGroup);
        ll_vertical_notication_scroll_bar_container = (LinearLayout) findViewById(R.id.ll_vertical_notication_scroll_bar_container);
        iv_layout_vertical_notication_scroll_bar_close = (ImageView) findViewById(R.id.iv_layout_vertical_notication_scroll_bar_close);
        iv_layout_vertical_notication_scroll_bar_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeScrollBar();
            }
        });
        mDatas = new ArrayList<>();
        tempArgbEvaluator = new ArgbEvaluator();
        mMhandler = new Handler();
    }


    private class Task implements Runnable {
        @Override
        public void run() {
            count++;
            /****/
            if (count >= mDatas.size()) {
                mMhandler.removeCallbacks(this);
                count = 0;//重置计数器
                isRunning = false;//结束正在运行的标示位
                mDatas = null;
                closeScrollBar();
            } else {
                mMhandler.postDelayed(this, DELAYTIME);
            }
        }
    }

    /**
     * 添加数据
     *
     * @param datas
     */
    public void addData(List<String> datas) {
        if (datas != null && !datas.isEmpty()) {
            this.mDatas = datas;
            mSwitchViewGroup.addData(datas);
        }
    }

    /**
     * 判断广告条数据
     */
    public List<String> getDatas() {
        return mDatas;
    }

    /***
     * 开始滑动
     **/
    public void startScroll() {
        if (!isRunning) {
            isRunning = true;
            /***重置广告条状态**/
            if (ll_vertical_notication_scroll_bar_container != null && ll_vertical_notication_scroll_bar_container.getVisibility() != View.VISIBLE) {
                // mSwitchViewGroup.clearData();
                ll_vertical_notication_scroll_bar_container.clearAnimation();
                ll_vertical_notication_scroll_bar_container.setVisibility(View.VISIBLE);
                ll_vertical_notication_scroll_bar_container.setBackgroundResource(R.color.price_text_black);
                ll_vertical_notication_scroll_bar_container.setAlpha(1.0f);
            }
            startTask();
            mSwitchViewGroup.startScroll();
        }
    }

    /**
     * 停止滑动
     **/
    public void stopScroll() {
        //mSwitchViewGroup.stopScroll();
        mSwitchViewGroup.clearData();
    }

    /**
     * 设置滚动条背景颜色
     **/
    public void setScrollBarBackgroudColor(int _color) {
        ll_vertical_notication_scroll_bar_container.setBackgroundResource(_color);
    }


    public void reset() {
        ll_vertical_notication_scroll_bar_container.setVisibility(View.GONE);
        mSwitchViewGroup.stopScroll();
        mSwitchViewGroup.clearData();
        mMhandler.removeCallbacks(mMTask);
        count = 0;//重置计数器
        isRunning = false;//结束正在运行的标示位
        mDatas = null;
    }

    /***
     * 关闭滚动条
     **/
    public void closeScrollBar() {
        mSwitchViewGroup.stopScroll();
        startAlphaAnimation(ll_vertical_notication_scroll_bar_container, mSwitchViewGroup, 3000, View.GONE);
        // stopScroll();
    }


    // 设置渐变的动画
    public static void startAlphaAnimation(final View v, final SwitchViewGroup tempSwitchGroup, long duration, final int visibility) {
        final ObjectAnimator tempObjectAnimator = (visibility == View.VISIBLE) ? ObjectAnimator.ofFloat(v, "alpha", 0f, 1f) : ObjectAnimator.ofFloat(v, "alpha", 1f, 0f);
        tempObjectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                tempSwitchGroup.clearData();
                v.setVisibility(visibility);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        tempObjectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int evaluate = (Integer) tempArgbEvaluator.evaluate(animation.getAnimatedFraction(),
                        Color.rgb(35, 42, 46), Color.argb(255,00, 00, 00));
                v.setBackgroundColor(evaluate);
            }
        });
        tempObjectAnimator.setDuration(duration);
        tempObjectAnimator.start();
    }


    /**
     * 设置是否显示关闭按钮
     *
     * @param _visiablity
     */
    public void setScrollBarCloseVisiablity(boolean _visiablity) {
        if (_visiablity) {
            iv_layout_vertical_notication_scroll_bar_close.setVisibility(View.VISIBLE);
        } else {
            iv_layout_vertical_notication_scroll_bar_close.setVisibility(View.GONE);
        }
    }


    private void startTask() {
        /**如果用户开启了自动关闭广告条，并设置了延迟时间，就开启任务**/
        if (mAutoHidden) {
            mMTask = new Task();
            mMhandler.postDelayed(mMTask, DELAYTIME);
        }
    }

    /***
     * 是否开启自动隐藏
     *
     * @param _autoHidden
     */
    public void setAutoHidden(boolean _autoHidden) {
        this.mAutoHidden = _autoHidden;
    }

    /**
     * 设置广告条延迟消失的时长
     *
     * @param _delayTime
     */
    public void setHiddenDelayTime(long _delayTime) {
        this.mDelayTime = _delayTime;
    }
}
