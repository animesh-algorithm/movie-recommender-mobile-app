package com.example.moviebuzz.Model;

public class MovieModel {
    String title,poster_path,movieId,released,tagline,plot,overview,genre,actors,duration,writers,production,awards,imdbRatings,boxOffice;

    public MovieModel() {
    }

    public MovieModel(String movieId) {
        this.movieId = movieId;
    }

    public MovieModel(String title, String tagline, String overview, String poster_path, String movieId, String released, String plot,
                      String genre, String actors, String duration, String writers, String production, String awards, String imdbRatings,String boxOffice) {
        this.title = title;
        this.tagline = tagline;
        this.overview = overview;
        this.poster_path = poster_path;
        this.movieId = movieId;
        this.released = released;
        this.plot = plot;
        this.genre = genre;
        this.actors = actors;
        this.duration = duration;
        this.writers = writers;
        this.production = production;
        this.awards = awards;
        this.imdbRatings = imdbRatings;
        this.boxOffice = boxOffice;
    }


    public String getTitle() {
        return title;
    }

    public String getTagline() {
        return tagline;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getReleased() {
        return released;
    }

    public String getPlot() {
        return plot;
    }

    public String getGenre() {
        return genre;
    }

    public String getActors() {
        return actors;
    }

    public String getDuration() {
        return duration;
    }

    public String getWriters() {
        return writers;
    }

    public String getProduction() {
        return production;
    }

    public String getAwards() {
        return awards;
    }

    public String getImdbRatings() {
        return imdbRatings;
    }

    public String getBoxOffice() {
        return boxOffice;
    }
}
