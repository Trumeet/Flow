package moe.yuuta.flow;

/**
 * Stores basic info, for instance, title and subtitle for the flow. These data will
 * be auto applied when user switch to the related flow and will be reset after leaving the flow.
 */
data class FlowInfo(
        var headerConfig: HeaderConfig,
        var navigationBarConfig: NavigationBarConfig?
)