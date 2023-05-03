package ayds.lisboa.songinfo.home.controller

import ayds.lisboa.songinfo.home.model.HomeModelInjector
import ayds.lisboa.songinfo.home.view.HomeView

object HomeControllerInjector {

    fun onViewStarted(homeView: HomeView) {
        HomeControllerImpl(HomeModelInjector.getHomeModel()).apply {
            setHomeView(homeView)
        }
    }
}