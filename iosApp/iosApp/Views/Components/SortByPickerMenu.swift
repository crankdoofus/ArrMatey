//
//  SortByPickerView.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-03.
//

import Shared
import SwiftUI

struct SortByPickerMenu: View {
    private let type: InstanceType
    
    private let changeSortBy: (SortBy) -> Void
    private let changeSortOrder: (Shared.SortOrder) -> Void
    
    private let sortByOptions: [SortBy]
    
    @State private var sortedBy: SortBy
    @State private var sortOrder: Shared.SortOrder
    
    init(
        type: InstanceType,
        sortBy: SortBy,
        sortOrder: Shared.SortOrder,
        changeSortBy: @escaping (SortBy) -> Void,
        changeSortOrder: @escaping (Shared.SortOrder) -> Void,
        limitToLookup: Bool = false
    ) {
        self.type = type
        self.sortedBy = sortBy
        self.sortOrder = sortOrder
        self.changeSortBy = changeSortBy
        self.changeSortOrder = changeSortOrder
        
        self.sortByOptions = limitToLookup ? SortBy.companion.lookupEntries() : SortBy.companion.typeEntries(type: type)
    }
    
    var body: some View {
        Menu {
            Picker(MR.strings().sort_by.localized(), selection: $sortedBy) {
                ForEach(sortByOptions, id: \.self) { sortOption in
                    Label {
                        Text(sortOption.resource.localized())
                    } icon: {
                        Image(systemName: sortOption.iosIcon)
                    }
                    .tag(sortOption)
                }
            }
            .pickerStyle(.inline)
            
            Section {
                Picker(MR.strings().direction.localized(), selection: $sortOrder) {
                    ForEach(Shared.SortOrder.allCases, id: \.self) { sortOrder in
                        Label {
                            Text(sortOrder.resource.localized())
                        } icon: {
                            Image(systemName: sortOrder.iosIcon)
                        }
                        .tag(sortOrder)
                    }
                }
                .pickerStyle(.inline)
            }
        } label: {
            Label(sortedBy.resource.localized(), systemImage: "arrow.up.arrow.down")
        }
        .onChange(of: sortedBy, { _, newValue in
            changeSortBy(newValue)
        })
        .onChange(of: sortOrder, { _, newValue in
            changeSortOrder(newValue)
        })
    }
}
