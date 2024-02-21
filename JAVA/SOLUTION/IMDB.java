package SOLUTION;

import java.security.SecureRandom;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;


public class IMDB {
    private static IMDB instance;
    private List<Actor> actors;
    private List<Production> productions;
    private List<Request> requests;
    private List<User<?>> users;

    public IMDB(List<User<?>> users, List<Actor> actors, List<Request> requests, List<Production> productions) {
        this.users = users;
        this.actors = actors;
        this.requests = requests;
        this.productions = productions;
    }

    public static synchronized IMDB getInstance(List<User<?>> users, List<Actor> actors, List<Request> requests, List<Production> productions) {
        if (instance == null) {
            instance = new IMDB(users, actors, requests, productions);
        }
        return instance;
    }

    public User<?> findUserByProductionContribution(Production p) {
        for(User<?> user : users) {
            if(user instanceof Staff && ((Staff) user).containsContribution(p))
                return user;
        }
        return null;
    }

    public User<?> findUserByActorContribution(Actor actor) {
        for(User<?> user : users) {
            if(user instanceof Staff && ((Staff) user).containsContribution(actor))
                return user;
        }
        return null;
    }

    public User<?> findUserByEmail(String enteredEmail) {
        for (User<?> user : users) {
            if (user.getInformation().getCredentials().getEmail().equals(enteredEmail)) {
                return user;
            }
        }
        return null;
    }

    public void authentication() {
        boolean authenticated = false;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome back! Enter your credentials!\n");

        while (!authenticated) {
            System.out.print("      email: ");
            String enteredEmail = scanner.nextLine();
            System.out.print("      password: ");
            String enteredPassword = scanner.nextLine();

            User<?> foundUser = findUserByEmail(enteredEmail);
            if (foundUser != null && foundUser.authenticate(enteredEmail, enteredPassword)) {
                System.out.println();
                authenticated = true;

                System.out.println("Welcome back user " + enteredEmail);
                System.out.println("Username: " + foundUser.getUsername());

                if(foundUser.getExperience() == Integer.MAX_VALUE)
                    System.out.println("User experience: - ");
                else
                    System.out.println("User experience: " + foundUser.getExperience());
                System.out.println();

                Action action = Action.getInstance(this, foundUser);
                action.displayAction(foundUser.getUserType());
            } else {
                System.out.println("\nAuthentication failed. Enter your email address and password again.\n");
            }
        }
    }

    public void logout(User<?> user) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose action:");
        System.out.println("1) Log into the application again");
        System.out.println("2) Exit the application");

