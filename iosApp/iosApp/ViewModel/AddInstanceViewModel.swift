//
//  AddInstanceViewModel.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-04.
//

import SwiftUI
import Shared

@MainActor
class AddInstanceViewModel: ObservableObject {
    private let repository = AddInstanceRepository()
        
        @Published var apiEndpoint: String = ""
        @Published var apiKey: String = ""
        @Published var saveButtonEnabled: Bool = false
        @Published var showInfoCard: Bool = true
        @Published var endpointError: Bool = false
        @Published var testing: Bool = false
        @Published var result: Bool? = nil
        @Published var isSlowInstance: Bool = false
        @Published var customTimeout: Int64? = nil
        @Published var cacheOnDisk: Bool = false
        @Published var instanceLabel: String = ""
        
        init() {
            observeFlows()
        }
        
        private func observeFlows() {
            // SKIE makes Flow collection easy
            Task {
                for await value in repository.apiEndpoint {
                    apiEndpoint = value
                }
            }
            
            Task {
                for await value in repository.apiKey {
                    apiKey = value
                }
            }
            
            Task {
                for await value in repository.saveButtonEnabled {
                    saveButtonEnabled = value.boolValue
                }
            }
            
            Task {
                for await value in repository.showInfoCard {
                    showInfoCard = value.boolValue
                }
            }
            
            Task {
                for await value in repository.endpointError {
                    endpointError = value.boolValue
                }
            }
            
            Task {
                for await value in repository.testing {
                    testing = value.boolValue
                }
            }
            
            Task {
                for await value in repository.result {
                    result = value?.boolValue
                }
            }
            
            Task {
                for await value in repository.isSlowInstance {
                    isSlowInstance = value.boolValue
                }
            }
            
            Task {
                for await value in repository.customTimeout {
                    customTimeout = value?.int64Value
                }
            }
            
            Task {
                for await value in repository.cacheOnDisk {
                    cacheOnDisk = value.boolValue
                }
            }
            
            Task {
                for await value in repository.instanceLabel {
                    instanceLabel = value
                }
            }
        }
        
        func setApiEndpoint(_ value: String) {
            repository.setApiEndpoint(value: value)
        }
        
        func setApiKey(_ value: String) {
            repository.setApiKey(value: value)
        }
        
        func setIsSlowInstance(_ value: Bool) {
            repository.setIsSlowInstance(value: value)
        }
        
        func setCustomTimeout(_ value: Int64?) {
            repository.setCustomTimeout(value: value?.kotlinLong)
        }
        
        func setCacheOnDisk(_ value: Bool) {
            repository.setCacheOnDisk(value: value)
        }
        
        func setInstanceLabel(_ value: String) {
            repository.setInstanceLabel(value: value)
        }
        
        func testConnection() {
            Task {
                try await repository.testConnection()
            }
        }
        
        func reset() {
            repository.reset()
        }
        
        func dismissInfoCard(instanceType: InstanceType) {
            repository.dismissInfoCard(instanceType: instanceType)
        }
        
        func saveInstance(instanceType: InstanceType) {
            Task {
                try await repository.saveInstance(instanceType: instanceType)
            }
        }
}

// Helper extension for KotlinLong conversion
extension Int64 {
    var kotlinLong: KotlinLong {
        KotlinLong(value: self)
    }
}
