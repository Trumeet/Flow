package moe.yuuta.flow

import android.view.View

class NavigationBarConfig(
        var rightButtonText: CharSequence,
        var leftButtonText: CharSequence,
        @field:View.Visibility
        var leftButtonVisibility: Int,
        @field:View.Visibility
        var rightButtonVisibility: Int,
        @field:View.Visibility
        var navBarVisibility: Int,
        var leftListener: View.OnClickListener?,
        var rightListener: View.OnClickListener?
)