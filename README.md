# IMDb Database Project
## POO - Andronache Madalina-Georgiana 322CC

## Overview

The IMDb Database Project is a Java-based application designed to create a
simplified version of the Internet Movie Database (IMDb). The project focuses
on applying the principles of object-oriented programming in Java to build a
well-organized class hierarchy, implement object-oriented design, handle runtime
exceptions, and translate a real-world problem into a functional application.

The application simulates a movie and TV show database, offering features
similar to IMDb, including information on movies, TV shows, actors, directors,
producers, reviews, ratings, and release dates. 

### 1. CLASSES

#### 1.1 IMDB
- Represents the application.
- Stores details from JSON files.
- Manages user roles (Regular, Contributor, Admin).
- Loads parsed data from JSON files.
- Authenticates users and initiates the application flow based on user roles.
- Includes methods to handle user choices.

#### 1.2 Production (Abstract Class)
- Implements Comparable interface.
- Represents cinematographic productions in the system.
- Includes title, director names, actor names, genres, ratings, description, and an abstract method for displaying information.
- Implements compareTo method for sorting productions by title.

#### 1.3 Movie
- Extends Production.
- Includes movie-specific details such as duration and release year.

#### 1.4 Episode
- Contains episode-specific details.

#### 1.5 Series
- Extends Production.
- Includes release year, season count, and a dictionary mapping season names to lists of episodes.

#### 1.6 RequestsHolder (Static Class)
- Contains a list of requests for the admin team.
- Provides methods for managing the request list.

#### 1.7 Request
- Represents a user request.
- Includes request type, creation date, product/actor name, issue description, 
and usernames.

#### 1.8 Rating
- Represents user ratings for productions.
- Includes username, score (1-10), and comments.

#### 1.9 User (Abstract Class)
- Represents system users.
- Includes user information, account type, username, experience, notifications, 
and a sorted collection of favorite productions and actors.
- Provides methods for managing favorites, updating experience, and logging out.

#### 1.10 Regular
- Represents regular users.
- Extends User.
- Implements RequestsManager interface.
- Allows regular users to create/delete requests and add ratings.

#### 1.11 Staff (Abstract Class)
- Represents staff members.
- Extends User.
- Implements StaffInterface.
- Includes methods for adding/deleting productions and actors, resolving 
requests, and updating information.

#### 1.12 Contributor
- Represents contributor users.
- Extends Staff.
- Implements RequestsManager.
- Allows contributors to create/delete requests.

#### 1.13 Admin
- Represents admin users.
- Extends Staff.
- Allows admins to add/delete users, removing associated details.
- Overrides methods for modifying team-wide elements.

#### 1.14 Information (Inner Class of User)
- Contains user credentials and personal information.

#### 1.15 Credentials
- Contains user authentication data.
- Utilizes the Builder design pattern for instantiation.
- Uses DateTimeFormatter for date formatting.

#### 1.16 Actor
- Represents actors in the system.
- Includes actor name, a list of productions and their types, and a biography.

#### 1.17 Action
- Implements a singleton pattern to handle user actions in the terminal 
version of the app.
- Provides a menu for users based on their account type (Regular, Contributor, 
Admin).
- Offers actions such as viewing production/actor details, notifications, 
searching, managing favorites, adding/deleting requests, ratings, and system 
operations.
- Ensures a responsive and intuitive command-line interface for users to 
interact with the IMDB app.
- Uses Scanner for user input and validates the input to avoid errors.
- Facilitates a clean and organized way for users to navigate and execute 
desired actions.

### 2. INTERFACES

#### 2.1 RequestsManager
- Contains methods for creating and removing requests:
    - `public void createRequest(Request r);`
    - `public void removeRequest(Request r);`

#### 2.2 StaffInterface
- Contains methods for managing productions and actors in the system:
    - `public void addProductionSystem(Production p);`
    - `public void addActorSystem(Actor a);`
    - `public void removeProductionSystem(String name);`
    - `public void removeActorSystem(String name);`
    - `public void updateProduction(Production p);`
    - `public void updateActor(Actor a);`

### 3. ENUMERATIONS

#### 3.1 AccountType
- Enumerates user types: REGULAR, CONTRIBUTOR, ADMIN.

#### 3.2 Genre
- Enumerates movie genres: Action, Adventure, Comedy, Drama, Horror, SF, 
Fantasy, Romance, Mystery, Thriller, Crime, Biography, War.

#### 3.3 RequestTypes
- Enumerates user request types:
    - `DELETE_ACCOUNT`: Request for account deletion;
    - `ACTOR_ISSUE`: Request related to actor data;
    - `MOVIE_ISSUE`: Request related to movie data;
    - `OTHERS`: Miscellaneous requests not covered by the above;

### 4. DESIGN PATTERNS

#### 4.1 SINGLETON PATTERN

The Singleton Pattern is implemented in the `IMDB` class to ensure that only
one instance of the class is created, providing a global point of access.

#### 4.2 BUILDER PATTERN

The Builder Pattern is used in the instantiation of objects of type
`Information`, facilitating the step-by-step creation of complex user
information objects.

#### 4.3 FACTORY PATTERN

The Factory Pattern is applied to instantiate different types of users
(regular, contributor, admin) in the IMDb project.

#### 4.4 OBSERVER PATTERN

The Observer Pattern is utilized to implement notification functionality in
the project. Regular users receive notifications for resolved or rejected
requests and new reviews. Admins or contributors are notified of requests
assigned to them and reviews on productions they added.

