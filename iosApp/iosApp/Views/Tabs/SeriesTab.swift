//
// Created by Owen LeJeune on 2025-11-20.
//

import Foundation
import SwiftUI
import Shared

struct SeriesTab: View {
//    @StateObject var instanceViewModel = InstanceViewModel(instanceType: .sonarr)
    
    var body: some View {
        ArrTab(type: .sonarr)
//        NavigationStack {
//            VStack {
////                Text("\(instanceViewModel.firstInstance?.label ?? "")")
//            }
//            .navigationTitle(instanceViewModel.firstInstance?.label ?? "Sonarr")
//        }
//        .task {
//            await instanceViewModel.getFirstInstance()
//        }
    }
}
