package com.android.nanodegree.udacity.myappportifolio;

/**
 * Created by ramon on 16/03/17.
 */

public class Movie {

    private String id;
    private String originalTitle;
    private  String overview;
    // vote_average in the api
    private  String voteAverage;
    private String releaseDate;


    public Movie(String id, String originalTitle, String overview, String voteAverage, String releaseDate) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public Movie(){}


    public enum  ParametersEnum{
        RESULTS("results"), ID("id"), ORIGINAL_TITLE("original_title"), OVERVIEW("overview"),
        VOTE_AVERAGE("vote_average"), RELEASE_DATE("release_date");

        private final String value;

        ParametersEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
