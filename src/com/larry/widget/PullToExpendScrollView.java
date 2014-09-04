package com.larry.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.larry.androidpulltoscrollsample.R;

@SuppressLint("ClickableViewAccessibility")
public class PullToExpendScrollView extends ScrollView {

	private float mDeltaY = 0;
	private View mTopView;
	private View mBottomView;
	/** 弹性系数。相对与手指的移动距离的百分比 */
	private static final float ELASTIC = 0.3f;
	/** 上部控件想对于下部空间的位移百分比。*/
	private static final float RELATIVE_RATE = 0.5f;
	
	private float mInitDownY = 0.0f;

	private static final String TAG = "PullToExpendScrollView";

	public PullToExpendScrollView(Context context) {
		super(context);
		init();
	}

	public PullToExpendScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * 初始化设置
	 * 
	 */
	private void init() {
		setOverScrollMode(OVER_SCROLL_NEVER);
	}
	
    @Override
    protected void onFinishInflate() {
    	// 获取需要做相对移动的两个子控件
    	View child = getChildAt(0);
		mTopView = child.findViewById(R.id.top);
		mBottomView = child.findViewById(R.id.bottom);
    }	
	

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			// 获取初始化点击位置
			mInitDownY = ev.getY();
		}

		if (action == MotionEvent.ACTION_MOVE) {
			mDeltaY = ev.getY() - mInitDownY; // 获取相对初始值的移动距离
			if (!isScrollEnable()) { // 如果判定当前的移动为上下两个控件的移动
				// 根据定义的位移参数计算出上下两个控件的移动位置
				int topScrollDistance = -(int) (mDeltaY * ELASTIC * RELATIVE_RATE);
				int bottomScrollDistance = -(int) (mDeltaY * ELASTIC);
				// 移动上下两个控件，并且消费掉这次的MotionEvent
				mTopView.scrollTo(0, topScrollDistance);
				mBottomView.scrollTo(0, bottomScrollDistance);
				return true;
			}
		} 
		
		if (action == MotionEvent.ACTION_UP) {
			if (!isScrollEnable()) { // 如果判定当前的移动为上下两个控件的移动，当手指放开的时候做回弹操作
				mTopView.scrollTo(0, 0);
				mBottomView.scrollTo(0, 0);
				return true;
			}
		}

		return super.onTouchEvent(ev);
	}

	/**
	 * ScrollView是否可以滚动
	 * @return
	 */
	private boolean isScrollEnable() {
		// 通过检测手指的方向(从down开始到当前的手指所处的位置的方向，而不是瞬时手指移动方向，下同)和当前ScrollView的滚动状态来测试
		// 当手指方向是下拉并且ScrollView已经滚动到最顶端的时候，判断当前ScrollView的移动事件不应该被触发
		if (mDeltaY > 0 && getScrollY() <= 0) { 
			return false;
		}
		return true;
	}


}