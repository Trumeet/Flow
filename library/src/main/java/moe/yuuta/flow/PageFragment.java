package moe.yuuta.flow;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public abstract class PageFragment extends Fragment {
    private IFlowFragment mHostFragment;

    /**
     * Once mInfo is changed, you should call {@link IFlowFragment#notifyCurrentFlowInfoUpdated()} to publish it.
     * Note: it will be permanently change the recorded info.
     */
    protected FlowInfo mInfo;

    final void setHostFragment(@NonNull IFlowFragment hostFragment) {
        this.mHostFragment = hostFragment;
    }

    @NonNull
    protected final IFlowFragment getHostFragment() {
        return mHostFragment;
    }

    /**
     * @return true: the fragment handled this event and you do not need to call super. false: call super.
     */
    public boolean onBackPressed() {
        return false;
    }
}