package ayds.lisboa.songinfo.utils

import ayds.lisboa.songinfo.utils.navigation.NavigationUtils
import ayds.lisboa.songinfo.utils.navigation.NavigationUtilsImpl
import ayds.lisboa.songinfo.utils.view.ImageLoader
import ayds.lisboa.songinfo.utils.view.ImageLoaderImpl
import com.squareup.picasso.Picasso

object UtilsInjector {

    val imageLoader: ImageLoader = ImageLoaderImpl(Picasso.get())

    val navigationUtils: NavigationUtils = NavigationUtilsImpl()
}