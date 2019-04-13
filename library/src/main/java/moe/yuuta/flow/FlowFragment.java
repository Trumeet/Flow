package moe.yuuta.flow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * The Host fragment. It is marked as final because it was set to remain instance.
 */
public final class FlowFragment extends Fragment implements IFlowFragment, View.OnClickListener {
    private static final String TAG = "FlowFragment";

    private List<PageFragment> mPages = new ArrayList<>(0);
    // true: after the view settles, update the UI immediately.
    private volatile boolean mUIUpdateScheduled;

    private static final String ARG_CURRENT = FlowFragment.class.getName() + ".ARG_CURRENT";
    private static final String ARG_UI_UPDATE_SCHEDULED = FlowFragment.class.getName() + ".ARG_UI_UPDATE_SCHEDULED";

    private Header mHeader;
    private ViewPager mPager;
    private NavigationBar mNav;

    /**
     * Should only be called once.
     */
    public void setPages(@NonNull List<PageFragment> pages) {
        if (mPages != null && mPages.size() > 0) {
            throw new IllegalStateException("This method should only be called once. Current size: " + mPages.size());
        }
        mPages = pages;
        // Because this method can be called before setting up the layout, so we need to schedule it until the layout is set up.
        // notifyCurrentFlowInfoUpdated();
    }

    /**
     * Update the WHOLE UI.
     */
    private void updateUI() {
        final int currentIndex = mPager.getCurrentItem();
        final PageFragment currentFragment = mPages.get(currentIndex);
        if (currentFragment.mInfo == null) {
            throw new NullPointerException("Info is null");
        }
        if (currentFragment.mInfo.getNavigationBarConfig() != null) {
            mNav.applyInfo(currentFragment.mInfo.getNavigationBarConfig());
        } else {
            mNav.applyInfo(new NavigationBarConfig(getString(R.string.flow_nav_bar_next),
                    getString(R.string.flow_nav_bar_previous),
                    currentIndex == 0 ? View.GONE : View.VISIBLE,
                    currentIndex >= (mPages.size() - 1) ? View.GONE : View.VISIBLE,
                    View.VISIBLE,
                    this,
                    this));
        }
        mHeader.applyInfo(currentFragment.mInfo.getHeaderConfig());
    }

    @Override
    public void onClick(View v) {
        final int currentIndex = mPager.getCurrentItem();
        if (v.getId() == R.id.flow_nav_left_button) {
            if (currentIndex > 0) previousFlow();
        } else if (v.getId() == R.id.flow_nav_right_button) {
            if (currentIndex < (mPages.size() - 1)) nextFlow();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getView() != null) return getView();
        final View view = inflater.inflate(R.layout.fragment_flow, container, false);
        mHeader = new Header(view.<ConstraintLayout>findViewById(R.id.flow_host_header));
        mNav = new NavigationBar(view.<ConstraintLayout>findViewById(R.id.flow_host_nav));
        mPager = view.findViewById(R.id.flow_host_pager);
        mPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mPages.get(position);
            }

            @Override
            public int getCount() {
                return mPages.size();
            }
        });
        mPager.addOnPageChangeListener(mPageListener);
        return view;
    }

    @Override
    public void onDestroyView() {
        mPager.removeOnPageChangeListener(mPageListener);
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");
        if (savedInstanceState != null) {
            Log.d(TAG, "onViewCreated - restore " + savedInstanceState.getInt(ARG_CURRENT, -1));
            mPager.setCurrentItem(savedInstanceState.getInt(ARG_CURRENT, 0));
            // TODO: updateUI() should be preformed here, but I did not find a appropriate way to retrieve FlowInfo. The temporarily workaround is to notify update when the view re-creates.
        }
        if (mUIUpdateScheduled) {
            updateUI();
            mUIUpdateScheduled = false;
        }
    }

    @Override
    public void notifyCurrentFlowInfoUpdated() {
        if (getView() != null) {
            mUIUpdateScheduled = false;
            updateUI();
        } else {
            mUIUpdateScheduled = true;
        }
    }

    @Override
    public void nextFlow() {
        switchToFlow(mPager.getCurrentItem() + 1);
    }

    @Override
    public void previousFlow() {
        switchToFlow(mPager.getCurrentItem() - 1);
    }

    @Override
    public int getFlowCount() {
        return mPages.size();
    }

    @Override
    public void switchToFlow(int index) {
        mPager.setCurrentItem(index, true);
    }

    private ViewPager.OnPageChangeListener mPageListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            notifyCurrentFlowInfoUpdated();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * @return true: the fragment handled this event and you do not need to call super. false: call super.
     */
    public boolean onBackPressed() {
        if (getView() == null) return false;
        if (mPages.size() <= 0) return false;
        final PageFragment pf = mPages.get(mPager.getCurrentItem());
        if (pf != null) {
            if (pf.onBackPressed()) return true;
        }
        // Default handling
        if (mPager.getCurrentItem() == 0) return false;
        mNav.getButton(NavigationBar.ButtonPosition.LEFT).performClick();
        return true;
    }

    @Override
    @NonNull
    public View.OnClickListener getGeneralFlowNavListener() {
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // To save the child fragment instances
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "save");
        if (mPager != null) outState.putInt(ARG_CURRENT, mPager.getCurrentItem());
        if (mPager != null) Log.d(TAG, "current " + mPager.getCurrentItem());
        outState.putBoolean(ARG_UI_UPDATE_SCHEDULED, mUIUpdateScheduled);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
        if (savedInstanceState != null) {
            Log.d(TAG, "restore");
            mUIUpdateScheduled = savedInstanceState.getBoolean(ARG_UI_UPDATE_SCHEDULED, false);
        }
    }
}
