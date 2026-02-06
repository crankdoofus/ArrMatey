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
            Picker(MR.strings().sort_by.localized(), selection: $sortBy) {
                ForEach(QueueSortBy.companion.allEntries(), id: \.self) { sortOption in
                    Text(sortOption.resource.localized()).tag(sortOption)
                }
            }
            .pickerStyle(.inline)
            
            Section {
                Picker(MR.strings().direction.localized(), selection: $sortOrder) {
                    ForEach(Shared.SortOrder.allCases, id: \.self) { order in
                        Label {
                            Text(order.resource.localized())
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
