package ayds.lisboa.songinfo.moredetails.dependencyinyector

import android.content.Context
import ayds.lisboa.songinfo.moredetails.data.ArtistInfoRepositoryImpl
import ayds.lisboa.songinfo.moredetails.data.external.LastFmApi
import ayds.lisboa.songinfo.moredetails.data.external.LastFmService
import ayds.lisboa.songinfo.moredetails.data.external.LastFmServiceImpl
import ayds.lisboa.songinfo.moredetails.data.external.LastFmToArtistInfoResolver
import ayds.lisboa.songinfo.moredetails.data.external.LastFmToArtistInfoResolverImpl
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
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LAST_FM_API_BASE_URL = "https://ws.audioscrobbler.com/2.0/"

object MoreDetailsInjector {
    private lateinit var cursorToLastFMArtistMapper: CursorToLastFMArtistMapper
    private lateinit var lastFmLocalStorage: LastFmLocalStorageImpl

    private lateinit var retrofit: Retrofit
    private lateinit var lastFmApi: LastFmApi
    private lateinit var lastFmToArtistInfoResolver: LastFmToArtistInfoResolver
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

    private fun getLastFmApi(retrofit: Retrofit): LastFmApi {
        return retrofit.create(LastFmApi::class.java)
    }

    private fun initArtistInfoRepository() {
        artistInfoRepository = ArtistInfoRepositoryImpl(lastFmLocalStorage, lastFmService)
    }

    private fun initOtherInfoPresenter(){
        otherInfoPresenter = OtherInfoPresenterImpl(artistInfoRepository, artistInfoHelper)
    }

    fun getPresenter() = otherInfoPresenter

}