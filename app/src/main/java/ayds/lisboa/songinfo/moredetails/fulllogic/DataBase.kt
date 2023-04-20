package ayds.lisboa.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

private const val dbUrl = "jdbc:sqlite:./dictionary.db"
private const val artistQuery = "select * from artists"
private const val ID = "id"
private const val ARTIST = "artist"
private const val INFO = "info"
private const val SOURCE = "source"
private const val TABLE_ARTISTS = "artists"
private const val resultSetSortOrder = "artist DESC"
private const val artistColumn = "artist  = ?"
private val items: MutableList<String> = ArrayList()
private const val creationQuery = "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, info string, source integer)"

class DataBase(context: Context?) : SQLiteOpenHelper(context, "dictionary.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            creationQuery
        )
        Log.i("DB", "DB created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun testDB() {
        var connection: Connection? = null
        try {
            connection = DriverManager.getConnection(dbUrl)
            val statement = connection.createStatement()
            statement.queryTimeout = 30
            resultSetValues(statement)
        } catch (e: SQLException) {
            System.err.println(e.message)
        } finally {
            try {
                connection?.close()
            } catch (e: SQLException) {
                System.err.println(e)
            }
        }
    }
    private fun resultSetValues(statement: Statement){
        val rs = statement.executeQuery(artistQuery)
        while (rs.next()) {
            println("id = " + rs.getInt(ID))
            println("artist = " + rs.getString(ARTIST))
            println("info = " + rs.getString(INFO))
            println("source = " + rs.getString(SOURCE))
        }
    }
    fun saveArtist(artist: String, info: String) {
        val writableDatabase = this.writableDatabase
        val artistValues = getArtistValues(artist, info)
        writableDatabase.insert(TABLE_ARTISTS, null, artistValues)
    }

    private fun getArtistValues(artist: String, info: String): ContentValues {
        val values = ContentValues()
        values.put(ARTIST, artist)
        values.put(INFO, info)
        values.put(SOURCE, 1)
        return values
    }

    fun getInfo(artist: String): String {
        val readableDatabase = this.readableDatabase

        val databaseColumns = arrayOf(
            ID,
            ARTIST,
            INFO
        )

        val artistValues = arrayOf(artist)
        val cursor = readableDatabase.query(
            TABLE_ARTISTS,
            databaseColumns,
            artistColumn,
            artistValues,
            null,
            null,
            resultSetSortOrder
        )
        insertInItems(cursor)
        cursor.close()
        return if (items.isEmpty()) {
            ""
        } else {
            items[0]
        }
    }

    private fun insertInItems(cursor: Cursor) {
        var info: String
        while (cursor.moveToNext()) {
            info = cursor.getString(cursor.getColumnIndexOrThrow(INFO))
            items.add(info)
        }
    }
}