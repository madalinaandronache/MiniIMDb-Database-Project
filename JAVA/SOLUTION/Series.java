package SOLUTION;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Series extends Production{
    private int releaseYear;
    private int numSeasons;
    private Map<String, List<Episode>> seasons;

    public Series(){
        this.seasons = new HashMap<>();
    }

    public Series(String title, String type, List<String> directors, List<String> actors,
                  List<Genre> genres, List<Rating> ratings, String plot,
                  Double averageRating, int releaseYear, int numSeasons,
                  Map<String, List<Episode>> seasons) {
        super(title, type, directors, actors, genres, ratings, plot, averageRating);
        this.releaseYear = releaseYear;
        this.numSeasons = numSeasons;
        this.seasons = seasons;

        if (genres == null) {
            this.setGenres(new ArrayList<>());
        }
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getNumSeasons() {
        return numSeasons;
    }

    public void setNumSeasons(int numSeasons) {
        this.numSeasons = numSeasons;
    }

    public Map<String, List<Episode>> getSeasons() {
        return seasons;
    }

    public void setSeasons(Map<String, List<Episode>> seasons) {
        this.seasons = seasons;
    }

    @Override
    public void displayInfo() {
        System.out.println();
        System.out.println("Title: " + getTitle());
        System.out.println("Type: Series" );

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
        if(getNumSeasons() != 0) {
            System.out.println("Number of Seasons: " + getNumSeasons());

            seasons = getSeasons();
            if (seasons != null) {
                System.out.println("Seasons and Episodes:");
                for (Map.Entry<String, List<Episode>> entry : seasons.entrySet()) {
                    String seasonName = entry.getKey();
                    List<Episode> episodes = entry.getValue();

                    System.out.println("  Season: " + seasonName);
                    System.out.println("  Episodes: ");
                    for (Episode episode : episodes) {
                        System.out.println("    Episode Name: " + episode.getName());
                        System.out.println("    Duration: " + episode.getDuration());
                    }
                }
            }
        }
    }

    public String displayInfoGraphic() {
        StringBuilder result = new StringBuilder();

        result.append("\n");
        result.append("Title: ").append(getTitle()).append("\n");
        result.append("Type: Series\n");

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

        if (getNumSeasons() != 0) {
            result.append("Number of Seasons: ").append(getNumSeasons()).append("\n");

            seasons = getSeasons();
            if (seasons != null) {
                result.append("Seasons and Episodes:\n");
                for (Map.Entry<String, List<Episode>> entry : seasons.entrySet()) {
                    String seasonName = entry.getKey();
                    List<Episode> episodes = entry.getValue();

                    result.append("  Season: ").append(seasonName).append("\n");
                    result.append("  Episodes: \n");
                    for (Episode episode : episodes) {
                        result.append("    Episode Name: ").append(episode.getName()).append("\n");
                        result.append("    Duration: ").append(episode.getDuration()).append("\n");
                        result.append("\n");
                    }
                }
            }
        }
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
