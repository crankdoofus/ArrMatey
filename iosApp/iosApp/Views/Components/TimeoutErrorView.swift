//
//  TimeoutErrorView.swift
//  iosApp
//
//  Created for ArrMatey
//

import SwiftUI
import Shared

struct TimeoutErrorView: View {
    let message: String
    let onOpenSettings: () -> Void
    let onRetry: (() -> Void)?
    
    init(message: String, onOpenSettings: @escaping () -> Void, onRetry: (() -> Void)? = nil) {
        self.message = message
        self.onOpenSettings = onOpenSettings
        self.onRetry = onRetry
    }
    
    var body: some View {
        VStack(spacing: 16) {
            Image(systemName: "clock.badge.exclamationmark")
                .font(.system(size: 48))
                .foregroundColor(.orange)
            
            Text("Request Timed Out")
                .font(.headline)
            
            Text("The search took too long to complete. This can happen with slower indexers or network connections.")
                .font(.subheadline)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
                .padding(.horizontal)
            
            VStack(spacing: 12) {
                Button(action: onOpenSettings) {
                    Label("Increase Timeout", systemImage: "gear")
                        .frame(maxWidth: .infinity)
                }
                .buttonStyle(.borderedProminent)
                
                if let onRetry = onRetry {
                    Button(action: onRetry) {
                        Label("Try Again", systemImage: "arrow.clockwise")
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(.bordered)
                }
            }
            .padding(.horizontal, 32)
            .padding(.top, 8)
            
            Text("Tip: Enable \"Slow Instance\" in instance settings for indexers that need more time.")
                .font(.caption)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
                .padding(.horizontal)
                .padding(.top, 8)
        }
        .padding()
    }
}

struct GenericErrorView: View {
    let message: String
    let onRetry: (() -> Void)?
    
    init(message: String, onRetry: (() -> Void)? = nil) {
        self.message = message
        self.onRetry = onRetry
    }
    
    var body: some View {
        VStack(spacing: 16) {
            Image(systemName: "exclamationmark.triangle")
                .font(.system(size: 48))
                .foregroundColor(.red)
            
            Text("Something went wrong")
                .font(.headline)
            
            Text(message)
                .font(.subheadline)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
                .padding(.horizontal)
            
            if let onRetry = onRetry {
                Button(action: onRetry) {
                    Label("Try Again", systemImage: "arrow.clockwise")
                        .frame(maxWidth: .infinity)
                }
                .buttonStyle(.bordered)
                .padding(.horizontal, 32)
                .padding(.top, 8)
            }
        }
        .padding()
    }
}

#Preview("Timeout Error") {
    TimeoutErrorView(
        message: "Request timed out",
        onOpenSettings: {},
        onRetry: {}
    )
}

#Preview("Generic Error") {
    GenericErrorView(
        message: "Could not connect to server",
        onRetry: {}
    )
}
