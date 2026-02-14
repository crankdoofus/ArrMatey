//
//  InstancePicker.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-17.
//

import SwiftUI
import Shared

struct InstancePickerMenu: View {
    let instances: [Instance]
    let onChangeInstance: (Instance) -> Void
    
    var body: some View {
        if instances.count > 1 {
            Menu {
                ForEach(instances, id: \.self) { i in
                    Button(action: {
                        onChangeInstance(i)
                    }) {
                        HStack {
                            Text(i.label)
                            Spacer()
                            if i.selected {
                                Image(systemName: "checkmark")
                                    .foregroundColor(.themePrimary)
                            }
                        }
                    }
                }
            } label: {
                Image(systemName: "externaldrive.connected.to.line.below.fill")
                    .imageScale(.medium)
            }
        }
    }
}
