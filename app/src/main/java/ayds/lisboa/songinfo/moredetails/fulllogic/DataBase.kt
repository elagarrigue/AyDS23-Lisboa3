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



class DataBase(context: Context?) : SQLiteOpenHelper(context, "dictionary.db", null, 1) {

    private val db_url = "jdbc:sqlite:./dictionary.db"
    private val ArtistQuery = "select * from artists"
    private val ID = "id"
    private val ARTIST = "artist"
    private val INFO = "info"
    private val SOURCE = "source"
    private val TABLE_ARTISTS = "artists"
    private val resultSetSortOrder = "artist DESC"
    private val artistColumn = "artist  = ?"
    private val items: MutableList<String> = ArrayList()
    private val creationQuery = "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, info string, source integer)"


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
            // create a database connection
            connection = DriverManager.getConnection(db_url)
            val statement = connection.createStatement()
            statement.queryTimeout = 30 // set timeout to 30 sec.

            //statement.executeUpdate("drop table if exists person");
            //statement.executeUpdate("create table person (id integer, name string)");
            //statement.executeUpdate("insert into person values(1, 'leo')");
            //statement.executeUpdate("insert into person values(2, 'yui')");
            val rs = statement.executeQuery(ArtistQuery)
            while (rs.next()) {
                // read the result set
                println("id = " + rs.getInt(ID))
                println("artist = " + rs.getString(ARTIST))
                println("info = " + rs.getString(INFO))
                println("source = " + rs.getString(SOURCE))
            }
        } catch (e: SQLException) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.message)
        } finally {
            try {
                connection?.close()
            } catch (e: SQLException) {
                // connection close failed.
                System.err.println(e)
            }
        }
    }

    fun saveArtist(dbHelper: DataBase, artist: String, info: String) {
        // Gets the data repository in write mode
        val writableDatabase = dbHelper.writableDatabase

        // Create a new map of values, where column names are the keys
        val artistValues = getArtistValues(artist, info)

        // Insert the new row, returning the primary key value of the new row
        writableDatabase.insert(TABLE_ARTISTS, null, artistValues)
    }

    private fun getArtistValues(artist: String, info: String): ContentValues {
        val values = ContentValues()
        values.put(ARTIST, artist)
        values.put(INFO, info)
        values.put(SOURCE, 1)
        return values
    }

    fun getInfo(dbHelper: DataBase, artist: String): String? {
        val readableDatabase = dbHelper.readableDatabase

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        val databaseColumns = arrayOf(
            ID,
            ARTIST,
            INFO
        )

        // Filter results WHERE "title" = 'My Title'
        val artistValues = arrayOf(artist)
        val cursor = readableDatabase.query(
            TABLE_ARTISTS,  // The table to query
            databaseColumns,  // The array of columns to return (pass null to get all)
            artistColumn,  // The columns for the WHERE clause
            artistValues,  // The values for the WHERE clause
            null,  // don't group the rows
            null,  // don't filter by row groups
            resultSetSortOrder // The sort order
        )
        insertInItems(cursor)
        cursor.close()
        return if (items.isEmpty()) {
            null
        } else {
            items[0]
        }
    }

    private fun insertInItems(cursor: Cursor) {
        var info = ""
        while (cursor.moveToNext()) {
            info = cursor.getString(cursor.getColumnIndexOrThrow(INFO))
            items.add(info)
        }
    }
}