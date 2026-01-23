//
//  MovieExtraFilesView.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-01-23.
//

import Shared
import SwiftUI

struct MovieExtraFilesView: View {
    let extraFiles: [ExtraFile]
    
    var body: some View {
        ForEach(extraFiles, id: \.id) { extraFile in
            VStack(alignment: .leading, spacing: 4) {
                Text(extraFile.relativePath)
                    .font(.system(size: 16, weight: .medium))
                
                Text(extraFile.type.name)
                    .font(.system(size: 14))
            }
            .padding(.vertical, 12)
            .padding(.horizontal, 18)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(
                RoundedRectangle(cornerRadius: 10, style: .continuous)
                    .fill(Color(.systemGroupedBackground))
            )
        }
    }
}
