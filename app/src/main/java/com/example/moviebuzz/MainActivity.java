package com.example.moviebuzz;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.moviebuzz.Adapters.ExpandListAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    NavigationView nav;
    ActionBarDrawerToggle bar;
    DrawerLayout layout;
    Toolbar toolbar;
    View header;
    List<String> listDataHeader;
    HashMap<String,List<String>> listDataChild;
    ExpandableListView expListView;
    ExpandListAdapter listAdapter;
    Fragment temp;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    NetworkChange networkChange = new NetworkChange();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        processRequest();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        nav = findViewById(R.id.nav_menu);
        layout = findViewById(R.id.draw_layout);
        header = nav.getHeaderView(0);

        setSupportActionBar(toolbar);
        bar = new ActionBarDrawerToggle(this, layout, toolbar, R.string.open, R.string.close);
        layout.addDrawerListener(bar);
        bar.syncState();
        bar.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MovieHomePage()).commit();
        nav.setCheckedItem(R.id.nav_Home);

        enableExpandableList();
    }

  
    private void enableExpandableList() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        expListView = findViewById(R.id.left_drawer);

        prepareListData(listDataHeader, listDataChild);
        listAdapter = new ExpandListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
              switch (groupPosition)
              {
                  case 0:
                      temp = new CategoryPage("Search Movies");
                      layout.closeDrawer(GravityCompat.START);
                      break;
                  case  1:
                      temp = new MovieHomePage();
                      layout.closeDrawer(GravityCompat.START);
                        break;
                  case 3:
                      processLogin();
              }
              if(temp!=null)
                  getSupportFragmentManager().beginTransaction().replace(R.id.container, temp).commit();

                return false;
            }
        });
        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {


            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                // Temporary code:

                // till here
                switch (childPosition){
                    case 0:
                        temp = new CategoryPage("Action");
                        layout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        temp = new CategoryPage("Adventure");
                        layout.closeDrawer(GravityCompat.START);
                        break;
                    case 2:
                        temp = new CategoryPage("Comedy");
                        layout.closeDrawer(GravityCompat.START);
                        break;
                    case 3:
                        temp = new CategoryPage("Crime");
                        layout.closeDrawer(GravityCompat.START);
                        break;
                    case 4:
                        temp = new CategoryPage("Drama");
                        layout.closeDrawer(GravityCompat.START);
                        break;
                    case 5:
                        temp = new CategoryPage("Fantasy");
                        layout.closeDrawer(GravityCompat.START);
                        break;
                    case 6:
                        temp = new CategoryPage("Horror");
                        layout.closeDrawer(GravityCompat.START);
                        break;
                    case 7:
                        temp = new CategoryPage("Romance");
                        layout.closeDrawer(GravityCompat.START);
                        break;
                    case 8:
                        temp = new CategoryPage("Science Fiction");
                        layout.closeDrawer(GravityCompat.START);
                        break;
                    case 9:
                        temp = new CategoryPage("Thriller");
                        layout.closeDrawer(GravityCompat.START);
                        break;

                }
                if(temp!=null)
                   getSupportFragmentManager().beginTransaction().replace(R.id.container, temp).commit();


                return false;
            }
        });}
    private void prepareListData(List<String> listDataHeader, Map<String,
                List<String>> listDataChild) {


        // Adding child data
        listDataHeader.add("Search All Movies");
        listDataHeader.add("Home");
        listDataHeader.add("Categories");
        listDataHeader.add("Google SignIn");
    //    listDataHeader.add("SignOut");

        // Adding child data
        List<String> top = new ArrayList<String>();
        top.add("Action");
        top.add("Adventure");
        top.add("Comedy");
        top.add("Crime");
        top.add("Drama");
        top.add("Fantasy");
        top.add("Horror");
        top.add("Romance");
        top.add("Science Fiction");
        top.add("Thriller");

        listDataChild.put(listDataHeader.get(2), top); // Header, Child data

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
                Toast.makeText(getApplicationContext(),"Error getting information from google.",Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(getApplicationContext(),"Sign in successful.",Toast.LENGTH_SHORT).show();

                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(),"Error signing in.",Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChange,filter);

        super.onResume();
    }

    @Override
    protected void onPause() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChange,filter);
        super.onPause();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChange);
        super.onStop();
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this).setTitle("Exit!!")
                .setMessage("Are you sure, you want to exit the app")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                }).setNegativeButton("No",null).show();
    }}