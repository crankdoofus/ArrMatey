//
//  ReleaseSortByPickerMenu.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-01-24.
//

import Shared
import SwiftUI

struct ReleaseSortByPickerMenu: View {
    
    @Binding var sortBy: ReleaseSortBy
    @Binding var sortOrder: Shared.SortOrder
    
    var body: some View {
        Menu {
            Picker(MR.strings().sort_by.localized(), selection: $sortBy) {
                ForEach(ReleaseSortBy.allCases, id: \.self) { sort in
                    Text(sort.resource.localized()).tag(sort)
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
