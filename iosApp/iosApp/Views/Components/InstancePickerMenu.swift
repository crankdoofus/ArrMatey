//
//  InstancePicker.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-17.
//

import SwiftUI
import Shared

struct InstancePickerMenu: View {
    let type: InstanceType
    
    @EnvironmentObject private var instanceViewModel: InstanceViewModel
    
    private var instance: Instance? {
        instanceViewModel.instances.first {
            $0.type == type && $0.selected
        }
    }
    
    private var instances: [Instance] {
        instanceViewModel.instances.filter {
            $0.type == type
        }
    }
    
    private var hasMultipleInstances: Bool {
        instanceViewModel.instances.count {
            $0.type == type
        } > 1
    }
    
    var body: some View {
        if hasMultipleInstances {
            Menu {
                ForEach(instances, id: \.self) { i in
                    Button(action: {
                        instanceViewModel.setSelected(i)
                    }) {
                        HStack {
                            Text(i.label)
                            Spacer()
                            if i.id == instance?.id {
                                Image(systemName: "checkmark")
                                    .foregroundColor(.accentColor)
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
