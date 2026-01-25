//
//  QueueSortPickerMenu.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-01-25.
//

import Shared
import SwiftUI

struct QueueSortPickerMenu: View {
    @Binding var sortBy: QueueSortBy
    @Binding var sortOrder: Shared.SortOrder
    
    var body: some View {
        Menu {
            Picker("sort_by", selection: $sortBy) {
                ForEach(QueueSortBy.companion.allEntries(), id: \.self) { sortOption in
                    Text(sortOption.label()).tag(sortOption)
                }
            }
            .pickerStyle(.inline)
            
            Section {
                Picker("direction", selection: $sortOrder) {
                    ForEach(Shared.SortOrder.allCases, id: \.self) { order in
                        Label {
                            Text(String(localized: String.LocalizationValue(order.iosText)))
                        } icon: {
                            Image(systemName: order.iosIcon)
                        }
                        .tag(order)
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
