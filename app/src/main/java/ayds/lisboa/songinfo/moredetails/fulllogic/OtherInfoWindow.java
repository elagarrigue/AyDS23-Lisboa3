package ayds.lisboa.songinfo.moredetails.fulllogic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
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
  private final static String JSON = "JSON";
  private final static String JSON_ARTIST = "artist";
  private final static String JSON_BIO = "bio";
  private final static String JSON_CONTENT = "content";
  private final static String JSON_URL = "url";
  private final static String LAST_FM_API_BASE_URL = "https://ws.audioscrobbler.com/2.0/";
  private final static String DB_SAVE_SYMBOL = "[*]";
  private final static String LAST_FM_DEFAULT_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png";
  private final static String HTML_WIDTH = "<html><div width=400>";
  private final static String HTML_FONT = "<font face=\"arial\">";
  private final static String HTML_FINALS = "</font></div></html>";
  private final static String NO_RESULTS = "No Results";

  private String artistInfo;
  private String artistName;
  private TextView view;
  private Retrofit retrofit;
  private LastFMAPI lastFMAPI;
  private DataBase dataBase;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initContentView();
    initView();
    initDatabase();
    initRetrofit();
    initLastFMAPI();
    open();
  }

  private void initContentView() {
    setContentView(R.layout.activity_other_info);
  }

  private void initView() {
    view = findViewById(R.id.textPane2);
  }

  private void initDatabase() {
    dataBase = new DataBase(this);
  }
  private void initRetrofit() {
    Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
    retrofitBuilder.baseUrl(LAST_FM_API_BASE_URL);
    retrofitBuilder.addConverterFactory(ScalarsConverterFactory.create());
    retrofit = retrofitBuilder.build();
  }
  private void initLastFMAPI() {
    lastFMAPI = retrofit.create(LastFMAPI.class);
  }

  private void open() {
    getArtistName();
    getArtistInfo();
  }

  private void getArtistName() {
    artistName = getIntent().getStringExtra(ARTIST_NAME_EXTRA);
  }

  public void getArtistInfo() {
    Log.e(TAG,ARTIST_NAME_EXTRA + " " + artistName);

    new Thread(() -> {
      getArtistInfoDB();
      if (existArtistInfoDB()) {
        markArtistInfoAsSavedDB();
      } else {
        getFromService();
      }
      update();
    }).start();
  }

  private void getArtistInfoDB(){
    artistInfo = DataBase.getInfo(dataBase, artistName);
  }

  private boolean existArtistInfoDB() {
    return artistInfo != null;
  }

  private void markArtistInfoAsSavedDB() {
    artistInfo = DB_SAVE_SYMBOL + artistInfo;
  }

  private void getFromService(){
    JsonObject callResponseJson;

    try {
      callResponseJson = getJSONResponse();
      setBioContent(callResponseJson);
      setURL(callResponseJson);
    } catch (IOException ioException) {
      Log.e(TAG, "Error " + ioException);
      ioException.printStackTrace();
    }
  }

  private void setBioContent(JsonObject callResponseJson) {
    JsonElement bioContent = getBioContent(callResponseJson);

    if (bioContent == null) {
      artistInfo = NO_RESULTS;
    } else {
      String lineBreakJSON = "\\n";
      String lineBreak = "\n";
      artistInfo = bioContent.getAsString().replace(lineBreakJSON, lineBreak);
      artistInfo = textToHtml(artistInfo, artistName);
      saveArtistInfoDB();
    }
  }

  private void setURL(JsonObject callResponseJson) {
    JsonElement url = getURL(callResponseJson);
    String urlString = url.getAsString();

    findViewById(R.id.openUrlButton).setOnClickListener(v -> {
      Intent intent = new Intent(Intent.ACTION_VIEW);
      intent.setData(Uri.parse(urlString));
      startActivity(intent);
    });
  }

  private JsonObject getJSONResponse() throws IOException {
    Response<String> callResponse = lastFMAPI.getArtistInfo(artistName).execute();
    Gson gson = new Gson();
    JsonObject result = gson.fromJson(callResponse.body(), JsonObject.class);

    Log.e(TAG,JSON + " " + callResponse.body());

    return result;
  }

  private JsonElement getBioContent(JsonObject callResponseJson) {
    JsonObject artist = callResponseJson.get(JSON_ARTIST).getAsJsonObject();
    JsonObject bio = artist.get(JSON_BIO).getAsJsonObject();
    JsonElement content = bio.get(JSON_CONTENT);

    return content;
  }

  private JsonElement getURL(JsonObject callResponseJson) {
    JsonObject artist = callResponseJson.get(JSON_ARTIST).getAsJsonObject();
    JsonElement url = artist.get(JSON_URL);

    return url;
  }

  private void saveArtistInfoDB() {
    DataBase.saveArtist(dataBase, artistName, artistInfo);
  }

  private void update() {
    Log.e(TAG,"Get Image from " + LAST_FM_DEFAULT_IMAGE);

    runOnUiThread( () -> {
      setDefaultImage();
      setArtistInfo();
    });
  }

  private void setDefaultImage() {
    int imageViewID = R.id.imageView;
    ImageView imageView = findViewById(imageViewID);
    Picasso.get().load(LAST_FM_DEFAULT_IMAGE).into(imageView);
  }

  private void setArtistInfo() {
    view.setText(Html.fromHtml(artistInfo));
  }

  public static String textToHtml(String text, String term) {
    StringBuilder builder = new StringBuilder();
    builder.append(HTML_WIDTH);
    builder.append(HTML_FONT);
    builder.append(getTextWithBold(text, term));
    builder.append(HTML_FINALS);

    return builder.toString();
  }

  private static String getTextWithBold(String text, String term) {
    String textWithBold = text;
    String singleQuote = "'";
    String space = " ";
    String lineBreak = "\n";
    String lineBreakHTML = "<br>";
    String caseInsensitive = "(?i)";
    String startBoldLabel = "<b>";
    String finishBoldLabel = "</b>";

    textWithBold.replace(singleQuote, space);
    textWithBold.replace(lineBreak, lineBreakHTML);
    textWithBold.replaceAll(caseInsensitive + term,  startBoldLabel + term.toUpperCase() + finishBoldLabel);

    return textWithBold;
  }

}
