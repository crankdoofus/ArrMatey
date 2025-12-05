//
//  SVGImageView.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-04.
//

import SwiftUI

struct SVGImageView: View {
    let filename: String
    
    var body: some View {
        Image(filename)
            .resizable()
            .aspectRatio(contentMode: .fit)
    }
}
