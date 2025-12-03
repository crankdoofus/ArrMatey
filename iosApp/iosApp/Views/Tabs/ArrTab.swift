//
//  ArrTab.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-03.
//

import Foundation
import SwiftUI
import Shared

struct ArrTab: View {
    let type: InstanceType
    
    @ObservedObject var instanceViewModel: InstanceViewModel
    
    init(type: InstanceType) {
        self.type = type
        self.instanceViewModel = InstanceViewModel(instanceType: type)
    }
    
    var body: some View {
        NavigationStack {
            VStack {
//                Text("\(instanceViewModel.firstInstance?.label ?? "")")
            }
            .navigationTitle(instanceViewModel.firstInstance?.label ?? "Sonarr")
        }
        .task {
            await instanceViewModel.getFirstInstance()
        }
    }
}
