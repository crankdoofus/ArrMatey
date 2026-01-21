//
//  PosterListView.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-08.
//

import SwiftUI
import Shared

struct PosterListView: UIViewControllerRepresentable {
    private let items: [ArrMedia]
    private let onItemClick: (ArrMedia) -> Void
    private let itemIsActive: (ArrMedia) -> KotlinBoolean
    
    init(
        items: [ArrMedia],
        onItemClick: @escaping (ArrMedia) -> Void,
        itemIsActive: @escaping (ArrMedia) -> Bool
    ) {
        self.items = items
        self.onItemClick = onItemClick
        self.itemIsActive = { item in
            KotlinBoolean(bool: itemIsActive(item))
        }
    }
    
    func makeUIViewController(context: Context) -> some UIViewController {
        let vc = MediaListViewController(items: self.items, onItemClick: self.onItemClick, itemIsActive: self.itemIsActive)
        vc.view.backgroundColor = .clear
        return vc
    }
    
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
        // left empty
    }
    
    func sizeThatFits(_ proposal: ProposedViewSize, uiViewController: UIViewControllerType, context: Context) -> CGSize? {
        return proposal.replacingUnspecifiedDimensions()
    }
    
}
