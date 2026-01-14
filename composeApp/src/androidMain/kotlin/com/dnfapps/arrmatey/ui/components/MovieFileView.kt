package com.dnfapps.arrmatey.ui.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnfapps.arrmatey.R
import com.dnfapps.arrmatey.api.arr.model.ArrMovie
import com.dnfapps.arrmatey.api.arr.model.CommandPayload
import com.dnfapps.arrmatey.navigation.ArrScreen
import com.dnfapps.arrmatey.navigation.ArrTabNavigation
import com.dnfapps.arrmatey.ui.tabs.LocalArrTabNavigation
import com.dnfapps.arrmatey.ui.tabs.LocalArrViewModel
import com.dnfapps.arrmatey.ui.viewmodel.RadarrViewModel
import kotlin.collections.get
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun MovieFileView(
    movie: ArrMovie,
    navigation: ArrTabNavigation = LocalArrTabNavigation.current
) {
    val arrViewModel = LocalArrViewModel.current
    if (arrViewModel == null || arrViewModel !is RadarrViewModel) return

    val context = LocalContext.current

    val searchIds by arrViewModel.automaticSearchIds.collectAsStateWithLifecycle()
    val searchResult by arrViewModel.automaticSearchResult.collectAsStateWithLifecycle()

    val movieExtraFileMap by arrViewModel.movieExtraFilesMap.collectAsStateWithLifecycle()
    val movieExtraFiles = remember(movieExtraFileMap) {
        movieExtraFileMap[movie.id] ?: emptyList()
    }

    LaunchedEffect(Unit) {
        movie.id?.let {
            arrViewModel.getMovieExtraFile(it)
        }
    }

    val searchQueuedMessage = stringResource(R.string.search_queued)
    val searchErrorMessage = stringResource(R.string.search_error)

    LaunchedEffect(searchResult) {
        when (searchResult) {
            true -> Toast.makeText(context, searchQueuedMessage, Toast.LENGTH_SHORT).show()
            false -> Toast.makeText(context, searchErrorMessage, Toast.LENGTH_SHORT).show()
            else -> {}
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ReleaseDownloadButtons(
            onInteractiveClicked = {
                val destination = ArrScreen.MovieReleases(movie.id!!)
                navigation.navigateTo(destination)
            },
            onAutomaticClicked = {
                movie.id?.let { id ->
                    val movieSearchCommand = CommandPayload.Movie(listOf(id))
                    arrViewModel.command(movieSearchCommand)
                }
            },
            automaticSearchEnabled = movie.monitored,
            automaticSearchInProgress = searchIds.contains(movie.id),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.files),
                fontWeight = FontWeight.Medium,
                fontSize = 26.sp
            )
            Text(
                text = stringResource(R.string.history),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable {
                    navigation.navigateTo(ArrScreen.MovieFiles(movie))
                }
            )
        }
        movie.movieFile?.let { file ->
            FileCard(file)
        }
        movieExtraFiles.takeUnless { it.isEmpty() }?.forEach { extraFile ->
            ExtraFileCard(extraFile)
        }

        if (movie.movieFile == null && movieExtraFiles.isEmpty()) {
            Text(
                text = stringResource(R.string.no_files),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}