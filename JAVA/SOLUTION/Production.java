package SOLUTION;

import java.util.ArrayList;
import java.util.List;

public abstract class Production implements Comparable, ExperienceStrategy{
    private String title;
    private String type;
    private List<String> directors;
    private List<String> actors;
    private List<Genre> genres;
    private List<Rating> ratings;
    private String plot;
    private Double averageRating;

    @Override
    public int calculateExperience() {
        return 5;
    }

    public Production(){
        ratings = new ArrayList<>();
    }
    public Production(String title, String type, List<String> directors, List<String> actors,
                      List<Genre> genres, List<Rating> ratings, String plot,
                      Double averageRating) {
        this.title = title;
        this.type = type;
        this.directors = directors;
        this.actors = actors;
        this.genres = genres;
        this.ratings = ratings;
        this.plot = plot;
        this.averageRating = averageRating;
    }

    public boolean hasGenre(Genre target) {
        if(genres != null) {
            for(Genre genre : genres) {
                if(genre != null && genre.equals(target))
                    return true;
            }
        }
        return false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }


    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public abstract void displayInfo();

    public abstract String displayInfoGraphic();

    public void addDirector(String director) {
        if (directors == null) {
            directors = new ArrayList<>();
        }

        directors.add(director);
    }

    public void removeDirector(String director) {
        if (directors != null) {
            directors.remove(director);
        }
    }

    public void addActor(String actor) {
        if (actors == null) {
            actors = new ArrayList<>();
        }

        actors.add(actor);
    }

    public void removeActor(String actor) {
        if (actors != null) {
            actors.remove(actor);
        }
    }
    public void addRating(Rating rating, User<?> user) {
        if(rating != null) {
            if(ratings == null) {
                ratings = new ArrayList<>();
            }
            ratings.add(rating);
            if(user.getExperience() != Integer.MAX_VALUE) {
                int experienceGain = user.getExperience() + calculateExperience();
                user.setExperience(experienceGain);
            }
        } else {
            System.out.println("Invalid rating");
        }
    }

    public void deleteRatingByUsername(String username) {
        Rating found = null;
        for(Rating rating : ratings) {
            if(rating.getUsername().equals(username)) {
                found = rating;
                break;
            }
        }
        ratings.remove(found);
    }

    public boolean containsRatingByUser(String username) {
        for(Rating rating : ratings) {
            if(rating.getUsername().equals(username))
                return true;
        }
        return false;
    }

    public int compareTo(Object o) {
        if (o instanceof Actor) {
            Actor otherActor = (Actor) o;
            return this.getTitle().compareTo(otherActor.getName());
        } else if(o instanceof Production) {
            Production otherProduction = (Production) o;
            return this.getTitle().compareTo(otherProduction.getTitle());
        }
        return 0;
    }

    public int getNumRatings() {
        return (ratings != null) ? ratings.size() : 0;
    }
}
