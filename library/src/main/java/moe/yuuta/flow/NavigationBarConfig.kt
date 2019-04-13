package moe.yuuta.flow

import android.view.View

class NavigationBarConfig(
        var rightButtonText: CharSequence,
        var leftButtonText: CharSequence,
        var leftButtonVisibility: Int,
        var rightButtonVisibility: Int,
        var navBarVisibility: Int,
        var leftListener: View.OnClickListener?,
        var rightListener: View.OnClickListener?
)