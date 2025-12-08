//
//  SettingsNavigation.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-03.
//

import SwiftUI

class SettingsNavigation: ObservableObject {
    @Published var path = NavigationPath()
    
    func go(to route: SettingsRoute) {
        path.append(route)
    }
}

enum SettingsRoute : Hashable {
    case newInstance
    case dev
}
