package ayds.lisboa.songinfo.moredetails.data.local

import android.database.Cursor
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import java.sql.SQLException

interface CursorToCardMapper {
    fun map(cursor: Cursor): Card?
}

internal class CursorToCardMapperImpl : CursorToCardMapper {

    override fun map(cursor: Cursor): Card? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    Card(
                        description = getString(getColumnIndexOrThrow(DESCRIPTION)),
                        infoUrl = getString(getColumnIndexOrThrow(INFO_URL)),
                        source = Source.valueOf(getString(getColumnIndexOrThrow(SOURCE))),
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

