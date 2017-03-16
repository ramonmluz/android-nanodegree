package com.android.nanodegree.udacity.myappportifolio;

/**
 * Created by ramon on 16/03/17.
 */

public class Movie {

    private Integer id;
    private String originalTitle;
    // overview in APi
    private  String plotSynopsis;
    // vote_average in the api
    private  Number userRating;

    private String releaseDate;

    public Movie(Integer id, String originalTitle, String plotSynopsis, Number userRating, String releaseDate) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

}
