package moe.yuuta.flow;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * A bridge which is exposed to {@link PageFragment} for controlling the {@link FlowFragment}
 */
public interface IFlowFragment {
    void notifyCurrentFlowInfoUpdated();
    void nextFlow();
    void previousFlow();
    int getFlowCount();
    void switchToFlow(int index);
    @NonNull View.OnClickListener getGeneralFlowNavListener();
}
