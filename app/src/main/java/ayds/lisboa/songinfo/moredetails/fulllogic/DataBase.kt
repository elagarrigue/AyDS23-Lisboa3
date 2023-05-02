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
private const val DB_NAME = "dictionary.db"
private const val CREATE_ARTIST_INFO_TABLE =
    "create table $TABLE_ARTISTS (" +
            "$ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$ARTIST string, " +
            "$BIO_CONTENT string, " +
            "$URL string, " +
            "$SOURCE integer)"

interface DataBase{
    fun saveArtist(artist: String, artistInfo: LastFmArtistInfo)
    fun getArtistInfo(artist: String): LastFmArtistInfo?
}

internal class DataBaseImpl(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, 1), DataBase {
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
            projection,
            ARTIST_COLUMN,
            artistValues,
            null,
            null,
            RESULT_SET_ORDER
        )
        val artistInfo = map(cursor)
        cursor.close()

        return artistInfo
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