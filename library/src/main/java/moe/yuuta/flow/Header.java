package moe.yuuta.flow;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

class Header {
    public enum WhichView {
        TITLE,
        SUBTITLE
    }

    private ViewGroup mRoot;
    private TextView mTitle;
    private TextView mSubtitle;
    private ProgressBar mProgressBar;

    Header() {}

    Header(@NonNull ViewGroup root) {
        attach(root);
    }

    /**
     * The config will be reset after re-attaching.
     */
    void attach(@NonNull ViewGroup root) {
        mRoot = root;
        mTitle = mRoot.findViewById(R.id.flow_header_title);
        mSubtitle = mRoot.findViewById(R.id.flow_header_subtitle);
        mProgressBar = mRoot.findViewById(R.id.flow_header_progressbar);
    }

    @NonNull
    private TextView getText(@NonNull WhichView position) {
        switch (position) {
            case TITLE:
                return mTitle;
            case SUBTITLE:
                return mSubtitle;
            default:
                throw new IllegalArgumentException("Unexpected position");
        }
    }

    void applyInfo(@NonNull HeaderConfig config) {
        mTitle.setText(config.getTitleText());
        mSubtitle.setText(config.getSubtitleText());
        mProgressBar.setVisibility(config.getShowProgressBar() ? View.VISIBLE : View.GONE);
    }
}
