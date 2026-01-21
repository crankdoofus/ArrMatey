//
//  MoreScreenViewModelS.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-01-19.
//

import Shared
import SwiftUI

@MainActor
class MoreScreenViewModelS: ObservableObject {
    private let viewModel: MoreScreenViewModel
    
    @Published private(set) var instances: [Instance] = []
    
    init() {
        self.viewModel = KoinBridge.shared.getMoreScreenViewModel()
        
        Task {
            for try await instances in viewModel.instances {
                self.instances = instances
            }
        }
    }
}
