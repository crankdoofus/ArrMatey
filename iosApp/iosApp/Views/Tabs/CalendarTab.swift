//
//  CalendarTab.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-02-09.
//

import Shared
import SwiftUI

struct CalendarTab: View {
    
    @ObservedObject private var viewModel = CalendarViewModelS()
    @ObservedObject private var preferences = PreferencesViewModel()
    
    var body: some View {
        NavigationStack {
            ZStack {
                if preferences.calendarViewMode == .list {
                    CalendarListView(state: viewModel.calendarState, onLoadMore: { viewModel.loadMore() })
                } else {
                    CalendarMonthView(state: viewModel.calendarState)
                }
            }
        }
        .navigationTitle(MR.strings().schedule.localized())
        .navigationBarTitleDisplayMode(.inline)
        .toolbar {
            toolbarContent
        }
        .refreshable {
            viewModel.load()
        }
        .onAppear {
            viewModel.load()
        }
    }
    
    @ToolbarContentBuilder
    private var toolbarContent: some ToolbarContent {
        ToolbarItem(placement: .topBarTrailing) {
            Button(action: {
                preferences.toggleCalendarViewMode()
            }) {
                Image(systemName: preferences.calendarViewMode == .list ? "calendar" : "list.bullet")
            }
        }
        
        ToolbarItem(placement: .topBarTrailing) {
            CalendarFilterMenu(
                instanceId: Binding(
                    get: { viewModel.filterState.instanceId?.int64Value },
                    set: { viewModel.setFilterInstanceId($0) }
                ),
                contentFilter: Binding(
                    get: { viewModel.filterState.contentFilter },
                    set: { viewModel.setContentFilter($0) }
                ),
                onlyMonitored: Binding(
                    get: { viewModel.filterState.showMonitoredOnly },
                    set: { _ in viewModel.toggleShowMonitoredOnly() }
                ),
                onlyPremiers: Binding(
                    get: { viewModel.filterState.showPremiersOnly },
                    set: { _ in viewModel.toggleShowPremiersOnly() }
                ),
                onlyFinales: Binding(
                    get: { viewModel.filterState.showFinalesOnly },
                    set: { _ in viewModel.toggleShowFinalesOnly() }
                ),
                instances: viewModel.instances
            )
            .menuIndicator(.hidden)
        }
    }
}
