package com.example.moviesappstage2nano.roomdatabase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.moviesappstage2nano.MovieDescription.MovieResults;

import java.util.List;


@Database(entities = {MovieResults.class}, version = 1, exportSchema = false)
public abstract class FavouriteDatabase extends RoomDatabase {

    public abstract FavDao favdao();

    private static FavouriteDatabase INSTANCE;

    public static FavouriteDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FavouriteDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FavouriteDatabase.class, "favourites.db")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){

        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            new PopulateDatabaseAsync(INSTANCE).execute();
        }
    };


    private static class PopulateDatabaseAsync extends AsyncTask<Void, Void, LiveData<List<MovieResults>>> {

        private final FavDao Dao;

        PopulateDatabaseAsync(FavouriteDatabase db) {
            Dao = db.favdao();
        }


        @Override
        protected LiveData<List<MovieResults>> doInBackground(Void... voids) {
            return Dao.getlivedataMovies();
        }
    }

}

