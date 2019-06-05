package com.example.moviesappstage2nano;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.bumptech.glide.Glide;
import com.example.moviesappstage2nano.MovieDescription.MovieResults;
import com.example.moviesappstage2nano.adapterclasses.YoutubeAdapter;
import com.example.moviesappstage2nano.modellingclasses.VideoDetails;
import com.example.moviesappstage2nano.roomdatabase.FavouriteDatabase;
import com.example.moviesappstage2nano.roomdatabase.FavouritesViewmodel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.provider.Contacts.SettingsColumns.KEY;
import static com.example.moviesappstage2nano.MainActivity.MOVIE_BASE_URL;
import static com.example.moviesappstage2nano.MainActivity.SAVE_KEY;
import static com.example.moviesappstage2nano.adapterclasses.OriginalAdapter.IMAGE_BASE_URL;
import static com.example.moviesappstage2nano.adapterclasses.OriginalAdapter.SHARE;

public class Outlinedata extends AppCompatActivity {
    FavouritesViewmodel viewModel;
    FavouriteDatabase database;
    List<MovieResults> checklist;
    @BindView(R.id.over_view_poster)
    ImageView poster;
    @BindView(R.id.over_view_overView_tv)
    TextView overView_tv;
    @BindView(R.id.over_view_vote_count_tv)
    TextView voteCount_tv;
    @BindView(R.id.over_view_rating_tv)
    TextView rating_tv;
    @BindView(R.id.over_view_favorites_image)
    ImageView favoriteimages;
    @BindView(R.id.over_view_release_date_tv)
    TextView releaseDate_tv;
    MovieResults result;
    @BindView(R.id.over_view_rating_label)
    TextView overViewRatingLabel;
    @BindView(R.id.over_view_release_date_label)
    TextView overViewReleaseDateLabel;
    @BindView(R.id.over_view_vote_count_label)
    TextView overViewVoteCountLabel;
    @BindView(R.id.over_view_favorites_label)
    TextView overViewFavoritesLabel;
    @BindView(R.id.over_view_overView_label)
    TextView overViewOverViewLabel;
    @BindView(R.id.over_view_recyclerView)
    RecyclerView overViewRecycler;
    List<VideoDetails> list = new ArrayList<>();
    LinearLayoutManager layoutManager;
    boolean state=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlinedata);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(FavouritesViewmodel.class);
        Bundle bundle = getIntent().getExtras();
        checklist=new ArrayList<>();
        assert bundle != null;
        result = (MovieResults) bundle.getSerializable(SHARE);
        assert result != null;
        setTitle(result.getOriginalTitle());

        Glide.with(this)
                .load(IMAGE_BASE_URL + result.getPosterPath())
                .into(poster);
        overView_tv.setText(result.getOverview());
        rating_tv.setText(String.valueOf(result.getVoteAverage()));
        releaseDate_tv.setText(result.getReleaseDate());
        voteCount_tv.setText(String.valueOf(result.getVoteCount()));
        updateImageView();
        if (savedInstanceState!=null)
        {
            int position=savedInstanceState.getInt(SAVE_KEY);
            list= (List<VideoDetails>) savedInstanceState.getSerializable(KEY);
            overViewRecycler.setAdapter(new YoutubeAdapter(list, Outlinedata.this));
            layoutManager = new LinearLayoutManager(Outlinedata.this);
            overViewRecycler.setLayoutManager(layoutManager);
        }
        else {
            parseYoputube(result.getId());
            updateImageView();
        }
    }

    private void updateImageView()
    {
        MovieResults r =  viewModel.checkMovieInDatabase(result.getId());
        if(r!=null)
        {
            favoriteimages.setImageResource(R.drawable.ic_favorite_black_24dp);
            state = true;
        }
        else
        {
            favoriteimages.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            state = false;
        }
    }

    public void parseYoputube(String id) {
        RequestQueue queue = Volley.newRequestQueue(this);
        Uri uri = Uri.parse(MOVIE_BASE_URL + id + "/videos?&api_key=" + "19d6e8afa81597feec6ea0a33635a2d1");
        String url = uri.toString();
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject root = new JSONObject(response);
                            JSONArray array = root.optJSONArray("results");
                            int len = array.length();
                            for (int i = 0; i < len; i++) {
                                JSONObject object = array.optJSONObject(i);
                                String key = object.optString("key");
                                String name = object.optString("name");
                                VideoDetails vd = new VideoDetails(name, key);
                                list.add(vd);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        overViewRecycler.setAdapter(new YoutubeAdapter(list, Outlinedata.this));
                        layoutManager = new LinearLayoutManager(Outlinedata.this);
                        overViewRecycler.setLayoutManager(layoutManager);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError || error instanceof TimeoutError
                        ||error instanceof AuthFailureError||error instanceof ParseError
                        ||error instanceof NetworkError||error instanceof ServerError) {
                    new AlertDialog.Builder(Outlinedata.this)
                            .setMessage("")
                            .setPositiveButton("",null)
                            .show();
                }

            }
        });
        queue.add(request);
    }


    public void openReview(View view) {
        Intent intent = new Intent(this, ReviewActivity.class);
        intent.putExtra("name", result.getOriginalTitle());
        intent.putExtra("id", result.getId());
        startActivity(intent);
    }


    public void change(View view)
    {

        if (state) {
            delete();
            favoriteimages.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            state = !state;
        } else {
            insert();
            favoriteimages.setImageResource(R.drawable.ic_favorite_black_24dp);
            state = !state;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    public void insert()  {
        String name = result.getOriginalTitle();
        String overview = result.getOverview();
        String releasedate = result.getReleaseDate();
        String poster = result.getPosterPath();
        double rating = result.getVoteAverage();
        String id = result.getId();
        int votcount = result.getVoteCount();
        MovieResults result = new MovieResults();
        result.setId(id);
        result.setOriginalTitle(name);
        result.setPosterPath(poster);
        result.setOverview(overview);
        result.setReleaseDate(releasedate);
        result.setVoteAverage(rating);
        result.setVoteCount(votcount);
        viewModel.insert(result);
        Toast.makeText(this, "Inserted to favorites", Toast.LENGTH_SHORT).show();

    }

    public void delete() {
        String name = result.getOriginalTitle();
        String overview = result.getOverview();
        String releasedate = result.getReleaseDate();
        String poster = result.getPosterPath();
        Double rating = result.getVoteAverage();
        String id = result.getId();
        int votcount = result.getVoteCount();
        MovieResults result = new MovieResults();
        result.setOriginalTitle(name);
        result.setVoteCount(votcount);
        result.setPosterPath(poster);
        result.setOverview(overview);
        result.setId(id);
        result.setVoteAverage(rating);
        result.setReleaseDate(releasedate);
        viewModel.delete(result);
        Toast.makeText(this, "Deleted from favorites", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (layoutManager!=null)
        {
            int pos=layoutManager.findFirstCompletelyVisibleItemPosition();
            outState.putInt(SAVE_KEY,pos);
        }
        outState.putSerializable(KEY, (Serializable) list);

    }
}

