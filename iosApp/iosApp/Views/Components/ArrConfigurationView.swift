//
//  ArrConfigurationView.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-01-20.
//

import Shared
import SwiftUI

struct ArrConfigurationView: View {
    let uiState: AddInstanceUiState
    let onApiEndpointChanged: (String) -> Void
    let onApiKeyChanged: (String) -> Void
    let onInstanceLabelChanged: (String) -> Void
    let onIsSlowInstanceChanged: (Bool) -> Void
    let onCustomTimeoutChanged: (Int64?) -> Void
    let onTestConnection: () -> Void
    let onDismissInfoCard: (InstanceType) -> Void
    let showInfoCard: Bool
    let showInstancePicker: Bool
    @Binding var instanceType: InstanceType
    @Binding var showError: Bool
    
    @State private var apiEndpoint: String = ""
    @State private var apiKey: String = ""
    @State private var instanceLabel: String = ""
    @State private var customTimeoutText: String = ""
    
    @Environment(\.openURL) var openURL
    
    private var urlPlaceholder: String {
        "https://example.com:\(instanceType.defaultPort)"
    }
    
    private var hasLabelConflict: Bool {
        if let result = uiState.createResult as? InsertResultConflict {
            return result.fields.contains(.instanceLabel)
        }
        return false
    }
    
    private var hasUrlConflict: Bool {
        if let result = uiState.createResult as? InsertResultConflict {
            return result.fields.contains(.instanceUrl)
        }
        return false
    }
    
    var body: some View {
        Form {
            if showInfoCard {
                infoCardView()
            }
            
            instanceSection
            testSection
            slowInstanceSection
        }
        .onChange(of: instanceType, initial: true) { _, newValue in
            instanceLabel = newValue.name
            onInstanceLabelChanged(newValue.name)
        }
        .alert("Error", isPresented: $showError) {
            Button("ok") { showError = false }
        } message: {
            Group {
                if let error = uiState.createResult as? InsertResultError {
                    Text(error.message)
                } else {
                    if hasLabelConflict {
                        Text(String(localized: LocalizedStringResource("instance_label_exists")))
                    }
                    if hasUrlConflict {
                        Text(String(localized: LocalizedStringResource("instance_url_exists")))
                    }
                }
            }
        }
    }
    
    @ViewBuilder
    private func infoCardView() -> some View {
        VStack(spacing: 8) {
            HStack {
                SVGImageView(filename: instanceType.iconKey)
                    .frame(width: 24, height: 24)
                Text(String(instanceType.name))
                    .font(.system(size: 18, weight: .medium))
                Spacer()
                Button(action: {
                    onDismissInfoCard(instanceType)
                }) {
                    Image(systemName: "xmark")
                        .font(.system(size: 18, weight: .medium))
                        .foregroundStyle(.secondary)
                        .frame(width: 32, height: 32)
                }
                .buttonStyle(.plain)
            }
            Text(LocalizedStringResource(stringLiteral: instanceType.descriptionKey))
                .font(.system(size: 14))
            HStack(spacing: 8) {
                Button(LocalizedStringResource("github"), action: {
                    if let url = URL(string: instanceType.github) {
                        openURL(url)
                    }
                })
                .frame(maxWidth: .infinity)
                
                Button(LocalizedStringResource("website"), action: {
                    if let url = URL(string: instanceType.website) {
                        openURL(url)
                    }
                })
                .frame(maxWidth: .infinity)
            }
            .padding(.horizontal)
            .alignmentGuide(VerticalAlignment.center) { d in d[VerticalAlignment.center]}
        }
    }
    
