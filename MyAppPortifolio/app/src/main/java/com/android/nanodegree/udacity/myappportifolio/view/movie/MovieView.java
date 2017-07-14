package com.android.nanodegree.udacity.myappportifolio.view.movie;


import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Review;
import com.android.nanodegree.udacity.myappportifolio.model.vo.movie.Trailer;
import com.android.volley.VolleyError;

import java.util.List;


public interface MovieView {

      void fillTrailersRecyclerView(List<Trailer> trailers);
      void fillReviewsRecyclerView(List<Review> reviews);
      void notifyError(VolleyError error);
}
