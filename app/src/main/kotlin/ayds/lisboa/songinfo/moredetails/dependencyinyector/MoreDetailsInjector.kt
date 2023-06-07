package ayds.lisboa.songinfo.moredetails.dependencyinyector

import android.content.Context
import ayds.lisboa.songinfo.moredetails.data.CardsRepositoryImpl
import ayds.lisboa.songinfo.moredetails.data.external.broker.CardsBroker
import ayds.lisboa.songinfo.moredetails.data.external.broker.CardsBrokerImpl
import ayds.lisboa.songinfo.moredetails.data.external.broker.proxy.CardProxy
import ayds.lisboa.songinfo.moredetails.domain.repository.CardsRepository
import ayds.lisboa.songinfo.moredetails.presentation.ArtistCardHelper
import ayds.lisboa.songinfo.moredetails.presentation.ArtistCardHelperImpl
import ayds.lisboa.songinfo.moredetails.presentation.HtmlHelper
import ayds.lisboa.songinfo.moredetails.presentation.HtmlHelperImpl
import ayds.lisboa.songinfo.moredetails.presentation.OtherInfoPresenter
import ayds.lisboa.songinfo.moredetails.presentation.OtherInfoPresenterImpl
import ayds.lisboa.songinfo.moredetails.presentation.OtherInfoView
import ayds.lisboa3.submodule.lastFm.LastFmInjector
import ayds.lisboa3.submodule.lastFm.LastFmService
import ayds.lisboa.songinfo.moredetails.data.external.broker.proxy.CardProxyLastFm
import ayds.lisboa.songinfo.moredetails.data.external.broker.proxy.CardProxyNewYorkTimes
import ayds.lisboa.songinfo.moredetails.data.external.broker.proxy.CardProxyWikipedia
import ayds.lisboa.songinfo.moredetails.data.local.CardsLocalStorage
import ayds.lisboa.songinfo.moredetails.data.local.CardsLocalStorageImpl
import ayds.lisboa.songinfo.moredetails.data.local.CursorToCardMapper
import ayds.lisboa.songinfo.moredetails.data.local.CursorToCardMapperImpl
import ayds.lisboa.songinfo.moredetails.presentation.SourceFactory
import ayds.lisboa.songinfo.moredetails.presentation.SourceFactoryImpl
import ayds.newYork4.artist.external.NYTimesArtistService
import ayds.newYork4.artist.external.artists.NYTimesArtistInjector
import ayds.winchester.artistinfo.external.WikipediaInjector
import ayds.winchester.artistinfo.external.WikipediaService

object MoreDetailsInjector {
    private lateinit var cursorToCardMapper: CursorToCardMapper
    private lateinit var cardsLocalStorage: CardsLocalStorage

    private lateinit var lastFmService: LastFmService
    private lateinit var newYorkTimesService: NYTimesArtistService
    private lateinit var wikipediaService: WikipediaService

    private lateinit var cardProxyLastFm: CardProxy
    private lateinit var cardProxyNewYorkTimes: CardProxy
    private lateinit var cardProxyWikipedia: CardProxy
    private lateinit var cardsBroker: CardsBroker

    private lateinit var cardsRepository: CardsRepository

    private lateinit var artistCardHelper: ArtistCardHelper
    private lateinit var htmlHelper: HtmlHelper
    private lateinit var sourceFactory: SourceFactory
    private lateinit var otherInfoPresenter: OtherInfoPresenter

    fun init(otherInfoView: OtherInfoView) {
        initLocalStorage(otherInfoView)
        initExternalServices()
        initBroker()
        initRepository()
        initPresenter()
    }

    private fun initLocalStorage(otherInfoView: OtherInfoView){
        cursorToCardMapper = CursorToCardMapperImpl()
        cardsLocalStorage = CardsLocalStorageImpl(otherInfoView as Context, cursorToCardMapper)
    }

    private fun initExternalServices() {
        lastFmService = LastFmInjector.getService()
        newYorkTimesService = NYTimesArtistInjector.nyTimesArtistService
        wikipediaService = WikipediaInjector.getWikipediaService()
    }

    private fun initBroker(){
        cardProxyLastFm = CardProxyLastFm(lastFmService)
        cardProxyNewYorkTimes = CardProxyNewYorkTimes(newYorkTimesService)
        cardProxyWikipedia = CardProxyWikipedia(wikipediaService)
        cardsBroker = CardsBrokerImpl(listOf(cardProxyLastFm, cardProxyNewYorkTimes, cardProxyWikipedia))
    }
    private fun initRepository() {
        cardsRepository = CardsRepositoryImpl(cardsLocalStorage, cardsBroker)
    }

    private fun initPresenter(){
        htmlHelper = HtmlHelperImpl()
        sourceFactory = SourceFactoryImpl
        artistCardHelper = ArtistCardHelperImpl(htmlHelper, sourceFactory)
        otherInfoPresenter = OtherInfoPresenterImpl(cardsRepository, artistCardHelper)
    }

    fun getPresenter() = otherInfoPresenter

}