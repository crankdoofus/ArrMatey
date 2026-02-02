//
//  ActivityTab.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-01-21.
//

import SwiftUI
import Shared

struct ActivityTab: View {
    
    @ObservedObject private var viewModel = ActivityQueueViewModelS()
    
    @State private var selectedItem: IdentifiableQueueItem? = nil
    
    private var titleText: String {
        guard !viewModel.queueItems.isEmpty else { return String(localized: LocalizedStringResource("activity")) }
        return "\(String(localized: LocalizedStringResource("activity"))) (\(viewModel.queueItems.count))"
    }
    
    var body: some View {
        queueItemContent
            .navigationTitle(titleText)
            .toolbar {
                if viewModel.isPolling {
                    ToolbarItem(placement: .primaryAction) {
                        ProgressView()
                            .progressViewStyle(.circular)
                    }
                }
                
                if #available(iOS 26, *) {
                    ToolbarSpacer()
                }
            
                ToolbarItem(placement: .primaryAction) {
                    QueueSortPickerMenu(sortBy: Binding(
                        get: { viewModel.uiState.sortBy },
                        set: { viewModel.setSortBy($0) }
                    ), sortOrder: Binding(
                        get: { viewModel.uiState.sortOrder },
                        set: { viewModel.setSortOrder($0) }
                    ))
                }
                ToolbarItem(placement: .primaryAction) {
                    QueueFilterMenu(instances: viewModel.instances, instanceId: Binding(
                        get: { viewModel.uiState.instanceId?.int64Value },
                        set: { viewModel.setInstanceId($0) }
                    ))
                }
            }
            .sheet(item: $selectedItem) { wrapper in
                QueueItemInfoSheet(item: wrapper.item, deleteInProgress: viewModel.removeInProgress, onDelete: { remove, block, skip in
                    viewModel.removeQueueItem(wrapper.item, remove, block, skip)
                })
                    .presentationDetents([.fraction(0.7)])
            }
            .onChange(of: viewModel.removeSuccesss) { _, newValue in
                if newValue {
                    selectedItem = nil
                    viewModel.refresh()
                }
            }
    }
    
    @ViewBuilder
    private var queueItemContent: some View {
        if viewModel.queueItems.isEmpty {
            emptyActivityView
                .frame(maxWidth: .infinity, maxHeight: .infinity)
        } else {
            ScrollView {
                VStack(spacing: 12) {
                    ForEach(viewModel.queueItems, id: \.id) { item in
                        ActivityQueueItem(item: item, onClick: { selectedItem = IdentifiableQueueItem(item: item) })
                    }
                    if viewModel.queueItems.isEmpty {
                        emptyActivityView
                    }
                }
                .padding(.vertical, 12)
                .padding(.horizontal, 18)
            }
        }
    }
    
    @ViewBuilder
    private var emptyActivityView: some View {
        VStack(alignment: .center, spacing: 12) {
            Image(systemName: "square.and.arrow.down.fill")
                .font(.system(size: 64))
                .foregroundStyle(.secondary)
            Text(LocalizedStringResource("no_activity"))
                .font(.system(size: 20, weight: .bold))
        }
        .padding(.horizontal, 24)
    }
    
}
