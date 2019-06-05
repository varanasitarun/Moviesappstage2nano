package com.example.moviesappstage2nano.roomdatabase;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.example.moviesappstage2nano.MovieDescription.MovieResults;



import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;


@Dao
public interface FavDao
{
    @Insert(onConflict = REPLACE)
    void insert(MovieResults favMovie);

    @Delete
    void delete(MovieResults favMovie);

    @Query("SELECT * FROM MovieResults")
    LiveData<List<MovieResults>> getlivedataMovies();

    @Query("SELECT * FROM MovieResults WHERE id = :id LIMIT 1")
    MovieResults checkMovieDatabase(String id);
}


