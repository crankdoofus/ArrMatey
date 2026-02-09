//
//  GridPosterItemRepresentable.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-02-09.
//

import SwiftUI
import Shared

struct PosterItemRepresentable: UIViewControllerRepresentable {
    let item: ArrMedia
    let isActive: Bool

    func makeUIViewController(context: Context) -> UIViewController {
        let vc = GridPosterItemViewController(
            item: item,
            isActive: isActive
        )
        vc.view.backgroundColor = .clear
        return vc
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        // If the item or status changes, Compose will re-render inside the VC
    }
}
