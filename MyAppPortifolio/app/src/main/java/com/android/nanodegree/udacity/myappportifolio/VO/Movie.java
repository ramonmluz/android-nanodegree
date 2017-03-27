package com.android.nanodegree.udacity.myappportifolio.VO;

/**
 * Created by ramon on 16/03/17.
 */

public class Movie {

    public static final String BASE_URL_IMAGE = "http://image.tmdb.org/t/p/w185";
    private String id;
    private String posterPath;
    private String originalTitle;
    private  String overview;
    private  String voteAverage;
    private String releaseDate;


    public Movie(String id, String posterPath, String originalTitle, String overview, String voteAverage, String releaseDate) {
        this.id = id;
        this.posterPath = BASE_URL_IMAGE + posterPath;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public Movie(){}


    public enum  ParametersEnum{
        RESULTS("results"), ID("id"),  POSTER_PATH("poster_path"), ORIGINAL_TITLE("original_title"), OVERVIEW("overview"),
        VOTE_AVERAGE("vote_average"), RELEASE_DATE("release_date");

        private final String value;

        ParametersEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
