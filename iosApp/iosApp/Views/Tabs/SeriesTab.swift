//
// Created by Owen LeJeune on 2025-11-20.
//

import Foundation
import SwiftUI
import Shared

struct SeriesTab: View {
    @EnvironmentObject private var navigationManager: NavigationManager
    
    var body: some View {
        NavigationStack(path: $navigationManager.seriesPath) {
            ArrTab(type: .sonarr)
                .navigationDestination(for: MediaRoute.self) { value in
                    destination(for: value)
                }
        }
    }
    
    @ViewBuilder
    private func destination(for route: MediaRoute) -> some View {
        switch route {
        case .details(let id):
            MediaDetailsScreen(id: id, type: .sonarr)
        case .search(let query):
            MediaSearchScreen(query: query, type: .sonarr)
        case .preview(let json):
            MediaPreviewScreen(json: json, type: .sonarr)
        }
    }
}
