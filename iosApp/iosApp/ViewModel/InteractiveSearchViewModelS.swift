//
//  InteractiveSearchViewModelS.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-01-19.
//

import Shared
import SwiftUI

@MainActor
class InteractiveSearchViewModelS: ObservableObject {
    private let viewModel: InteractiveSearchViewModel
    
    @Published private(set) var releaseUiState: LibraryUiState = LibraryUiStateInitial()
    @Published private(set) var downloadReleaseState: DownloadState = DownloadStateInitial()
    
    init(type: InstanceType) {
        self.viewModel = KoinBridge.shared.getInteractiveSearchViewModel(type: type)
        startObserving()
    }
    
    private func startObserving() {
        Task {
            for try await state in viewModel.releaseUiState {
                self.releaseUiState = state
            }
        }
        Task {
            for try await state in viewModel.downloadReleaseState {
                self.downloadReleaseState = state
            }
        }
    }
    
    func getRelease(_ params: ReleaseParams) {
        viewModel.getRelease(params: params)
    }
    
    func downloadRelease(_ release: ArrRelease, _ force: Bool = false) {
        viewModel.downloadRelease(release: release, force: force)
    }
    
    func resetDownloadState() {
        viewModel.resetDownloadState()
    }
}
