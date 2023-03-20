package ayds.lisboa.songinfo.moredetails.fulllogic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Html;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ayds.lisboa.songinfo.R;
import ayds.lisboa.songinfo.home.model.entities.Song;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;


import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;



public class OtherInfoWindow extends AppCompatActivity {

  public final static String ARTIST_NAME_EXTRA = "artistName";

  private TextView textPane2;
  //private JPanel imagePanel;
 // private JLabel posterImageLabel;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_other_info);

    textPane2 = findViewById(R.id.textPane2);


    open(getIntent().getStringExtra("artistName"));
  }

  public void getARtistInfo(String artistName) {

    // create
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

    LastFMAPI lastFMAPI = retrofit.create(LastFMAPI.class);

    Log.e("TAG","artistName " + artistName);

        new Thread(new Runnable() {
          @Override
          public void run() {

            String text = DataBase.getInfo(dataBase, artistName);


            if (text != null) { // exists in db

              text = "[*]" + text;
            } else { // get from service
              Response<String> callResponse;
              try {
                callResponse = lastFMAPI.getArtistInfo(artistName).execute();

                Log.e("TAG","JSON " + callResponse.body());

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
                Log.e("TAG", "Error " + e1);
                e1.printStackTrace();
              }
            }


            String imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png";

            Log.e("TAG","Get Image from " + imageUrl);



            final String finalText = text;

            runOnUiThread( () -> {
              Picasso.get().load(imageUrl).into((ImageView) findViewById(R.id.imageView));


              textPane2.setText(Html.fromHtml( finalText));


            });



          }
        }).start();

  }

  private DataBase dataBase = null;

  private void open(String artist) {


    dataBase = new DataBase(this);

    DataBase.saveArtist(dataBase, "test", "sarasa");


    Log.e("TAG", ""+ DataBase.getInfo(dataBase,"test"));
    Log.e("TAG",""+ DataBase.getInfo(dataBase,"nada"));

    getARtistInfo(artist);
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
