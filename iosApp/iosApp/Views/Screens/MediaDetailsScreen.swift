//
//  MediaDetailsScreen.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-11.
//

import SwiftUI
import Shared

struct MediaDetailsScreen: View {
    private let id: Int64
    private let type: InstanceType
    
    @Environment(\.dismiss) private var dismiss

    @ObservedObject private var viewModel: ArrMediaDetailsViewModelS
    
    @State private var showConfirmSheet: Bool = false
    @State private var showEditSheet: Bool = false
    @State private var confirmDeleteSeason: Int32? = nil
    
    init(id: Int64, type: InstanceType) {
        self.id = id
        self.type = type
        self.viewModel = ArrMediaDetailsViewModelS(id: id, type: type)
    }
    
    var body: some View {
        contentForState()
            .toolbar {
                ToolbarItem(placement: .primaryAction) {
                    Image(systemName: viewModel.isMonitored ? "bookmark.fill" : "bookmark")
                        .imageScale(.medium)
                        .onTapGesture {
                            viewModel.toggleMonitor()
                        }
                }
                ToolbarItem(placement: .primaryAction) {
                    Menu {
                        Section {
                            Button(MR.strings().refresh.localized(), systemImage: "arrow.clockwise") {
                                viewModel.performRefresh()
                            }
                            if type == .sonarr {
                                Button(MR.strings().search_monitored.localized(), systemImage: "magnifyingglass") {
                                    viewModel.performSeriesAutomaticLookup()
                                }
                                .disabled(!viewModel.isMonitored)
                            }
                        }
                        Section {
                            Button(MR.strings().edit.localized(), systemImage: "pencil") {
                                showEditSheet = true
                            }
                            Button(MR.strings().delete.localized(), systemImage: "trash") {
                                showConfirmSheet = true
                            }
                            .tint(.red)
                        }
                    } label: {
                        Image(systemName: "ellipsis")
                            .imageScale(.medium)
                    }
                }
            }
            .task {
                viewModel.refreshDetails()
            }
            .sheet(isPresented: $showConfirmSheet) {
                DeleteMediaSheet(isLoading: viewModel.deleteInProgress, onConfirm: { addExclusion, deleteFiles in
                    viewModel.delete(addExclusion, deleteFiles)
                })
                .presentationDetents([.fraction(0.33)])
                .presentationBackground(.ultraThinMaterial)
            }
            .sheet(isPresented: $showEditSheet) {
                switch viewModel.item {
                case nil: EmptyView()
                case let movie as ArrMovie: EditMovieSheet(item: movie, qualityProfiles: viewModel.qualityProfiles, rootFolders: viewModel.rootFolders, tags: viewModel.tags, editInProgress: viewModel.editInProgress, onEditItem: { newMovie, moveFiles in
                    viewModel.editItem(newMovie, moveFiles: moveFiles)
                })
                        .presentationBackground(.ultraThinMaterial)
                case let series as ArrSeries: EditSeriesSheet(item: series, qualityProfiles: viewModel.qualityProfiles, rootFolders: viewModel.rootFolders, tags: viewModel.tags, editInProgress: viewModel.editInProgress, onEditItem: { newSeries, moveFiles in
                    viewModel.editItem(newSeries, moveFiles: moveFiles)
                })
                        .presentationBackground(.ultraThinMaterial)
                default: EmptyView()
                }
            }
            .onChange(of: viewModel.deleteSucceeded) { _, success in
                if success {
                    dismiss()
                }
            }
            .onChange(of: viewModel.editItemSucceeded) { _, success in
                if success {
                    showEditSheet = false
                    viewModel.refreshDetails()
                }
            }
            .alert(
                MR.strings().delete_season.formatted(args: [confirmDeleteSeason ?? 0]),
                isPresented: Binding(
                    get: { confirmDeleteSeason != nil },
                    set: { if !$0 { confirmDeleteSeason = nil } }
                ),
                presenting: confirmDeleteSeason
            ) { season in
                Button(MR.strings().delete.localized(), role: .destructive) {
                    viewModel.deleteSeasonFiles(season)
                    confirmDeleteSeason = nil
                }
                Button(MR.strings().cancel.localized(), role: .cancel) {
                    confirmDeleteSeason = nil
                }
            } message: { season in
                Text(MR.strings().delete_season_confirm.formatted(args: [season]))
            }
    }
    
