package com.desperado.customerlib.view.infiniteviewpager;

import android.view.View;
import android.view.ViewGroup;

/**
 * 无限轮播适配器，外部类只要继承此类然后配合infiniteViewPager便可以实现自动轮播
 */
public abstract class InfinitePagerAdapter extends RecyclingPagerAdapter {


    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        return null;
    }

    @Override
    /**
     * Note: use getItemCount instead*/
    public final int getCount() {
        return getItemCount() * InfiniteViewPager.FakePositionHelper.MULTIPLIER;
    }

    @Deprecated

    protected View getViewInternal(int position, View convertView, ViewGroup container) {
        if(getItemCount()==0)
            return null;
        return getView(position % getItemCount(), convertView, container);
    }

    public abstract int getItemCount();

}
