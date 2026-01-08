//
//  MovieFilesView.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-15.
//

import SwiftUI
import Shared

struct MovieFilesView: View {
    let movie: ArrMovie
    let viewModel: RadarrViewModel
    
    @State private var observationTask: Task<Void, Never>? = nil
    @State private var extraFileMap: [KotlinInt:[ExtraFile]] = [:]
    
    private var extraFiles: [ExtraFile]? {
        if let id = movie.id {
            extraFileMap[id]
        } else { nil }
    }
    
    var body: some View {
        content
            .task {
                await setupViewModel()
                await viewModel.getMovieExtraFile(id: movie.id as! Int32)
            }
    }
    
    @ViewBuilder
    private var content: some View {
        Section {
            if let file = movie.movieFile {
                VStack(alignment: .leading, spacing: 4) {
                    Text(file.relativePath)
                        .font(.system(size: 18, weight: .medium))
                    
                    Text(fileInfoLine(file: file))
                        .font(.system(size: 14))
                    
                    let dateAdded = file.dateAdded.format(pattern: "MMM d, yyyy")
                    Text(String(localized: LocalizedStringResource("added_on \(dateAdded)")))
                        .font(.system(size: 14))
                }
                .padding(.vertical, 12)
                .padding(.horizontal, 18)
                .frame(maxWidth: .infinity, alignment: .leading)
                .background(
                    RoundedRectangle(cornerRadius: 10, style: .continuous)
                        .fill(Color(.systemGroupedBackground))
                )
            }
            if let extraFiles = extraFiles {
                ForEach(extraFiles, id: \.id) { extraFile in
                    VStack(alignment: .leading, spacing: 4) {
                        Text(extraFile.relativePath)
                            .font(.system(size: 16, weight: .medium))
                        
                        Text(extraFile.type.name)
                            .font(.system(size: 14))
                    }
                    .padding(.vertical, 12)
                    .padding(.horizontal, 18)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .background(
                        RoundedRectangle(cornerRadius: 10, style: .continuous)
                            .fill(Color(.systemGroupedBackground))
                    )
                }
            }
            
            if movie.movieFile == nil && extraFiles == nil {
                Text(String(localized: LocalizedStringResource("no_files")))
                    .font(.system(size: 14))
                    .foregroundColor(.secondary)
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 12)
                    .multilineTextAlignment(.center)
            }
        } header: {
            HStack(alignment: .center) {
                Text(String(localized: LocalizedStringResource("files")))
                    .font(.system(size: 20, weight: .bold))
                Spacer()
                Text(String(localized: LocalizedStringResource("history")))
                    .font(.system(size: 16))
                    .foregroundColor(.accentColor)
            }
            .frame(maxWidth: .infinity)
        }
    }
    
    private func fileInfoLine(file: MovieFile) -> String {
        let languageName = file.languages.first?.name ?? ""
        let sizeString = file.size.bytesAsFileSizeString()
        let qualityName = file.quality.quality.name
        return [languageName, sizeString, qualityName]
            .filter { !$0.isEmpty }
            .joined(separator: " • ")
    }
    
    @MainActor
    private func setupViewModel() async {
        observationTask?.cancel()
        observationTask = Task {
            await observeExtraFileMap()
        }
    }
    
    @MainActor
    private func observeExtraFileMap() async {
        do {
            let flow = viewModel.getMovieExtraFileMap()
            for try await map in flow {
                self.extraFileMap = map
            }
        } catch {
            print("Error observing extra file map: \(error)")
        }
    }
}
