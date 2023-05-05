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
import ayds.lisboa.songinfo.moredetails.fulllogic.view.OtherInfoPresenter
import ayds.lisboa.songinfo.moredetails.fulllogic.view.OtherInfoPresenterImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.view.OtherInfoView
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LAST_FM_API_BASE_URL = "https://ws.audioscrobbler.com/2.0/"

object MoreDetailsInjector {
    private val retrofit = getRetrofit()
    private val lastFmApi = getLastFmApi(retrofit)
    private val lastFmToArtistInfoResolver: LastFmToArtistInfoResolver = LastFmToArtistInfoResolverImpl()
    private val lastFmService: LastFmService = LastFmServiceImpl(lastFmApi, lastFmToArtistInfoResolver)

    private val cursorToLastFMArtistMapper: CursorToLastFMArtistMapper = CursorToLastFMArtistMapperImpl()
    private lateinit var lastFmLocalStorage: LastFmLocalStorageImpl

    private val artistInfoRepository: ArtistInfoRepository = ArtistInfoRepositoryImpl(lastFmLocalStorage, lastFmService)
    private lateinit var  otherInfoView: OtherInfoView
    private val otherInfoPresenter: OtherInfoPresenter = OtherInfoPresenterImpl(artistInfoRepository)

    fun init(otherInfoView: OtherInfoView) {
        initOtherInfoView(otherInfoView)
        initLastFmLocalStorage()
        initOtherInfoPresenter()
    }

    private fun initOtherInfoView(otherInfoView: OtherInfoView){
        this.otherInfoView = otherInfoView
    }
    private fun initLastFmLocalStorage(){
        lastFmLocalStorage = LastFmLocalStorageImpl(otherInfoView as Context, cursorToLastFMArtistMapper)
    }
    private fun initOtherInfoPresenter(){
        otherInfoPresenter.setOtherInfoView(otherInfoView)
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

    fun getArtistInfoRepository():ArtistInfoRepository = artistInfoRepository




}