//
//  SettingsNavigation.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-03.
//

import SwiftUI

class SettingsNavigation: ObservableObject {
    @Published var path = NavigationPath()
    
    func goToNewInstance() {
        path.append(SettingsRoute.newInstance)
    }
}

enum SettingsRoute : Hashable {
    case newInstance
}
