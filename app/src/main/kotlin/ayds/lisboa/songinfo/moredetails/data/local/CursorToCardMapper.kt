package ayds.lisboa.songinfo.moredetails.data.local

import android.database.Cursor
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.ArtistCard
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import java.sql.SQLException

interface CursorToCardMapper {
    fun map(cursor: Cursor): ArtistCard?
}

internal class CursorToCardMapperImpl : CursorToCardMapper {

    override fun map(cursor: Cursor): ArtistCard? =
        try {
            with(cursor) {
                ArtistCard(
                    description = getString(getColumnIndexOrThrow(DESCRIPTION)),
                    infoUrl = getString(getColumnIndexOrThrow(INFO_URL)),
                    source = Source.values()[getInt(getColumnIndexOrThrow(SOURCE))],
                    sourceLogo = getString(getColumnIndexOrThrow(SOURCE_LOGO))
                )
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }

}

