package moe.yuuta.flow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * The Host fragment
 */
public class FlowFragment extends Fragment implements IFlowFragment, View.OnClickListener {
    private List<PageFragment> mPages = new ArrayList<>(0);
    // true: after the view settles, update the UI immediately.
    private volatile boolean mUIUpdateScheduled;

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
        notifyCurrentFlowInfoUpdated();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_flow, container, false);
        mHeader = new Header(view.<ConstraintLayout>findViewById(R.id.flow_host_header));
        mNav = new NavigationBar(view.<ConstraintLayout>findViewById(R.id.flow_host_nav));
        mPager = view.findViewById(R.id.flow_host_pager);
        mPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                PageFragment fragment = mPages.get(position);
                return fragment;
            }

            @Override
            public int getCount() {
                return mPages.size();
            }
        });
        mPager.addOnPageChangeListener(mPageListener);
        getChildFragmentManager().registerFragmentLifecycleCallbacks(mCallback, false);
        return view;
    }

    @Override
    public void onDestroyView() {
        getChildFragmentManager().unregisterFragmentLifecycleCallbacks(mCallback);
        mPager.removeOnPageChangeListener(mPageListener);
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    private FragmentManager.FragmentLifecycleCallbacks mCallback = new FragmentManager.FragmentLifecycleCallbacks() {
        @Override
        public void onFragmentPreCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
            if (f instanceof PageFragment) {
                final PageFragment pf = (PageFragment) f;
                pf.setHostFragment(FlowFragment.this);
            }
        }
    };

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
}
