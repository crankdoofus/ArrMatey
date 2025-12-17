import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        KoinHelperKt.doInitKoin()
    }
    
    @StateObject private var navigationManager = NavigationManager()
    @StateObject private var instanceViewModel = InstanceViewModel()

    var body: some Scene {
        WindowGroup {
            ContentView()
                .environmentObject(navigationManager)
                .environmentObject(instanceViewModel)
        }
    }
}

extension View {
    func apply<V: View>(@ViewBuilder _ block: (Self) -> V) -> V { block(self) }
}
