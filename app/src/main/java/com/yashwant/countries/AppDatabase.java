package com.yashwant.countries;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.yashwant.countries.activity.CountriesDAO;
import com.yashwant.countries.model.CountriesModel;

@Database(entities = {CountriesModel.class} ,  version = 1)

public abstract class AppDatabase extends RoomDatabase {
    public abstract CountriesDAO recipeDao();

}