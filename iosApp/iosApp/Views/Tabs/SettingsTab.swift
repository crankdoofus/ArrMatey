//
// Created by Owen LeJeune on 2025-11-20.
//

import Foundation
import SwiftUI
import Shared

struct SettingsTab: View {
    @StateObject var settingsNavigation = SettingsNavigation()
    @StateObject var instanceViewModel = InstanceViewModel()
    
    var body: some View {
        NavigationStack(path: $settingsNavigation.path) {
            Form {
                Section {
                    ForEach(instanceViewModel.instances, id: \.self) { instance in
                        Text("\(instance.label ?? instance.type.name) - \(instance.url)")
                    }
                    Button("Add instance") {
                        settingsNavigation.goToNewInstance()
                    }
                } header: {
                    Text("Instances")
                }
            }
            .navigationDestination(for: SettingsRoute.self) { value in
                switch value {
                case .newInstance:
                    NewInstanceView()
                }
            }
            .navigationTitle("Settings")
        }
        .task {
            await instanceViewModel.refresh()
        }
    }
}
