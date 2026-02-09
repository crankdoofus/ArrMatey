//
//  ActivityQueueViewModelWrapper.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-01-16.
//

import Shared
import SwiftUI

@MainActor
class ActivityQueueViewModelS: ObservableObject {
    private let viewModel: ActivityQueueViewModel
    
    @Published private(set) var queueItems: [QueueItem] = []
    @Published private(set) var tasksWithIssues: Int = 0
    @Published private(set) var isPolling: Bool = false
    @Published private(set) var instances: [Instance] = []
    @Published private(set) var uiState: ActivityQueueUiState = ActivityQueueUiState()
    @Published private(set) var removeItemStatus: OperationStatus = OperationStatusIdle()
    @Published private(set) var removeInProgress: Bool = false
    @Published private(set) var removeSuccesss: Bool = false
    
    init() {
        self.viewModel = KoinBridge.shared.getActivityQueueViewModel()
        startObserving()
    }
    
    private func startObserving() {
        viewModel.queueItems.observeAsync { self.queueItems = $0 }
        viewModel.tasksWithIssues.observeAsync { self.tasksWithIssues = $0.intValue }
        viewModel.isPolling.observeAsync { self.isPolling = $0.boolValue }
        viewModel.instances.observeAsync { self.instances = $0 }
        viewModel.activityQueueUiState.observeAsync { self.uiState = $0 }
        viewModel.removeItemState.observeAsync {
            self.removeItemStatus = $0
            self.removeInProgress = $0 is OperationStatusInProgress
            self.removeSuccesss = $0 is OperationStatusSuccess
        }
    }
    
    func startPolling() {
        viewModel.startPolling()
    }
    
    func stopPolling() {
        viewModel.stopPolling()
    }
    
    func setInstanceId(_ id: Int64?) {
        viewModel.setInstanceId(id: id?.asKotlinLong)
    }
    
    func setSortBy(_ sortBy: QueueSortBy) {
        viewModel.setSortBy(sortBy: sortBy)
    }
    
    func setSortOrder(_ order: Shared.SortOrder) {
        viewModel.setSortOrder(order: order)
    }
    
    func getQueueItemForEpisode(_ episode: Episode) -> SonarrQueueItem? {
        return viewModel.getQueueItemForEpisode(episode: episode)
    }
    
    func removeQueueItem(_ item: QueueItem, _ removeFromClient: Bool, _ addToBlocklist: Bool, _ skipRedownload: Bool) {
        viewModel.removeQueueItem(item: item, removeFromClient: removeFromClient, addToBlocklist: addToBlocklist, skipRedownload: skipRedownload)
    }
    
    func refresh() {
        viewModel.refresh()
    }
    
}
