package SOLUTION;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Comparator;

public class Action {
    private static Action instance;
    private final User<?> user;
    private final IMDB imdb;

    public Action(IMDB imdb, User<?> user) {
        this.imdb = imdb;
        this.user = user;
    }

    public static Action getInstance(IMDB imdb, User<?> user) {
        if (instance == null) {
            instance = new Action(imdb, user);
        }
        return instance;
    }

    public void displayAction(AccountType accountType) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose action:");
        System.out.println("1) View productions details");
        System.out.println("2) View actors details");
        System.out.println("3) View notifications");
        System.out.println("4) Search for actor/movie/series");
        System.out.println("5) Add/Delete actor/movie/series to/from favorites");
        switch (accountType) {
            case Regular:
                System.out.println("6) Add/Delete request");
                System.out.println("7) Add/Delete rating for a production");
                System.out.println("8) Logout");
                break;
            case Contributor:
                System.out.println("6) Add/Delete request");
                System.out.println("7) Add/Delete actor/movie/series from system");
                System.out.println("8) Solve a request");
                System.out.println("9) Update Production Details");
                System.out.println("10) Update Actor Details");
                System.out.println("11) Logout");
                break;
            case Admin:
                System.out.println("6) Add/Delete user from system");
                System.out.println("7) Add/Delete actor/movie/series from system");
                System.out.println("8) Solve a request");
                System.out.println("9) Update Production Details");
                System.out.println("10) Update Actor Details");
                System.out.println("11) Logout");
                break;
        }
        try {
            System.out.println();
            System.out.print("      Enter the number of the action: ");
            String action = scanner.nextLine();
            int actionNumber = Integer.parseInt(action);
            if (1 <= actionNumber && actionNumber <= 5) {
                executeSimpleAction(actionNumber);
            } else {
                executeAction(accountType, actionNumber);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        } catch (InvalidCommandException e) {
            System.out.println("Invalid input. Please enter a valid action number.");
        }
    }

    public void executeSimpleAction(int actionNumber) {
        Scanner scanner = new Scanner(System.in);
        switch (actionNumber) {
            case 1:
                handleViewProductions(scanner);
                break;
            case 2:
                handleViewActors(scanner);
                break;
            case 3:
                imdb.displayNotifications(user);
                break;
            case 4:
                handleSearchAction(scanner);
                break;
            case 5:
                handleFavoritesAction(scanner);
                break;
        }

        displayAction(this.user.getUserType());
    }