#### 4.5 STRATEGY PATTERN

The Strategy Pattern is implemented to calculate user experience based on
specific actions. Different strategies are used for updating user experience
when actions contributing to experience gain are performed.

### 5. APPLICATION FLOW

1. User selects application mode: terminal or graphical interface.
2. User enters credentials to log in. If unsuccessful, user retries until
   correct credentials are provided.
3. After successful login, the application displays user options based on
   their role (regular, contributor, admin). The table below indicates
   available actions for each user type.

   | Action                                           | Regular User | Contributor User | Admin User |
   |--------------------------------------------------|--------------|------------------|------------|
   | View Productions                                 | X            | X                | X          |
   | View Actors                                      | X            | X                | X          |
   | View Notifications                               | X            | X                | X          |
   | Search Actor/Production                          | X            | X                | X          |
   | Add/Delete Actor/Production <br/> from favorites | X            | X                | X          |
   | Add/Delete request                               | X            | X                |            |
   | Add/Delete Actor/Production <br/> from system    |              | X                | X          |
   | Solve request                                    |              | X                | X          |
   | Update Actor/Production                          |              | X                | X          |
   | Add/Delete rating for a production               | X            |                  |            |
   | Add/Delete user from system                      |              |                  | X          |
   | Log Out                                          | X            | X                | X          |
   | Exit Application                                 | X            | X                | X          |

4. Application executes the selected option, as described in the requirements.
5. After completing an action, the application returns to step 3.
6. If the user chooses the Log Out option, the application offers the option
   to log in again (go back to step 2) or exit the application.

### 6. GRAPHICAL INTERFACE

The graphical interface, implemented using the SWING package, comprises the
following pages:

#### Authentication Page
- User enters credentials and accesses the main page upon successful login.

#### Main Page
- Displays movie and TV show recommendations. User can filter recommendations
  based on genre, number of ratings, etc.
- Allows searching for a specific movie/show/actor.
- Enables navigation to the actors' page, displaying actors in alphabetical order.
- Selecting a movie/show/actor displays specific details. Users can add a review
  or add a movie/actor to their favorites.

#### Menu Page
- Accessible from the main page, offering users the choice of actions presented
  in the table.

Contains the following classes:

#### 6.1 Graphic Class

- Implements the graphical interface using Java Swing.
- Features a welcome panel for user authentication and a main page for user interaction.
- Utilizes components like JLabels, JTextFields, JButtons, JComboBox, JList, and JScrollPane.
- Manages user authentication, dynamic UI updates based on user roles, and various actions.
- Implements sorting, filtering, and searching functionalities for productions and actors.
- Utilizes CardLayout for smooth transitions between panels.
- Demonstrates effective use of Swing components and event handling for a user-friendly experience.

#### 6.2 DetailsPage Class

- Represents a dialog displaying details of a production or actor.
- Provides options to add or delete ratings and add to favorites.
- Utilizes Java Swing components for an interactive user interface.
- Ensures valid user input for rating and handles user interactions.
- Utilizes Java Swing components for an interactive user interface.

#### 6.3 FavoritesPage Class

- Represents a dialog displaying user favorites (productions or actors).
- Allows users to choose between favorite productions and actors.
- Dynamic list updates based on user selections.
- Provides options to delete items from the favorites list.
- Utilizes Java Swing components for an interactive user interface.
- Part of the solution for the IMDB application.

#### 6.3 MenuPage Class

- Represents a JFrame displaying various actions based on user input.
- Handles options such as viewing notifications, managing requests, 
actor and production system operations, updating actor and production
details, solving user and admin requests, and managing user addition/deletion 
depending on the user type.
- Ensures a responsive and user-friendly interface for IMDB application actions.
- Utilizes Java Swing components to provide an interactive experience.
- Enhances user navigation with clear options and a well-organized layout.

### 7. Bonus

#### 7.1 Filter Criteria
- The user can choose if he wants to filter the productions by type:
he can select if he wants to view only the movies or the series.
- The user can filter the productions by the actor playing: he introduces
the name of the actor and all the productions that have the actor playing in it
will be displayed.
- The user can filter the productions by the release year, the user introduces
the year and all the production that were released in that year will be 
displayed.
- The user can filter the actors by the number of the performances they have in 
the system.
- The user can filter the productions by the director: he introduces
the name of the director and all the productions that have the director in it
will be displayed.

### 8. Challenges and Time Commitment

Throughout the development of this project, several challenges were encountered,
primarily related to the graphical user interface (GUI) design and ensuring a
seamless user experience. Debugging and refining the interactions between
different classes and components, especially in handling user actions and
system updates, required careful attention. Additionally, accommodating diverse
user roles (Regular, Contributor, Admin) and implementing their specific
functionalities added complexity.

One of the prominent challenges encountered during the project development
revolved around managing user favorites using `SortedSet<T>`. The complexities
arose from efficiently forming and manipulating this sorted set, especially
when dealing with various types of favorites, such as productions and actors.

The project spanned several weeks of dedicated work, involving both
conceptualization and hands-on coding. Time was allocated to understanding user
requirements, designing class structures, implementing features, and refining
the codebase. The iterative nature of software development led to continuous
revisions and optimizations, ensuring the reliability and efficiency of the IMDb
application. Overall, I spent 20 days working on this project, more than 60 
hours.

In the future, I want to improve some aspects of the IMDB application.