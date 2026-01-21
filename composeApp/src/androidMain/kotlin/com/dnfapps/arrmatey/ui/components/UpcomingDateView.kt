package com.dnfapps.arrmatey.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dnfapps.arrmatey.R
import com.dnfapps.arrmatey.arr.api.model.ArrMedia
import com.dnfapps.arrmatey.arr.api.model.ArrMovie
import com.dnfapps.arrmatey.arr.api.model.ArrSeries
import com.dnfapps.arrmatey.arr.api.model.MediaStatus
import com.dnfapps.arrmatey.utils.format
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun UpcomingDateView(item: ArrMedia) {
    when (item) {
        is ArrSeries -> if (item.status == MediaStatus.Continuing) item.nextAiring?.format()?.let {
            "${stringResource(R.string.airing_next)} $it"
        } ?: stringResource(R.string.continuing_unknown) else null
        is ArrMovie -> item.inCinemas?.format()?.takeUnless {
            item.digitalRelease != null || item.physicalRelease != null
        }?.let { "${stringResource(R.string.in_cinemas)} $it" }
    }?.let { airingString ->
        Text(
            text = airingString,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}