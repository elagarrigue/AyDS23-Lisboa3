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
                val source = Source.valueOf(getString(getColumnIndexOrThrow(SOURCE)))

                if (!isNull(getColumnIndexOrThrow(DESCRIPTION)) &&
                    !isNull(getColumnIndexOrThrow(INFO_URL)) &&
                    !isNull(getColumnIndexOrThrow(SOURCE_LOGO))
                ) {
                    Card.RegularCard(
                        description = getString(getColumnIndexOrThrow(DESCRIPTION)),
                        infoUrl = getString(getColumnIndexOrThrow(INFO_URL)),
                        source = source,
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

