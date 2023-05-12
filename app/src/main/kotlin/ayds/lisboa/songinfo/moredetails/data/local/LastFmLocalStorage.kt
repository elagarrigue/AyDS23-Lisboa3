package ayds.lisboa.songinfo.moredetails.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.lisboa.songinfo.moredetails.domain.entities.ArtistInfo.LastFmArtistInfo

interface LastFmLocalStorage {
    fun saveArtist(artistName: String, artistInfo: LastFmArtistInfo)
    fun getArtistInfo(artistName: String): LastFmArtistInfo?
}

internal class LastFmLocalStorageImpl (
    context: Context,
    private val cursorToLastFMArtistMapper: CursorToLastFMArtistMapper,
) : SQLiteOpenHelper(context, DB_NAME, null, 1),
    LastFmLocalStorage {

    private val projection = arrayOf(
        ID,
        ARTIST,
        BIO_CONTENT,
        URL
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_ARTIST_INFO_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun saveArtist(artistName: String,artistInfo: LastFmArtistInfo) {
        val artistValues = getArtistValues(artistName, artistInfo)
        this.writableDatabase.insert(TABLE_ARTISTS, null, artistValues)
    }

    private fun getArtistValues(artist: String, artistInfo: LastFmArtistInfo): ContentValues {
        val values = ContentValues().apply {
            put(ARTIST, artist)
            put(BIO_CONTENT, artistInfo.bioContent)
            put(URL, artistInfo.url)
            put(SOURCE, 1)
        }

        return values
    }

    override fun getArtistInfo(artistName: String): LastFmArtistInfo? {
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
        val artistInfo = cursorToLastFMArtistMapper.map(cursor)
        cursor.close()

        return artistInfo
    }

}