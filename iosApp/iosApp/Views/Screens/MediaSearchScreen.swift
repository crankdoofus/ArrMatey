//
//  MediaSearchScreen.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-27.
//

import SwiftUI
import Shared

struct MediaSearchScreen: View {
    private let type: InstanceType
    
    @ObservedObject private var viewModel: ArrSearchViewModelS
    @ObservedObject private var activityQueueViewModel = ActivityQueueViewModelS()
    
    @Environment(\.dismiss) var dismiss
    @EnvironmentObject private var navigation: NavigationManager
    
    @State private var searchQuery: String
    @State private var searchPresented: Bool = false
    
    init(query: String, type: InstanceType) {
        self._searchQuery = .init(initialValue: query)
        self.type = type
        self.viewModel = ArrSearchViewModelS(type: type)
    }
    
    private var uiState: LibraryUiState {
        viewModel.uiState
    }
    
    private var queueItems: [QueueItem] {
        activityQueueViewModel.queueItems
    }
    
    var body: some View {
        contentForState()
            .task {
                try? await Task.sleep(nanoseconds: 500_000_000)
                searchPresented = true
            }
            .onDebounceSearch(searchQuery) { query in
                guard !query.isEmpty else { return }
                viewModel.performLookup(query)
            }
            .toolbar {
                toolbarContent
            }
            .searchable(text: $searchQuery, isPresented: $searchPresented, placement: .navigationBarDrawer)
            
    }
    
    @ViewBuilder
    private func contentForState() -> some View {
        if uiState is LibraryUiStateInitial {
            Color.clear
        } else if uiState is LibraryUiStateLoading {
            ZStack {
                ProgressView()
                    .progressViewStyle(.circular)
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
        } else if let state = uiState as? LibraryUiStateSuccess<AnyObject> {
            resultsArea(state)
        } else if uiState is LibraryUiStateError {
            Text("error state")
        } else {
            EmptyView()
        }
    }
    
    @ViewBuilder
    private func resultsArea(_ state: LibraryUiStateSuccess<AnyObject>) -> some View {
        if let items = state.items as? [ArrMedia] {
            PosterGridView(items: items, onItemClick: { media in
                if let id = media.id?.int64Value {
                    navigation.go(to: .details(id), of: type)
                } else {
                    let json = media.toJson()
                    navigation.go(to: .preview(json), of: type)
                }
            }, itemIsActive: { media in
                queueItems.contains(where: { $0.mediaId == media.id })
            })
        } else {
            EmptyView()
        }
    }
    
    @ToolbarContentBuilder
    private var toolbarContent: some ToolbarContent {
        ToolbarItem(placement: .bottomBar) {
            Image(systemName: "magnifyingglass")
                .imageScale(.medium)
        }
    }
}
