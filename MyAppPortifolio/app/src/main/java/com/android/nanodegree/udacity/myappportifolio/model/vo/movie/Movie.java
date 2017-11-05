package com.android.nanodegree.udacity.myappportifolio.model.vo.movie;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.android.nanodegree.udacity.myappportifolio.util.Constants;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ramon on 16/03/17.
 */

public class Movie implements Parcelable {

    private String id;

    private String overview;

    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("vote_average")
    private String voteAverage;
    @SerializedName("release_date")
    private String releaseDate;

    public Movie() {
    }

    /**
     * Desserialiaza os atributos
     *
     * @param in
     */
    protected Movie(Parcel in) {
        id = in.readString();
        posterPath = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        voteAverage = in.readString();
        releaseDate = in.readString();
    }

    /**
     * Cria o objeto a partir de um parcel
     */
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Serializa os atibutos (transforma em bytes)
     *
     * @param parcel
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(posterPath);
        parcel.writeString(originalTitle);
        parcel.writeString(overview);
        parcel.writeString(voteAverage);
        parcel.writeString(releaseDate);
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

    public String getPosterPathUrl() {
        return Constants.BASE_URL_IMAGE + this.posterPath;
    }

    public String getReleaseYear() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY");

        try {
            return simpleDateFormat.format(simpleDateFormat.parse(releaseDate));
        } catch (ParseException e) {
            Log.e(Constants.TAG_MOVIE, "Error format date:" + e.getMessage());
            return null;
        }


    }
}
