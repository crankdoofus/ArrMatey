//
//  EpisodeDetailsViewModelS.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-01-19.
//

import Shared
import SwiftUI

@MainActor
class EpisodeDetailsViewModelS: ObservableObject {
    private let viewModel: EpisodeDetailsViewModel
    
    @Published private(set) var episode: Episode
    @Published private(set) var history: NetworkResult? = nil
    @Published private(set) var monitorStatus: OperationStatus = OperationStatusIdle()
    
    init(seriesId: Int64, episode: Episode) {
        self.episode = episode
        self.viewModel = KoinBridge.shared.getEpisodeDetailsViewModel(seriesId: seriesId, episode: episode)
        startObserving()
    }
    
    private func startObserving() {
        Task {
            for try await episode in viewModel.episode {
                self.episode = episode
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
    }
    
    func toggleMonitor() {
        viewModel.toggleMonitor()
    }
    
    func executeAutomaticSearch() {
        viewModel.executeAutomaticSearch()
    }
    
    func refreshHistory() {
        viewModel.refreshHistory()
    }
    
    func resetMonitorStatus() {
        viewModel.resetMonitorStatus()
    }
}
