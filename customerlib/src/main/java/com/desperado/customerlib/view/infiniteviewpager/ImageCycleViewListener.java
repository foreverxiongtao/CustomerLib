package com.desperado.customerlib.view.infiniteviewpager;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by desperado on 2016/2/22.
 * 轮播监听的回调事件
 */
public interface ImageCycleViewListener {
    /**
     * 轮播控件的监听事件
     *
     * @author minking
     */

    /**
     * 加载图片资源
     *
     * @param imageURL
     * @param imageView
     */
    public void displayImage(String imageURL, ImageView imageView);

    /**
     * 单击图片事件
     *
     * @param position
     * @param imageView
     */
    public void onImageClick(int position, View imageView);
}