        try {
            System.out.print("\nEnter the number of the action: ");
            int choice = Integer.parseInt(scanner.nextLine());
            System.out.println();

            switch (choice) {
                case 1:
                    authentication();
                case 2:
                    user.logout();
                default:
                    throw new InvalidCommandException("Invalid command. Please enter 1 or 2.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter a valid number.\n");
        } catch (InvalidCommandException e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    public void parseData(){
        String fileActorsPath = "SOLUTION/actors.json";
        actors = parseActors(fileActorsPath);

        String fileProductionPath = "SOLUTION/production.json";
        productions = parseProductions(fileProductionPath);

        addActorsFromProductions();

        String fileAccountsPath = "SOLUTION/accounts.json";
        users = parseAccounts(fileAccountsPath);

        String fileRequestsPath = "SOLUTION/requests.json";
        requests = parseRequests(fileRequestsPath);
    }

    public void run() {
        System.out.println("Choose how you want to use the application:");
        System.out.println("1) Terminal version");
        System.out.println("2) GUI version");

        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("\nEnter the number of the choice: ");
            int choice = Integer.parseInt(scanner.nextLine());
            System.out.println();

            switch (choice) {
                case 1:
                    authentication();
                    break;
                case 2:
                    new Graphic(this);
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

    public Production findProductionByTitle(String title, List<Production> productions) {
        for (Production production : productions) {
            if (production.getTitle().equals(title)) {
                return production;
            }
        }
        return null;
    }

    public Actor findActorByName(String name, List<Actor> actors) {
        for (Actor actor : actors) {
            if (actor.getName().equals(name)) {
                return actor;
            }
        }
        return null;
    }

    // trebuie adaugati si la lista de actori din imdb
    public void addActorsFromProductions() {
        for(Production production : productions) {
            for(String name : production.getActors()) {
                if(findActorByName(name, this.actors) == null) {
                    List<Actor.Performance> performances = new ArrayList<>();
                    Actor.Performance performance = new Actor.Performance(production.getTitle(), production.getType());
                    performances.add(performance);
                    Actor actor = new Actor(name, performances , null);
                    ContributionHolder.addActor(actor);
                }
            }
        }
    }

    public User<?> findUserByUsername(String username) {
        for(User<?> user : users) {
            if(user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    private List<Actor> parseActors(String filePath) {
        try {
            String json = new String(Files.readAllBytes(Paths.get(filePath)));
            return new Gson().fromJson(json, new TypeToken<List<Actor>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Request> parseRequests(String filePath) {
        try {
            String json = new String(Files.readAllBytes(Paths.get(filePath)));
            JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();

            List<Request> result = new ArrayList<>();
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();

                RequestTypes type = RequestTypes.valueOf(jsonObject.get("type").getAsString());
                String title = "";
                if (jsonObject.has("actorName")) {
                    title = jsonObject.get("actorName").getAsString();
                } else if(jsonObject.has("movieTitle")) {
                    title = jsonObject.get("movieTitle").getAsString();
                }

                String description = jsonObject.get("description").getAsString();
                String username = jsonObject.get("username").getAsString();
                String to = jsonObject.get("to").getAsString();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                LocalDateTime createdDate = LocalDateTime.parse(jsonObject.get("createdDate").getAsString(), formatter);

                Request request = new Request(type, createdDate, title, description, username, to);

                User<?> creationUser = findUserByUsername(username);
                if(creationUser.getUserType().equals(AccountType.Regular)) {
                    request.addObserver((Regular) creationUser);
                } else if (creationUser.getUserType().equals(AccountType.Contributor)) {
                    request.addObserver((Contributor) creationUser);
                } else {
                    request.addObserver((Admin) creationUser);
                }

                if(request.getType() == RequestTypes.DELETE_ACCOUNT || request.getType() == RequestTypes.OTHERS) {
                    RequestHolder.addRequest(request);
                } else {
                    User<?> user = findUserByUsername(to);
                    if(user == null) {
                        System.out.println("Eroare! Utilizatorul nu a fost gasit!");
                    } else {
                        ((Staff<?>)user).addRequest(request);
                    }
                }
                result.add(request);
            }

            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Request> displayRequestsCreatedByUser(String username){
        List<Request> created = new ArrayList<>();
        int i = 0;
        for(Request request : requests) {
            if(request.getUsername().equals(username)) {
                System.out.println("Request number " + (i + 1) + "\n");
                i++;
                request.displayInfo();
                created.add(request);
                System.out.println();
            }
        }
        return created;
    }


    private List<Production> parseProductions(String filePath) {
        try {
            String json = new String(Files.readAllBytes(Paths.get(filePath)));
            JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();

            List<Production> result = new ArrayList<>();
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                String type = jsonObject.get("type").getAsString();
                if ("Movie".equals(type)) {
                    result.add(new Gson().fromJson(jsonObject, Movie.class));
                } else if ("Series".equals(type)) {
                    result.add(new Gson().fromJson(jsonObject, Series.class));
                }
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void processFavoritesProductions(JsonObject jsonObject, User user) {
        JsonArray favoriteProductions = jsonObject.getAsJsonArray("favoriteProductions");
        if (favoriteProductions != null) {
            for (JsonElement favProductionElement : favoriteProductions) {
                String productionTitle = favProductionElement.getAsString();
                Production production = findProductionByTitle(productionTitle, productions);
                if (production != null) {
                    user.addFavorite(production);
                }
            }
        }
    }

    private void processFavoritesActors(JsonObject jsonObject, User user) {
        JsonArray favoriteActors = jsonObject.getAsJsonArray("favoriteActors");
        if (favoriteActors != null) {
            for (JsonElement favActorsElement : favoriteActors) {
                String actorName = favActorsElement.getAsString();
                Actor actor = findActorByName(actorName, actors);
                if (actor != null) {
                    user.addFavorite(actor);
                }
            }
        }
    }

    private void processContributionProductions(JsonObject jsonObject, User user) {
        JsonArray contributionProductions = jsonObject.getAsJsonArray("productionsContribution");
        if(contributionProductions != null) {
            for(JsonElement contProductionsElement : contributionProductions) {
                String productionTitle = contProductionsElement.getAsString();
                Production production = findProductionByTitle(productionTitle, productions);
                if (production != null) {
                    ((Staff) user).addContribution(production);
                }
            }
        }
    }

    private void processContributionActors(JsonObject jsonObject, User user) {
        JsonArray contributionActors = jsonObject.getAsJsonArray("actorsContribution");
        if(contributionActors != null) {
            for(JsonElement contActorsElement : contributionActors) {
                String actorName = contActorsElement.getAsString();
                Actor actor = findActorByName(actorName, actors);
                if (actor != null) {
                    ((Staff) user).addContribution(actor);
                }
            }
        }
    }

    private List<User<?>> parseAccounts(String filePath) {
        try {
            String json = new String(Files.readAllBytes(Paths.get(filePath)));
            JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();

            List<User<?>> result = new ArrayList<>();
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();

                String username = jsonObject.get("username").getAsString();
                String userType = jsonObject.get("userType").getAsString();
                JsonElement experienceElement = jsonObject.get("experience");

                int experience = (experienceElement != null && !experienceElement.isJsonNull()) ? experienceElement.getAsInt() : Integer.MAX_VALUE;
                try {
                    User.Information information = new User.Information.InformationBuilder()
                            .setName(jsonObject.getAsJsonObject("information").get("name").getAsString())
                            .setCountry(jsonObject.getAsJsonObject("information").get("country").getAsString())
                            .setAge(jsonObject.getAsJsonObject("information").get("age").getAsInt())
                            .setGender(jsonObject.getAsJsonObject("information").get("gender").getAsString())
                            .setBirthDate(LocalDate.parse(jsonObject.getAsJsonObject("information").get("birthDate").getAsString(),
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                            .setCredentials(parseCredentials(jsonObject.getAsJsonObject("information").getAsJsonObject("credentials")))
                            .build();

                    AccountType accountType = AccountType.valueOf(userType);

                    User user = UserFactory.createUser(accountType, information, username, experience);
                    JsonArray notificationsArray = jsonObject.getAsJsonArray("notifications");

                    if (notificationsArray != null) {
                        List<String> notifications = new ArrayList<>();
                        for (JsonElement notificationElement : notificationsArray) {
                            notifications.add(notificationElement.getAsString());
                        }
                        user.setNotifications(notifications);
                    }

                    processFavoritesProductions(jsonObject, user);
                    processFavoritesActors(jsonObject, user);
                    if (user instanceof Staff) {
                        processContributionProductions(jsonObject, user);
                        processContributionActors(jsonObject, user);
                    }
                    result.add(user);
                } catch (InformationIncompleteException e) {
                    System.out.println("Exception caught: " + e.getMessage());
                }

            }

            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Credentials parseCredentials(JsonObject credentialsObject) {
        if (credentialsObject != null) {
            String email = credentialsObject.get("email").getAsString();
            String password = credentialsObject.get("password").getAsString();
            return new Credentials(email, password);
        }
        return null;
    }

    public List<Production> getProductions() {
        return productions;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void addNewActor(Actor actor) {
        this.actors.add(actor);
    }

    public void deleteActor(Actor actor) {
        this.actors.remove(actor);
    }

    public void deleteProduction(Production production) {
        this.productions.remove(production);
    }
    public void addNewProduction(Production production) {
        this.productions.add(production);
    }

    public void deleteRequest(Request request) {
        if(request.getType() == RequestTypes.DELETE_ACCOUNT || request.getType() == RequestTypes.OTHERS){
            RequestHolder.deleteRequest(request);
        } else {
            User<?> user = findUserByUsername(request.getTo());
            if(user == null) {
                System.out.println("Eroare! Utilizatorul nu a fost gasit!");
            } else {
                ((RequestsManager) user).removeRequest(request, user);
            }
        }
        requests.remove(request);
    }

    public void addRequest(Request request) {
        if(request.getType() == RequestTypes.DELETE_ACCOUNT || request.getType() == RequestTypes.OTHERS){
            RequestHolder.addRequest(request);
        }
        requests.add(request);
    }

    public void addUser(User<?> user) {
        users.add(user);
    }

    public void deleteUser(User<?> user) {
        if(user instanceof Contributor)
            ((Contributor) user).addContributionsHolder();
        users.remove(user);
    }
    public void viewRequests() {
        for(Request request : requests) {
            request.displayInfo();
            System.out.println();
        }
    }

    public void displayProductions() {
        System.out.println();
        for(Production production : this.getProductions()) {
            if(production instanceof Movie) {
                ((Movie) production).displayInfo();
            } else {
                ((Series) production).displayInfo();
            }
            System.out.println();
        }
    }

    public void displayActors() {
        System.out.println();
        for(Actor actor: this.getActors()) {
            actor.displayInfo();
            System.out.println();
        }
    }

    public void displayNotifications(User<?> user) {
        System.out.println();
        for(String notification : user.getNotifications()) {
            System.out.println(notification);
        }
        System.out.println();
    }

    public void displayUsers() {
        System.out.println();
        int i = 1;
        for(User<?> user: users) {
            System.out.println("User " + i + " : " + user.getUsername());
            i++;
        }
        System.out.println();
    }

    public String generateUniqueUsername(String fullName) {
        String baseUsername = fullName.toLowerCase().replace(" ", "_");

        List<String> existingUsernames = new ArrayList<>();
        for (User<?> user : users) {
            existingUsernames.add(user.getUsername());
        }

        Random random = new Random();
        String uniqueUsername = baseUsername + random.nextInt(1000);

        while (existingUsernames.contains(uniqueUsername)) {
            uniqueUsername = baseUsername + "_" + random.nextInt(1000);
        }

        return uniqueUsername;
    }

    public List<User<?>> getUsers() {
        return users;
    }

    public String generateStrongPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[]{}|;:'\",.<>/?";

        for (int i = 0; i < 12; i++) {
            int randomIndex = random.nextInt(chars.length());
            password.append(chars.charAt(randomIndex));
        }
        return password.toString();
    }

    public static void main(String[] args) {
        List<User<?>> users = new ArrayList<>();
        List<Actor> actors = new ArrayList<>();
        List<Request> requests = new ArrayList<>();
        List<Production> productions = new ArrayList<>();

        IMDB imdb = IMDB.getInstance(users, actors, requests, productions);
        imdb.parseData();
        imdb.run();
  }
}