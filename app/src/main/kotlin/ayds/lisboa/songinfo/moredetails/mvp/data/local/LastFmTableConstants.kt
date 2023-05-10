package ayds.lisboa.songinfo.moredetails.mvp.data.local

const val ID = "id"
const val ARTIST = "artist"
const val BIO_CONTENT = "bio_content"
const val URL = "url"
const val SOURCE = "source"
const val TABLE_ARTISTS = "artists"
const val RESULT_SET_ORDER = "artist DESC"
const val ARTIST_COLUMN = "artist  = ?"
const val DB_NAME = "dictionary.db"
const val CREATE_ARTIST_INFO_TABLE: String =
    "create table $TABLE_ARTISTS (" +
            "$ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$ARTIST string, " +
            "$BIO_CONTENT string, " +
            "$URL string, " +
            "$SOURCE integer)"