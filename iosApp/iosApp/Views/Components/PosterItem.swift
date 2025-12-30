//
//  PosterItem.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-14.
//

import SwiftUI
import Shared

struct PosterItemView<T: AnyArrMedia>: UIViewControllerRepresentable {
    let item: T
    let onItemClick: ((AnyArrMedia) -> Void)?
    var enabled: Bool = true

    func makeUIViewController(context: Context) -> UIViewController {
        let controller = PosterItemViewController(
            item: self.item,
            onItemClick: self.onItemClick,
            enabled: self.enabled
        )
        controller.view.backgroundColor = .clear
        return controller
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        // left empty
    }
}
