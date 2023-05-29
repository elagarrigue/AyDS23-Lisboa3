package ayds.lisboa.songinfo.moredetails.data.local

const val ID = "id"
const val ARTIST = "artist"
const val DESCRIPTION = "description"
const val INFO_URL = "info_url"
const val SOURCE = "source"
const val SOURCE_LOGO = "source_logo"
const val TABLE_ARTISTS = "artists"
const val RESULT_SET_ORDER = "artist DESC"
const val ARTIST_COLUMN = "artist  = ?"
const val DB_NAME = "dictionary.db"
const val CREATE_ARTIST_INFO_TABLE: String =
    "create table $TABLE_ARTISTS (" +
            "$ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$ARTIST string, " +
            "$DESCRIPTION string, " +
            "$INFO_URL string, " +
            "$SOURCE string, " +
            "$SOURCE_LOGO int)"