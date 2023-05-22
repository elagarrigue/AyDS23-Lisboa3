package ayds.lisboa.songinfo.moredetails.dependencyinyector

import android.content.Context
import ayds.lisboa.songinfo.moredetails.data.ArtistInfoRepositoryImpl
import ayds.lisboa.songinfo.moredetails.data.local.CursorToLastFMArtistMapper
import ayds.lisboa.songinfo.moredetails.data.local.CursorToLastFMArtistMapperImpl
import ayds.lisboa.songinfo.moredetails.data.local.LastFmLocalStorageImpl
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistInfoRepository
import ayds.lisboa.songinfo.moredetails.presentation.ArtistInfoHelper
import ayds.lisboa.songinfo.moredetails.presentation.ArtistInfoHelperImpl
import ayds.lisboa.songinfo.moredetails.presentation.HtmlHelper
import ayds.lisboa.songinfo.moredetails.presentation.HtmlHelperImpl
import ayds.lisboa.songinfo.moredetails.presentation.OtherInfoPresenter
import ayds.lisboa.songinfo.moredetails.presentation.OtherInfoPresenterImpl
import ayds.lisboa.songinfo.moredetails.presentation.OtherInfoView
import ayds.lisboa3.submodule.lastFm.external.LastFmInjector
import ayds.lisboa3.submodule.lastFm.external.LastFmService

object MoreDetailsInjector {
    private lateinit var cursorToLastFMArtistMapper: CursorToLastFMArtistMapper
    private lateinit var lastFmLocalStorage: LastFmLocalStorageImpl

    private lateinit var lastFmService: LastFmService

    private lateinit var artistInfoHelper: ArtistInfoHelper
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
        artistInfoHelper = ArtistInfoHelperImpl(htmlHelper)

    }

    private fun initLastFmLocalStorage(otherInfoView: OtherInfoView){
        cursorToLastFMArtistMapper = CursorToLastFMArtistMapperImpl()
        lastFmLocalStorage = LastFmLocalStorageImpl(otherInfoView as Context, cursorToLastFMArtistMapper)
    }

    private fun initLastFmService() {
        lastFmService = LastFmInjector.getService()
    }

    private fun initArtistInfoRepository() {
        artistInfoRepository = ArtistInfoRepositoryImpl(lastFmLocalStorage, lastFmService)
    }

    private fun initOtherInfoPresenter(){
        otherInfoPresenter = OtherInfoPresenterImpl(artistInfoRepository, artistInfoHelper)
    }

    fun getPresenter() = otherInfoPresenter

}