//
//  MoviesTab.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-03.
//

import Foundation
import SwiftUI
import Shared

struct MoviesTab: View {
    @EnvironmentObject private var navigationManager: NavigationManager
    @EnvironmentObject private var instanceViewModel: InstanceViewModel
    
    @StateObject private var tabViewModel: ArrTabViewModel
    
    init() {
        _tabViewModel = StateObject(wrappedValue: ArrTabViewModel(type: .radarr, instanceViewModel: InstanceViewModel()))
    }
    
    var body: some View {
        NavigationStack(path: $navigationManager.moviePath) {
            ArrTab(type: .radarr)
                .environmentObject(tabViewModel)
                .navigationDestination(for: MediaRoute.self) { value in
                    destination(for: value)
                        .environmentObject(tabViewModel)
                }
        }
        .onAppear {
            tabViewModel.mightUpdateInstanceViewModel(instanceViewModel)
        }
    }
    
    @ViewBuilder
    private func destination(for route: MediaRoute) -> some View {
        switch route {
        case .details(let id):
            MediaDetailsScreen(id: id, type: .radarr)
        case .search(let query):
            MediaSearchScreen(query: query, type: .radarr)
        case .preview(let json):
            MediaPreviewScreen(json: json)
        }
    }
}
