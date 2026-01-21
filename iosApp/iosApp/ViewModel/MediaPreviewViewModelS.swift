//
//  MediaPreviewViewModelS.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-01-19.
//

import Shared
import SwiftUI

@MainActor
class MediaPreviewViewModelS: ObservableObject {
    private let viewModel: MediaPreviewViewModel
    
    @Published private(set) var qualityProfiles: [QualityProfile] = []
    @Published private(set) var rootFolders: [RootFolder] = []
    @Published private(set) var tags: [Tag] = []
    @Published private(set) var addItemStatus: OperationStatus = OperationStatusIdle()
    @Published private(set) var lastAddedItemId: Int64? = nil
    
    init(type: InstanceType) {
        self.viewModel = KoinBridge.shared.getMediaPreviewViewModel(type: type)
        startObserving()
    }
    
    private func startObserving() {
        Task {
            for try await profiles in viewModel.qualityProfiles {
                self.qualityProfiles = profiles
            }
        }
        Task {
            for try await folders in viewModel.rootFolders {
                self.rootFolders = folders
            }
        }
        Task {
            for try await tags in viewModel.tags {
                self.tags = tags
            }
        }
        Task {
            for try await status in viewModel.addItemStatus {
                self.addItemStatus = status
            }
        }
        Task {
            for try await id in viewModel.lastAddedItemId {
                self.lastAddedItemId = id?.int64Value
            }
        }
    }
    
    func addItem(_ item: ArrMedia) {
        viewModel.addItem(item: item)
    }
    
    func resetAddStatus() {
        viewModel.resetAddStatus()
    }
    
    func clearLastAddedItemId() {
        viewModel.clearLastAddedItemId()
    }
}
