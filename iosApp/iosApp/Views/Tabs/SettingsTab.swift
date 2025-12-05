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
                        settingsNavigation.goToNewInstance()
                    }
                } header: {
                    Text(LocalizedStringResource("instances"))
                }
            }
            .navigationDestination(for: SettingsRoute.self) { value in
                switch value {
                case .newInstance:
                    NewInstanceView()
                }
            }
            .navigationTitle(LocalizedStringResource("settings"))
        }
        .task {
            await instanceViewModel.refresh()
        }
    }
}
