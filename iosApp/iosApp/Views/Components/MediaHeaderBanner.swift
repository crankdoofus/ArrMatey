//
//  MediaHeaderBanner.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-29.
//

import SwiftUI
import Shared

struct MediaHeaderBanner<T: ArrMedia>: UIViewControllerRepresentable {
    let item: T
    
    func makeUIViewController(context: Context) -> some UIViewController {
        DetailHeaderBannerViewController(item: item)
    }
    
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
        // left blank
    }
}
