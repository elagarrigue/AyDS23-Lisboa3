package ayds.lisboa.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.lisboa.songinfo.moredetails.fulllogic.ArtistInfo.LastFmArtistInfo

interface LastFMLocalStorage {
    fun saveArtist(artistValues: ContentValues)
    fun getArtistInfo(artistValues: Array<String>): LastFmArtistInfo?
}

internal class LastFMLocalStorageImpl (
    context: Context,
    private val cursorToLastFMArtistMapper: CursorToLastFMArtistMapper,
) : SQLiteOpenHelper(context, DB_NAME, null, 1),
    LastFMLocalStorage {

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

    override fun saveArtist(artistValues: ContentValues) {
        this.writableDatabase.insert(TABLE_ARTISTS, null, artistValues)
    }

    override fun getArtistInfo(artistValues: Array<String>): LastFmArtistInfo? {
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