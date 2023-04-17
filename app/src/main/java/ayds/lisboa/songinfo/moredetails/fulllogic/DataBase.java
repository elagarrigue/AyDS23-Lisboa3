package ayds.lisboa.songinfo.moredetails.fulllogic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {

  private static final String db_url = "jdbc:sqlite:./dictionary.db";
  private static final String ArtistQuery = "select * from artists";
  private static final String ID = "id";
  private static final String ARTIST = "artist";
  private static final String INFO = "info";
  private static final String SOURCE = "source";
  private static final String TABLE_ARTISTS = "artists";

  public static void testDB() {

    Connection connection = null;
    try {
      // create a database connection
      connection = DriverManager.getConnection(db_url);
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);  // set timeout to 30 sec.

      //statement.executeUpdate("drop table if exists person");
      //statement.executeUpdate("create table person (id integer, name string)");
      //statement.executeUpdate("insert into person values(1, 'leo')");
      //statement.executeUpdate("insert into person values(2, 'yui')");
      ResultSet rs = statement.executeQuery(ArtistQuery);
      while(rs.next()) {
        // read the result set
        System.out.println("id = " + rs.getInt(ID));
        System.out.println("artist = " + rs.getString(ARTIST));
        System.out.println("info = " + rs.getString(INFO));
        System.out.println("source = " + rs.getString(SOURCE));

      }
    }
    catch(SQLException e) {
      // if the error message is "out of memory",
      // it probably means no database file is found
      System.err.println(e.getMessage());
    }
    finally {
      try {
        if(connection != null)
          connection.close();
      }
      catch(SQLException e) {
        // connection close failed.
        System.err.println(e);
      }
    }
  }

  public static void saveArtist(DataBase dbHelper, String artist, String info) {
    // Gets the data repository in write mode
    SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
    ContentValues artistValues = getArtistValues(artist,info);

// Insert the new row, returning the primary key value of the new row
    writableDatabase.insert(TABLE_ARTISTS, null,artistValues);
  }

 protected static ContentValues getArtistValues(String artist, String info){
    ContentValues values = new ContentValues();
    values.put(ARTIST, artist);
    values.put(INFO, info);
    values.put(SOURCE, 1);
    return values;
  }

  public static String getInfo(DataBase dbHelper, String artist) {

    SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
    String[] projection = {
            ID,
            ARTIST,
            INFO
    };

// Filter results WHERE "title" = 'My Title'
    String selection = "artist  = ?";
    String[] selectionArgs = { artist };

// How you want the results sorted in the resulting Cursor
    String sortOrder = "artist DESC";

    Cursor cursor = readableDatabase.query(
            "artists",   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
    );

    List<String> items = new ArrayList<String>();
    while(cursor.moveToNext()) {
      String info = cursor.getString(
              cursor.getColumnIndexOrThrow("info"));
      items.add(info);
    }
    cursor.close();

    if(items.isEmpty()) return null;
    else return items.get(0);
  }

  public DataBase(Context context) {
    super(context, "dictionary.db", null, 1);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(
            "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, info string, source integer)");

    Log.i("DB", "DB created");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }
}
