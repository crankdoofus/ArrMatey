//
//  ArrInstanceDashboard.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-02-20.
//

import Shared
import SwiftUI

struct ArrInstanceDashboard: View {
    private let id: Int64
    
    @ObservedObject private var viewModel: ArrInstanceDashboardViewModelS
    
    @EnvironmentObject private var navigationManager: NavigationManager
    
    init(id: Int64) {
        self.id = id
        self.viewModel = ArrInstanceDashboardViewModelS(id)
    }
    
    var body: some View {
        Form {
            healthArea
            diskSpaceArea
            infoArea
        }
        .toolbar {
            ToolbarItem(placement: .topBarTrailing) {
                NavigationLink(value: SettingsRoute.editInstance(id)) {
                    Image(systemName: "pencil")
                }
            }
        }
    }
    
    private var healthArea: some View { 
        Section {
            ForEach(viewModel.health) { health in
                ArrHealthCard(health: health)
            }
        } header: {
            Text(MR.strings().health.localized())
        }
    }
    
    private var diskSpaceArea: some View {
        Section {
            ForEach(viewModel.diskSpaces) { disk in
                VStack(alignment: .leading, spacing: 8) {
                    HStack {
                        Text(disk.path ?? MR.strings().unknown.localized())
                            .font(.headline)
                        Spacer()
                        Text(disk.freeSpace.bytesAsFileSizeString() + " " + MR.strings().free_space.localized())
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                    }
                    
                    ProgressView(value: disk.usedPercentage)
                        .tint(disk.usedPercentage > 0.9 ? .red : .accentColor)
                    
                    HStack {
                        Text(MR.strings().total_space.localized() + ": " + disk.totalSpace.bytesAsFileSizeString())
                            .font(.caption)
                            .foregroundColor(.secondary)
                        Spacer()
                        Text("\(Int(disk.usedPercentage * 100))%")
                            .font(.caption)
                            .bold()
                    }
                }
                .padding(.vertical, 4)
            }
        } header: {
            Text(MR.strings().disk_space.localized())
        }
    }
    
    private var infoItems: [InfoItem] {
        [
            InfoItem(label: MR.strings().host_endpoint.localized(), value: viewModel.instance?.url ?? ""),
            InfoItem(label: MR.strings().version.localized(), value: viewModel.softwareStatus?.version ?? MR.strings().unknown.localized()),
            InfoItem(label: MR.strings().startup_path.localized(), value: viewModel.softwareStatus?.startupPath ?? MR.strings().unknown.localized()),
            InfoItem(label: MR.strings().app_data_path.localized(), value: viewModel.softwareStatus?.appData ?? MR.strings().unknown.localized()),
            InfoItem(label: MR.strings().host_platform.localized(), value: viewModel.softwareStatus?.hostPlatform.localized() ?? MR.strings().unknown.localized()),
            InfoItem(label: MR.strings().host_os.localized(), value: viewModel.softwareStatus?.hostOs ?? MR.strings().unknown.localized())
        ]
    }
    
    private var infoArea: some View {
        Section {
            VStack(spacing: 12) {
                ForEach(Array(infoItems), id: \.self) { info in
                    HStack(alignment: .center) {
                        Text(info.label)
                            .font(.system(size: 14))
                        Spacer()
                        Text(info.value)
                            .font(.system(size: 14))
                            .foregroundColor(.themePrimary)
                            .lineLimit(1)
                            .truncationMode(.tail)
                            .multilineTextAlignment(.trailing)
                            .frame(maxWidth: .infinity, alignment: .trailing)
                    }
                    
                    if info != infoItems.last {
                        Divider()
                    }
                }
            }
        } header: {
            Text(MR.strings().system_info.localized())
        }
    }
}
