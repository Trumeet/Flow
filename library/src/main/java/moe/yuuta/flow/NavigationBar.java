package moe.yuuta.flow;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class NavigationBar {
    public enum ButtonPosition {
        LEFT,
        RIGHT
    }

    private ViewGroup mRoot;
    private Button mLeftButton;
    private Button mRightButton;

    NavigationBar() {}

    NavigationBar(@NonNull ViewGroup navBarRoot) {
        attach(navBarRoot);
    }

    /**
     * The config will be reset after re-attaching.
     */
    void attach(@NonNull ViewGroup navBarRoot) {
        mRoot = navBarRoot;
        mLeftButton = mRoot.findViewById(R.id.flow_nav_left_button);
        mRightButton = mRoot.findViewById(R.id.flow_nav_right_button);
    }

    @NonNull
    Button getButton(@NonNull ButtonPosition position) {
        switch (position) {
            case LEFT:
                return mLeftButton;
            case RIGHT:
                return mRightButton;
            default:
                throw new IllegalArgumentException("Unexpected position");
        }
    }

    private void setListener(@NonNull ButtonPosition position, @Nullable View.OnClickListener listener) {
        switch (position) {
            case LEFT:
                mLeftButton.setOnClickListener(listener);
                break;
            case RIGHT:
                mRightButton.setOnClickListener(listener);
                break;
        }
    }

    void applyInfo(@NonNull NavigationBarConfig config) {
        mLeftButton.setText(config.getLeftButtonText());
        mRightButton.setText(config.getRightButtonText());
        setNavigationBarVisibility(config.getNavBarVisibility());
        setButtonVisibility(ButtonPosition.LEFT, config.getLeftButtonVisibility());
        setButtonVisibility(ButtonPosition.RIGHT, config.getRightButtonVisibility());
        setListener(ButtonPosition.LEFT, config.getLeftListener());
        setListener(ButtonPosition.RIGHT, config.getRightListener());
    }

    private void setNavigationBarVisibility(@View.Visibility int visibility) {
        mRoot.setVisibility(visibility);
    }

    private void setButtonVisibility(@NonNull ButtonPosition position, @View.Visibility int visibility) {
        switch (position) {
            case LEFT:
                mLeftButton.setVisibility(visibility);
                break;
            case RIGHT:
                mRightButton.setVisibility(visibility);
                break;
        }
    }
}
