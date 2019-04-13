package moe.yuuta.flow;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public abstract class PageFragment extends Fragment {
    /**
     * Once mInfo is changed, you should call {@link IFlowFragment#notifyCurrentFlowInfoUpdated()} to publish it.
     * Note: it will be permanently change the recorded info.
     */
    protected FlowInfo mInfo;

    @NonNull
    protected final IFlowFragment getHostFragment() {
        final Fragment parent = getParentFragment();
        if (!(parent instanceof IFlowFragment)) {
            throw new IllegalStateException("This fragment is not attached to a valid flow host");
        }
        Log.d("Page", "get");
        return (IFlowFragment) parent;
    }

    /**
     * @return true: the fragment handled this event and you do not need to call super. false: call super.
     */
    public boolean onBackPressed() {
        return false;
    }
}