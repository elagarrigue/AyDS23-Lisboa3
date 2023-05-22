package ayds.lisboa.songinfo.moredetails.data.local

import android.database.Cursor
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.ArtistCard
import java.sql.SQLException

interface CursorToArtistCardMapper{
    fun map(cursor: Cursor): ArtistCard?
}

internal class CursorToArtistCardMapperImpl: CursorToArtistCardMapper {

    override fun map(cursor: Cursor): ArtistCard? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    ArtistCard(
                        description = getString(getColumnIndexOrThrow(DESCRIPTION)),
                        infoUrl = getString(getColumnIndexOrThrow(INFO_URL)),
                        source = getInt(getColumnIndexOrThrow(SOURCE)),
                        sourceLogo = getString(getColumnIndexOrThrow(SOURCE_LOGO))
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

