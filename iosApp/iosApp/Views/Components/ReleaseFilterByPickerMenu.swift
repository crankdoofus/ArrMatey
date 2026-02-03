//
//  ReleaseFilterByPickerMenu.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-01-24.
//

import Shared
import SwiftUI

struct ReleaseFilterByPickerMenu: View {
    @Binding var filterBy: ReleaseFilterBy
    
    @Binding var filterQuality: QualityInfo?
    @Binding var filterLanguage: Language?
    @Binding var filterIndexer: String?
    @Binding var filterProtocol: ReleaseProtocol?
    @Binding var filterCustomFormat: CustomFormat?
    
    let type: InstanceType
    let languages: Set<Language>
    let indexers: Set<String>
    let qualities: Set<QualityInfo>
    let protocols: Set<ReleaseProtocol>
    let customFormats: Set<CustomFormat>
    
    var body: some View {
        Menu {
            if qualities.count > 1 {
                qualitiesPicker
            }
            
            if languages.count > 1 {
                languagesPicker
            }
            
            if customFormats.count > 1 {
                customFormatPicker
            }
            
            if protocols.count > 1 {
                protocolPicker
            }
            
            if indexers.count > 1 {
                indexerPicker
            }
            
            if type == .sonarr {
                Section {
                    Picker("filter_by", selection: $filterBy) {
                        ForEach(ReleaseFilterBy.companion.allEntries(), id: \.self) { filter in
                            Text(filter.label()).tag(filter)
                        }
                    }
                    .pickerStyle(.inline)
                }
            }
        } label: {
            Image(systemName: "line.3.horizontal.decrease")
                .imageScale(.medium)
        }
    }
    
    private var qualitiesPicker: some View {
        Menu {
            Picker("quality", selection: $filterQuality) {
                Text("any").tag(nil as QualityInfo?)
                Divider()
                ForEach(Array(qualities), id: \.quality.id) { quality in
                    Text(quality.qualityLabel).tag(quality)
                }
            }
            .pickerStyle(.inline)
        } label: {
            Text("quality")
        }
    }
    
    private var languagesPicker: some View {
        Menu {
            Picker("language", selection: $filterLanguage) {
                Text("any").tag(nil as Language?)
                Divider()
                ForEach(Array(languages), id: \.id) { lang in
                    Text(lang.name ?? "unknown").tag(lang)
                }
            }
            .pickerStyle(.inline)
        } label: {
            Text("language")
        }
    }
    
    private var customFormatPicker: some View {
        Menu {
            Picker("custom_format", selection: $filterCustomFormat) {
                Text("any").tag(nil as CustomFormat?)
                Divider()
                ForEach(Array(customFormats), id: \.id) { format in
                    Text(format.name).tag(format)
                }
            }
            .pickerStyle(.inline)
        } label: {
            Text("custom_format")
        }
    }
    
    private var protocolPicker: some View {
        Menu {
            Picker("protocol", selection: $filterProtocol) {
                Text("any").tag(nil as ReleaseProtocol?)
                Divider()
                ForEach(Array(protocols), id: \.self) { p in
                    Text(p.name).tag(p)
                }
            }
            .pickerStyle(.inline)
        } label: {
            Text("custom_format")
        }
    }
    
    private var indexerPicker: some View {
        Menu {
            Picker("indexer", selection: $filterIndexer) {
                Text("any").tag(nil as String?)
                Divider()
                ForEach(Array(indexers), id: \.self) { indexer in
                    Text(indexer).tag(indexer)
                }
            }
            .pickerStyle(.inline)
        } label: {
            Text("indexer")
        }
    }
}
