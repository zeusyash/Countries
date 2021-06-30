package com.yashwant.countries.activity;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.yashwant.countries.model.CountriesModel;

import java.util.List;

@Dao
public interface CountriesDAO {

    @Query("SELECT * FROM CountriesModel")
    List<CountriesModel> getAll();

    @Insert
    void insert(CountriesModel recipe);

}