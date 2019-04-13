package moe.yuuta.flow;

import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Arrays;

// You don't need to use AppCompatActivity and AppCompat styles.
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private FlowFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        Log.i(TAG, "onCreate()");
        if (mFragment == null && savedInstanceState == null) {
            mFragment = new FlowFragment();
            mFragment.setPages(Arrays.asList(new Page1(), new Page2(), new Page3(), new Page4()));
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, mFragment)
                    .commitAllowingStateLoss();
            Log.i(TAG, "done");
        }
    }

    public static class Page1 extends PageFragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mInfo = new FlowInfo(new HeaderConfig("Page 1", "lol", false), null);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            TextView view = new TextView(requireContext());
            view.setText("Page 1");
            return view;
        }
    }

    public static class Page2 extends PageFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mInfo = new FlowInfo(new HeaderConfig("Page 2", "built with love", false), null);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            TextView view = new TextView(requireContext());
            view.setText("Page 2");
            mInfo = new FlowInfo(new HeaderConfig("Page 2 lol", "built with love", false),
                    new NavigationBarConfig("2", "1", View.VISIBLE, View.VISIBLE, View.VISIBLE, getHostFragment().getGeneralFlowNavListener(),
                            getHostFragment().getGeneralFlowNavListener()));
            getHostFragment().notifyCurrentFlowInfoUpdated();
            return view;
        }
    }

    public static class Page3 extends PageFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mInfo = new FlowInfo(new HeaderConfig("Page 3", "zzz~", true), null);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            TextView view = new TextView(requireContext());
            view.setText("Page 3");
            return view;
        }

        @Override
        public boolean onBackPressed() {
            Vibrator vibrator = (Vibrator) requireContext().getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(new long[]{50L, 30L}, 1);
            return true;
        }
    }

    public static class Page4 extends PageFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mInfo = new FlowInfo(new HeaderConfig("Page 4", "just a counter", false), null);
        }

        // TODO: The FlowFragment keeps the instance, maybe we don't want that happened to child fragments.
        private int mCount;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Button button = new Button(requireContext());
            button.setMinHeight(20);
            button.setText("+1s");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCount++;
                    HeaderConfig config = mInfo.getHeaderConfig();
                    config.setSubtitleText(Integer.toString(mCount));
                    mInfo.setHeaderConfig(config);
                    getHostFragment().notifyCurrentFlowInfoUpdated();
                }
            });
            return button;
        }
    }

    @Override
    public void onBackPressed() {
        if (mFragment != null && mFragment.onBackPressed()) return;
        super.onBackPressed();
    }
}
