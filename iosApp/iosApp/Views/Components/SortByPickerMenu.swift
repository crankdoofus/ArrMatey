//
//  SortByPickerView.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-03.
//

import Shared
import SwiftUI

struct SortByPickerMenu: View {
    let type: InstanceType
    @Binding var sortedBy: SortBy
    @Binding var sortOrder: Shared.SortOrder
    
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
    }
}