    public void executeAction(AccountType accountType, int actionNumber) throws InvalidCommandException {
        switch (accountType) {
            case Regular:
                if(actionNumber == 7) {
                    handleAddDeleteRating();
                    break;
                } else if(actionNumber == 8) {
                    imdb.logout(user);
                    break;
                }
            case Contributor:
                if(actionNumber == 6) {
                    handleAddDeleteRequest();
                    break;
                } else if(actionNumber == 8) {
                    handleRequestsContributor();
                    break;
                }
            case Admin:
                if(actionNumber == 6) {
                    handleAddDeleteUser();
                } else if(actionNumber == 7) {
                    handleAddDeleteSystem();
                } else if(actionNumber == 8) {
                    handleRequestsAdmin();
                } else if(actionNumber == 9) {
                    updateProductions();
                } else if(actionNumber == 10) {
                    updateActors();
                } else if(actionNumber == 11) {
                    imdb.logout(user);
                    break;
                }
                throw new InvalidCommandException("Invalid command number: " + actionNumber);
        }
    }
    private void handleViewProductions(Scanner scanner) {

        imdb.displayProductions();

        System.out.println("Choose action:");
        System.out.println("1) Filter the productions by genre");
        System.out.println("2) Filter the productions according to the number of evaluations received");
        System.out.println("3) Filter the productions by type");
        System.out.println("4) Filter the productions by the actor playing");
        System.out.println("5) Filter the productions by the release year");
        System.out.println("6) Filter the productions by the director");
        System.out.println("7) Return to the main menu");

        try {
            System.out.print("\nEnter the number of the action: ");
            int filterOption = Integer.parseInt(scanner.nextLine());
            System.out.println();

            switch (filterOption) {
                case 1:
                    try {
                        filterProductionsGenre();
                    } catch (InvalidCommandException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    filterProductionsRatings();
                    break;
                case 3:
                    filterProductionsType();
                    break;
                case 4:
                    filterProductionsActor();
                    break;
                case 5:
                    filterProductionsYear();
                    break;
                case 6:
                    filterProductionsDirector();
                    break;
                case 7:
                    displayAction(user.getUserType());
                    break;
                default:
                    throw new InvalidCommandException("Invalid command. Please enter 1, 2, or 3.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter a valid number.\n");
        } catch (InvalidCommandException e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
        }
    }


    public void filterProductionsGenre() throws InvalidCommandException {
        System.out.println("Choose the genre:");
        Genre[] genres = Genre.values();

        for (int i = 0; i < genres.length; i++) {
            System.out.println((i + 1) + ") " + genres[i]);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("      Enter the number of the target genre: ");

        try {
            int selectedGenreIndex = Integer.parseInt(scanner.nextLine());

            if (selectedGenreIndex >= 1 && selectedGenreIndex <= genres.length) {
                Genre selectedGenre = genres[selectedGenreIndex - 1];

                for (Production production : this.imdb.getProductions()) {
                    if (production.hasGenre(selectedGenre)) {
                        if (production instanceof Movie) {
                            production.displayInfo();
                        } else if (production instanceof Series) {
                            production.displayInfo();
                        }
                        System.out.println();
                    }
                }

                displayAction(this.user.getUserType());
            } else {
                throw new InvalidCommandException("\n   Invalid genre number. Please enter a valid number.\n");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n  Invalid input. Please enter a valid number.\n");
        }
    }

    public void filterProductionsRatings() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("      Enter minimum number of ratings: ");

        try {
            int ratingsNumber = Integer.parseInt(scanner.nextLine());
            for (Production production : this.imdb.getProductions()) {
                if (production.getNumRatings() >= ratingsNumber) {
                    if (production instanceof Movie) {
                        production.displayInfo();
                    } else if (production instanceof Series) {
                        production.displayInfo();
                    }
                    System.out.println();
                }
            }
            displayAction(this.user.getUserType());
        } catch (NumberFormatException e) {
            System.out.println("\n  Invalid input. Please enter a valid number.\n");
        }
    }

    private void filterProductionsType() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose action:");
        System.out.println("1) Movie");
        System.out.println("2) Series");
        System.out.println("3) Return to the main menu");

        try {
            System.out.print("\n    Enter the number of the action: ");
            int sortOption = Integer.parseInt(scanner.nextLine());
            System.out.println();

            switch (sortOption) {
                case 1:
                    sortProductionsMovie();
                    break;
                case 2:
                    sortProductionsSeries();
                    break;
                case 3:
                    displayAction(user.getUserType());
                    break;
                default:
                    throw new InvalidCommandException("Invalid command. Please enter 1, 2, or 3.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter a valid number.\n");
        } catch (InvalidCommandException e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    private void filterProductionsActor() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("    Enter the name of the actor that you want: ");
        String name = scanner.nextLine();

        if(imdb.findActorByName(name, imdb.getActors()) != null) {
            for (Production production : this.imdb.getProductions()) {
                List<String> actors = production.getActors();
                if(actors != null) {
                    for(String nameActor : actors) {
                        if(nameActor.equals(name)) {
                            if (production instanceof Movie) {
                                production.displayInfo();
                            } else if (production instanceof Series) {
                                production.displayInfo();
                            }
                            System.out.println();
                        }
                    }
                }
            }
        } else {
            System.out.println("\nActor was not found!\n");
        }
    }

    private void filterProductionsDirector() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("    Enter the name of the director that you want: ");
        String name = scanner.nextLine();

            for (Production production : this.imdb.getProductions()) {
                List<String> directors = production.getDirectors();
                if(directors != null) {
                    for(String nameDirector : directors) {
                        if(nameDirector.equals(name)) {
                            if (production instanceof Movie) {
                                production.displayInfo();
                            } else if (production instanceof Series) {
                                production.displayInfo();
                            }
                            System.out.println();
                        }
                    }
                }
            }
    }
    private void filterProductionsYear() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("      Enter year: ");

        try {
            int year = Integer.parseInt(scanner.nextLine());
            int realeaseYear;
            for (Production production : this.imdb.getProductions()) {
                if (production instanceof Movie) {
                    realeaseYear = ((Movie)production).getReleaseYear();
                    if(realeaseYear == year) {
                        production.displayInfo();
                        System.out.println();
                    }
                } else if (production instanceof Series) {
                    realeaseYear = ((Series)production).getReleaseYear();
                    if(realeaseYear == year) {
                        production.displayInfo();
                        System.out.println();
                    }
                }
            }
            displayAction(this.user.getUserType());
        } catch (NumberFormatException e) {
            System.out.println("\n  Invalid input. Please enter a valid number.\n");
        }
    }

    private void sortProductionsMovie() {
        List<Production> productions = imdb.getProductions();
        if (productions != null) {
            for (Production production : productions) {
                if(production instanceof Movie) {
                    ((Movie)production).displayInfo();
                }
            }
        } else {
            System.out.println("No movies available.");
        }
    }

    private void sortProductionsSeries() {
        List<Production> productions = imdb.getProductions();
        if (productions != null) {
            for (Production production : productions) {
                if(production instanceof Series) {
                    ((Series)production).displayInfo();
                }
            }
        } else {
            System.out.println("No series available.");
        }
    }

    private void handleViewActors(Scanner scanner) {

        imdb.displayActors();

        System.out.println("Choose action:");
        System.out.println("1) Sort the actors by name in alphabetical order");
        System.out.println("2) Sort the actors by name in reverse alphabetical order");
        System.out.println("3) Sort the actors by the number of performances");
        System.out.println("4) Return to the main menu");

        try {
            System.out.print("\n    Enter the number of the action: ");
            int sortOption = Integer.parseInt(scanner.nextLine());
            System.out.println();

            switch (sortOption) {
                case 1:
                    sortActorsAscending();
                    break;
                case 2:
                    sortActorsDescending();
                    break;
                case 3:
                    sortActorsPerformances();
                case 4:
                    displayAction(user.getUserType());
                    break;
                default:
                    throw new InvalidCommandException("Invalid command. Please enter 1, 2, or 3.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter a valid number.\n");
        } catch (InvalidCommandException e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    public void sortActorsAscending() {
        List<Actor> actors = imdb.getActors();
        if (actors != null) {
            List<Actor> sortedActors = new ArrayList<>(actors);
            sortedActors.sort(Comparator.comparing(Actor::getName));
            for (Actor actor : sortedActors) {
                actor.displayInfo();
            }
        } else {
            System.out.println("No actors available.");
        }
    }

    private void sortActorsDescending() {
        List<Actor> actors = imdb.getActors();
        if (actors != null) {
            List<Actor> sortedActors = new ArrayList<>(actors);
            sortedActors.sort(Comparator.comparing(Actor::getName).reversed());
            for (Actor actor : sortedActors) {
                actor.displayInfo();
            }
        } else {
            System.out.println("No actors available.");
        }
    }

    private void sortActorsPerformances() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("      Enter the number of performances: ");

        try {
            int performances = Integer.parseInt(scanner.nextLine());

            List<Actor> actors = imdb.getActors();
            if (actors != null) {
                for (Actor actor : actors) {
                    if(actor.getPerformances().size() == performances) {
                        actor.displayInfo();
                    }

                }
            } else {
                System.out.println("No actors available.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n  Invalid input. Please enter a valid number.\n");
        }
    }

    private void handleSearchAction(Scanner scanner) {
        System.out.println("\nChoose action:");
        System.out.println("1) Search for an actor");
        System.out.println("2) Search for a production (movie/series)");
        System.out.println("3) Return to the main menu");

        try {
            System.out.print("\n    Enter the number of the action: ");
            int searchOption = Integer.parseInt(scanner.nextLine());
            System.out.println();

            switch (searchOption) {
                case 1:
                    searchActor();
                    break;
                case 2:
                    searchProduction();
                    break;
                case 3:
                    displayAction(user.getUserType());
                    break;
                default:
                    throw new InvalidCommandException("Invalid command. Please enter 1, 2, or 3.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter a valid number.\n");
        } catch (InvalidCommandException e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    public void searchActor() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("    Enter the name of the actor to view its details: ");
        String name = scanner.nextLine();

        if(imdb.findActorByName(name, imdb.getActors()) != null) {
            Actor found = imdb.findActorByName(name, imdb.getActors());
            found.displayInfo();
        } else {
            System.out.println("\nActor was not found!\n");
        }
    }

    public void searchProduction() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("    Enter the title of the production to view its details: ");
        String name = scanner.nextLine();

        if(imdb.findProductionByTitle(name, imdb.getProductions()) != null) {
            Production found = imdb.findProductionByTitle(name, imdb.getProductions());
            found.displayInfo();
        } else {
            System.out.println("\nProduction was not found!\n");
        }
    }

    private void handleFavoritesAction(Scanner scanner) {
        System.out.println("\nThese are your favorites:\n");
        user.displayFavorites();

        System.out.println("\nChoose action:");
        System.out.println("1) Add an actor/production");
        System.out.println("2) Remove an actor/production");
        System.out.println("3) Return to the main menu");
        try {
            System.out.print("\nEnter the number of the action: ");
            int favoriteOption = Integer.parseInt(scanner.nextLine());

            switch (favoriteOption) {
                case 1:
                    handleAddFavorite(scanner);
                    break;
                case 2:
                    handleRemoveFavorite(scanner);
                    break;
                default:
                    throw new InvalidCommandException("Invalid command. Please enter 1, 2, or 3.");
            }
            System.out.println("\nNow your favorites list contains: ");
            user.displayFavorites();
            System.out.println();
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter a valid number.\n");
        } catch (InvalidCommandException e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    private void handleAddFavorite(Scanner scanner) {
        System.out.println("Enter the name/title of the actor/production:");
        String name = scanner.nextLine();

        Production production = imdb.findProductionByTitle(name, imdb.getProductions());
        Actor actor = imdb.findActorByName(name, imdb.getActors());

        if (production != null) {
            ((User) user).addFavorite(production);
            System.out.println("The element was added successfully!");
        } else if (actor != null) {
            ((User) user).addFavorite(actor);
            System.out.println("The element was added successfully!");
        } else {
            System.out.println("Please provide a valid name/title");
        }
    }

    private void handleRemoveFavorite(Scanner scanner) {
        System.out.println("Enter the name/title of the actor/production:");
        String name = scanner.nextLine();

        if (!user.deleteFavoriteByName(name)) {
            System.out.println("\nPlease provide a correct name!\n");
        } else {
            System.out.println("The element was deleted successfully!\n");
        }
    }

    public void handleAddDeleteRating() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter the title of the production to view its details:");
        String name = scanner.nextLine();

        Production found = imdb.findProductionByTitle(name, imdb.getProductions());

        if (found != null) {
            found.displayInfo();

            if (!found.containsRatingByUser(user.getUsername())) {
                handleAddRating(scanner, found);
            } else {
                handleDeleteRating(scanner, found);
            }

            System.out.println();
            displayAction(user.getUserType());
        } else {
            System.out.println("Production was not found! Make sure you are introducing the correct name!");
        }
    }

    private void handleAddRating(Scanner scanner, Production production) {
        System.out.println("\nYou can add a rating for this production or go back to the main menu!\n");
        System.out.println("1) Add rating");
        System.out.println("2) Return to the main menu");

        try {
            System.out.print("\nEnter the number of the action: ");
            int number = Integer.parseInt(scanner.nextLine());

            switch (number) {
                case 1:
                    addRatingDetails(scanner, production);
                    break;
                case 2:
                    displayAction(user.getUserType());
                    break;
                default:
                    throw new InvalidCommandException("Invalid command. Please enter 1, 2.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter a valid number.\n");
        } catch (InvalidCommandException e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    private void addRatingDetails(Scanner scanner, Production production) throws InvalidCommandException {
        System.out.println("Enter the rating:");
        int ratingNumber = Integer.parseInt(scanner.nextLine());

        if (ratingNumber < 1 || ratingNumber > 10) {
            throw new InvalidCommandException("Rating must be between 1 and 10. Please enter a valid number.");
        }

        System.out.println("Enter the comment:");
        String comment = scanner.nextLine();

        Rating rating = new Rating(user.getUsername(), ratingNumber, comment);
        production.addRating(rating, user);

        for(Rating addedRating : production.getRatings()) {
            String username = addedRating.getUsername();
            User<?> found = imdb.findUserByUsername(username);

            if(found.getUserType().equals(AccountType.Regular)) {
                rating.addObserver((Regular) found);
            } else if (found.getUserType().equals(AccountType.Contributor)) {
                rating.addObserver((Contributor) found);
            } else {
                rating.addObserver((Admin) found);
            }
        }

        User<?> userContributor = imdb.findUserByProductionContribution(production);
        if(userContributor.getUserType().equals(AccountType.Regular)) {
            rating.addObserver((Regular) userContributor);
        } else if (userContributor.getUserType().equals(AccountType.Contributor)) {
            rating.addObserver((Contributor) userContributor);
        } else {
            rating.addObserver((Admin)userContributor);
        }
        rating.setRating(production.getTitle());

        System.out.println("\nRating added with success!");

        if(user.getExperience() == Integer.MAX_VALUE)
            System.out.println("User experience: - ");
        else
            System.out.println("User experience: " + user.getExperience());


        production.displayInfo();
    }

    private void handleDeleteRating(Scanner scanner, Production production) {
        System.out.println("\nYou can delete your rating for this production or go back to the main menu!\n");
        System.out.println("1) Delete rating");
        System.out.println("2) Return to the main menu");

        try {
            System.out.print("\nEnter the number of the action: ");
            int number = Integer.parseInt(scanner.nextLine());

            switch (number) {
                case 1:
                    production.deleteRatingByUsername(user.getUsername());
                    break;
                case 2:
                    displayAction(user.getUserType());
                    break;
                default:
                    throw new InvalidCommandException("Invalid command. Please enter 1, 2.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter a valid number.\n");
        } catch (InvalidCommandException e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    public void handleAddDeleteRequest() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("These are requests created by you:\n");

        List<Request> created = imdb.displayRequestsCreatedByUser(user.getUsername());

        System.out.println("Choose action:");
        System.out.println("1) Add request");
        System.out.println("2) Delete request");
        System.out.println("3) Return to the main menu");

        try {
            System.out.print("\nEnter the number of the action: ");
            int action = Integer.parseInt(scanner.nextLine());

            switch (action) {
                case 1:
                    addRequest();
                    break;
                case 2:
                    deleteRequest(created);
                    break;
                case 3:
                    displayAction(user.getUserType());
                    break;
                default:
                    throw new InvalidCommandException("Invalid command. Please enter 1, 2, or 3.");
            }
            displayAction(user.getUserType());
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter a valid number.\n");
            handleAddDeleteRequest();
        } catch (InvalidCommandException e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
            handleAddDeleteRequest();
        }
    }

    private void addRequest() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("These are requests types you can choose from:");
        RequestTypes[] requestTypes = RequestTypes.values();
        for (int i = 0; i < requestTypes.length; i++) {
            System.out.println((i + 1) + ") " + requestTypes[i]);
        }

        try {
            System.out.print("\nEnter the number of the request type: ");
            int number = Integer.parseInt(scanner.nextLine());
            System.out.println();
            number--;

            if(number < 0 || number >= requestTypes.length) {
                throw new InvalidCommandException("Invalid command. Please enter 1, 2, or 3.");
            }

            RequestTypes type = requestTypes[number];

            String username = user.getUsername();
            LocalDateTime createdDate = LocalDateTime.now();

            System.out.println("Enter the description of the problem:");
            String description = scanner.nextLine();

            String title = "";
            String to = "";
            User<?> toUser = null;

            if(type == RequestTypes.DELETE_ACCOUNT || type == RequestTypes.OTHERS) {
                to = "ADMIN";
            } else if(type == RequestTypes.MOVIE_ISSUE){
                System.out.println("Enter the title of the production:");
                title = scanner.nextLine();

                if(imdb.findProductionByTitle(title, imdb.getProductions()) != null) {
                    Production p = imdb.findProductionByTitle(title, imdb.getProductions());
                    toUser = imdb.findUserByProductionContribution(p);
                    to = toUser.getUsername();
                    if(toUser.equals(user)) {
                        System.out.println("You can not add a request for a production you added! Try again!\n");
                        return;
                    }
                } else {
                    System.out.println("Production not found! Try again!\n");
                    handleAddDeleteRequest();
                }


            } else if(type == RequestTypes.ACTOR_ISSUE) {
                System.out.println("Enter the name of the actor:");
                title = scanner.nextLine();

                if(imdb.findActorByName(title, imdb.getActors()) != null) {
                    Actor a = imdb.findActorByName(title, imdb.getActors());
                    toUser = imdb.findUserByActorContribution(a);
                    to = toUser.getUsername();
                    if(toUser.equals(user)) {
                        System.out.println("You can not add a request for an actor you added! Try again!\n");
                        return;
                    }
                } else {
                    System.out.println("Actor not found! Try again!\n");
                    handleAddDeleteRequest();
                }
            }

            Request request = new Request(type, createdDate, title, description, username, to);

            User<?> creationUser = imdb.findUserByUsername(username);
            if(creationUser.getUserType().equals(AccountType.Regular)) {
                request.addObserver((Regular) creationUser);
            } else if (creationUser.getUserType().equals(AccountType.Contributor)) {
                request.addObserver((Contributor) creationUser);
            } else {
                request.addObserver((Admin) creationUser);
            }

            if(toUser != null) {
                ((RequestsManager) user).createRequest(request, toUser);
                request.requestCreated(toUser);
                if(toUser.getUserType().equals(AccountType.Regular)) {
                    request.addObserver((Regular) toUser);
                } else if (toUser.getUserType().equals(AccountType.Contributor)) {
                    request.addObserver((Contributor) toUser);
                } else {
                    request.addObserver((Admin) toUser);
                }
            }

            imdb.addRequest(request);

            System.out.println("System Requests are:\n");
            imdb.viewRequests();
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter a valid number.\n");
        } catch (InvalidCommandException e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    public void deleteRequest(List<Request> created){
        if(created.isEmpty()) {
            System.out.println("\nYour request list is empty!\n");
            handleAddDeleteRequest();
        } else {
            Scanner scanner = new Scanner(System.in);
            try {
                System.out.println("Enter the number of the request you want to delete:");
                int number = Integer.parseInt(scanner.nextLine());
                number--;

                if (number < 0 || number >= created.size()) {
                    throw new InvalidCommandException("Invalid command. Please enter valid number.");
                } else {
                    Request toBeDelete = created.get(number);
                    imdb.deleteRequest(toBeDelete);
                    System.out.println("Request deleted successfully!");
                }
            } catch (InvalidCommandException e) {
                System.out.println();
                System.out.println(e.getMessage());
                System.out.println();
            }
        }
    }

    public void handleRequestsContributor() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("These are your requests:\n");
        ((Staff<?>) user).resolveUserRequests();

        try {
            System.out.println("Enter the number of the request you want to modify: ");
            String num = scanner.nextLine();
            int number = Integer.parseInt(num);
            number--;

            if (number < 0 || number >= ((Staff<?>) user).getRequests().size()) {
                throw new InvalidCommandException("Invalid command. Please enter valid number.");
            } else {
                Request found = ((Staff<?>) user).getRequests().get(number);
                System.out.println("This is the request you chose: ");
                found.displayInfo();

                solveRequestsContributor(scanner, found);
            }

            System.out.println("System Requests are:\n");
            imdb.viewRequests();

        } catch (InvalidCommandException e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    private void solveRequestsContributor(Scanner scanner, Request found) {
        System.out.println("These are your options: ");
        System.out.println("1) Solve");
        System.out.println("2) Reject");
        System.out.println("3) Return to the main menu");

        try {
            System.out.print("\nEnter the number of the action: ");
            int action = Integer.parseInt(scanner.nextLine());

            switch (action) {
                case 1:
                    solveRequest(scanner, found);
                    break;
                case 2:
                    found.setStatus("Reject", user);
                    imdb.deleteRequest(found);
                    break;
                case 3:
                    displayAction(user.getUserType());
                    break;
                default:
                    throw new InvalidCommandException("Invalid command. Please enter 1, 2, or 3.");
            }
            displayAction(user.getUserType());
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter a valid number.\n");
        } catch (InvalidCommandException e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    private void solveRequest(Scanner scanner, Request found) {
        System.out.println("These are your options: ");
        System.out.println("1) Go to solve the request");
        System.out.println("2) The request was solved successfully");

        try {
            System.out.print("\nEnter the number of the action: ");
            int action = Integer.parseInt(scanner.nextLine());

            switch (action) {
                case 1:
                    displayAction(user.getUserType());
                    break;
                case 2:
                    found.setStatus("Solved", user);

                    if(user.getExperience() == Integer.MAX_VALUE)
                        System.out.println("User experience: - ");
                    else
                        System.out.println("User experience: " + user.getExperience());

                    imdb.deleteRequest(found);
                    break;
                default:
                    throw new InvalidCommandException("Invalid command. Please enter 1, 2, or 3.");
            }
            displayAction(user.getUserType());
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter a valid number.\n");
        } catch (InvalidCommandException e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    private void handleRequestsAdmin() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("These are requests you can solve:\n");
        System.out.println("1) Your requests");
        System.out.println("2) All admins requests");

        try {
            System.out.print("\nEnter the number of the list: ");
            int action = Integer.parseInt(scanner.nextLine());

            switch (action) {
                case 1:
                    handleRequestsContributor();
                    break;
                case 2:
                    allAdminsRequests();
                    break;
                default:
                    throw new InvalidCommandException("Invalid command. Please enter 1 or 2.");
            }
            displayAction(user.getUserType());
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter a valid number.\n");
        } catch (InvalidCommandException e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    private void allAdminsRequests() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("These are your requests:\n");

        ((Admin) user).displayRequests();
        try {
            System.out.println("Enter the number of the request you want to modify: ");
            int number = Integer.parseInt(scanner.nextLine());
            number--;

            if (number < 0 || number >= RequestHolder.getAllRequests().size()) {
                throw new InvalidCommandException("Invalid command. Please enter valid number.");
            } else {
                Request found = RequestHolder.getAllRequests().get(number);
                System.out.println("This is the request you chose: ");
                found.displayInfo();

                solveRequestsContributor(scanner, found);
            }

            System.out.println("System Requests are:\n");
            imdb.viewRequests();
        } catch (InvalidCommandException e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    public void handleAddDeleteSystem() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nChoose action:");
        System.out.println("1) Add");
        System.out.println("2) Delete");
        System.out.println("3) Return to the main menu");

        try {
            System.out.print("\nEnter the number of the action: ");
            int action = Integer.parseInt(scanner.nextLine());

            switch (action) {
                case 1:
                    addOption();
                    break;
                case 2:
                    deleteOption();
                    break;
                case 3:
                    displayAction(user.getUserType());
                    break;
                default:
                    throw new InvalidCommandException("Invalid command. Please enter 1, 2, or 3.");
            }
            displayAction(user.getUserType());
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter a valid number.\n");
        } catch (InvalidCommandException e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    public void addOption() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nChoose action:");
        System.out.println("1) Add actor");
        System.out.println("2) Add movie");
        System.out.println("3) Add series");

        try {
            System.out.print("\n      Enter the number of the action: ");
            int action = Integer.parseInt(scanner.nextLine());
            switch (action) {
                case 1:
                    Actor a = new Actor();
                    ((Staff<?>) user).addActorSystem(a);
                    a.displayInfo();
                    imdb.addNewActor(a);
                    ((Staff) user).addContribution(a);

                    if(user.getExperience() == Integer.MAX_VALUE)
                        System.out.println("User experience: - ");
                    else
                        System.out.println("User experience: " + user.getExperience());

                    break;
                case 2:
                    Movie m = new Movie();
                    ((Staff<?>) user).addProductionSystem(m);
                    m.displayInfo();
                    ((Staff) user).addContribution(m);
                    imdb.addNewProduction(m);

                    if(user.getExperience() == Integer.MAX_VALUE)
                        System.out.println("User experience: - ");
                    else
                        System.out.println("User experience: " + user.getExperience());

                    break;
                case 3:
                    Series s = new Series();
                    ((Staff<?>) user).addProductionSystem(s);
                    s.displayInfo();
                    ((Staff) user).addContribution(s);
                    imdb.addNewProduction(s);

                    if(user.getExperience() == Integer.MAX_VALUE)
                        System.out.println("User experience: - ");
                    else
                        System.out.println("User experience: " + user.getExperience());
                    break;
                default:
                    throw new InvalidCommandException("Invalid command. Please enter 1, 2, or 3.");
            }
            displayAction(this.user.getUserType());
        }  catch (NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter a valid number.\n");
        } catch (InvalidCommandException e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    private void deleteOption() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose action:");
        System.out.println("1) Delete actor");
        System.out.println("2) Delete movie/series");
        System.out.println("3) Return to the main menu");

        try {
            System.out.print("\nEnter the number of the action: ");
            int action = Integer.parseInt(scanner.nextLine());

            switch (action) {
                case 1:
                    if(user instanceof Admin){
                        deleteActorAdmin(scanner);
                    } else {
                        deleteActor(scanner);
                    }
                    break;
                case 2:
                    deleteProduction(scanner);
                    break;
                case 3:
                    displayAction(user.getUserType());
                    break;
                default:
                    throw new InvalidCommandException("Invalid command. Please enter 1, 2, or 3.");
            }
            displayAction(user.getUserType());
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter a valid number.\n");
        } catch (InvalidCommandException e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    private void deleteActor(Scanner scanner){
        System.out.println("These are your actors contribution: ");
        ((Staff<?>)user).displayActorContributions();

        System.out.println("Enter the name of the actor you want to delete:");
        String name = scanner.nextLine();

        Actor found = ((Staff<?>) user).removeActorSystem(name);
        if(found == null) {
            System.out.println("\nPlease provide a correct name!\n");
        } else {
            imdb.deleteActor(found);
            System.out.println("The element was deleted successfully!\n");
            displayAction(this.user.getUserType());
        }
    }

    private void deleteActorAdmin(Scanner scanner) {
        System.out.println("These are your actors contribution: ");
        ((Staff<?>)user).displayActorContributions();

        System.out.println("These are the actors from the all admins list: ");
        ContributionHolder.displayActors();

        System.out.println("\nChoose action:");
        System.out.println("1) Delete actor from your contribution list");
        System.out.println("2) Delete actor from all admins list");
        System.out.println("3) Return to the main menu");

        try {
            System.out.print("\nEnter the number of the action: ");
            int action = Integer.parseInt(scanner.nextLine());

            String name;
            Actor found;

            switch (action) {
                case 1:
                    System.out.println("Enter the name of the actor you want to delete:");
                    name = scanner.nextLine();

                    found = ((Staff<?>) user).removeActorSystem(name);
                    if(found == null) {
                        System.out.println("\nPlease provide a correct name!\n");
                    } else {
                        imdb.deleteActor(found);
                        System.out.println("The element was deleted successfully!\n");
                        displayAction(this.user.getUserType());
                    }
                    break;
                case 2:
                    System.out.println("Enter the name of the actor you want to delete:");
                    name = scanner.nextLine();
                    found = ContributionHolder.removeActorContributions(name);
                    if(found == null) {
                        System.out.println("\nPlease provide a correct name!\n");
                    } else {
                        System.out.println("The element was deleted successfully!\n");
                        displayAction(this.user.getUserType());
                    }
                    break;
                case 3:
                    displayAction(user.getUserType());
                    break;
                default:
                    throw new InvalidCommandException("Invalid command. Please enter 1, 2, or 3.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter a valid number.\n");
        } catch (InvalidCommandException e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    private void deleteProduction(Scanner scanner) {
        System.out.println("These are your productions contribution: ");
        ((Staff<?>)user).displayProductionContributions();

        System.out.println("Enter the title of the production you want to delete:");
        String title = scanner.nextLine();

        Production found = ((Staff<?>) user).removeProductionSystem(title);
        if(found == null) {
            System.out.println("\nPlease provide a correct name!\n");
        } else {
            imdb.deleteProduction(found);
            System.out.println("The element was deleted successfully!\n");
            displayAction(this.user.getUserType());
        }
    }

    public void updateProductions() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("The productions added by you are: ");
        ((Staff<?>) user).displayProductionContributions();

        System.out.println("Enter the title of the production to view its details:");
        String name = scanner.nextLine();
        if(imdb.findProductionByTitle(name, imdb.getProductions()) != null) {
            Production found = imdb.findProductionByTitle(name, imdb.getProductions());
            found.displayInfo();
            ((Staff<?>) user).updateProduction(found);
            displayAction(this.user.getUserType());
        } else {
            System.out.println("Production was not found!");
        }
    }

    public void updateActors() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("The actors added by you are: ");
        ((Staff<?>) user).displayActorContributions();

        System.out.println("Enter the name of the actor to view its details:");
        String name = scanner.nextLine();
        if(imdb.findActorByName(name, imdb.getActors()) != null) {
            Actor found = imdb.findActorByName(name, imdb.getActors());
            found.displayInfo();
            ((Staff<?>)user).updateActor(found);
            displayAction(this.user.getUserType());
        } else {
            System.out.println("Actor was not found!");
        }
    }

    private void handleAddDeleteUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nChoose action:");
        System.out.println("1) Add");
        System.out.println("2) Delete");
        System.out.println("3) Return to the main menu");

        try {
            System.out.print("\nEnter the number of the action: ");
            int action = Integer.parseInt(scanner.nextLine());

            switch (action) {
                case 1:
                    addUser(scanner);
                    break;
                case 2:
                    deleteUser();
                    break;
                case 3:
                    displayAction(user.getUserType());
                    break;
                default:
                    throw new InvalidCommandException("Invalid command. Please enter 1, 2, or 3.");
            }
            displayAction(user.getUserType());
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter a valid number.\n");
        } catch (InvalidCommandException e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    private void addUser(Scanner scanner) {
        System.out.print("\nEnter the name of the person: ");
        String name = scanner.nextLine();

        String username = imdb.generateUniqueUsername(name);
        String password = imdb.generateStrongPassword();

        System.out.print("\nEnter the email of the person: ");
        String email = scanner.nextLine();

        System.out.println("Password: " + password);

        System.out.println("Choose the account type:");
        System.out.println("1) Regular");
        System.out.println("2) Contributor");
        System.out.println("3) Admin");

        int accountTypeChoice = Integer.parseInt(getUserInput("Enter the number of the desired account type: ", scanner));
        AccountType accountType;

        try {
            switch (accountTypeChoice) {
                case 1:
                    accountType = AccountType.Regular;
                    break;
                case 2:
                    accountType = AccountType.Contributor;
                    break;
                case 3:
                    accountType = AccountType.Admin;
                    break;
                default:
                    throw new InvalidCommandException("Invalid command. Please enter 1, 2, or 3.");
            }

            Credentials credentials = new Credentials(email, password);
            try {
                User.Information information = new User.Information.InformationBuilder()
                        .setName(name)
                        .setCredentials(credentials)
                        .setCountry(getUserInput("Enter the country: ", scanner))
                        .setAge(Integer.parseInt(getUserInput("Enter the age: ", scanner)))
                        .setGender(getUserInput("Enter the gender: ", scanner))
                        .build();

                User<?> newUser = null;
                switch (accountType) {
                    case Regular:
                        newUser = new Regular(information, username, 0);
                        break;
                    case Contributor:
                        newUser = new Contributor(information, username, 0);
                        break;
                    case Admin:
                        newUser = new Admin(information, username, 0);
                        break;
                }
                imdb.addUser(newUser);
                System.out.println("User added successfully!");

            } catch (InformationIncompleteException e) {
                System.out.println("Exception caught: " + e.getMessage());
            }
        } catch (InvalidCommandException e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    private static String getUserInput(String prompt, Scanner scanner) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private void deleteUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nThis is the list with all the users in the system: ");
        imdb.displayUsers();
        System.out.print("\nEnter the number of the user you want to delete: ");
        int number = Integer.parseInt(scanner.nextLine());
        number--;

        try{
            if(number < 0 ||  number >= imdb.getUsers().size()) {
                throw new InvalidCommandException("Invalid command. Please enter a valid number.");
            }
            User<?> user = imdb.getUsers().get(number);
            imdb.deleteUser(user);

            System.out.println("\nThe account was successfully deleted!");
            System.out.println("Actors :" + ContributionHolder.getActors());
        } catch (InvalidCommandException e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
        }
    }
}