    @ViewBuilder
    private var instanceSection: some View {
        Section {
            if (showInstancePicker) {
                Picker(LocalizedStringResource("instance_type"), selection: $instanceType) {
                    ForEach(InstanceType.companion.allValue(),  id: \.self) { type in
                        Text(String(localized: LocalizedStringResource(stringLiteral: type.name)))
                            .tag(type)
                    }
                }
                .tint(.primary)
            }
            
            VStack(alignment: .leading, spacing: 4) {
                HStack(spacing: 24) {
                    Text(LocalizedStringResource("label")).layoutPriority(2)
                    TextField(
                        text: Binding(
                            get: { instanceLabel.isEmpty ? uiState.instanceLabel : instanceLabel },
                            set: { newValue in
                                instanceLabel = newValue
                                onInstanceLabelChanged(newValue)
                            }),
                        prompt: Text(instanceType.name)) {
                            EmptyView()
                        }
                        .multilineTextAlignment(.trailing)
                }
                
                if hasLabelConflict {
                    Text(LocalizedStringResource("instance_label_exists"))
                        .font(.caption)
                        .foregroundColor(.red)
                }
            }
            
            VStack(alignment: .leading, spacing: 4) {
                HStack(spacing: 24) {
                    Text(LocalizedStringResource("host")).layoutPriority(2)
                    TextField(
                        text: Binding(
                            get: { apiEndpoint.isEmpty ? uiState.apiEndpoint : apiEndpoint },
                            set: { newValue in
                                apiEndpoint = newValue
                                onApiEndpointChanged(newValue)
                            }
                        ),
                        prompt: Text(urlPlaceholder)
                    ) {
                        EmptyView()
                    }
                    .multilineTextAlignment(.trailing)
                    .textInputAutocapitalization(.never)
                }
                
                if uiState.endpointError {
                    Text(LocalizedStringResource("invalid_host"))
                        .font(.caption)
                        .foregroundColor(.red)
                } else if hasUrlConflict {
                    Text(LocalizedStringResource("instance_url_exists"))
                        .font(.caption)
                        .foregroundColor(.red)
                }
            }
            
            HStack(spacing: 24) {
                Text(LocalizedStringResource("api_key"))
                TextField(
                    text: Binding(
                        get: { apiKey.isEmpty ? uiState.apiKey : apiKey },
                        set: { newValue in
                            apiKey = newValue
                            onApiKeyChanged(newValue)
                        }
                    ),
                    prompt: Text(LocalizedStringResource("api_key_placeholder"))
                ) {
                    EmptyView()
                }
                .multilineTextAlignment(.trailing)
                .textInputAutocapitalization(.never)
            }
        } footer: {
            Text(LocalizedStringKey("host_description \(String(instanceType.name))"))
        }
    }
    
    @ViewBuilder
    private var testSection: some View {
        HStack {
            Button(action: onTestConnection) {
                if uiState.testing {
                    ProgressView()
                        .progressViewStyle(CircularProgressViewStyle())
                } else {
                    Text(LocalizedStringResource("test"))
                }
            }
            .disabled(uiState.testing || uiState.apiEndpoint.isEmpty || uiState.apiKey.isEmpty)
            .layoutPriority(2)
            
            if let testResult = uiState.testResult {
                HStack(spacing: 4) {
                    Text(LocalizedStringResource(testResult.boolValue ? "success" : "failure"))
                        .foregroundColor(testResult.boolValue ? .green : .red)
                        .multilineTextAlignment(.trailing)
                }
            }
        }
    }
    
    @ViewBuilder
    private var slowInstanceSection: some View {
        Section {
            Toggle(
                LocalizedStringResource("slow_instance"),
                isOn: Binding(
                    get: { uiState.isSlowInstance },
                    set: { onIsSlowInstanceChanged($0) }
                )
            )
            HStack(spacing: 24) {
                Text(LocalizedStringResource("custom_timeout_seconds"))
                    .foregroundStyle(uiState.isSlowInstance ? Color.primary.opacity(1.0) : Color.primary.opacity(0.3))
                TextField(
                    text: Binding(
                        get: {
                            if !customTimeoutText.isEmpty {
                                return customTimeoutText
                            }
                            return "\(uiState.customTimeout?.int64Value, default: "")"
                        },
                        set: { newValue in
                            customTimeoutText = newValue
                            let timeout = Int64(newValue)
                            onCustomTimeoutChanged(timeout)
                        }
                    ),
                    prompt: Text("300")
                ) {
                    EmptyView()
                }
                .multilineTextAlignment(.trailing)
                .keyboardType(.numberPad)
                .disabled(!uiState.isSlowInstance)
            }
        }
    }
}
