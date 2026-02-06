//
//  FilterByPickerMenu.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-03.
//

import Shared
import SwiftUI

struct FilterByPickerMenu: View {
    private let type: InstanceType
    
    private let changeFilterBy: (FilterBy) -> Void
    
    @State private var filteredBy: FilterBy
    
    init(
        type: InstanceType,
        filterBy: FilterBy,
        changeFilterBy: @escaping (FilterBy) -> Void
    ) {
        self.type = type
        self.filteredBy = filterBy
        self.changeFilterBy = changeFilterBy
    }
    
    var body: some View {
        Menu {
            Picker(MR.strings().filter_by.localized(), selection: $filteredBy) {
                ForEach(FilterBy.companion.typeEntries(type: type), id: \.self) { filterOption in
                    Text(filterOption.resource.localized())
                        .tag(filterOption)
                }
            }
            .pickerStyle(.inline)
        } label: {
            Image(systemName: "line.3.horizontal.decrease")
                .imageScale(.medium)
        }
        .onChange(of: filteredBy, { _, newValue in
            changeFilterBy(newValue)
        })
    }
}
