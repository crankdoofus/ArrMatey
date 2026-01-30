//
//  DebounceSearchModifier.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-28.
//

import SwiftUI

struct DebounceSearchModifier: ViewModifier {
    let searchText: String
    let debounceTime: TimeInterval
    let onSearch: (String) async -> Void
    let initial: Bool
    
    @State private var searchTask: Task<Void, Never>?
    
    func body(content: Content) -> some View {
        content
            .onChange(of: searchText, initial: initial) { oldValue, newValue in
                searchTask?.cancel()
                
                guard oldValue != newValue || initial else { return }
                
                searchTask = Task {
                    try? await Task.sleep(nanoseconds: UInt64(debounceTime * 1_000_000_000))
                    guard !Task.isCancelled else { return }
                    await onSearch(newValue)
                }
            }
            .onDisappear {
                searchTask?.cancel()
            }
    }
}

extension View {
    func onDebounceSearch(
        _ searchText: String,
        initial: Bool = false,
        debounceTime: TimeInterval = 0.5,
        perform: @escaping (String) async -> Void
    ) -> some View {
        modifier(DebounceSearchModifier(searchText: searchText, debounceTime: debounceTime, onSearch: perform, initial: initial))
    }
}
