//
//  Moko.swift
//  iosApp
//
//  Created by Owen LeJeune on 2026-02-06.
//

import Shared

extension StringResource {
    func localized() -> String {
        return StringDescCompanion().Resource(stringRes: self).localized()
    }
    
    func formatted(args: [Any]) -> String {
        return StringDescCompanion().ResourceFormatted(stringRes: self, args: args).localized()
    }
}
