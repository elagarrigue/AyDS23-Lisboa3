package ayds.lisboa.songinfo.moredetails.fulllogic;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LastFMAPI {

  @GET("?method=artist.getinfo&api_key=0a657854db69e551c97d541ca9ebcef4&format=json")
  Call<String> getArtistInfo(@Query("artist") String artist);

}
