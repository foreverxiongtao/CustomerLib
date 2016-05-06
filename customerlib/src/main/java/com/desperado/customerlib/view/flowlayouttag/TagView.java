package com.desperado.customerlib.view.flowlayouttag;

import android.content.Context;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;

/**
 * 创建人：quzongyang
 * <p/>
 * 创建时间：2016/3/30 0030 18:55
 * <p/>
 * 类描述：
 * <p/>
 * 版本：V1.0
 */
public class TagView extends FrameLayout implements Checkable
{
    private boolean isChecked;
    private static final int[] CHECK_STATE = new int[]{android.R.attr.state_checked};

    public TagView(Context context)
    {
        super(context);
    }

    public View getTagView()
    {
        return getChildAt(0);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace)
    {
        int[] states = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked())
        {
            mergeDrawableStates(states, CHECK_STATE);
        }
        return states;
    }


    /**
     * Change the checked state of the view
     *
     * @param checked The new checked state
     */
    @Override
    public void setChecked(boolean checked)
    {
        if (this.isChecked != checked)
        {
            this.isChecked = checked;
            refreshDrawableState();
        }
    }

    /**
     * @return The current checked state of the view
     */
    @Override
    public boolean isChecked()
    {
        return isChecked;
    }

    /**
     * Change the checked state of the view to the inverse of its current state
     */
    @Override
    public void toggle()
    {
        setChecked(!isChecked);
    }


}

