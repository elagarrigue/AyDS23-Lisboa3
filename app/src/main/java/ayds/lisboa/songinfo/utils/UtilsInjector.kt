package ayds.lisboa.songinfo.utils

import ayds.lisboa.songinfo.utils.navigation.NavigationUtils
import ayds.lisboa.songinfo.utils.navigation.NavigationUtilsImpl
import ayds.lisboa.songinfo.utils.view.ImageLoader
import ayds.lisboa.songinfo.utils.view.ImageLoaderImpl
import ayds.lisboa.songinfo.utils.view.LeapYear
import ayds.lisboa.songinfo.utils.view.LeapYearImpl
import com.squareup.picasso.Picasso

object UtilsInjector {

    val imageLoader: ImageLoader = ImageLoaderImpl(Picasso.get())

    val navigationUtils: NavigationUtils = NavigationUtilsImpl()

    val leapYear: LeapYear = LeapYearImpl()
}