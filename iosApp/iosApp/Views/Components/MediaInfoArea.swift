//
//  MediaInfoArea.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-15.
//

import SwiftUI
import Shared

struct MediaInfoArea: View {
    let item: ArrMedia
    let qualityProfiles: [QualityProfile]
    let tags: [Tag]
    
    private var infoItems: [InfoItem] {
        if let series = item as? ArrSeries {
            seriesInfo(series)
        } else if let movie = item as? ArrMovie {
            movieInfo(movie)
        } else { [] }
    }
    
    var body: some View {
        Section {
            VStack(spacing: 12) {
                ForEach(Array(infoItems), id: \.self) { info in
                    HStack(alignment: .center) {
                        Text(info.label)
                            .font(.system(size: 14))
                        Spacer()
                        Text(info.value)
                            .font(.system(size: 14))
                            .foregroundColor(.accentColor)
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
            Text(String(localized: LocalizedStringResource("information")))
                .font(.system(size: 26, weight: .bold))
        }
    }
    
    private func seriesInfo(_ series: ArrSeries) -> [InfoItem] {
        let qualityProfile = qualityProfiles.first(where: { $0.id == series.qualityProfileId })
        let qualityLabel = qualityProfile?.name ?? String(localized: "unknown")
        let tagsLabel = series.formatTags(availableTags: tags) ?? String(localized: "none")
        
        let unknown = String(localized: "unknown")
        
        let monitorLabel = if series.monitorNewItems == .all {
            String(localized: "monitored")
        } else {
            String(localized: "unmonitored")
        }
        
        let seasonFolderLabel = if series.seasonFolder {
            String(localized: "yes")
        } else {
            String(localized: "no")
        }
        
        return [
            InfoItem(label: String(localized: "series_type"), value: series.seriesType.name),
            InfoItem(label: String(localized: "root_folder"), value: series.rootFolderPath ?? unknown),
            InfoItem(label: String(localized: "path"), value: series.path ?? unknown),
            InfoItem(label: String(localized: "new_seasons"), value: monitorLabel),
            InfoItem(label: String(localized: "season_folders"), value: seasonFolderLabel),
            InfoItem(label: String(localized: "quality_profile"), value: qualityLabel),
            InfoItem(label: String(localized: "tags"), value: tagsLabel)
        ]
    }
    
    private func movieInfo(_ movie: ArrMovie) -> [InfoItem] {
        let qualityProfile = qualityProfiles.first(where: { $0.id == movie.qualityProfileId })
        let qualityLabel = qualityProfile?.name ?? String(localized: "unknown")
        let tagsLabel = movie.formatTags(availableTags: tags) ?? String(localized: "none")
        
        let unknown = String(localized: "unknown")
        
        let rootFolderValue = if movie.rootFolderPath.isEmpty { unknown } else {
            movie.rootFolderPath
        }
        
        var info: [InfoItem] = [
            InfoItem(label: String(localized: "minimum_availability"), value: movie.minimumAvailability.name),
            InfoItem(label: String(localized: "root_folder"), value: rootFolderValue),
            InfoItem(label: String(localized: "path"), value: movie.path ?? unknown)
        ]
        
        if let inCinemas = movie.inCinemas?.format(pattern: "MMM d, yyyy") {
            info.append(InfoItem(label: String(localized: "in_cinemas"), value: inCinemas))
        }
        
        if let physicalRelease = movie.physicalRelease?.format(pattern: "MMM d, yyyy") {
            info.append(InfoItem(label: String(localized: "physical_release"), value: physicalRelease))
        }
        
        if let digitalRelease = movie.digitalRelease?.format(pattern: "MMM d, yyy") {
            info.append(InfoItem(label: String(localized: "digital_release"), value: digitalRelease))
        }
        
        info.append(InfoItem(label: String(localized: "quality_profile"), value: qualityLabel))
        info.append(InfoItem(label: String(localized: "tags"), value: tagsLabel))
        
        return info
    }
}
