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
    
    @State private var sortedBy: SortBy
    @State private var sortOrder: Shared.SortOrder
    
    init(
        type: InstanceType,
        sortBy: SortBy,
        sortOrder: Shared.SortOrder,
        changeSortBy: @escaping (SortBy) -> Void,
        changeSortOrder: @escaping (Shared.SortOrder) -> Void
    ) {
        self.type = type
        self.sortedBy = sortBy
        self.sortOrder = sortOrder
        self.changeSortBy = changeSortBy
        self.changeSortOrder = changeSortOrder
    }
    
    var body: some View {
        Menu {
            Picker("Sort By", selection: $sortedBy) {
                ForEach(SortBy.companion.typeEntries(type: type), id: \.self) { sortOption in
                    Label {
                        Text(String(localized: String.LocalizationValue(sortOption.textKey)))
                    } icon: {
                        Image(systemName: sortOption.iosIcon)
                    }
                    .tag(sortOption)
                }
            }
            .pickerStyle(.inline)
            
            Section {
                Picker("Direction", selection: $sortOrder) {
                    ForEach(Shared.SortOrder.allCases, id: \.self) { sortOrder in
                        Label {
                            Text(String(localized: String.LocalizationValue(sortOrder.iosText)))
                        } icon: {
                            Image(systemName: sortOrder.iosIcon)
                        }
                        .tag(sortOrder)
                    }
                }
                .pickerStyle(.inline)
            }
        } label: {
            Image(systemName: "arrow.up.arrow.down")
                .imageScale(.medium)
        }
        .onChange(of: sortedBy, { _, newValue in
            changeSortBy(newValue)
        })
        .onChange(of: sortOrder, { _, newValue in
            changeSortOrder(newValue)
        })
    }
}
