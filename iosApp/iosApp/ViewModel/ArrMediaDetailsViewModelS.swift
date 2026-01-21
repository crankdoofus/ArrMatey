//
//  ArrMediaDetailsViewModelS.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-01-20.
//

import Shared
import SwiftUI

@MainActor
class ArrMediaDetailsViewModelS: ObservableObject {
    private let viewModel: ArrMediaDetailsViewModel
    
    @Published private(set) var uiState: MediaDetailsUiState = MediaDetailsUiStateInitial()
    @Published private(set) var history: [HistoryItem] = []
    @Published private(set) var monitorStatus: OperationStatus = OperationStatusIdle()
    @Published private(set) var automaticSearchIds: Set<Int64> = Set()
    @Published private(set) var lastSearchResult: Bool? = nil
    
    init(id: Int64, type: InstanceType) {
        self.viewModel = KoinBridge.shared.getArrMediaDetailsViewModel(id: id, type: type)
        startObserving()
    }
    
    private func startObserving() {
        Task {
            for try await state in viewModel.uiState {
                self.uiState = state
            }
        }
        Task {
            for try await history in viewModel.history {
                self.history = history
            }
        }
        Task {
            for try await status in viewModel.monitorStatus {
                self.monitorStatus = status
            }
        }
        Task {
            for try await ids in viewModel.automaticSearchIds {
                self.automaticSearchIds = Set(ids.map { $0.int64Value })
            }
        }
        Task {
            for try await result in viewModel.lastSearchResult {
                self.lastSearchResult = result?.boolValue
            }
        }
    }
    
    func refreshDetails() {
        viewModel.refreshDetails()
    }
    
    func toggleMonitor() {
        viewModel.toggleMonitored()
    }
    
    func toggleSeasonMonitor(seasonNumber: Int32) {
        viewModel.toggleSeasonMonitored(seasonNumber: seasonNumber)
    }
    
    func toggleEpisodeMonitor(episode: Episode) {
        viewModel.toggleEpisodeMonitored(episode: episode)
    }
    
    func performEpisodeAutomaticLookup(episodeId: Int64) {
        viewModel.performEpisodeAutomaticLookup(episodeId: episodeId)
    }
    
    func performSeasonAutomaticLookup(seasonNumber: Int32) {
        viewModel.performSeasonAutomaticLookup(seasonNumber: seasonNumber)
    }
    
    func performMovieAutomaticLookup(movieId: Int64) {
        viewModel.performMovieAutomaticLookup(movieId: movieId)
    }
}
