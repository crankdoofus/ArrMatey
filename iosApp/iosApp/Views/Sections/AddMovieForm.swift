//
//  AddMovieForm.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-30.
//

import Shared
import SwiftUI

struct AddMovieForm: View {
    let movie: ArrMovie
    let addItemStatus: OperationStatus
    let qualityProfiles: [QualityProfile]
    let rootFolders: [RootFolder]
    let tags: [Tag]
    let onAddItem: (ArrMedia) -> Void
    let onDismiss: () -> Void
    
    @State private var isMonitored: Bool = true
    @State private var selectedMinimumAvailability: MediaStatus = .announced
    @State private var selectedQualityProfileId: Int32? = nil
    @State private var selectedRootFolderId: Int32? = nil
    
    private let selectableStatuses: [MediaStatus] = [
        .announced,
        .inCinemas,
        .released,
    ]
    
    private var selectedRootFolderPath: String? {
        rootFolders.first { $0.id == selectedRootFolderId }?.path
    }
    
    private var isLoading: Bool {
        addItemStatus is OperationStatusInProgress
    }
    
    var body: some View {
        NavigationStack {
            content
                .toolbar {
                    toolbarButtons
                }
                .onChange(of: qualityProfiles, initial: true) {
                    if !qualityProfiles.isEmpty && selectedQualityProfileId == nil {
                        selectedQualityProfileId = qualityProfiles[0].id
                    }
                }
                .onChange(of: rootFolders, initial: true) {
                    if !rootFolders.isEmpty && selectedRootFolderId == nil {
                        selectedRootFolderId = rootFolders[0].id
                    }
                }
        }
    }
    
    @ViewBuilder
    private var content: some View {
        Form {
            Section {
                Toggle("monitored", isOn: $isMonitored)
                
                if selectedQualityProfileId != nil {
                    Picker("quality_profile", selection: $selectedQualityProfileId) {
                        ForEach(qualityProfiles, id: \.self) { qualityProfile in
                            if let name = qualityProfile.name {
                                Text(name)
                                    .tag(qualityProfile.id)
                            }
                        }
                    }
                }
                
                Picker("minimum_availability", selection: $selectedMinimumAvailability) {
                    ForEach(selectableStatuses, id: \.self) { status in
                        Text(status.label()).tag(status)
                    }
                }
                
                if selectedRootFolderId != nil {
                    Picker("root_folder", selection: $selectedRootFolderId) {
                        ForEach(rootFolders, id: \.self) { rootFolder in
                            Text("\(rootFolder.path) (\(rootFolder.freeSpaceString))")
                                .tag(rootFolder.id)
                        }
                    }
                }
            }
        }
    }
    
    @ToolbarContentBuilder
    private var toolbarButtons: some ToolbarContent {
        ToolbarItem(placement: .cancellationAction) {
            Button {
                onDismiss()
            } label: {
                Label("cancel", systemImage: "xmark")
            }
            .tint(.primary)
        }
        
        ToolbarItem(placement: .primaryAction) {
            Button {
                Task {
                    if let profileId = selectedQualityProfileId, let path = selectedRootFolderPath {
                        let newMovie = movie.doCopyForCreation(monitored: isMonitored, minimumAvailability: selectedMinimumAvailability, qualityProfileId: profileId, rootFolderPath: path)
                        onAddItem(newMovie)
                    }
                }
            } label: {
                if (isLoading) {
                    ProgressView().tint(nil)
                } else {
                    Label("save", systemImage: "checkmark")
                }
            }
            .disabled(isLoading)
        }
    }
    
}
