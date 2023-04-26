package ayds.lisboa.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

private const val ID = "id"
private const val ARTIST = "artist"
private const val INFO = "info"
private const val SOURCE = "source"
private const val TABLE_ARTISTS = "artists"
private const val RESULT_SET_ORDER = "artist DESC"
private const val ARTIST_COLUMN = "artist  = ?"
private const val CREATION_QUERY = "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, info string, source integer)"
private const val DB_NAME = "dictionary.db"

class DataBase(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, 1) {
    private val items: MutableList<String> = ArrayList()
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            CREATION_QUERY
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    fun saveArtist(artist: String, info: String) {
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

    fun getInfo(artist: String): String {
        val databaseColumns = arrayOf(
            ID,
            ARTIST,
            INFO
        )
        val artistValues = arrayOf(artist)
        val cursor = this.readableDatabase.query(
            TABLE_ARTISTS,
            databaseColumns,
            ARTIST_COLUMN,
            artistValues,
            null,
            null,
            RESULT_SET_ORDER
        )
        insertInItems(cursor)
        cursor.close()
        return items.firstOrNull() ?:""
    }

    private fun insertInItems(cursor: Cursor) {
        var info: String
        while (cursor.moveToNext()) {
            info = cursor.getString(cursor.getColumnIndexOrThrow(INFO))
            items.add(info)
        }
    }
}