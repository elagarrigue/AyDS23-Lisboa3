package ayds.lisboa.songinfo.moredetails.dependencyinyector

import android.content.Context
import ayds.lisboa.songinfo.moredetails.data.ArtistInfoRepositoryImpl
import ayds.lisboa.songinfo.moredetails.data.external.Broker
import ayds.lisboa.songinfo.moredetails.data.external.BrokerImpl
import ayds.lisboa.songinfo.moredetails.data.external.ProxyInterface
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistInfoRepository
import ayds.lisboa.songinfo.moredetails.presentation.ArtistCardHelper
import ayds.lisboa.songinfo.moredetails.presentation.ArtistCardHelperImpl
import ayds.lisboa.songinfo.moredetails.presentation.HtmlHelper
import ayds.lisboa.songinfo.moredetails.presentation.HtmlHelperImpl
import ayds.lisboa.songinfo.moredetails.presentation.OtherInfoPresenter
import ayds.lisboa.songinfo.moredetails.presentation.OtherInfoPresenterImpl
import ayds.lisboa.songinfo.moredetails.presentation.OtherInfoView
import ayds.lisboa3.submodule.lastFm.LastFmInjector
import ayds.lisboa3.submodule.lastFm.LastFmService
import ayds.lisboa.songinfo.moredetails.data.external.proxy.ProxyLastFm
import ayds.lisboa.songinfo.moredetails.data.external.proxy.ProxyNewYorkTimes
import ayds.lisboa.songinfo.moredetails.data.external.proxy.ProxyWikipedia
import ayds.lisboa.songinfo.moredetails.data.local.CardLocalStorage
import ayds.lisboa.songinfo.moredetails.data.local.CardLocalStorageImpl
import ayds.lisboa.songinfo.moredetails.data.local.CursorToCardMapper
import ayds.lisboa.songinfo.moredetails.data.local.CursorToCardMapperImpl
import ayds.winchester.artistinfo.external.WikipediaInjector
import ayds.winchester.artistinfo.external.WikipediaService
import com.test.artist.external.NYTimesArtistService
import com.test.artist.external.artists.NYTimesArtistInjector

object MoreDetailsInjector {
    private lateinit var cursorToCardMapper: CursorToCardMapper
    private lateinit var cardLocalStorage: CardLocalStorage

    private lateinit var lastFmService: LastFmService
    private lateinit var newYorkTimesService: NYTimesArtistService
    private lateinit var wikipediaService: WikipediaService

    private lateinit var proxyLastFm: ProxyLastFm
    private lateinit var proxyNewYorkTimes: ProxyNewYorkTimes
    private lateinit var proxyWikipedia: ProxyWikipedia
    private lateinit var proxyList: MutableList<ProxyInterface>
    private lateinit var broker: Broker

    private lateinit var artistInfoRepository: ArtistInfoRepository

    private lateinit var artistCardHelper: ArtistCardHelper
    private lateinit var htmlHelper: HtmlHelper
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
        cardLocalStorage = CardLocalStorageImpl(otherInfoView as Context, cursorToCardMapper)
    }

    private fun initExternalServices() {
        lastFmService = LastFmInjector.getService()
        newYorkTimesService = NYTimesArtistInjector.nyTimesArtistService
        wikipediaService = WikipediaInjector.getWikipediaService()
    }

    private fun initBroker(){
        proxyLastFm = ProxyLastFm(lastFmService)
        proxyNewYorkTimes = ProxyNewYorkTimes(newYorkTimesService)
        proxyWikipedia = ProxyWikipedia(wikipediaService)
        proxyList.add(proxyLastFm)
        proxyList.add(proxyNewYorkTimes)
        proxyList.add(proxyWikipedia)
        broker = BrokerImpl(proxyList)
    }
    private fun initRepository() {
        artistInfoRepository = ArtistInfoRepositoryImpl(cardLocalStorage, broker)
    }

    private fun initPresenter(){
        htmlHelper = HtmlHelperImpl()
        artistCardHelper = ArtistCardHelperImpl(htmlHelper)
        otherInfoPresenter = OtherInfoPresenterImpl(artistInfoRepository, artistCardHelper)
    }

    fun getPresenter() = otherInfoPresenter

}