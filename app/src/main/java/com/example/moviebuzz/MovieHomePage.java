package com.example.moviebuzz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviebuzz.Adapters.AutoSlideAdapter;
import com.example.moviebuzz.Adapters.MovieAdapter;
import com.example.moviebuzz.Model.MovieModel;
import com.example.moviebuzz.Model.SlideItem;
import com.opencsv.CSVReader;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MovieHomePage extends Fragment implements MovieAdapter.onClickListener, MovieAdapter.onClickListen ,
AutoSlideAdapter.OnSlideClickListener{


    RecyclerView recyclerView, recyclerView2;
    String movieId, movieTitle, moviePoster, movieTagline, movieReleased, movieDuration, movieIMDBRating,
            moviePlot, movieOverview, movieGenre, movieActors, movieWriters, movieProduction, movieAwards, movieBoxOffice;
    List<SlideItem> moviePosters;
    MovieAdapter adapter, adapter2;
    Thread thread1, thread2, thread3;
    private static final String TAG = "MovieHomePage";
    List<MovieModel> movieList;
    List<MovieModel> movieList2;
    List<MovieModel> movieList3;
    Toolbar toolbar;
    List<SlideItem> slideItems;
    ViewPager2 viewPager2;
    MovieModel movieModel;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    AutoSlideAdapter slideAdapter;
    SliderView sliderView;
    String[][] dataArr;
    ProgressDialog mProgressDialog;
    public MovieHomePage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_home_page, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView2 = view.findViewById(R.id.recyclerView2);
        //  viewPager2 = view.findViewById(R.id.slider);
        toolbar = view.findViewById(R.id.toolbar);
        sliderView = view.findViewById(R.id.sliderView);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        AppCompatActivity actionBar = (AppCompatActivity) getActivity();
        actionBar.setSupportActionBar(toolbar);

        DrawerLayout drawer = actionBar.findViewById(R.id.draw_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        moviePosters = new ArrayList<>();
        slideItems = new ArrayList<>();
        List<String[]> list = null;
        movieList = new ArrayList<>();
        movieList2 = new ArrayList<>();
        movieList3 = new ArrayList<>();


        CSVReader csvReader = new CSVReader(new InputStreamReader(getResources().openRawResource(R.raw.movies)));

        try {
            list = csvReader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dataArr = new String[list.size()][];
        dataArr = list.toArray(dataArr);

         mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setTitle("Loading Data");
        mProgressDialog.setMessage("Please wait.");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int num = 1; num <= 10; num++) {
                    movieId = dataArr[num][0];
                    movieTitle = dataArr[num][1];
                    movieTagline = dataArr[num][2];
                    movieOverview = dataArr[num][3];
                    movieGenre = dataArr[num][4];

                    getData(movieId, movieTagline, movieOverview, movieList, 1);
                }
                Log.d(TAG, "run1: " + thread1.getName());

            }
        });
        thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int num = 11; num <= 20; num++) {
                    movieId = dataArr[num][0];
                    movieTitle = dataArr[num][1];
                    movieTagline = dataArr[num][2];
                    movieOverview = dataArr[num][3];
                    movieGenre = dataArr[num][4];

                    getData(movieId, movieTagline, movieOverview, movieList2, 2);
                }
                Log.d(TAG, "run2: " + thread2.getName());

            }
        });
        thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int num = 21; num <= 30; num++) {
                    movieId = dataArr[num][0];
                    movieTitle = dataArr[num][1];
                    movieTagline = dataArr[num][2];
                    movieOverview = dataArr[num][3];
                    movieGenre = dataArr[num][4];
                    getData(movieId, movieTagline, movieOverview, movieList3, 3);
                }
                Log.d(TAG, "run3: " + thread3.getName());

            }
        });
        thread1.start();
        thread3.start();
        thread2.start();

        setAdapter();



        return view;
    }

    public void setAdapter() {
        adapter = new MovieAdapter(getContext(), movieList, this, this::onClick);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        adapter2 = new MovieAdapter(getContext(), movieList2, this, this::onClick2);
        recyclerView2.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(adapter2);
        

        slideAdapter = new AutoSlideAdapter(moviePosters, getContext(), movieList3,this::onSlideClick);
        sliderView.setSliderAdapter(slideAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
    }

    @Override
    public void onClick(int position) {
        getAllMovieData(position, movieList);
    }

    @Override
    public void onClick2(int position) {
        getAllMovieData(position, movieList2);
    }

    @Override
    public void onSlideClick(int position) {
        getAllMovieData(position, movieList3);
    }

    private void getData(String url, String tagline, String overview, List<MovieModel> moviesList, int counter) {
        requestQueue = Volley.newRequestQueue(getContext());
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://www.omdbapi.com/?i=" + url + "&apikey=cfdb5a43", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    getCompleteMovieData(response, url, tagline, overview);
                    //  slideItems.add(new SlideItem(moviePoster));

                    moviesList.add(movieModel);
                    //  viewPager2.setAdapter(new SliderAdapter(getContext(), slideItems, movieHomePage.this::onSlideClick));
                    if (counter == 1)
                        adapter.notifyDataSetChanged();
                    if (counter == 2)
                        adapter2.notifyDataSetChanged();
                    if (counter == 3) {
                        moviePosters.add(new SlideItem(moviePoster));
                        slideAdapter.notifyDataSetChanged();
                    }
                    mProgressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);

    }


    public void getCompleteMovieData(JSONObject response, String url, String tagline, String overview) {
        try {
            movieTitle = response.getString("Title");
            moviePoster = response.getString("Poster");
            movieReleased = response.getString("Released");
            movieTagline = tagline;
            movieOverview = overview;
            moviePlot = response.getString("Plot");
            movieGenre = response.getString("Genre");
            movieActors = response.getString("Actors");
            movieWriters = response.getString("Writer");
            movieProduction = response.getString("Production");
            movieAwards = response.getString("Awards");
            movieIMDBRating = response.getString("imdbRating");
            movieDuration = response.getString("Runtime");
            movieBoxOffice = response.getString("BoxOffice");
            //String rotten[2] = response.getJSONArray("Ratings");
            movieModel = new MovieModel(movieTitle, movieTagline, movieOverview, moviePoster, url, movieReleased, moviePlot,
                    movieGenre, movieActors, movieDuration, movieWriters, movieProduction, movieAwards, movieIMDBRating, movieBoxOffice);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getAllMovieData(int position, List<MovieModel> movieList) {
        String movieTitle = movieList.get(position).getTitle();
        String movieTagline = movieList.get(position).getTagline();
        String movieOverview = movieList.get(position).getOverview();
        String moviePoster = movieList.get(position).getPoster_path();
        String movieId = movieList.get(position).getMovieId();
        String moviePlot = movieList.get(position).getPlot();
        String movieGenre = movieList.get(position).getGenre();
        String movieActors = movieList.get(position).getActors();
        String movieWriters = movieList.get(position).getWriters();
        String movieProduction = movieList.get(position).getProduction();
        String movieAwards = movieList.get(position).getAwards();
        String movieIMDBRating = movieList.get(position).getImdbRatings();
        String movieDuration = movieList.get(position).getDuration();
        String movieReleased = movieList.get(position).getReleased();
        String movieBoxOffice = movieList.get(position).getBoxOffice();
        intentData(movieTitle, moviePoster, movieTagline, movieReleased, movieDuration, movieIMDBRating,
                moviePlot, movieOverview, movieGenre, movieActors, movieWriters, movieProduction, movieAwards, movieBoxOffice, movieId);
    }


    public void intentData(String movieTitle, String moviePoster, String movieTagline, String movieReleased, String movieDuration, String movieIMDBRating,
                           String moviePlot, String movieOverview, String movieGenre, String movieActors, String movieWriters,
                           String movieProduction, String movieAwards, String movieBoxOffice, String movieId) {
        Intent intent = new Intent(getContext(), MovieInfo.class);
        intent.putExtra("MovieTitle", movieTitle);
        intent.putExtra("MovieTagline", movieTagline);
        intent.putExtra("MovieOverview", movieOverview);
        intent.putExtra("MoviePoster", moviePoster);
        intent.putExtra("MovieId", movieId);
        intent.putExtra("MoviePlot", moviePlot);
        intent.putExtra("MovieGenre", movieGenre);
        intent.putExtra("MovieActors", movieActors);
        intent.putExtra("MovieWriters", movieWriters);
        intent.putExtra("MovieProduction", movieProduction);
        intent.putExtra("MovieAwards", movieAwards);
        intent.putExtra("MovieIMDBRatings", movieIMDBRating);
        intent.putExtra("MovieDuration", movieDuration);
        intent.putExtra("MovieReleased", movieReleased);
        intent.putExtra("MovieBoxOffice", movieBoxOffice);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        sliderView.stopAutoCycle();
        super.onPause();
    }

}