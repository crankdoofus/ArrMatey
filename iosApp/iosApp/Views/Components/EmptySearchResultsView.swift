//
//  EmptySearchResultsView.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-01-30.
//

import Shared
import SwiftUI

struct EmptySearchResultsView: View {
    let type: InstanceType
    let query: String
    let onShouldSearch: () -> Void
    
    private var mediaType: String {
        type == .sonarr ? "series" : "movie"
    }
    
    var body: some View {
        VStack(spacing: 8) {
            Text("No results for \"\(query)\" in your library")
                .font(.system(size: 18, weight: .medium))
            
            HStack(spacing: 4) {
                Text("Check your spelling or")
                    .foregroundColor(.secondary)
                
                Button(action: onShouldSearch) {
                    Text("add a new \(mediaType)")
                        .fontWeight(.bold)
                        .foregroundColor(.accentColor)
                }
            }
        }
        .multilineTextAlignment(.center)
        .padding(.horizontal, 16)
        .frame(maxHeight: .infinity)
    }
}
