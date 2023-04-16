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
  private final static String ARTIST_NAME_EXTRA = "artistName";
  private final static String TAG = "tag";
  private TextView view;
  private String text;
  private final static int layout = R.layout.activity_other_info;
  private final static int textPaneID = R.id.textPane2;
  private final static String retrofitBaseURL = "https://ws.audioscrobbler.com/2.0/";

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
        saveInDb();
      }
      else {
        getFromService(lastFMAPI);
        }


        String imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png";

        Log.e(TAG,"Get Image from " + imageUrl);



        final String finalText = text;

        runOnUiThread( () -> {
          Picasso.get().load(imageUrl).into((ImageView) findViewById(R.id.imageView));


          view.setText(Html.fromHtml( finalText));


        });



      }).start();

  }

  private void getFromService(LastFMAPI lastFMAPI){
    Response<String> callResponse;
    try {
      callResponse = lastFMAPI.getArtistInfo(artistName).execute();

      Log.e(TAG,"JSON " + callResponse.body());

      Gson gson = new Gson();
      JsonObject jobj = gson.fromJson(callResponse.body(), JsonObject.class);
      JsonObject artist = jobj.get("artist").getAsJsonObject();
      JsonObject bio = artist.get("bio").getAsJsonObject();
      JsonElement extract = bio.get("content");
      JsonElement url = artist.get("url");


      if (extract == null) {
        text = "No Results";
      } else {
        text = extract.getAsString().replace("\\n", "\n");

        text = textToHtml(text, artistName);


        // save to DB  <o/

        DataBase.saveArtist(dataBase, artistName, text);
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

    } catch (IOException e1) {
      Log.e(TAG, "Error " + e1);
      e1.printStackTrace();
    }
  }

  private void saveInDb() {
    text  = "[*]" + text;
  }
  private boolean existInDb() {
    return text != null;
  }

  private void getArtistInfoDb(String artistName){
    text =  DataBase.getInfo(dataBase, artistName);
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

  private DataBase dataBase = null;

  private void open(String artist) {


    dataBase = new DataBase(this);

    DataBase.saveArtist(dataBase, "test", "sarasa");


    Log.e("TAG", ""+ DataBase.getInfo(dataBase,"test"));
    Log.e("TAG",""+ DataBase.getInfo(dataBase,"nada"));

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
