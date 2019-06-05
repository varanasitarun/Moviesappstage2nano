package com.example.moviesappstage2nano.roomdatabase;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import com.example.moviesappstage2nano.MovieDescription.MovieResults;

import java.util.List;

public class FavouriteMovieRepository {


    private FavDao FavMovieDao;
    private LiveData<List<MovieResults>> Allresults;

    FavouriteMovieRepository(Application application) {
        FavouriteDatabase db = FavouriteDatabase.getDatabase(application);
        FavMovieDao = db.favdao();
        Allresults = FavMovieDao.getlivedataMovies();
    }
    LiveData<List<MovieResults>> getAllResults() {
        return Allresults;
    }
    public void insert (MovieResults result) {
        new insertAsyncTask(FavMovieDao).execute(result);
    }
    public void delete (MovieResults result) {
        new deleteAsyncTask(FavMovieDao).execute(result);
    }
    MovieResults checkMovieInDatabase(String id)
    {
        MovieResults result =FavMovieDao.checkMovieDatabase(id);
        return result;
    }
    private static class insertAsyncTask extends AsyncTask<MovieResults, Void, Void> {

        private FavDao AsyncTaskDao;

        insertAsyncTask(FavDao dao) {
            AsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MovieResults... params) {
            AsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    private static class deleteAsyncTask extends AsyncTask<MovieResults, Void, Void> {

        private FavDao AsyncTaskDao;

        deleteAsyncTask(FavDao dao) {
            AsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MovieResults... params) {
            AsyncTaskDao.delete(params[0]);
            return null;
        }
    }

}
