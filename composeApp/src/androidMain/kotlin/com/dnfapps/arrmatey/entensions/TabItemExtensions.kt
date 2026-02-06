package com.dnfapps.arrmatey.entensions

import androidx.compose.material3.Badge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnfapps.arrmatey.arr.viewmodel.ActivityQueueViewModel
import com.dnfapps.arrmatey.compose.TabItem
import org.koin.compose.koinInject

@Composable
fun BadgeContent(
    tabItem: TabItem,
    viewModel: ActivityQueueViewModel = koinInject()
) {
    when (tabItem) {
        TabItem.ACTIVITY -> {
            val activityQueueIssuesCount by viewModel.tasksWithIssues.collectAsStateWithLifecycle()
            if (activityQueueIssuesCount > 0) {
                Badge { Text(activityQueueIssuesCount.toString()) }
            }
        }
        else -> {}
    }
}