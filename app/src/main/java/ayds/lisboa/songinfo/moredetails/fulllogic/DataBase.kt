package ayds.lisboa.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.lisboa.songinfo.moredetails.fulllogic.ArtistInfo.LastFmArtistInfo
import java.sql.SQLException

private const val ID = "id"
private const val ARTIST = "artist"
private const val BIO_CONTENT = "bio_content"
private const val URL = "url"
private const val SOURCE = "source"
private const val TABLE_ARTISTS = "artists"
private const val RESULT_SET_ORDER = "artist DESC"
private const val ARTIST_COLUMN = "artist  = ?"
private const val CREATION_QUERY = "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, bio_content string, url string, source integer)"
private const val DB_NAME = "dictionary.db"

interface Database{
    fun saveArtist(artist: String, artistInfo: LastFmArtistInfo)
    fun getArtistInfo(artist: String): LastFmArtistInfo?
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

    override fun saveArtist(artist: String, artistInfo: LastFmArtistInfo) {
        val artistValues = getArtistValues(artist, artistInfo)
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

    override fun getArtistInfo(artist: String): LastFmArtistInfo? {
        val artistValues = arrayOf(artist)
        return cursorHandling(artistValues)
    }

    private fun cursorHandling(artistValues: Array<String>): LastFmArtistInfo? {
        val cursor = this.readableDatabase.query(
            TABLE_ARTISTS,
            databaseColumns,
            ARTIST_COLUMN,
            artistValues,
            null,
            null,
            RESULT_SET_ORDER
        )
        val info: LastFmArtistInfo? = map(cursor)
        cursor.close()
        return info
    }

    private fun map(cursor:Cursor): LastFmArtistInfo? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    LastFmArtistInfo(
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