package SOLUTION;

import java.util.*;

public abstract class Staff<T extends Comparable<T>> extends User implements StaffInterface, ExperienceStrategy{
    private final List<Request> requests;
    private final SortedSet<T> contributions;

    public Staff(Information information, AccountType type, String username, int experience) {
        super(information, type, username, experience);
        this.requests = new ArrayList<>();
        this.contributions = new TreeSet<>();
    }

    @Override
    public int calculateExperience() {
        return 15;
    }

    public void addProductionSystem(Production p){
        System.out.println("Please complete the following information about the new Production: ");
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the title: ");
        String title = scanner.nextLine();
        p.setTitle(title);

        System.out.println("Enter the number of directors you want to add: ");
        String num = scanner.nextLine();
        int number = Integer.parseInt(num);

        List<String> directors = new ArrayList<>();
        for(int i = 0; i < number; i++) {
            System.out.println("Enter the name of the director: ");
            String director = scanner.nextLine();
            directors.add(director);
        }
        p.setDirectors(directors);

        System.out.println("Enter the number of actors you want to add: ");
        num = scanner.nextLine();
        number = Integer.parseInt(num);

        List<String> actors = new ArrayList<>();
        for(int i = 0; i < number; i++) {
            System.out.println("Enter the name of the actor: ");
            String actor = scanner.nextLine();
            actors.add(actor);
        }
        p.setActors(actors);

        System.out.println("Enter the number of genres you want to add: ");
        num = scanner.nextLine();
        number = Integer.parseInt(num);

        List<Genre> genres = new ArrayList<Genre>();
        for(int i = 0; i < number; i++) {
            System.out.println("These are the available genres: ");

            for (int j = 0; j < Genre.values().length; j++) {
                System.out.println((j + 1) + ") " + Genre.values()[j]);
            }
            System.out.print("Enter the number of the specific genre: ");
            String genreNum = scanner.nextLine();
            int genreNumber = Integer.parseInt(genreNum);
            genres.add(Genre.values()[genreNumber - 1]);
        }
        p.setGenres(genres);

        System.out.println("Enter the plot: ");
        String plot = scanner.nextLine();
        p.setPlot(plot);

        if(p instanceof Movie) {
            System.out.print("Enter the duration: ");
            String duration = scanner.nextLine();
            ((Movie) p).setDuration(duration);

            System.out.print("Enter the release year: ");
            String year = scanner.nextLine();
            ((Movie) p).setReleaseYear(Integer.parseInt(year));
        } else if(p instanceof Series) {
            System.out.print("Enter the release year: ");
            String year = scanner.nextLine();
            ((Series) p).setReleaseYear(Integer.parseInt(year));

            System.out.println("Enter the number of season you want to add: ");
            num = scanner.nextLine();
            int numSeasons = Integer.parseInt(num);
            ((Series) p).setNumSeasons(numSeasons);

            Map<String, List<Episode>> seasons = new HashMap<>();

            for(int i = 0; i < numSeasons; i++) {
                System.out.println("Enter the name of Season " + (i + 1) + ": ");
                String seasonName = scanner.nextLine();

                System.out.println("Enter the number of episodes for Season " + (i + 1) + ": ");
                num = scanner.nextLine();
                int numEpisodes = Integer.parseInt(num);

                List<Episode> episodes = new ArrayList<>();

                for (int j = 0; j < numEpisodes; j++) {
                    System.out.println("Enter the name of Episode " + (j + 1) + ": ");
                    String episodeName = scanner.nextLine();

                    System.out.println("Enter the duration of Episode " + (j + 1) + ": ");
                    String episodeDuration = scanner.nextLine();

                    episodes.add(new Episode(episodeName, episodeDuration));
                }
                seasons.put(seasonName, episodes);
            }
            ((Series) p).setSeasons(seasons);
        }

        if(this instanceof Contributor) {
            if(((User<?>)this).getExperience() != Integer.MAX_VALUE) {
                int experienceGain = ((User<?>) this).getExperience() + calculateExperience();
                ((User<?>) this).setExperience(experienceGain);
            }
        }
    }
    public void addActorSystem(Actor a){
        System.out.println("Please complete the following information about the new Actor: ");
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the name: ");
        String name = scanner.nextLine();
        a.setName(name);

        System.out.println("Enter the biography: ");
        String biography = scanner.nextLine();
        a.setBiography(biography);

        System.out.println("Enter the number of performances you want to add: ");
        String num = scanner.nextLine();
        int number = Integer.parseInt(num);

        List<Actor.Performance> performances = new ArrayList<>();
        Actor.Performance performance;
        for(int i = 0; i < number; i++) {
            System.out.println("Enter the title: ");
            String title = scanner.nextLine();
            System.out.println("Choose the type: ");
            System.out.println("1) Movie");
            System.out.println("2) Series");
            String type = scanner.nextLine();
            int typeNumber = Integer.parseInt(type);

            if(typeNumber == 1) {
                performance = new Actor.Performance(title, "Movie");
            } else {
                performance = new Actor.Performance(title, "Series");
            }
            performances.add(performance);
        }
        a.setPerformances(performances);

        if(this instanceof Contributor) {
            if(((User<?>)this).getExperience() != Integer.MAX_VALUE){
                int experienceGain = ((User<?>) this).getExperience() + calculateExperience();
                ((User<?>) this).setExperience(experienceGain);
            }
        }
    }
    public Production removeProductionSystem(String name){
        for (T contribution : this.contributions) {
            if (contribution instanceof Production) {
                Production production = (Production) contribution;
                if (name.equals(production.getTitle())) {
                    removeContribution(contribution);
                    return production;
                }
            }
        }
        return null;
    }

