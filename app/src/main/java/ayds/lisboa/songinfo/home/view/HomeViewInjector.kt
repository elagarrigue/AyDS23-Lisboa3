package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.home.controller.HomeControllerInjector
import ayds.lisboa.songinfo.home.model.HomeModelInjector

object HomeViewInjector {

    private val dateCalculatorFactory: DateCalculatorFactory = DateCalculatorFactoryImpl
    val songDescriptionHelper: SongDescriptionHelper = SongDescriptionHelperImpl(dateCalculatorFactory)
    
    fun init(homeView: HomeView) {
        HomeModelInjector.initHomeModel(homeView)
        HomeControllerInjector.onViewStarted(homeView)
    }
}