package com.example.moviebuzz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.android.volley.toolbox.HttpResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class BackGroundTask  extends AsyncTask<String, Void,String> {
    Context context;
    MovieInfo info;
    Button watchTrailer;
    String uri,URL;
    private static final String TAG = "BackGroundTask";
    public BackGroundTask(Context context,MovieInfo info,Button watchTrailer) {
        this.context = context;
        this.info = info;
        this.watchTrailer = watchTrailer;
    }

    protected String doInBackground(String... urls) {
        try {
            String url = urls[0];
            Document document = Jsoup.connect(url).get();
             Elements data = document.getElementsByClass("slate");
             uri =  data.select("a").attr("href");
            Log.d(TAG, "doInBackground: "+uri);
        } catch (IOException e) {
            Log.d(TAG, "Some Error Occured ");
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        info.getUri(uri);
        watchTrailer.setVisibility(View.VISIBLE);
    }

}
