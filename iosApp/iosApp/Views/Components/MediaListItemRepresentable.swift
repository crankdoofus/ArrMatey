//
//  MediaListItemRepresentable.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-02-09.
//

import Shared
import SwiftUI

struct MediaListItemRepresentable: UIViewControllerRepresentable {
    let item: ArrMedia
    let isActive: Bool
    
    func makeUIViewController(context: Context) -> some UIViewController {
        let vc = MediaListItemViewController(item: item, isActive: isActive)
        vc.view.backgroundColor = .clear
        return vc
    }
    
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
        // left empty
    }
}
