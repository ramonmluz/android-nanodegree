package com.android.nanodegree.udacity.myappportifolio.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.android.nanodegree.udacity.myappportifolio.R;
import com.android.nanodegree.udacity.myappportifolio.VO.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ramon on 22/03/17.
 */

public class MovieArrayAdapter  extends ArrayAdapter<Movie>{

    private static String LOG_TAG = MovieArrayAdapter.class.getSimpleName();

    /**
     *
     * @param context
     * @param resource
     * @param objects
     */
    public MovieArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Movie> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Movie movie = getItem(position);
        // Verifica se o layout está sendo apresentado
        if(convertView == null){
            // Carrega o layout a partir do id do Item presente na ViewGroup
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movie,parent,false);
        }
        // Instacia a imagem do layout carregado
        ImageView posterView = (ImageView) convertView.findViewById(R.id.list_item_movie_image);

        // Obtem a imagem do filme e a insere no componente de imagem instaciada
        Picasso.with(getContext()).load(movie.getPosterPath()).into(posterView);

        return convertView;
    }
}
