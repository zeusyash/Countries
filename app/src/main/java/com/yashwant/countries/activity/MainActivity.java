package com.yashwant.countries.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.yashwant.countries.DatabaseClient;
import com.yashwant.countries.MyApplication;
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
    CountriesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler_search = findViewById(R.id.recycler_search);
        recycler_search.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        if (isConnected(MainActivity.this)) {
            search();
        } else {
            fetchfromRoom();
        }

    }

    public static boolean isConnected(Context context){
        ConnectivityManager
                cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
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

                        int id = i+1;
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


                        CountriesModel countries = new CountriesModel(id,name,capital,flag,region,subregion,allborders,alllanguages,population);
/*
                        countries.setName(name);
                        countries.setCapital(capital);
                        countries.setFlag(flag);
                        countries.setRegion(region);
                        countries.setSubregion(subregion);
                        countries.setPopulation(population);
                        countries.setBorders(allborders);
                        countries.setLanguages(alllanguages);
*/

                        countriesModel.add(countries);


//                            Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                    }
                    CountriesAdapter allProduct_adapter = new CountriesAdapter(countriesModel);
                    recycler_search.setAdapter(allProduct_adapter);

                    SaveTask();

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

    private void SaveTask() {

        Log.d("MAINLOG", "INSAVE TASK");

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task

                for (int i = 0; i < countriesModel.size(); i++) {
                    CountriesModel country= new CountriesModel();
                    country.setName(countriesModel.get(i).getName());
                    country.setCapital(countriesModel.get(i).getCapital());
                    country.setRegion(countriesModel.get(i).getRegion());
                    country.setSubregion(countriesModel.get(i).getSubregion());
                    country.setFlag(countriesModel.get(i).getFlag());
                    country.setLanguages(countriesModel.get(i).getLanguages());
                    country.setBorders(countriesModel.get(i).getBorders());
                    country.setPopulation(countriesModel.get(i).getPopulation());
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().recipeDao().insert(country);
                }


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
//                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }


    private void fetchfromRoom() {

        Log.d("MAINLOG", "IN FETCH");


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {


                List<CountriesModel> recipeList = DatabaseClient.getInstance(MainActivity.this).getAppDatabase().recipeDao().getAll();
                countriesModel.clear();
                for (CountriesModel cout: recipeList) {
                    CountriesModel repo = new CountriesModel(cout.getId(),cout.getName(),
                            cout.getCapital(),
                            cout.getFlag(),
                            cout.getRegion(),
                            cout.getSubregion(),
                            cout.getBorders(),
                            cout.getLanguages(),
                            cout.getPopulation());
                    countriesModel.add(repo);
                }
                // refreshing recycler view
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("MAINLOG", "IN RUN");

                        adapter=new CountriesAdapter(countriesModel);
                        adapter.notifyDataSetChanged();
                        recycler_search.setAdapter(adapter);
                    }
                });
            }
        });
        thread.start();


    }

}