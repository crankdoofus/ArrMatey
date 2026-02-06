//
//  QueueFilterMenu.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-01-25.
//

import Shared
import SwiftUI

struct QueueFilterMenu: View {
    let instances: [Instance]
    @Binding var instanceId: Int64?
    
    var body: some View {
        Menu {
            Picker(MR.strings().filter_by.localized(), selection: $instanceId) {
                Text(MR.strings().all.localized()).tag(nil as Int64?)
                ForEach(instances, id: \.id) { instance in
                    if let label = instances.first(where: { $0.id == instance.id })?.label {
                        Text(label).tag(instance.id)
                    }
                }
            }
        } label: {
            Image(systemName: "line.3.horizontal.decrease")
                .imageScale(.medium)
        }
    }
}
