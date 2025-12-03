//
//  FilterByPickerMenu.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-03.
//

import Shared
import SwiftUI

struct FilterByPickerMenu: View {
    let type: InstanceType
    @Binding var filteredBy: FilterBy
    
    var body: some View {
        Menu {
            Picker("Filter By", selection: $filteredBy) {
                ForEach(FilterBy.companion.typeEntries(type: type), id: \.self) { filterOption in
                    Text(String(localized: String.LocalizationValue(filterOption.iosText)))
                        .tag(filterOption)
                }
            }
            .pickerStyle(.inline)
        } label: {
            Image(systemName: "line.3.horizontal.decrease")
                .imageScale(.medium)
        }
    }
}
