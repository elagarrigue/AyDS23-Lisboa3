package ayds.lisboa.songinfo.moredetails.mvp.data.local

import android.database.Cursor
import ayds.lisboa.songinfo.moredetails.mvp.domain.ArtistInfo
import ayds.lisboa.songinfo.moredetails.mvp.domain.ArtistInfo.LastFmArtistInfo
import java.sql.SQLException

interface CursorToLastFMArtistMapper{

    fun map(cursor: Cursor): LastFmArtistInfo?
}

internal class CursorToLastFMArtistMapperImpl(): CursorToLastFMArtistMapper {

    override fun map(cursor: Cursor): LastFmArtistInfo? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    ArtistInfo.LastFmArtistInfo(
                        bioContent = getString(getColumnIndexOrThrow(BIO_CONTENT)),
                        url = getString(getColumnIndexOrThrow(URL)),
                    )
                } else {
                    null
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
}

