package com.yashwant.countries.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;
import com.yashwant.countries.R;
import com.yashwant.countries.SvgDecoder;
import com.yashwant.countries.SvgDrawableTranscoder;
import com.yashwant.countries.SvgSoftwareLayerSetter;
import com.yashwant.countries.model.CountriesModel;

import java.io.InputStream;
import java.util.List;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.holder> {

    List<CountriesModel> countriesModels;
    Context context;
    String o = "0";

    Dialog dialog;
    ImageView pImage;
    TextView closeIMG;
    TextView pname, pdesc;


    public CountriesAdapter(List<CountriesModel> countriesModels) {
        this.countriesModels = countriesModels;


    }

    @NonNull
    @Override
    public CountriesAdapter.holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem_countries, viewGroup, false);
        context = viewGroup.getContext();
        return new holder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final holder holder, int i) {

        CountriesModel countriesModel = countriesModels.get(i);

        holder.name.setText("Name: " + countriesModel.getName());
        holder.capital.setText("Capital: " + countriesModel.getCapital());
        holder.region.setText("Region: " + countriesModel.getRegion());
        holder.subregion.setText("Sub-Region: " + countriesModel.getSubregion());
        holder.population.setText("Population: " + countriesModel.getPopulation());
        holder.language.setText("Languages: " + countriesModel.getLanguages());
        holder.border.setText("Borders: " + countriesModel.getBorders());

        GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder = Glide.with(context)
                .using(Glide.buildStreamModelLoader(Uri.class, context), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .listener(new SvgSoftwareLayerSetter<Uri>());

        requestBuilder.diskCacheStrategy(DiskCacheStrategy.NONE)
                .load(Uri.parse(countriesModel.getFlag()))
                .into(holder.flag);
    }

    @Override
    public int getItemCount() {
        return countriesModels.size();
    }

    private void updateintent() {
        Intent updates = new Intent("Grocery_cart");
        updates.putExtra("type", "update");
        context.sendBroadcast(updates);
    }

    public class holder extends RecyclerView.ViewHolder {

        TextView name, capital, region, subregion, population, language, border;
        ImageView flag;
        CardView cardView;

        public holder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_et);
            capital = itemView.findViewById(R.id.capital_et);
            region = itemView.findViewById(R.id.region_et);
            subregion = itemView.findViewById(R.id.subregion_et);
            population = itemView.findViewById(R.id.population_et);
            language = itemView.findViewById(R.id.language_et);
            border = itemView.findViewById(R.id.border_et);
            flag = itemView.findViewById(R.id.flag);


        }
    }
}
