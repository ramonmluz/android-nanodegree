package com.android.nanodegree.udacity.myappportifolio.adapter.movie;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.nanodegree.udacity.myappportifolio.R;
import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Trailer;

import java.util.List;

/**
 * Created by ramon on 09/03/17.
 */

public class TrailerRecyclerViewAdapter extends RecyclerView.Adapter<TrailerRecyclerViewAdapter.TrailerViewHolder> {

    private List<Trailer> trailers;

    public class  TrailerViewHolder extends RecyclerView.ViewHolder{

        public ImageButton playTrailerbt;
        public TextView trailerName;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            // Instancia os componentes a partir dos ids no recyler_tralier_item
            playTrailerbt = (ImageButton) itemView.findViewById(R.id.play_trailer_bt);
            trailerName = (TextView) itemView.findViewById(R.id.trailer_name_ed);
        }
    }

    public TrailerRecyclerViewAdapter(List<Trailer> trailers) {
        this.trailers = trailers;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_trailer_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        final Trailer trailer = trailers.get(position);
        holder.trailerName.setText(trailer.getName());

        holder.playTrailerbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ao clicar em cada imagem chama a tela Youtube
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Trailer.TRAILER_URL.concat(trailer.getKey()))));
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }



}

