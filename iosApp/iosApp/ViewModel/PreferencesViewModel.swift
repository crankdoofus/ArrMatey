//
//  PreferencesViewModel.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-05.
//

import SwiftUI
import Shared

@MainActor
class PreferencesViewModel: ObservableObject {

    private let preferenceStore = PreferencesStore()

    @Published var sortBy: SortBy = .title
    @Published var sortOrder: Shared.SortOrder = .asc
    @Published var filterBy: FilterBy = .all
    
    init() {
        observeFlows()
    }
    
    private func observeFlows() {
        Task {
            for await value in preferenceStore.sortBy {
                self.sortBy = value
            }
        }
        Task {
            for await value in preferenceStore.sortOrder {
                self.sortOrder = value
            }
        }
        Task {
            for await value in preferenceStore.filterBy {
                self.filterBy = value
            }
        }
    }
    
    func saveSortBy(_ value: SortBy) {
        preferenceStore.saveSortBy(sortBy: value)
    }
    
    func saveSortOrder(_ value: Shared.SortOrder) {
        preferenceStore.saveSortOrder(sortOrder: value)
    }
    
    func saveFilterBy(_ value: FilterBy) {
        preferenceStore.saveFilterBy(filterBy: value)
    }
    
}
