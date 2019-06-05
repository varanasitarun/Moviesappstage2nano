package com.example.moviesappstage2nano.roomdatabase;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import com.example.moviesappstage2nano.MovieDescription.MovieResults;

import java.util.List;

public class FavouritesViewmodel extends AndroidViewModel
{
    private FavouriteMovieRepository Repository;
    private LiveData<List<MovieResults>> AllResults;
    MovieResults result;

    public FavouritesViewmodel (Application application)
    {
        super(application);
        Repository = new FavouriteMovieRepository(application);
        AllResults = Repository.getAllResults();
    }

    public LiveData<List<MovieResults>> getAllResults() { return AllResults; }
    public MovieResults checkMovieInDatabase(String id)
    {
        result = Repository.checkMovieInDatabase(id);
        return result;
    }
    public void insert(MovieResults result) { Repository.insert(result); }
    public void delete(MovieResults result) { Repository.delete(result); }
}

