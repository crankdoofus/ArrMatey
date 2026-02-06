//
//  EmptyLibraryView.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-01-20.
//

import Shared
import SwiftUI

struct EmptyLibraryView: View {
    var body: some View {
        VStack(alignment: .center, spacing: 8) {
            Image(systemName: "popcorn.fill")
                .font(.system(size: 64))
                .imageScale(.large)
            
            Text(MR.strings().empty_library.localized())
                .font(.system(size: 20, weight: .medium))
                .multilineTextAlignment(.center)
            Text(MR.strings().empty_library_message.localized())
                .multilineTextAlignment(.center)
        }
        .padding(.horizontal, 24)
    }
}
