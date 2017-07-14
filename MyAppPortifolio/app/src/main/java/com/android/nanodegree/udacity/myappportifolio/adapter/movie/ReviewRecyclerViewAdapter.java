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
import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Review;

import java.util.List;

/**
 * Created by ramon on 09/03/17.
 */

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ReviewViewHolder> {

    private List<Review> reviews;

    public class ReviewViewHolder extends RecyclerView.ViewHolder{

        public ImageButton playReviewbt;
        public TextView reviewAuthor;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            // Instancia os componentes a partir dos ids no recyler_review_item
            playReviewbt = (ImageButton) itemView.findViewById(R.id.play_review_bt);
            reviewAuthor = (TextView) itemView.findViewById(R.id.review_author_text);
        }
    }

    public ReviewRecyclerViewAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        final Review review = reviews.get(position);
        holder.reviewAuthor.setText(review.getAuthor());

        holder.playReviewbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ao clicar em cada imagem chama as reviews via browser
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }



}

