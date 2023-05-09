package ayds.lisboa.songinfo.moredetails.fulllogic

import android.content.Context
import ayds.lisboa.songinfo.moredetails.fulllogic.data.ArtistInfoRepositoryImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.LastFmApi
import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.LastFmService
import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.LastFmServiceImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.LastFmToArtistInfoResolver
import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.LastFmToArtistInfoResolverImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.data.local.CursorToLastFMArtistMapper
import ayds.lisboa.songinfo.moredetails.fulllogic.data.local.CursorToLastFMArtistMapperImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.data.local.LastFmLocalStorageImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.ArtistInfoRepository
import ayds.lisboa.songinfo.moredetails.fulllogic.view.ArtistInfoHelper
import ayds.lisboa.songinfo.moredetails.fulllogic.view.ArtistInfoHelperImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.view.HtmlHelper
import ayds.lisboa.songinfo.moredetails.fulllogic.view.HtmlHelperImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.view.OtherInfoPresenter
import ayds.lisboa.songinfo.moredetails.fulllogic.view.OtherInfoPresenterImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.view.OtherInfoView
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LAST_FM_API_BASE_URL = "https://ws.audioscrobbler.com/2.0/"

object MoreDetailsInjector {
    private lateinit var otherInfoView: OtherInfoView

    private lateinit var cursorToLastFMArtistMapper: CursorToLastFMArtistMapper
    private lateinit var lastFmLocalStorage: LastFmLocalStorageImpl

    private lateinit var retrofit: Retrofit
    private lateinit var lastFmApi: LastFmApi
    private lateinit var lastFmToArtistInfoResolver: LastFmToArtistInfoResolver
    private lateinit var lastFmService: LastFmService

    private lateinit var artistInfoRepository: ArtistInfoRepository

    private lateinit var artistInfoHelper: ArtistInfoHelper
    private lateinit var htmlHelper: HtmlHelper

    private lateinit var otherInfoPresenter: OtherInfoPresenter

    fun init(otherInfoView: OtherInfoView) {
        initOtherInfoView(otherInfoView)
        initLastFmLocalStorage()
        initLastFmService()
        initArtistInfoRepository()
        initArtistInfoHelper()
        initOtherInfoPresenter()
    }

    private fun initOtherInfoView(otherInfoView: OtherInfoView){
        this.otherInfoView = otherInfoView
    }
    private fun initLastFmLocalStorage(){
        cursorToLastFMArtistMapper = CursorToLastFMArtistMapperImpl()
        lastFmLocalStorage = LastFmLocalStorageImpl(otherInfoView as Context, cursorToLastFMArtistMapper)
    }

    private fun initLastFmService() {
        retrofit = getRetrofit()
        lastFmApi = getLastFmApi(retrofit)
        lastFmToArtistInfoResolver = LastFmToArtistInfoResolverImpl()
        lastFmService = LastFmServiceImpl(lastFmApi, lastFmToArtistInfoResolver)
    }

    private fun getRetrofit(): Retrofit {
        val retrofitBuilder = Retrofit.Builder()
        retrofitBuilder.baseUrl(LAST_FM_API_BASE_URL)
        retrofitBuilder.addConverterFactory(ScalarsConverterFactory.create())
        return retrofitBuilder.build()
    }

    private fun getLastFmApi(retrofit: Retrofit): LastFmApi{
        return retrofit.create(LastFmApi::class.java)
    }

    private fun initArtistInfoRepository() {
        artistInfoRepository = ArtistInfoRepositoryImpl(lastFmLocalStorage, lastFmService)
    }

    private fun initArtistInfoHelper() {
        htmlHelper = HtmlHelperImpl()
        artistInfoHelper = ArtistInfoHelperImpl(htmlHelper)
        otherInfoView.setArtistInfoHelper(artistInfoHelper)
    }

    private fun initOtherInfoPresenter(){
        otherInfoPresenter = OtherInfoPresenterImpl(artistInfoRepository)
        otherInfoPresenter.setOtherInfoView(otherInfoView)
    }

}