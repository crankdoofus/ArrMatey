//
//  ArrInstanceDashboardViewModelS.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-02-20.
//

import Shared
import SwiftUI

@MainActor
class ArrInstanceDashboardViewModelS: ObservableObject {
    private let viewModel: ArrInstanceDashboardViewModel
    
    @Published private(set) var softwareStatus: ArrSoftwareStatus? = nil
    @Published private(set) var diskSpaces: [ArrDiskSpace] = []
    @Published private(set) var health: [ArrHealth] = []
    @Published private(set) var instance: Instance? = nil
    
    init(_ id: Int64) {
        self.viewModel = KoinBridge.shared.getArrInstanceDashboardViewModel(instanceId: id)
        startObserving()
    }
    
    private func startObserving() {
        viewModel.softwareStatus.observeAsync { self.softwareStatus = $0 }
        viewModel.diskSpaces.observeAsync { self.diskSpaces = $0 }
        viewModel.health.observeAsync { self.health = $0 }
        viewModel.instance.observeAsync { self.instance = $0 }
    }
    
}
