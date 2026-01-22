package com.dnfapps.arrmatey.arr.state

import com.dnfapps.arrmatey.arr.api.model.ArrMedia
import com.dnfapps.arrmatey.arr.api.model.Episode
import com.dnfapps.arrmatey.arr.api.model.ExtraFile
import com.dnfapps.arrmatey.arr.api.model.QualityProfile
import com.dnfapps.arrmatey.arr.api.model.RootFolder
import com.dnfapps.arrmatey.arr.api.model.Tag

sealed interface MediaDetailsUiState {
    object Initial: MediaDetailsUiState
    object Loading: MediaDetailsUiState
    data class Error(val message: String?): MediaDetailsUiState
    data class Success(
        val item: ArrMedia,
        val extraFiles: List<ExtraFile> = emptyList(),
        val episodes: List<Episode> = emptyList(),
        val automaticSearchIds: Set<Long> = emptySet(),
        val lastSearchResult: Boolean? = null,
        val qualityProfiles: List<QualityProfile> = emptyList(),
        val rootFolders: List<RootFolder> = emptyList(),
        val tags: List<Tag> = emptyList()
    ): MediaDetailsUiState
}