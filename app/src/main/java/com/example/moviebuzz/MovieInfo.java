package com.example.moviebuzz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.moviebuzz.Adapters.CommentAdapter;
import com.example.moviebuzz.Adapters.MovieAdapter;
import com.example.moviebuzz.Model.CommentModel;
import com.example.moviebuzz.Model.MovieModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opencsv.CSVReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MovieInfo extends AppCompatActivity implements MovieAdapter.onClickListener, MovieAdapter.onClickListen {
    String movieId, movieTitle, moviePoster, movieTagline, movieReleased, movieDuration,
            moviePlot, movieOverview, movieGenre, movieActors, movieWriters, movieProduction, movieAwards, uri, movieIMDBRatings, movieBoxOffice;
    String movieId2, movieTitle2, moviePoster2, movieTagline2, movieReleased2, movieDuration2,
            moviePlot2, movieOverview2, movieGenre2, movieActors2, movieWriters2, movieProduction2, movieAwards2, uri2, movieIMDBRating2, movieBoxOffice2;
    String movieIMDBRating;
    String userId;
    ImageView imageView;
    TextView showMore, showLess, ratingValue, rateIt;
    CommentAdapter adapter;
    RecyclerView recyclerViewMovies;
    MovieAdapter movieAdapter;
    EditText editText;
    RatingBar ratingBar;
    ConstraintLayout expandableInfo;
    TextView textView, tagline, plot, overview, genre, actors, released, duration, writers, production, awards, imdbRating, boxOffice;
    Button watchTrailer, postComment;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference commentReference;
    List<CommentModel> commentList;
    List<MovieModel> movieList;
    RecyclerView recyclerView;
    GoogleSignInClient mGoogleSignInClient;
    NetworkChange networkChange = new NetworkChange();
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    MovieModel movieModel;

    private static final String TAG = "MovieInfo";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        imageView = findViewById(R.id.imageView2);
        textView = findViewById(R.id.movieTitle);
        watchTrailer = findViewById(R.id.watchTrailer);
        tagline = findViewById(R.id.tagline);
        plot = findViewById(R.id.plot);
        overview = findViewById(R.id.overview);
        genre = findViewById(R.id.genre);
        actors = findViewById(R.id.actors);
        released = findViewById(R.id.releaseDate);
        duration = findViewById(R.id.duration);
        writers = findViewById(R.id.writers);
        production = findViewById(R.id.production);
        awards = findViewById(R.id.awards);
        imdbRating = findViewById(R.id.imdbRating);
        boxOffice = findViewById(R.id.boxOffice);
        showMore = findViewById(R.id.showMore);
        expandableInfo = findViewById(R.id.expandableInfo);
        editText = findViewById(R.id.editText);
        postComment = findViewById(R.id.postComment);
        recyclerView = findViewById(R.id.recyclerView);
        showLess = findViewById(R.id.showLess);
        ratingBar = findViewById(R.id.ratingBar);
        ratingValue = findViewById(R.id.ratingValue);
        rateIt = findViewById(R.id.rateIt);
        recyclerViewMovies = findViewById(R.id.recyclerViewRecommendation);

        movieTitle = getIntent().getStringExtra("MovieTitle");
        movieTagline = getIntent().getStringExtra("MovieTagline");
        movieOverview = getIntent().getStringExtra("MovieOverview");
        moviePoster = getIntent().getStringExtra("MoviePoster");
        movieId = getIntent().getStringExtra("MovieId");
        movieReleased = getIntent().getStringExtra("MovieReleased");
        movieTagline = getIntent().getStringExtra("MovieTagline");
        moviePlot = getIntent().getStringExtra("MoviePlot");
        movieGenre = getIntent().getStringExtra("MovieGenre");
        movieActors = getIntent().getStringExtra("MovieActors");
        movieWriters = getIntent().getStringExtra("MovieWriters");
        movieProduction = getIntent().getStringExtra("MovieProduction");
        movieAwards = getIntent().getStringExtra("MovieAwards");
        movieIMDBRatings = getIntent().getStringExtra("MovieIMDBRatings");
        movieDuration = getIntent().getStringExtra("MovieDuration");
        movieBoxOffice = getIntent().getStringExtra("MovieBoxOffice");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = auth.getUid();
        database = FirebaseDatabase.getInstance();
        commentReference = database.getReference();
        commentList = new ArrayList<>();
        List<String[]> list = null;
        movieList = new ArrayList<>();


        CSVReader csvReader = new CSVReader(new InputStreamReader(getResources().openRawResource(R.raw.movies)));

        try {
            list = csvReader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[][] dataArr = new String[list.size()][];
        dataArr = list.toArray(dataArr);
        Random random = new Random();
        for (int num = 1; num <= 10; num++) {
            int number = 10 + random.nextInt(30000);
            movieId2 = dataArr[number][0];
            movieTitle2 = dataArr[number][1];
            movieTagline2 = dataArr[number][2];
            movieOverview2 = dataArr[number][3];
            movieGenre2 = dataArr[number][4];

            getData(movieId2, movieTagline2, movieOverview2);
        }

        String b = "<font color='#F44336'>Released Date:</font>";
        String c = "<font color='#F44336'>Plot:</font>";
        String d = "<font color='#F44336'>Genre:</font>";
        String e = "<font color='#F44336'>Actors:</font>";
        String f = "<font color='#F44336'>Writers:</font>";
        String g = "<font color='#F44336'>Production:</font>";
        String h = "<font color='#F44336'>Awards:</font>";
        String i = "<font color='#F44336'>IMDB Rating:</font>";
        String j = "<font color='#F44336'>Duration:</font>";
        String k = "<font color='#F44336'>Tagline:</font>";
        String l = "<font color='#F44336'>Overview:</font>";
        String m = "<font color='#F44336'>Box Office:</font>";

        textView.setText(movieTitle);
        released.setText(Html.fromHtml(b + " " + movieReleased));
        plot.setText(Html.fromHtml(c + " " + movieTitle));
        genre.setText(Html.fromHtml(d + " " + movieGenre));
        actors.setText(Html.fromHtml(e + " " + movieActors));
        writers.setText(Html.fromHtml(f + " " + movieWriters));
        production.setText(Html.fromHtml(g + " " + movieProduction));
        awards.setText(Html.fromHtml(h + " " + movieAwards));
        imdbRating.setText(Html.fromHtml(i + " " + movieIMDBRatings));
        duration.setText(Html.fromHtml(j + " " + movieDuration));
        tagline.setText(Html.fromHtml(k + " " + movieTagline));
        overview.setText(Html.fromHtml(l + " " + movieOverview));
        boxOffice.setText(Html.fromHtml(m + " " + movieBoxOffice));

        Glide.with(getApplicationContext()).load(moviePoster).into(imageView);

        BackGroundTask task = new BackGroundTask(this, this, watchTrailer);
        task.execute("https://www.imdb.com/title/" + movieId);


        rateIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    new AlertDialog.Builder(MovieInfo.this).
                            setIcon(android.R.drawable.ic_dialog_info).
                            setTitle("Sign In ").
                            setMessage("You must sign - in before rating any movie.").
                            setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    processRequest();
                                    processLogin();
                                }
                            }).setNegativeButton("Cancel", null).show();
                } else
                    ratingBar.setVisibility(View.VISIBLE);
            }
        });
        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    new AlertDialog.Builder(MovieInfo.this).
                            setIcon(android.R.drawable.ic_dialog_info).
                            setTitle("Sign In ").
                            setMessage("You must sign - in before posting any comment.").
                            setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    processRequest();
                                    processLogin();
                                }
                            }).setNegativeButton("Cancel", null).show();
                } else {
                    String comment_content = editText.getText().toString();
                    String userName = user.getDisplayName();
                    String userImage = user.getPhotoUrl().toString();
                    String userId = user.getUid();
                    CommentModel model = new CommentModel(comment_content, userId, userName, userImage);
                    if (editText.getText().toString().trim().length() != 0) {
                        commentReference.child("Comments").child(movieId).push().setValue(model)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        showMessage("Comment posted.");
                                        editText.setText(null);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                showMessage("Failed to post comment.");
                            }
                        });
                    }
                }
            }
        });
        setAdapter();
        loadComments();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                ratingValue.setText("User rating: " + rating + "/5");
                commentReference.child("Ratings").child(movieId).child(userId).child("Ratings").setValue(rating)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //  showMessage("Rated successfully.");

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("Failed to rate.");
                    }
                });
            }
        });
    }

    public void getUri(String url) {
        watchTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieInfo.this, TrailerScreen.class);
                intent.putExtra("Url", url);
                startActivity(intent);
            }
        });
    }


    public void onClickShowMore(View view) {
        expandableInfo.setVisibility(View.VISIBLE);
        showMore.setVisibility(View.GONE);
        editText.setVisibility(View.GONE);
        postComment.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    public void onClickShowLess(View view) {
        expandableInfo.setVisibility(View.GONE);
        showMore.setVisibility(View.VISIBLE);
        editText.setVisibility(View.VISIBLE);
        postComment.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void loadComments() {
        commentReference.child("Comments").child(movieId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    CommentModel model = snap.getValue(CommentModel.class);
                    commentList.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (userId != null)
            commentReference.child("Ratings").child(movieId).child(userId).child("Ratings").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String rating = "0";
                    if (snapshot.getValue() != null)
                        rating = snapshot.getValue().toString();
                    Log.d(TAG, "onDataChange: " + rating);
                    ratingValue.setText("User rating:" + rating + "/5");
                    float rate = Float.parseFloat(rating);
                    ratingBar.setRating(rate);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    public void setAdapter() {
        adapter = new CommentAdapter(getApplicationContext(), commentList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        movieAdapter = new MovieAdapter(this, movieList, this, this::onClick);
        recyclerViewMovies.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMovies.setLayoutManager(layoutManager2);
        recyclerViewMovies.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMovies.setAdapter(movieAdapter);
    }

    private void getData(String url, String tagline, String overview) {
        requestQueue = Volley.newRequestQueue(this);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://www.omdbapi.com/?i=" + url + "&apikey=cfdb5a43", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    getCompleteMovieData(response, url, tagline, overview);
                    movieList.add(movieModel);
                    movieAdapter.notifyDataSetChanged();
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
            movieTitle2 = response.getString("Title");
            moviePoster2 = response.getString("Poster");
            movieReleased2 = response.getString("Released");
            movieTagline2 = tagline;
            movieOverview2 = overview;
            moviePlot2 = response.getString("Plot");
            movieGenre2 = response.getString("Genre");
            movieActors2 = response.getString("Actors");
            movieWriters2 = response.getString("Writer");
            movieProduction2 = response.getString("Production");
            movieAwards2 = response.getString("Awards");
            movieIMDBRating2 = response.getString("imdbRating");
            movieDuration2 = response.getString("Runtime");
            movieBoxOffice2 = response.getString("BoxOffice");
            movieModel = new MovieModel(movieTitle2, movieTagline2, movieOverview2, moviePoster2, url, movieReleased2, moviePlot2,
                    movieGenre2, movieActors2, movieDuration2, movieWriters2, movieProduction2, movieAwards2, movieIMDBRating2, movieBoxOffice2);
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
        Intent intent = new Intent(this, MovieInfo.class);
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


    public void processLogin() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }

    public void processRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(getApplicationContext(), "Error getting information from google.", Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(getApplicationContext(), "Sign in successful.", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = auth.getCurrentUser();
                            recreate();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Error signing in.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MovieInfo.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChange, filter);

        super.onResume();
    }

    @Override
    protected void onPause() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChange, filter);

        super.onPause();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChange);
        super.onStop();
    }

    @Override
    public void onClick(int position) {
        getAllMovieData(position);

    }

    @Override
    public void onClick2(int position) {

    }
}