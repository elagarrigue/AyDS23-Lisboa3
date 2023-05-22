package ayds.lisboa.songinfo.moredetails.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface LastFmLocalStorage {
    fun saveArtistCard(artistName: String, artistCard: Card)
    fun getArtistCards(artistName: String): List<Card>
}

internal class LastFmLocalStorageImpl (
    context: Context,
    private val cursorToArtistCardMapper: CursorToArtistCardMapper,
) : SQLiteOpenHelper(context, DB_NAME, null, 1),
    LastFmLocalStorage {

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

    override fun saveArtistCard(artistName: String, artistCard: Card) {
        val artistValues = getArtistValues(artistName, artistCard)

        this.writableDatabase.insert(TABLE_ARTISTS, null, artistValues)
    }

    private fun getArtistValues(artistName: String, artistCard: Card): ContentValues {
        val values = ContentValues().apply {
            put(ARTIST, artistName)
            put(DESCRIPTION, artistCard.description)
            put(INFO_URL, artistCard.infoUrl)
            put(SOURCE, artistCard.source)
            put(SOURCE_LOGO, artistCard.sourceLogo)
        }

        return values
    }

    override fun getArtistCards(artistName: String): List<Card> {
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
        val artistCards = mutableListOf<Card>()

        while (cursor.moveToNext()) {
            val artistCard = cursorToArtistCardMapper.map(cursor)
            artistCard?.let { artistCards.add(it)}

        }

        cursor.close()

        return artistCards
    }

}