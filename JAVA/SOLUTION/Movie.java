package SOLUTION;

import java.util.List;
import java.util.ArrayList;

public class Movie extends Production {
    private String duration;
    private int releaseYear;

    public Movie() {

    }
    public Movie(String title, String type, List<String> directors, List<String> actors,
                 List<Genre> genres, List<Rating> ratings, String plot,
                 Double averageRating, String duration, int releaseYear) {
        super(title, type, directors, actors, genres, ratings, plot, averageRating);
        this.duration = duration;
        this.releaseYear = releaseYear;

        if (genres == null) {
            this.setGenres(new ArrayList<>());
        }
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    @Override
    public void displayInfo() {
        System.out.println();
        System.out.println("Title: " + getTitle());
        System.out.println("Type: Movie" );

        if(getDirectors() != null)
            System.out.println("Directors: " + getDirectors());

        if(getActors() != null)
            System.out.println("Actors: " + getActors());

        if(getGenres() != null)
            System.out.println("Genres: " + getGenres());

        List<Rating> ratings = getRatings();
        if(ratings != null) {
            System.out.println("Ratings: ");
            for (Rating rating : ratings) {
                rating.displayInfo();
                System.out.println();
            }
        }

        if(getPlot() != null)
            System.out.println("Plot: " + getPlot());

        System.out.println("Average Rating: " + getAverageRating());
        System.out.println("Release Year: " + getReleaseYear());
        System.out.println("Duration: " + getDuration());
    }

    public String displayInfoGraphic() {
        StringBuilder result = new StringBuilder();

        result.append("\n");
        result.append("Title: ").append(getTitle()).append("\n");
        result.append("Type: Movie\n");

        if (getDirectors() != null)
            result.append("Directors: ").append(getDirectors()).append("\n");

        if (getActors() != null)
            result.append("Actors: ").append(getActors()).append("\n");

        if (getGenres() != null)
            result.append("Genres: ").append(getGenres()).append("\n");

        List<Rating> ratings = getRatings();
        if (ratings != null) {
            result.append("Ratings: \n");
            for (Rating rating : ratings) {
                result.append("    Username: ").append(rating.getUsername()).append("\n");
                result.append("    Rating: ").append(rating.getRating()).append("\n");
                result.append("    Comment: ").append(rating.getComment()).append("\n\n");
            }
        }

        if (getPlot() != null)
            result.append("Plot: ").append(getPlot()).append("\n");

        result.append("Average Rating: ").append(getAverageRating()).append("\n");
        result.append("Release Year: ").append(getReleaseYear()).append("\n");
        result.append("Duration: ").append(getDuration()).append("\n");

        return result.toString();
    }

    public int compareTo(Object o) {
        if (o instanceof Production) {
            Production otherProduction = (Production) o;
            return this.getTitle().compareTo(otherProduction.getTitle());
        } else if (o instanceof Actor) {
            Actor otherActor = (Actor) o;
            return this.getTitle().compareTo(otherActor.getName());
        }
        return 0;
    }
}