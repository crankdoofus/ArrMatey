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
    @Published private(set) var instance: [Instance] = []
    
    init() {
        self.viewModel = KoinBridge.shared.getActivityQueueViewModel()
        startObserving()
    }
    
    private func startObserving() {
        Task {
            for try await items in viewModel.queueItems {
                self.queueItems = items
            }
        }
        Task {
            for try await count in viewModel.tasksWithIssues {
                self.tasksWithIssues = count.intValue
            }
        }
        Task {
            for try await polling in viewModel.isPolling {
                self.isPolling = polling.boolValue
            }
        }
        Task {
            for try await instances in viewModel.instances {
                self.instance = instances
            }
        }
    }
    
    func startPolling() {
        viewModel.startPolling()
    }
    
    func stopPolling() {
        viewModel.stopPolling()
    }
    
    func setInstanceId(_ id: Int64?) {
        let kotlinLong: KotlinLong? = if let id = id {
            KotlinLong(value: id)
        } else {nil }
        viewModel.setInstanceId(id: kotlinLong)
    }
    
    func setSortBy(_ sortBy: QueueSortBy) {
        viewModel.setSortBy(sortBy: sortBy)
    }
    
    func setSortOrder(_ order: Shared.SortOrder) {
        viewModel.setSortOrder(order: order)
    }
    
}