    @ViewBuilder
    private func contentForState() -> some View {
        switch viewModel.uiState {
        case is MediaDetailsUiStateInitial:
            ZStack {
                EmptyView()
            }
        case is MediaDetailsUiStateLoading:
            ZStack {
                ProgressView()
                    .progressViewStyle(.circular)
            }
        case let state as MediaDetailsUiStateSuccess:
            let item = state.item
            let episodes = state.episodes
            let extraFiles = state.extraFiles
            
            ScrollView {
                VStack(alignment: .leading, spacing: 0){
                    MediaDetailsHeader(item: item)
                        .frame(height: 400)
                    
                    VStack(alignment: .leading, spacing: 12) {
                        if let airingString = makeAiringString(for: item) {
                            Text(airingString)
                                .font(.system(size: 20, weight: .medium))
                                .foregroundColor(.accentColor)
                        }
                        
                        ItemDescriptionCard(overview: item.overview)
                        
                        filesArea(for: item, extraFiles, episodes)
                        
                        MediaInfoArea(item: item, qualityProfiles: viewModel.qualityProfiles, tags: viewModel.tags)
                        
                        Spacer()
                            .frame(height: 12)
                    }
                    .padding(.horizontal, 24)
                    .padding(.top, 12)
                }
            }
            .ignoresSafeArea(edges: .top)
        case _ as MediaDetailsUiStateError:
            VStack{}
        default:
            VStack {
                EmptyView()
            }
        }
    }
    
    private func makeAiringString(for item: ArrMedia) -> String? {
        switch item {
        case let series as ArrSeries:
            if series.status == .continuing {
                if let airing = series.nextAiring?.format(pattern: "HH:mm MMMM d, yyyy") {
                    return "\(MR.strings().airing_next.localized()) \(airing)"
                } else {
                    return MR.strings().continuing_unknown.localized()
                }
            } else { return nil }
        case let movie as ArrMovie:
            if let inCinemas = movie.inCinemas?.format(pattern: "HH:mm MMMM d, yyyy"), movie.digitalRelease == nil, movie.physicalRelease == nil {
                return "\(MR.strings().in_cinemas.localized()) \(inCinemas)"
            } else {
                return nil
            }
        default: return nil
        }
    }
    
    @ViewBuilder
    private func filesArea(
        for item: ArrMedia,
        _ extraFiles: [ExtraFile],
        _ episodes: [Episode]
    ) -> some View {
        if let series = item as? ArrSeries {
            SeriesFilesView(
                series: series,
                episodes: episodes,
                searchIds: viewModel.automaticSearchIds,
                searchResult: viewModel.lastSearchResult,
                onToggleSeasonMonitor: { sn in
                    viewModel.toggleSeasonMonitor(seasonNumber: sn)
                },
                onToggleEpisodeMonitor: { ep in
                    viewModel.toggleEpisodeMonitor(episode: ep)
                },
                onEpisodeAutomaticSearch: { id in
                    viewModel.performEpisodeAutomaticLookup(episodeId: id)
                },
                onSeasonAutomaticSearch: { sn in
                    viewModel.performSeasonAutomaticLookup(seasonNumber: sn)
                },
                onDeleteSeasonFiles: { seasonNumber in
                    confirmDeleteSeason = seasonNumber
                },
                seasonDeleteInProgress: false
            )
        } else if let movie = item as? ArrMovie {
            MovieFilesView(
                movie: movie,
                movieExtraFiles: extraFiles,
                searchIds: viewModel.automaticSearchIds,
                searchResult: viewModel.lastSearchResult,
                onAutomaticSearch: {
                    viewModel.performMovieAutomaticLookup(movieId: movie.id as! Int64)
                }
            )
        } else {
            EmptyView()
        }
    }
    
}
