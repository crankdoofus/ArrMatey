//
//  ReleaseDownloadButtons.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-01-22.
//

import SwiftUI

struct ReleaseDownloadButtons: View {
    var onInteractiveClicked: () -> Void
    var automaticSearchEnabled: Bool
    var onAutomaticClicked: () -> Void
    var automaticSearchInProgress: Bool = false
    
    var body: some View {
        HStack(spacing: 24) {
            Button(action: onInteractiveClicked) {
                Label(
                    title: { Text("interactive") },
                    icon: { Image(systemName: "person.fill") }
                )
                .frame(maxWidth: .infinity)
            }
            .buttonStyle(.borderedProminent)
            .tint(.accentColor)
            .controlSize(.large)
            
            Button(action: onAutomaticClicked) {
                Group {
                    if automaticSearchInProgress {
                        ProgressView()
                            .controlSize(.small)
                    } else {
                        Label(
                            title: { Text("automatic") },
                            icon: { Image(systemName: "magnifyingglass") }
                        )
                    }
                }
                .frame(maxWidth: .infinity)
            }
            .buttonStyle(.borderedProminent)
            .controlSize(.large)
            .disabled(!automaticSearchEnabled || automaticSearchInProgress)
        }
    }
}
