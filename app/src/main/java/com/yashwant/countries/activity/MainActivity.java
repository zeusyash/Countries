package com.yashwant.countries.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.yashwant.countries.R;
import com.yashwant.countries.adapter.CountriesAdapter;
import com.yashwant.countries.model.CountriesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String endpoint = "https://restcountries.eu/rest/v2/region/asia";
    List<CountriesModel> countriesModel = new ArrayList<>();
    RecyclerView recycler_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler_search = findViewById(R.id.recycler_search);
        recycler_search.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        search();
    }

    private void search() {


        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, endpoint, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("MAINLOG", response.toString());

                try {

                    countriesModel.clear();
                    for (int i = 0; i < response.length(); i++) {

                        JSONObject jsonObject = response.getJSONObject(i);

                        String name = jsonObject.getString("name");
                        String capital = jsonObject.getString("capital");
                        String flag = jsonObject.getString("flag");
                        String region = jsonObject.getString("region");
                        String subregion = jsonObject.getString("subregion");
                        int population = jsonObject.getInt("population");


                        JSONArray borders = jsonObject.getJSONArray("borders");
                        JSONArray languages = jsonObject.getJSONArray("languages");
                        String allborders = "", alllanguages = "";
                        for (int b = 0; b < borders.length(); b++) {
                            allborders = allborders.concat(borders.getString(b));
                            allborders += ", ";
                        }
                        for (int l = 0; l < languages.length(); l++) {
                            JSONObject language = languages.getJSONObject(l);
                            alllanguages = alllanguages.concat(language.getString("name"));
                            alllanguages += ", ";
                        }
                        alllanguages = alllanguages.replaceAll(", $", ".");
                        allborders = allborders.replaceAll(", $", ".");


                        CountriesModel countries = new CountriesModel();
                        countries.setName(name);
                        countries.setCapital(capital);
                        countries.setFlag(flag);
                        countries.setRegion(region);
                        countries.setSubregion(subregion);
                        countries.setPopulation(population);
                        countries.setBorders(allborders);
                        countries.setLanguages(alllanguages);

                        countriesModel.add(countries);


//                            Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                    }
                    CountriesAdapter allProduct_adapter = new CountriesAdapter(countriesModel);
                    recycler_search.setAdapter(allProduct_adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("MAINLOG", e.toString());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MAINLOG", error.toString());

//                Toast.makeText(Search.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonObjectRequest);


    }

}