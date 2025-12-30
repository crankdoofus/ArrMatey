//
//  MediaPreviewScreen.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-29.
//

import SwiftUI
import Shared

struct MediaPreviewScreen: View {
    private let media: AnyArrMedia

    @Environment(\.dismiss) private var dismiss
    @EnvironmentObject private var arrTabViewModel: ArrTabViewModel

    @State private var sheetPresented: Bool = false

    private var instance: Instance? {
        arrTabViewModel.currentInstance
    }
    
    private var arrViewModel: ArrViewModel? {
        arrTabViewModel.arrViewModel
    }
    
    init(json: String) {
        media = AnyArrMediaCompanion().fromJson(value: json)
    }
    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 12) {
                MediaDetailsHeader(item: media)

                VStack(alignment: .leading, spacing: 12) {
                    if let airingString = makeAiringString(for: media) {
                        Text(airingString)
                            .font(.system(size: 20, weight: .medium))
                            .foregroundColor(.accentColor)
                    }

                    ItemDescriptionCard(overview: media.overview)
                }
                .padding(.horizontal, 24)
            }
            .frame(alignment: .top)
        }
        .ignoresSafeArea(edges: .top)
        .toolbar {
            ToolbarItem(placement: .primaryAction) {
                Button("add", systemImage: "plus") {
                    sheetPresented = true
                }
                .glassCompatibleButtonStyle()
            }
        }
        .sheet(isPresented: $sheetPresented) {
            NavigationStack {
                Form {
                    
                }
                .toolbar {
                    toolbarButtons
                }
            }
            .presentationDetents([.medium])
            .presentationBackground(.ultraThinMaterial)
        }
    }
    
    @ToolbarContentBuilder
    private var toolbarButtons: some ToolbarContent {
        ToolbarItem(placement: .cancellationAction) {
            Button {
                sheetPresented = false
            } label: {
                Label("cancel", systemImage: "xmark")
            }
            .tint(.primary)
        }
        
        ToolbarItem(placement: .primaryAction) {
            Button {
                // todo - add
            } label: {
                Label("save", systemImage: "checkmark")
            }
            .glassCompatibleButtonStyle()
        }
    }

    private func makeAiringString(for item: AnyArrMedia) -> String? {
        switch item {
        case let series as ArrSeries:
            if series.status == .continuing {
                if let airing = series.nextAiring?.format(pattern: "HH:mm MMMM d, yyyy") {
                    return "\(String(localized: LocalizedStringResource("airing_next"))) \(airing)"
                } else {
                    return String(localized: LocalizedStringResource("continuing_unknown"))
                }
            } else { return nil }
        case let movie as ArrMovie:
            if let inCinemas = movie.inCinemas?.format(pattern: "HH:mm MMMM d, yyyy"), movie.digitalRelease == nil, movie.physicalRelease == nil {
                return "\(String(localized: LocalizedStringResource("in_cinemas"))) \(inCinemas)"
            } else {
                return nil
            }
        default: return nil
        }
    }
}
