package com.example.popularmovies.utils;

import android.util.Log;

import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.MoviePoster;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JsonUtils {
    public static final String LOG_TAG = JsonUtils.class.getSimpleName();

    public static Movie parseMovie(String stringUrl) {
        Movie movie = null;
        try {
            URL url = NetworkUtils.buildUrl(stringUrl);
            JSONObject root = NetworkUtils.makeHttpRequest(url);

            String title = root.optString("original_title");
            String releaseDateRaw = root.optString("release_date");
            String releaseDate = dateFormat(releaseDateRaw);
            String overview = root.optString("overview");
            String posterPath = root.optString("poster_path");
            double voteAverage = root.optDouble("vote_average");

            movie = new Movie(title, releaseDate, overview, posterPath, voteAverage);
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error retrieving movies: ", e);
        }
        return movie;
    }

    public static ArrayList<MoviePoster> parseMoviePosters(String stringUrl) {
        ArrayList<MoviePoster> searchResultMovies = new ArrayList<>();
        try {
            URL url = NetworkUtils.buildUrl(stringUrl);
            JSONObject root = NetworkUtils.makeHttpRequest(url);
            JSONArray results = root.optJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.optJSONObject(i);
                String posterPath = result.optString("poster_path");
                int id = result.optInt("id");
                MoviePoster moviePoster = new MoviePoster(id, posterPath);
                searchResultMovies.add(moviePoster);
            }
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error retrieving movies: ", e);
        }
        return searchResultMovies;
    }

    private static String dateFormat(String dateString) {
        String formattedDate = "";
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            return new SimpleDateFormat("dd MMM yyyy").format(date);
        }
        catch (ParseException e) {
            Log.e(LOG_TAG, "Unable to parse date: ", e);
        }
        return formattedDate;
    }
}

