//
//  RadarrViewModel.swift
//  iosApp
//
//  Created by Owen LeJeune on 2025-12-15.
//

import Shared

class RadarrViewModel: ArrViewModel {
    var repository: any IArrRepository
    
    let instance: Instance
    
    private var radarrRespository: RadarrRepository {
        repository as! RadarrRepository
    }
    
    init(instance: Instance) {
        self.instance = instance
        self.repository = RadarrRepository(instance: instance)
    }
    
    func getMovieExtraFileMap() -> SkieSwiftStateFlow<[KotlinInt:[ExtraFile]]> {
        return radarrRespository.movieExtraFileMap
    }
    
    func getMovieExtraFile(id: Int32) async {
        do {
            try await radarrRespository.getMovieExtraFile(id: id)
        } catch {
            return
        }
    }
}
