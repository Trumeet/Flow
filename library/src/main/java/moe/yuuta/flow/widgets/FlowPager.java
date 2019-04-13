package moe.yuuta.flow.widgets;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class FlowPager extends ViewPager {
    public FlowPager(@NonNull Context context) {
        super(context);
    }

    public FlowPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    // Thanks to https://stackoverflow.com/a/32488566/6792243
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(null != getAdapter()) {
            int height = 0;
            View child = ((FragmentStatePagerAdapter) getAdapter()).getItem(getCurrentItem()).getView();
            if (child != null) {
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                height = child.getMeasuredHeight();
                // TODO: Support api-14 and api-15?
                if (Build.VERSION.SDK_INT >= 16 && height < getMinimumHeight()) {
                    height = getMinimumHeight();
                }
            }

            int newHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            if (getLayoutParams().height != 0 && heightMeasureSpec != newHeight) {
                getLayoutParams().height = height;

            } else {
                heightMeasureSpec = newHeight;
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    // Thanks to https://stackoverflow.com/a/13437997/6792243
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    // Thanks to https://stackoverflow.com/a/13437997/6792243
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

}
