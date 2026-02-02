//
//  RemoveQueueItemView.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-02-02.
//

import Shared
import SwiftUI

struct RemoveQueueItemView: View {
    @State private var remove: Bool = false
    @State private var block: Bool = false
    @State private var skip: Bool = true
    
    let deleteInProgress: Bool
    let onDelete: (Bool, Bool, Bool) -> Void
    
    var body: some View {
        Form {
            Section {
                Toggle("client_remove_title", isOn: $remove)
            } footer: {
                Text("client_remove_message")
            }
            
            Section {
                Toggle("blocklist_title", isOn: $block)
            } footer: {
                Text("blocklist_message")
            }
            
            if block {
                Section {
                    Toggle("skip_redownload_title", isOn: $skip)
                } footer: {
                    Text("skip_redownload_message")
                }
            }
        }
        .toolbarTitleDisplayMode(.inline)
        .toolbar {
            ToolbarItem(placement: .primaryAction) {
                Button {
                    onDelete(remove, block, block && skip)
                } label: {
                    Label("delete", systemImage: "trash")
                        .foregroundStyle(.white)
                }
                .buttonStyle(.borderedProminent)
                .tint(.red)
            }
        }
    }
}
