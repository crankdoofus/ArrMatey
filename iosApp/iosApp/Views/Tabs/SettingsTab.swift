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
                        HStack(spacing: 24){
                            SVGImageView(filename: instance.type.iconKey)
                                .frame(width: 32, height: 32)
                            VStack(alignment: .leading, spacing: 1) {
                                Text(instance.label ?? instance.type.name)
                                    .font(.system(size: 18, weight: .medium))
                                Text(instance.url)
                                    .font(.system(size: 16))
                            }
                        }
                    }
                    Button(LocalizedStringResource("add_instance")) {
                        settingsNavigation.go(to: .newInstance)
                    }
                } header: {
                    Text(LocalizedStringResource("instances"))
                }
                if isDebug() {
                    Button("Dev settings") {
                        settingsNavigation.go(to: .dev)
                    }
                }
            }
            .navigationDestination(for: SettingsRoute.self) { value in
                switch value {
                case .newInstance:
                    NewInstanceView()
                case .dev:
                    DevSettingsScreen()
                }
            }
            .navigationTitle(LocalizedStringResource("settings"))
        }
        .task {
            await instanceViewModel.refresh()
        }
    }
}
