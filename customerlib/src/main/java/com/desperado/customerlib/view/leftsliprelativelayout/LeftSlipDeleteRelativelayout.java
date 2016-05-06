package com.desperado.customerlib.view.leftsliprelativelayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.desperado.customerlib.view.autolayout.AutoRelativeLayout;


/**
 * 带滑动删除按钮的Relativelayout
 *
 * @author yunyee
 *
 */
@SuppressLint("InlinedApi")
public class LeftSlipDeleteRelativelayout extends AutoRelativeLayout {



	private int x;
	private View view;
	private TextView goneView;
	private static int startR, startL;

	@SuppressLint("ResourceAsColor")
	public LeftSlipDeleteRelativelayout(final Context context, AttributeSet attrs) {
		super(context, attrs);
//		RelativeLayout.LayoutParams linearLayoutParams = new RelativeLayout.LayoutParams(
//				RelativeLayout.LayoutParams.WRAP_CONTENT,
//				LayoutParams.WRAP_CONTENT);
//		linearLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
//		linearLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//		LinearLayout linearLayout=new LinearLayout(context);
//		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//		linearLayout.setLayoutParams(linearLayoutParams);
		goneView = new TextView(context);
		goneView.setText("删除");
		goneView.setTextColor(Color.RED);
		LayoutParams layoutParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		goneView.setLayoutParams(layoutParams);
//		linearLayout.addView(goneView);
		addView(goneView);

		goneView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context,"删除",Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		goneView.setTextSize(getMeasuredWidth() / 10 > 15 ? (getMeasuredWidth() / 10 > 30 ? 30
				: getMeasuredWidth() / 10)
				: 15);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
//		if (getChildCount() > 1) {
//			view = getChildAt(1);
		view=this;
		view.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						x = (int) event.getX();
						if (startL == 0 && startR == 0) {
							startR = view.getRight();
							startL = view.getLeft();
						}
					} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
						int tempRight = view.getRight()
								- (x - (int) event.getX());
						if (x - (int) event.getX() > 0) {
							if (tempRight < goneView.getLeft()) {
								view.layout(startL - goneView.getWidth(),
										view.getTop(), goneView.getLeft(),
										view.getBottom());
							} else {
								view.layout(
										(int) (view.getLeft() - (x - (int) event
												.getX())),
										view.getTop(),
										(int) (view.getRight() - (x - (int) event
												.getX())), view.getBottom());
							}

						} else if ((int) event.getX() - x > 0) {
							tempRight = view.getRight()
									+ ((int) event.getX() - x);
							if (tempRight > goneView.getRight()) {
								view.layout(startL, view.getTop(), startR,
										view.getBottom());
							} else {
								view.layout(
										(int) (view.getLeft() + ((int) event
												.getX() - x)), view.getTop(),
										(int) (view.getRight() + ((int) event
												.getX() - x)), view.getBottom());
							}
						}
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						if ((int) event.getX() < x) {
							if (view.getRight() < (goneView.getLeft() + goneView
									.getRight()) / 2) {
								view.layout(startL - goneView.getWidth(),
										view.getTop(), goneView.getLeft(),
										view.getBottom());
							} else {
								view.layout(startL, view.getTop(), startR,
										view.getBottom());
							}
						} else {
							if (view.getRight() > (goneView.getLeft() + goneView
									.getRight()) / 2) {
								view.layout(startL, view.getTop(), startR,
										view.getBottom());

							} else {
								view.layout(startL - goneView.getWidth(),
										view.getTop(), goneView.getLeft(),
										view.getBottom());
							}
						}
					}
					return true;
				}
			});
//		}
	}
}
