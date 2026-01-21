//
//  InstancesViewModelS.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-01-19.
//

import Shared
import SwiftUI

@MainActor
class InstancesViewModelS: ObservableObject {
    private let viewModel: InstancesViewModel
    
    @Published private(set) var instancesState: InstancesState
    
    init(type: InstanceType) {
        self.instancesState = InstancesState(type: type, instances: [], selectedInstance: nil)
        self.viewModel = KoinBridge.shared.getInstancesViewModel(type: type)
        startObserving()
    }
    
    private func startObserving() {
        Task {
            for try await state in viewModel.instancesState {
                self.instancesState = state
            }
        }
    }
    
    func setInstanceActive(_ instance: Instance) {
        viewModel.setInstanceActive(instance: instance)
    }
}
