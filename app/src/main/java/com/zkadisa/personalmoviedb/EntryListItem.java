package com.zkadisa.personalmoviedb;

import com.zkadisa.personalmoviedb.DataHandling.Entry;

import java.io.Serializable;

public class EntryListItem extends SearchEntryListItem implements Serializable {

    private String Rated;
    private String Released;
    private String Runtime;
    private String Genre;
    private String Director;
    private String Writer;
    private String Actors;
    private String Plot;
    private String Language;
    private String Country;
    private String Awards;
    private RatingsSource[] Ratings;
    private String Metascore;
    private String imdbRating;
    private String imdbVotes;
    private String DVD;
    private String BoxOffice;
    private String Production;
    private String Website;

    public EntryListItem(String title, String year, String imdbID, String type, String poster, String rated, String released, String runtime, String genre, String director, String writer, String actors, String plot, String language, String country, String awards, RatingsSource[] ratings, String metascore, String imdbRating, String imdbVotes, String DVD, String boxOffice, String production, String website) {
        super(title, year, imdbID, type, poster);
        Rated = rated;
        Released = released;
        Runtime = runtime;
        Genre = genre;
        Director = director;
        Writer = writer;
        Actors = actors;
        Plot = plot;
        Language = language;
        Country = country;
        Awards = awards;
        Ratings = ratings;
        Metascore = metascore;
        this.imdbRating = imdbRating;
        this.imdbVotes = imdbVotes;
        this.DVD = DVD;
        BoxOffice = boxOffice;
        Production = production;
        Website = website;
    }
    public EntryListItem(Entry e) {
        super(e.Title, e.Year, e.imdbID, e.Type, e.Poster);
        Rated = e.Rated;
        Released = e.Released;
        Runtime = e.Runtime;
        Genre = e.Genre;
        Director = e.Director;
        Writer = e.Writer;
        Actors = e.Actors;
        Plot = e.Plot;
        Language = e.Language;
        Country = e.Country;
        Awards = e.Awards;
        Ratings = new RatingsSource[e.Ratings.length];
        for (int i = 0; i < e.Ratings.length; i++)
            Ratings[i] = new RatingsSource(e.Ratings[i]);
        Metascore = e.Metascore;
        this.imdbRating = e.imdbRating;
        this.imdbVotes = e.imdbVotes;
        this.DVD = e.DVD;
        BoxOffice = e.BoxOffice;
        Production = e.Production;
        Website = e.Website;
    }
    public EntryListItem(SearchEntryListItem e) {
        super(e.getTitle(), e.getYear(), e.getImdbID(), e.getType(), e.getPoster());
    }

    public String getRated() {
        return Rated;
    }

    public void setRated(String rated) {
        Rated = rated;
    }

    public String getReleased() {
        return Released;
    }

    public void setReleased(String released) {
        Released = released;
    }

    public String getRuntime() {
        return Runtime;
    }

    public void setRuntime(String runtime) {
        Runtime = runtime;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public String getWriter() {
        return Writer;
    }

    public void setWriter(String writer) {
        Writer = writer;
    }

    public String getActors() {
        return Actors;
    }

    public void setActors(String actors) {
        Actors = actors;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        Plot = plot;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getAwards() {
        return Awards;
    }

    public void setAwards(String awards) {
        Awards = awards;
    }

    public RatingsSource[] getRatings() {
        return Ratings;
    }

    public void setRatings(RatingsSource[] ratings) {
        Ratings = ratings;
    }

    public String getMetascore() {
        return Metascore;
    }

    public void setMetascore(String metascore) {
        Metascore = metascore;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public void setImdbVotes(String imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public String getDVD() {
        return DVD;
    }

    public void setDVD(String DVD) {
        this.DVD = DVD;
    }

    public String getBoxOffice() {
        return BoxOffice;
    }

    public void setBoxOffice(String boxOffice) {
        BoxOffice = boxOffice;
    }

    public String getProduction() {
        return Production;
    }

    public void setProduction(String production) {
        Production = production;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public class RatingsSource implements Serializable
    {
        private String Source;
        private String Value;

        public RatingsSource(String source, String value) {
            Source = source;
            Value = value;
        }

        public RatingsSource(Entry.RatingsSource e){
            Source = e.Source;
            Value = e.Value;
        }

        public String getSource() {
            return Source;
        }

        public void setSource(String source) {
            Source = source;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String value) {
            Value = value;
        }
    }
}
