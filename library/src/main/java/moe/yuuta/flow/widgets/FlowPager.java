package moe.yuuta.flow.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class FlowPager extends ViewPager {
    public FlowPager(@NonNull Context context) {
        super(context);
    }

    public FlowPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    // Thanks to https://stackoverflow.com/a/20784791/6792243
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if(h > height) height = h;
        }

        if (height != 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
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
