//
//  EnumStringExtensions.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-31.
//

import Shared
import SwiftUI

extension MediaStatus {
    func label() -> LocalizedStringKey {
        switch self {
        case .tba: "tba"
        case .deleted: "deleted"
        case .released: "released"
        case .announced: "announced"
        case .inCinemas: "in_cinemas"
        case .ended: "ended"
        case .upcoming: "upcoming"
        case .continuing: "continuing"
        }
    }
}

extension SeriesMonitorType {
    func label() -> LocalizedStringKey {
        switch self {
        case .unknown: "unknown"
        case .all: "all"
        case .future: "future"
        case .missing: "missing"
        case .existing: "existing"
        case .firstSeason: "first_season"
        case .lastSeason: "last_season"
        case .latestSeason: "latest_season"
        case .pilot: "pilot"
        case .recent: "recent"
        case .monitorSpecials: "monitor_specials"
        case .unmonitorSpecials: "unmonitor_specials"
        case .none: "none"
        case .skip: "skip"
        }
    }
}

extension SeriesType {
    func label() -> LocalizedStringKey {
        switch self {
        case .standard: "standard"
        case .daily: "daily"
        case .anime: "anime"
        }
    }
}

extension ReleaseSortBy {
    func label() -> LocalizedStringKey {
        switch self {
        case .age: "age"
        case .weight: "weight"
        case .quality: "quality"
        case .seeders: "seeders"
        case .fileSize: "file_size"
        case .customScore: "custom_score"
        }
    }
}

extension ReleaseFilterBy {
    func label() -> LocalizedStringKey {
        switch self {
        case .any: "any"
        case .seasonPack: "season_pack"
        case .singleEpisode: "single_episode"
        }
    }
}

extension HistoryEventType {
    func label() -> LocalizedStringKey {
        switch self {
        case .unknown: "unknown"
        case .grabbed: "grabbed"
        case .downloadFolderImported: "download_folder_imported"
        case .downloadFailed: "download_failed"
        case .downloadIgnored: "download_ignored"
        case .movieFileDeleted: "movie_file_deleted"
        case .movieFileRenamed: "movie_file_renamed"
        case .movieFolderImported: "movie_folder_imported"
        case .episodeFileDeleted: "episode_file_deleted"
        case .episodeFileRenamed: "epsiode_file_renamed"
        case .seriesFolderImported: "series_folder_imported"
        }
    }
}
