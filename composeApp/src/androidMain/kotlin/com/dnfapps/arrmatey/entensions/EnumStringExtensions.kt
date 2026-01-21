package com.dnfapps.arrmatey.entensions

import androidx.annotation.StringRes
import com.dnfapps.arrmatey.R
import com.dnfapps.arrmatey.arr.api.model.HistoryEventType
import com.dnfapps.arrmatey.arr.api.model.MediaStatus
import com.dnfapps.arrmatey.arr.api.model.SeriesMonitorType
import com.dnfapps.arrmatey.arr.api.model.SeriesType
import com.dnfapps.arrmatey.compose.utils.ReleaseFilterBy
import com.dnfapps.arrmatey.compose.utils.ReleaseSortBy

@StringRes
fun MediaStatus.stringResource() = when (this) {
    MediaStatus.Tba -> R.string.tba
    MediaStatus.Deleted -> R.string.deleted
    MediaStatus.Released -> R.string.released
    MediaStatus.Announced -> R.string.announced
    MediaStatus.InCinemas -> R.string.in_cinemas
    MediaStatus.Ended -> R.string.ended
    MediaStatus.Upcoming -> R.string.upcoming
    MediaStatus.Continuing -> R.string.continuing
}

@StringRes
fun SeriesMonitorType.stringResource() = when (this) {
    SeriesMonitorType.Unknown -> R.string.unknown
    SeriesMonitorType.All -> R.string.all
    SeriesMonitorType.Future -> R.string.future
    SeriesMonitorType.Missing -> R.string.missing
    SeriesMonitorType.Existing -> R.string.existing
    SeriesMonitorType.FirstSeason -> R.string.first_season
    SeriesMonitorType.LastSeason -> R.string.last_season
    SeriesMonitorType.LatestSeason -> R.string.latest_seasons
    SeriesMonitorType.Pilot -> R.string.pilot
    SeriesMonitorType.Recent -> R.string.recent
    SeriesMonitorType.UnmonitorSpecials -> R.string.unmonitor_specials
    SeriesMonitorType.MonitorSpecials -> R.string.monitor_specials
    SeriesMonitorType.None -> R.string.none
    SeriesMonitorType.Skip -> R.string.skip
}

@StringRes
fun SeriesType.stringResource() = when (this) {
    SeriesType.Standard -> R.string.standard
    SeriesType.Daily -> R.string.daily
    SeriesType.Anime -> R.string.anime
}

@StringRes
fun ReleaseSortBy.stringResource() = when (this) {
    ReleaseSortBy.Age -> R.string.age
    ReleaseSortBy.Weight -> R.string.weight
    ReleaseSortBy.Quality -> R.string.quality
    ReleaseSortBy.Seeders -> R.string.seeders
    ReleaseSortBy.FileSize -> R.string.file_size
    ReleaseSortBy.CustomScore -> R.string.custom_score
}

@StringRes
fun ReleaseFilterBy.stringResource() = when (this) {
    ReleaseFilterBy.Any -> R.string.any
    ReleaseFilterBy.SeasonPack -> R.string.season_pack
    ReleaseFilterBy.SingleEpisode -> R.string.single_episode
}

@StringRes
fun HistoryEventType.stringResource() = when (this) {
    HistoryEventType.Unknown -> R.string.unknown
    HistoryEventType.Grabbed -> R.string.grabbed
    HistoryEventType.DownloadFolderImported -> R.string.download_folder_imported
    HistoryEventType.DownloadFailed -> R.string.download_failed
    HistoryEventType.DownloadIgnored -> R.string.download_ignored
    HistoryEventType.MovieFileRenamed -> R.string.movie_file_renamed
    HistoryEventType.MovieFileDeleted -> R.string.movie_file_deleted
    HistoryEventType.MovieFolderImported -> R.string.movie_folder_imported
    HistoryEventType.EpisodeFileRenamed -> R.string.episode_file_renamed
    HistoryEventType.EpisodeFileDeleted -> R.string.episode_file_deleted
    HistoryEventType.SeriesFolderImported -> R.string.series_folder_imported
}