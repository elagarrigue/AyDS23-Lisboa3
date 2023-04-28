package ayds.lisboa.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.lisboa.songinfo.moredetails.fulllogic.ArtistInfo.SpotifyArtistInfo
import java.sql.SQLException

private const val ID = "id"
private const val ARTIST = "artist"
private const val INFO = "info"
private const val SOURCE = "source"
private const val TABLE_ARTISTS = "artists"
private const val RESULT_SET_ORDER = "artist DESC"
private const val ARTIST_COLUMN = "artist  = ?"
private const val CREATION_QUERY = "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, info string, source integer)"
private const val DB_NAME = "dictionary.db"
private const val BIO_CONTENT = "bioContent"
private const val URL = "URL"


interface Database{
    fun saveArtist(artist: String, info: String)
    fun getInfo(artist: String): SpotifyArtistInfo?
}
class DataBaseImpl(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, 1), Database {
    private val databaseColumns = arrayOf(
        ID,
        ARTIST,
        BIO_CONTENT,
        URL
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            CREATION_QUERY
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    override fun saveArtist(artist: String, info: String) {
        val artistValues = getArtistValues(artist, info)
        this.writableDatabase.insert(TABLE_ARTISTS, null, artistValues)
    }

    private fun getArtistValues(artist: String, info: String): ContentValues {
        val values = ContentValues()
        values.put(ARTIST, artist)
        values.put(INFO, info)
        values.put(SOURCE, 1)
        return values
    }

    override fun getInfo(artist: String): SpotifyArtistInfo? {
        val artistValues = arrayOf(artist)
        return cursorHandling(artistValues)
    }

    private fun cursorHandling(artistValues: Array<String>): SpotifyArtistInfo? {
        val cursor = this.readableDatabase.query(
            TABLE_ARTISTS,
            databaseColumns,
            ARTIST_COLUMN,
            artistValues,
            null,
            null,
            RESULT_SET_ORDER
        )
        val info: SpotifyArtistInfo? = map(cursor)
        cursor.close()
        return info
    }
    private fun map(cursor:Cursor): SpotifyArtistInfo? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    SpotifyArtistInfo(
                        bioContent = getString(getColumnIndexOrThrow(BIO_CONTENT)),
                        url= getString(getColumnIndexOrThrow(URL)),
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