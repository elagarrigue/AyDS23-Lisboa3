package ayds.lisboa.songinfo.moredetails.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.ArtistCard

interface CardLocalStorage {
    fun saveArtistCard(artistName: String, artistCard: ArtistCard)
    fun getArtistCards(artistName: String): List<Card>
}

internal class CardLocalStorageImpl (
    context: Context,
    private val cursorToCardMapper: CursorToCardMapper
) : SQLiteOpenHelper(context, DB_NAME, null, 1),
    CardLocalStorage {

    private val projection = arrayOf(
        ID,
        ARTIST,
        DESCRIPTION,
        INFO_URL,
        SOURCE,
        SOURCE_LOGO
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_ARTIST_INFO_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun saveArtistCard(artistName: String, artistCard: ArtistCard) {
        val artistValues = getArtistValues(artistName, artistCard)

        this.writableDatabase.insert(TABLE_ARTISTS, null, artistValues)
    }

    private fun getArtistValues(artistName: String, artistCard: ArtistCard): ContentValues {
        val values = ContentValues().apply {
            put(ARTIST, artistName)
            put(DESCRIPTION, artistCard.description)
            put(INFO_URL, artistCard.infoUrl)
            put(SOURCE, artistCard.source)
            put(SOURCE_LOGO, artistCard.sourceLogo)
        }

        return values
    }


    override fun getArtistCards(artistName: String): List<ArtistCard> {
        val artistValues = arrayOf(artistName)
        val cursor = this.readableDatabase.query(
            TABLE_ARTISTS,
            projection,
            ARTIST_COLUMN,
            artistValues,
            null,
            null,
            RESULT_SET_ORDER
        )
        val artistCards = mutableListOf<ArtistCard>()

        while (cursor.moveToNext()) {
            val artistCard = cursorToCardMapper.map(cursor)
            artistCard?.let { artistCards.add(it)}
        }

        cursor.close()

        return artistCards
    }

}