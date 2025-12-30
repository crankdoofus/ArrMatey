//
//  ArrTabViewModel.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-28.
//

import SwiftUI
import Shared
import Combine

@MainActor
class ArrTabViewModel: ObservableObject {
    let type: InstanceType
    
    @Published
    private(set) var currentInstance: Instance?
    
    @Published
    private(set) var arrViewModel: ArrViewModel?
    
    private weak var instanceViewModel: InstanceViewModel?
    private var cancellable: AnyCancellable?
    
    init(type: InstanceType, instanceViewModel: InstanceViewModel) {
        self.type = type
        self.instanceViewModel = instanceViewModel
        
        cancellable = instanceViewModel.$instances
            .map { instances in
                instances.first { $0.type == type && $0.selected }
            }
            .removeDuplicates(by: { $0?.id == $1?.id })
            .sink { [weak self] instance in
                self?.updateInstance(instance)
            }
        
        updateInstance(instanceViewModel.selectedInstance(for: type))
    }
    
    private func updateInstance(_ instance: Instance?) {
        currentInstance = instance
        
        if let instance = instance {
            arrViewModel = createArrViewModel(for: instance)
        } else {
            arrViewModel = nil
        }
    }
    
    func mightUpdateInstanceViewModel(_ other: InstanceViewModel) {
        if self.instanceViewModel !== other {
            self.cancellable = other.$instances
                .map { $0.first { $0.type == self.type && $0.selected }}
                .removeDuplicates(by: { $0?.id == $1?.id })
                .sink { self.updateInstance($0) }
        }
    }
}
