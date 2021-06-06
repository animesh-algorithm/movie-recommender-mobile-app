package com.example.moviebuzz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviebuzz.Adapters.CategoryAdapter;
import com.example.moviebuzz.Model.CategoryModel;
import com.opencsv.CSVReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class CategoryPage extends Fragment implements CategoryAdapter.onClickListener {


    CategoryAdapter adapter;
    RecyclerView recyclerView;
    CategoryModel model;
    Toolbar toolbar;
    TextView movieCategory;
    String movieType, movieSearch;
    List<String> allMovies;
    String[][] dataArr;
    RequestQueue requestQueue;
    private static final String TAG = "CategoryPage";
    JsonObjectRequest jsonObjectRequest;
    MovieHomePage page;
    String movieId, movieTitle, moviePoster, movieTagline, movieReleased, movieDuration, movieIMDBRating,
            moviePlot, movieOverview, movieGenre, movieActors, movieWriters, movieProduction, movieAwards,
            movieBoxOffice;
    List<CategoryModel> movieList;
    ProgressDialog mProgressDialog;
    public CategoryPage(String movieType) {
        // Required empty public constructor
        this.movieType = movieType;
    }

    public CategoryPage() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_page, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCategory);
        toolbar = view.findViewById(R.id.toolbar);
        movieCategory = view.findViewById(R.id.note_text_title);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        AppCompatActivity actionBar = (AppCompatActivity) getActivity();
        actionBar.setSupportActionBar(toolbar);


        DrawerLayout drawer = actionBar.findViewById(R.id.draw_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (movieType.equals("Search Movies")) {
            setHasOptionsMenu(true);
        }
        movieCategory.setText(movieType);
        movieList = new ArrayList<>();

        List<String[]> list = null;
        allMovies = new ArrayList<>();
        CSVReader csvReader = new CSVReader(new InputStreamReader(getResources().openRawResource(R.raw.movies)));

        try {
            list = csvReader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dataArr = new String[list.size()][];
        dataArr = list.toArray(dataArr);


        if (movieType != "Search Movies") {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setTitle("Loading Data");
            mProgressDialog.setMessage("Please wait.");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            int counter = 0;
            Loop:
            for (int num = 1; num <= 30000; num++) {
                Log.d(TAG, "Search movies not executed: ");
                movieGenre = dataArr[num][4];
                if (movieType != null)
                    if (movieGenre.contains(movieType)) {
                        counter++;
                        movieId = dataArr[num][0];
                        movieTitle = dataArr[num][1];
                        movieTagline = dataArr[num][2];
                        movieOverview = dataArr[num][3];
                        getData(movieId, movieTagline, movieOverview);
                        if (counter == 10)
                            break Loop;
                    }
            }

        }
        // Inflate the layout for this fragment
        setAdapter();
        return view;
    }

    public void setAdapter() {
        adapter = new CategoryAdapter(getContext(), movieList, this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(int position) {
        getAllMovieData(position);
    }

    private void getData(String url, String tagline, String overview) {
        requestQueue = Volley.newRequestQueue(getContext());
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://www.omdbapi.com/?i=" + url + "&apikey=cfdb5a43", null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(JSONObject response) {
                try {
                    getCompleteMovieData(response, url, tagline, overview);
                    CategoryModel matched = movieList.stream().filter(model -> model.getMovieId().equals(url))
                            .findFirst().orElse(null); // first occurrence
                    if (matched == null)
                        movieList.add(model);
                    adapter.notifyDataSetChanged();
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
            model = new CategoryModel(movieTitle, movieTagline, movieOverview, moviePoster, url, movieReleased, moviePlot,
                    movieGenre, movieActors, movieDuration, movieWriters, movieProduction, movieAwards, movieIMDBRating, movieBoxOffice);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getAllMovieData(int position) {
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.mainmenu, menu);
        final MenuItem searchItem = menu.findItem(R.id.search_menu);

        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setIconified(false);
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                     mProgressDialog = new ProgressDialog(getContext());
                    mProgressDialog.setTitle("Loading Data");
                    mProgressDialog.setMessage("Please wait.");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();

                    int counter = 0;
                    movieList.clear();
                    adapter.notifyDataSetChanged();
                    Loop:
                    for (int num = 1; num <= 30000; num++) {
                        Log.d(TAG, "Search movies not executed: ");
                        movieGenre = dataArr[num][1];
                        if (movieType.equals("Search Movies"))
                        { if (movieGenre.toLowerCase().contains(query.toLowerCase())) {
                                counter++;
                                movieId = dataArr[num][0];
                                movieTitle = dataArr[num][1];
                                movieTagline = dataArr[num][2];
                                movieOverview = dataArr[num][3];
                                getData(movieId, movieTagline, movieOverview);
                                adapter.notifyDataSetChanged();
                                if (counter == 20) {

                                    break Loop;

                                }
                            }
                        }
                        if(num==30000 && !movieGenre.toLowerCase().contains(query.toLowerCase()))
                        {
                            Toast.makeText(getContext(),"Movie not found",Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }
                    }

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }


            });
        }

    }
}