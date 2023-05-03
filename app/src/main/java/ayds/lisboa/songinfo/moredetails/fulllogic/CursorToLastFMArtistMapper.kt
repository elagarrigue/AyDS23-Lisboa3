package ayds.lisboa.songinfo.moredetails.fulllogic

import android.database.Cursor
import ayds.lisboa.songinfo.moredetails.fulllogic.ArtistInfo.LastFmArtistInfo
import java.sql.SQLException

interface CursorToLastFMArtistMapper{

    fun map(cursor: Cursor): LastFmArtistInfo?
}

internal class CursorToLastFMArtistMapperImpl(): CursorToLastFMArtistMapper{

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

