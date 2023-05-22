package ayds.lisboa.songinfo.moredetails.dependencyinyector

import android.content.Context
import ayds.lisboa.songinfo.moredetails.data.ArtistInfoRepositoryImpl
import ayds.lisboa.songinfo.moredetails.data.local.CursorToArtistCardMapper
import ayds.lisboa.songinfo.moredetails.data.local.CursorToArtistCardMapperImpl
import ayds.lisboa.songinfo.moredetails.data.local.LastFmLocalStorageImpl
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistInfoRepository
import ayds.lisboa.songinfo.moredetails.presentation.ArtistCardHelper
import ayds.lisboa.songinfo.moredetails.presentation.ArtistCardHelperImpl
import ayds.lisboa.songinfo.moredetails.presentation.HtmlHelper
import ayds.lisboa.songinfo.moredetails.presentation.HtmlHelperImpl
import ayds.lisboa.songinfo.moredetails.presentation.OtherInfoPresenter
import ayds.lisboa.songinfo.moredetails.presentation.OtherInfoPresenterImpl
import ayds.lisboa.songinfo.moredetails.presentation.OtherInfoView
import ayds.lisboa3.submodule.lastFm.external.LastFmInjector
import ayds.lisboa3.submodule.lastFm.external.LastFmService

object MoreDetailsInjector {
    private lateinit var cursorToArtistCardMapper: CursorToArtistCardMapper
    private lateinit var lastFmLocalStorage: LastFmLocalStorageImpl

    private lateinit var lastFmService: LastFmService

    private lateinit var artistCardHelper: ArtistCardHelper
    private lateinit var htmlHelper: HtmlHelper
    private lateinit var artistInfoRepository: ArtistInfoRepository

    private lateinit var otherInfoPresenter: OtherInfoPresenter

    fun init(otherInfoView: OtherInfoView) {
        initLastFmLocalStorage(otherInfoView)
        initLastFmService()
        initArtistInfoRepository()
        initArtistInfoHelper()
        initOtherInfoPresenter()
    }

    private fun initArtistInfoHelper() {
        htmlHelper = HtmlHelperImpl()
        artistCardHelper = ArtistCardHelperImpl(htmlHelper)

    }

    private fun initLastFmLocalStorage(otherInfoView: OtherInfoView){
        cursorToArtistCardMapper = CursorToArtistCardMapperImpl()
        lastFmLocalStorage = LastFmLocalStorageImpl(otherInfoView as Context, cursorToArtistCardMapper)
    }

    private fun initLastFmService() {
        lastFmService = LastFmInjector.getService()
    }

    private fun initArtistInfoRepository() {
        artistInfoRepository = ArtistInfoRepositoryImpl(lastFmLocalStorage, lastFmService)
    }

    private fun initOtherInfoPresenter(){
        otherInfoPresenter = OtherInfoPresenterImpl(artistInfoRepository, artistCardHelper)
    }

    fun getPresenter() = otherInfoPresenter

}