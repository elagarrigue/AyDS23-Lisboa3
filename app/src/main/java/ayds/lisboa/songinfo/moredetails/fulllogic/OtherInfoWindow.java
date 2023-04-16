package ayds.lisboa.songinfo.moredetails.fulllogic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ayds.lisboa.songinfo.R;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class OtherInfoWindow extends AppCompatActivity {
  public final static String ARTIST_NAME_EXTRA = "artistName";
  private final static String TAG = "tag";
  private TextView view;
  private final static int layout = R.layout.activity_other_info;
  private final static int textPaneID = R.id.textPane2;
  private final static int imageViewID = R.id.imageView;
  private final static String retrofitBaseURL = "https://ws.audioscrobbler.com/2.0/";
  private String artistInfoText;
  private final static String DBSaveSymbol = "[*]";
  private final static String lastFMDefaultImage = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png";

  private final static String JSON = "JSON";
  private final static String JSONArtist = "artist";
  private final static String JSONBio = "bio";
  private final static String JSONContent = "content";
  private final static String JSONUrl = "url";
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(layout);
    view = findViewById(textPaneID);
    open(getIntent().getStringExtra(ARTIST_NAME_EXTRA));
  }

  public void getArtistInfo(String artistName) {
    Retrofit retrofit = createRetrofit();
    LastFMAPI lastFMAPI = createLastFMAPI(retrofit);

    Log.e(TAG,ARTIST_NAME_EXTRA + " " + artistName);

    new Thread(() -> {
      getArtistInfoDb(artistName);

      if (existInDb()) {
        markAsSavedDB();
      } else {
        getFromService(lastFMAPI, artistName);
      }

      Log.e(TAG,"Get Image from " + lastFMDefaultImage);

      runOnUiThread( () -> {
        ImageView imageView = findViewById(imageViewID);
        Picasso.get().load(lastFMDefaultImage).into(imageView);
        view.setText(Html.fromHtml(artistInfoText));
      });

    }).start();
  }

  private Retrofit createRetrofit() {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(retrofitBaseURL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

    return retrofit;
  }
  private LastFMAPI createLastFMAPI(Retrofit retrofit) {
    Class<LastFMAPI> type = LastFMAPI.class;
    return retrofit.create(type);
  }
  private void getArtistInfoDb(String artistName){
    artistInfoText = DataBase.getInfo(dataBase, artistName);
  }
  private boolean existInDb() {
    return artistInfoText != null;
  }
  private void markAsSavedDB() {
    artistInfoText = DBSaveSymbol + artistInfoText;
  }
  private void getFromService(LastFMAPI lastFMAPI, String artistName){
    Response<String> callResponse;

    try {
      callResponse = lastFMAPI.getArtistInfo(artistName).execute();

      Log.e(TAG,JSON + " " + callResponse.body());

      Gson gson = new Gson();
      JsonObject jsonObject = gson.fromJson(callResponse.body(), JsonObject.class);
      JsonObject artist = jsonObject.get(JSONArtist).getAsJsonObject();
      JsonObject bio = artist.get(JSONBio).getAsJsonObject();
      JsonElement extract = bio.get(JSONContent);
      JsonElement url = artist.get(JSONUrl);

      if (extract == null) {
        artistInfoText = "No Results";
      } else {
        artistInfoText = extract.getAsString().replace("\\n", "\n");
        artistInfoText = textToHtml(artistInfoText, artistName);
        // save to DB  <o/
        DataBase.saveArtist(dataBase, artistName, artistInfoText);
      }


      final String urlString = url.getAsString();
      findViewById(R.id.openUrlButton).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(Intent.ACTION_VIEW);
          intent.setData(Uri.parse(urlString));
          startActivity(intent);
        }
      });

    } catch (IOException ioException) {
      Log.e(TAG, "Error " + ioException);
      ioException.printStackTrace();
    }
  }

  private DataBase dataBase = null;

  private void open(String artist) {
    dataBase = new DataBase(this);

    DataBase.saveArtist(dataBase, "test", "sarasa");

    Log.e(TAG, ""+ DataBase.getInfo(dataBase,"test"));
    Log.e(TAG,""+ DataBase.getInfo(dataBase,"nada"));

    getArtistInfo(artist);
  }

  public static String textToHtml(String text, String term) {
    StringBuilder builder = new StringBuilder();

    builder.append("<html><div width=400>");
    builder.append("<font face=\"arial\">");

    String textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replaceAll("(?i)" + term, "<b>" + term.toUpperCase() + "</b>");

    builder.append(textWithBold);

    builder.append("</font></div></html>");

    return builder.toString();
  }

}
