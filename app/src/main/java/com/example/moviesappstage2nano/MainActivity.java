package com.example.moviesappstage2nano;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviesappstage2nano.adapterclasses.OriginalAdapter;
import com.example.moviesappstage2nano.MovieDescription.MovieResults;
import com.example.moviesappstage2nano.MovieDescription.MovieResults;
import com.example.moviesappstage2nano.roomdatabase.FavouritesViewmodel;
import com.example.moviesappstage2nano.MovieDescription.MoviesInfo;
import com.example.moviesappstage2nano.roomdatabase.FavouriteDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
{
    public static final String MOVIE_BASE_URL="http://api.themoviedb.org/3/movie/";
    public static final String MOVIE_END_URL="?api_key="+"19d6e8afa81597feec6ea0a33635a2d1";
    public static final String POPULAR="popular";
    public static final String RATED="top_rated";
    public static final String SAVE_KEY="SAVE";
    public static final String KEY="KEY";
    public static final String TITLE="TITLE";
    String sort;
    String titletoSave;
    List<MovieResults> results;
    OriginalAdapter originalAdapter;
    FavouritesViewmodel FavMovieViewModel;
    GridLayoutManager gridLayoutManager;
    @BindView(R.id.id_progressbar)
    ProgressBar progressBar;
    @BindView(R.id.id_recyclerview)
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sort=POPULAR;
        results=new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        gridLayoutManager=new GridLayoutManager(this,checkColumnns(this));

        FavMovieViewModel = ViewModelProviders.of(this).get(FavouritesViewmodel.class);
        if (savedInstanceState!=null)
        {
            String title=savedInstanceState.getString("TITLE");
            setTitle(title);
            int position=savedInstanceState.getInt(SAVE_KEY);
            results= (List<MovieResults>) savedInstanceState.getSerializable(KEY);
            progressBar.setVisibility(View.GONE);
            recyclerView.setAdapter(new OriginalAdapter(this,results));

            recyclerView.setLayoutManager(gridLayoutManager);
            gridLayoutManager.scrollToPosition(position);
        } else {
            if (connection())
            {
                fetchingMovies(sort);
            }
            else
            {
                Favorites();
            }
        }
    }

    private boolean connection()
    {
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo info=connectivityManager.getActiveNetworkInfo();
        return info!=null&&info.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuitems,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.popular:
                sort=POPULAR;
                setTitle(getResources().getString(R.string.popular_movies));
                fetchingMovies(sort);
                break;
            case R.id.rated:
                sort=RATED;
                setTitle(getResources().getString(R.string.top_rated_moviw));
                fetchingMovies(sort);
                break;
            case R.id.favorites:
                setTitle(getResources().getString(R.string.favorite_movies));
                Favorites();
                break;

        }
        return true;

    }

    private void Favorites() {
        setTitle(getResources().getString(R.string.favorite_movies));
        progressBar.setVisibility(View.GONE);
        FavMovieViewModel.getAllResults().observe(this, new Observer<List<MovieResults>>() {
            @Override
            public void onChanged(@Nullable List<MovieResults> finalresult)
            {
                results=finalresult;
                originalAdapter = new OriginalAdapter(MainActivity.this,finalresult);
                recyclerView.setAdapter(originalAdapter);
                recyclerView.setLayoutManager(gridLayoutManager);
            }
        });
    }

    public void fetchingMovies(String sort)
    {
        if (results!=null)
        {
            results.clear();
        }

        RequestQueue queue= Volley.newRequestQueue(this);
        String url=MOVIE_BASE_URL+sort+MOVIE_END_URL;
        StringRequest request=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        Gson gson=new GsonBuilder().create();
                        MoviesInfo movieDetails=gson.fromJson(response,MoviesInfo.class);
                        results.addAll(movieDetails.getResults());
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setAdapter(new OriginalAdapter(MainActivity.this,results));
                        recyclerView.setLayoutManager(gridLayoutManager);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                if (error instanceof NoConnectionError || error instanceof TimeoutError
                        ||error instanceof AuthFailureError||error instanceof ParseError
                        ||error instanceof NetworkError||error instanceof ServerError) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Error in Connection")
                            .setMessage("There is some problem in intenet Connection")
                            .setPositiveButton("Ok", null)
                            .show();
                }

            }

        });
        queue.add(request);
    }

    public int checkColumnns(Context c)
    {
        DisplayMetrics displayMetrics=c.getResources().getDisplayMetrics();
        float width=displayMetrics.widthPixels/displayMetrics.density;
        int scale=150;
        int d= (int) (width/scale);
        return (d>=2?d:2);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (gridLayoutManager!=null)
        {
            int pos=gridLayoutManager.findFirstCompletelyVisibleItemPosition();
            outState.putInt(SAVE_KEY,pos);
        }
        outState.putSerializable(KEY, (Serializable) results);
        outState.putString(TITLE,getTitle().toString());
    }

}