    public Actor removeActorSystem(String name) {
        for (T contribution : this.contributions) {
            if (contribution instanceof Actor) {
                Actor actor = (Actor) contribution;
                if (name.equals(actor.getName())) {
                    removeContribution(contribution);
                    return actor;
                }
            }
        }
        return null;
    }

    public void addContributionsHolder() {
        if(this instanceof Contributor) {
            for (T contribution : this.contributions) {
                if (contribution instanceof Actor) {
                    Actor actor = (Actor) contribution;
                    ContributionHolder.addActor(actor);
                } else if(contribution instanceof Production) {
                    Production production = (Production) contribution;
                    ContributionHolder.addProduction(production);
                }
            }
        }
    }

    public int modifyProductionMessage(Production p) {
        System.out.println("Choose what you want to modify: ");
        System.out.println("1) Title");
        System.out.println("2) Directors");
        System.out.println("3) Actors");
        System.out.println("4) Plot");
        System.out.println("5) Release year");
        if(p instanceof Movie) {
            System.out.println("6) Duration");
        } else if(p instanceof Series) {
            System.out.println("6) Number of seasons");
            System.out.println("7) Seasons");
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number: ");
        String number = scanner.next();

        return Integer.parseInt(number);
    }

    public void updateProduction(Production p){
        Scanner scanner = new Scanner(System.in);
        int modifyNum = modifyProductionMessage(p);
        if(modifyNum == 1) {
            System.out.println("Enter the updated title: ");
            String title = scanner.nextLine();
            p.setTitle(title);
        } else if(modifyNum == 2) {
            System.out.println("This is the list of directors: ");
            System.out.println("Directors: " + p.getDirectors() + "\n");
            System.out.println("1) Remove a director");
            System.out.println("2) Add a director");
            System.out.println("Enter the number of the action: ");
            String text = scanner.nextLine();
            int num = Integer.parseInt(text);
            if(num == 1) {
                System.out.println("Enter the name of the director you want to remove: ");
                String name = scanner.nextLine();
                p.removeDirector(name);
            } else if(num == 2) {
                System.out.println("Enter the name of the new director: ");
                String name = scanner.nextLine();
                p.addDirector(name);
            }
        } else if(modifyNum == 3) {
            System.out.println("This is the list of actors: ");
            System.out.println("Actors: " + p.getActors() + "\n");
            System.out.println("1) Remove an actor");
            System.out.println("2) Add a actor");
            System.out.println("Enter the number of the action: ");
            String text = scanner.nextLine();
            int num = Integer.parseInt(text);
            if(num == 1) {
                System.out.println("Enter the name of the actor you want to remove: ");
                String name = scanner.nextLine();
                p.removeActor(name);
            } else if(num == 2) {
                System.out.println("Enter the name of the new actor: ");
                String name = scanner.nextLine();
                p.addActor(name);
            }
        } else if(modifyNum == 4) {
            System.out.println("Enter the updated plot: ");
            String plot = scanner.nextLine();
            p.setPlot(plot);
        }

        if(p instanceof Movie) {
            Movie movie = (Movie) p;
            if(modifyNum == 5) {
                System.out.println("Enter the updated year: ");
                String year = scanner.nextLine();
                movie.setReleaseYear(Integer.parseInt(year));
            } else if(modifyNum == 6) {
                System.out.println("Enter the updated duration: ");
                String duration = scanner.nextLine();
                movie.setDuration(duration);
            }
        } else {
            Series series = (Series) p;
            if(modifyNum == 5) {
                System.out.println("Enter the updated year: ");
                String year = scanner.nextLine();
                series.setReleaseYear(Integer.parseInt(year));
            } else if(modifyNum == 6) {
                System.out.println("Enter the updated number of seasons: ");
                String seasons = scanner.nextLine();
                series.setNumSeasons(Integer.parseInt(seasons));
            }
        }
        System.out.println("\nThe productions was updated sucessfully!");
        p.displayInfo();
    }

    public int modifyActorMessage(Actor a) {
        System.out.println("Choose what you want to modify: ");
        System.out.println("1) Name");
        System.out.println("2) Biography");
        System.out.println("3) Performances");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number: ");
        String number = scanner.next();

        return Integer.parseInt(number);
    }
    public void updateActor(Actor a){
        Scanner scanner = new Scanner(System.in);
        int modifyNum = modifyActorMessage(a);
        if(modifyNum == 1) {
            System.out.println("Enter the updated name: ");
            String name = scanner.nextLine();
            a.setName(name);
        } else if(modifyNum == 2) {
            System.out.println("Enter the updated biography: ");
            String biography = scanner.nextLine();
            a.setBiography(biography);
        } else if(modifyNum == 3) {
            System.out.println("Performances:");
            List<Actor.Performance> performances = a.getPerformances();
            if (performances != null) {
                for (Actor.Performance performance : performances) {
                    System.out.println("  Title: " + performance.getTitle());
                    System.out.println("  Type: " + performance.getType());
                    System.out.println();
                }
            } else {
                System.out.println("No performances available.");
            }
            System.out.println("Enter the title of the performance you want to modify: ");
            String title = scanner.nextLine();
            Actor.Performance foundPerformance = a.findPerformanceByTitle(title);
            if(foundPerformance == null) {
                System.out.println("No performances found.");
            } else {
                System.out.println("Choose what you want to modify: ");
                System.out.println("1) Title");
                System.out.println("2) Type");
                String choice = scanner.nextLine();

                if(Integer.parseInt(choice) == 1) {
                    System.out.println("Enter the updated title: ");
                    String name = scanner.nextLine();
                    foundPerformance.setTitle(name);
                } else if(Integer.parseInt(choice) == 2) {
                    if(foundPerformance.getType().equals("Movie")) {
                        foundPerformance.setType("Series");
                    } else {
                        foundPerformance.setType("Movie");
                    }
                }
            }
        }
        System.out.println("\nThe productions was updated sucessfully!");
        a.displayInfo();
    }
    public void resolveUserRequests(){
        if(this.requests.isEmpty()) {
            System.out.println("You don't have requests!\n");
        } else {
            System.out.println("Your Requests:");
            int i = 0;
            for (Request request : this.getRequests()) {
                System.out.println("Request number: " + (i + 1));
                i++;
                request.displayInfo();
                System.out.println();
            }
        }
    }

    public void addContribution(T contribution) {
        contributions.add(contribution);
    }

    public void removeContribution(T contribution) {
        contributions.remove(contribution);
    }

    public SortedSet<T> getContributions() {
        return contributions;
    }

    public boolean containsContribution(T contribution) {
        return contributions.contains(contribution);
    }

    public void displayProductionContributions() {
        for(Object contribution : contributions) {
            if(contribution instanceof Production) {
                Production production = (Production) contribution;
                System.out.println("  Title: " + production.getTitle());
            }
        }
    }

    public void displayActorContributions() {
        for(Object contribution : contributions) {
            if(contribution instanceof Actor) {
                Actor actor = (Actor) contribution;
                System.out.println("  Name: " + actor.getName());
            }
        }
    }

    public void addRequest(Request request) {
        requests.add(request);
    }

    public void deleteRequest(Request request) {
        requests.remove(request);
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void displayRequests(){
        for(Request request : requests) {
            request.displayInfo();
            System.out.println();
        }
    }
}
