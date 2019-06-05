package com.example.moviesappstage2nano;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

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
import com.example.moviesappstage2nano.adapterclasses.ReviewAdapter;
import com.example.moviesappstage2nano.modellingclasses.ReviewDetails;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import static com.example.moviesappstage2nano.MainActivity.MOVIE_BASE_URL;


public class ReviewActivity extends AppCompatActivity {
    @BindView(R.id.review_recycler_view)
    RecyclerView recyclerview;
    LinearLayoutManager linearLayoutManager;
    List<ReviewDetails> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review2);
        ButterKnife.bind(this);
        reviewList = new ArrayList<>();
        String id = getIntent().getStringExtra("id");
        String title = getIntent().getStringExtra("name");
        setTitle(title);
        load(id);
    }

    public void load(String id) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = MOVIE_BASE_URL + String.valueOf(id) + "/reviews?api_key=" + "19d6e8afa81597feec6ea0a33635a2d1";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.optJSONArray("results");
                            if (array.length() == 0) {

                                new AlertDialog.Builder(ReviewActivity.this)
                                        .setTitle(getTitle())
                                        .setMessage("There are no reviews to Dispaly")
                                        .show();
                            }
                            int length = array.length();
                            for (int i = 0; i < length; i++) {
                                JSONObject jsonObject1 = array.getJSONObject(i);
                                String author = jsonObject1.optString("author");
                                String comment = jsonObject1.optString("content");

                                ReviewDetails data = new ReviewDetails(author, comment);
                                reviewList.add(data);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        recyclerview.setAdapter(new ReviewAdapter(reviewList, ReviewActivity.this));
                        linearLayoutManager = new LinearLayoutManager(ReviewActivity.this);
                        recyclerview.setLayoutManager(linearLayoutManager);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError || error instanceof TimeoutError
                        ||error instanceof AuthFailureError||error instanceof ParseError
                        ||error instanceof NetworkError||error instanceof ServerError) {
                    new AlertDialog.Builder(ReviewActivity.this)
                            .setMessage("")
                            .setPositiveButton("",null)
                            .show();
                }

            }
        });
        queue.add(request);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return  true;
    }
}

