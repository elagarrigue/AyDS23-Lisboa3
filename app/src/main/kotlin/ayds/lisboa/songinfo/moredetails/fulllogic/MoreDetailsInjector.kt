package ayds.lisboa.songinfo.moredetails.fulllogic

import ayds.lisboa.songinfo.moredetails.fulllogic.data.ArtistInfoRepositoryImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.LastFmApi
import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.LastFmService
import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.LastFmServiceImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.LastFmToArtistInfoResolver
import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.LastFmToArtistInfoResolverImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.data.local.CursorToLastFMArtistMapper
import ayds.lisboa.songinfo.moredetails.fulllogic.data.local.CursorToLastFMArtistMapperImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.data.local.LastFmLocalStorage
import ayds.lisboa.songinfo.moredetails.fulllogic.data.local.LastFmLocalStorageImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.ArtistInfoRepository
import ayds.lisboa.songinfo.moredetails.fulllogic.view.OtherInfoPresenter
import ayds.lisboa.songinfo.moredetails.fulllogic.view.OtherInfoPresenterImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.view.OtherInfoView
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LAST_FM_API_BASE_URL = "https://ws.audioscrobbler.com/2.0/"

object MoreDetailsInjectorDetailsInjector {
    private val retrofit = getRetrofit()
    private val lastFmApi = getLastFmApi(retrofit)
    private val lastFmToArtistInfoResolver: LastFmToArtistInfoResolver = LastFmToArtistInfoResolverImpl()
    private val lastFmService: LastFmService = LastFmServiceImpl(lastFmApi, lastFmToArtistInfoResolver)

    private val cursorToLastFMArtistMapper: CursorToLastFMArtistMapper = CursorToLastFMArtistMapperImpl()
    private val lastFmLocalStorage: LastFmLocalStorage = LastFmLocalStorageImpl(OtherInfoView, cursorToLastFMArtistMapper)

    private val artistInfoRepository: ArtistInfoRepository = ArtistInfoRepositoryImpl(lastFmLocalStorage, lastFmService)

    private val otherInfoPresenter: OtherInfoPresenter = OtherInfoPresenterImpl(artistInfoRepository)

    private fun getRetrofit(): Retrofit {
        val retrofitBuilder = Retrofit.Builder()
        retrofitBuilder.baseUrl(LAST_FM_API_BASE_URL)
        retrofitBuilder.addConverterFactory(ScalarsConverterFactory.create())
        return retrofitBuilder.build()
    }

    private fun getLastFmApi(retrofit: Retrofit): LastFmApi{
        return retrofit.create(LastFmApi::class.java)
    }

    /*
    fun init(homeView: HomeView) {
        HomeModelInjector.initHomeModel(homeView)
        HomeControllerInjector.onViewStarted(homeView)
    }*/


}