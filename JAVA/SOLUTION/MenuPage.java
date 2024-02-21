package SOLUTION;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuPage extends JFrame {
    private final IMDB imdb;
    private final User<?> user;
    public MenuPage(JFrame parent, String action, User<?> user, IMDB imdb) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 300);
        setResizable(false);
        setLocationRelativeTo(parent);
        this.user = user;
        this.imdb = imdb;

        if("View notifications".equals(action)) {
            displayNotifications();
        } else if("Add/Delete request".equals(action)) {
            displayRequests();
        } else if("Add/Delete actor from system".equals(action)) {
            displayActorSystem();
        } else if("Add/Delete production from system".equals(action)) {
            displayProductionSystem();
        } else if("Update Actor Details".equals(action)) {
            displayActorUpdate();
        } else if("Update Production Details".equals(action)) {
            displayProductionUpdate();
        } else if("Solve a request".equals(action)) {
            displaySolveRequest();
        } else if("Solve admin request".equals(action)) {
            displaySolveAdminRequest();
        } else if("Add/Delete user".equals(action)) {
            displayUser();
        }
    }

    private void displayUser() {
        JPanel contentPanel = new JPanel(new BorderLayout());

        int padding = 20;
        contentPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> usersList = new JList<>(listModel);

        for(User<?> user: imdb.getUsers()) {
            listModel.addElement(user.getUsername());
        }

        JScrollPane scrollPane = new JScrollPane(usersList);

        JLabel emptyLabel = new JLabel("This is the list with all the users in the system: ");
        contentPanel.add(emptyLabel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JButton addUserButton = new JButton("Add User");
        contentPanel.add(addUserButton, BorderLayout.SOUTH);

        usersList.addListSelectionListener(e -> handleUserSelection(usersList));
        addUserButton.addActionListener(e -> handleAddUser(usersList));

        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleAddUser(JList<String> usersList) {
        JPanel panel = new JPanel(new GridLayout(8, 2));

        panel.add(new JLabel("Enter Name:"));
        JTextField nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Enter Email:"));
        JTextField emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Enter Country:"));
        JTextField countryField = new JTextField();
        panel.add(countryField);

        panel.add(new JLabel("Enter Age:"));
        JTextField ageField = new JTextField();
        panel.add(ageField);

        panel.add(new JLabel("Enter Gender:"));
        JTextField genderField = new JTextField();
        panel.add(genderField);

        int option = JOptionPane.showOptionDialog(
                this,
                panel,
                "Add User",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Add", "Cancel"},
                "Add"
        );

        if (option == 0) {
            try {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String country = countryField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());
                String gender = genderField.getText().trim();

                String username = imdb.generateUniqueUsername(name);
                String password = imdb.generateStrongPassword();

                int userId = (int) (Math.random() * 1000);
                AccountType accountType = chooseAccountType();
                Credentials credentials = new Credentials(email, password);

                User.Information information = new User.Information.InformationBuilder()
                        .setName(name)
                        .setCredentials(credentials)
                        .setCountry(country)
                        .setAge(age)
                        .setGender(gender)
                        .build();

                User<?> newUser = null;
                switch (accountType) {
                    case Regular:
                        newUser = new Regular(information, username, userId);
                        break;
                    case Contributor:
                        newUser = new Contributor(information, username, userId);
                        break;
                    case Admin:
                        newUser = new Admin(information, username, userId);
                        break;
                }

                imdb.addUser(newUser);

                JOptionPane.showMessageDialog(
                        this,
                        "User added successfully!\nUsername: " + username + "\nPassword: " + password,
                        "User Added",
                        JOptionPane.INFORMATION_MESSAGE
                );

                DefaultListModel<String> listModel = (DefaultListModel<String>) usersList.getModel();
                listModel.addElement(newUser.getUsername());
            } catch (NumberFormatException e) {
                showErrorDialog("Invalid input. Please enter a valid number for age.");
            } catch (InformationIncompleteException e) {
                showErrorDialog("Exception caught: " + e.getMessage());
            }
        }
    }

    private AccountType chooseAccountType() {
        String[] options = {"Regular", "Contributor", "Admin"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose the account type:",
                "Account Type",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0:
                return AccountType.Regular;
            case 1:
                return AccountType.Contributor;
            case 2:
                return AccountType.Admin;
            default:
                return null;
        }
    }

    private void handleUserSelection(JList<String> usersList) {
        int selectedIndex = usersList.getSelectedIndex();

        if (selectedIndex != -1) {
            User<?> found = imdb.getUsers().get(selectedIndex);

            int option = JOptionPane.showOptionDialog(
                    this,
                    "Are you sure you want to delete this account?",
                    "Mark Request as:",
                    JOptionPane.YES_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"Delete"},
                    "Delete"
            );

            if (option == JOptionPane.YES_OPTION) {
                imdb.deleteUser(found);
                DefaultListModel<String> listModel = (DefaultListModel<String>) usersList.getModel();
                listModel.removeElementAt(selectedIndex);
            }
        }
    }

    public void displayNotifications() {
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (String notification : user.getNotifications()) {
            listModel.addElement(notification);
        }

        JList<String> notificationsList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(notificationsList);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private void displaySolveAdminRequest() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        int padding = 20;
        contentPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));

        DefaultListModel<String> listModel = new DefaultListModel<>();

        JList<String> requestsList = new JList<>(listModel);

        for(Request request : RequestHolder.getAllRequests()) {
            listModel.addElement(request.getDescription());
        }

        JScrollPane scrollPane = new JScrollPane(requestsList);

        JLabel emptyLabel = new JLabel("These are your requests: ");
        contentPanel.add(emptyLabel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        requestsList.addListSelectionListener(e -> handleSolveAdminSelection(requestsList));

        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleSolveAdminSelection(JList<String> requestsList) {
        int selectedIndex = requestsList.getSelectedIndex();

        if (selectedIndex != -1) {
            Request selectedRequest = RequestHolder.getAllRequests().get(selectedIndex);
            String message = selectedRequest.displayInfoGraphic();

            int option = JOptionPane.showOptionDialog(
                    this,
                    message,
                    "Mark Request as :",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"Solved", "Reject"},
                    "Solved"
            );

            if (option == 0) {
                imdb.deleteRequest(selectedRequest);
                selectedRequest.setStatus("Solved", user);

                DefaultListModel<String> listModel = (DefaultListModel<String>) requestsList.getModel();
                listModel.removeElementAt(selectedIndex);
            } else if(option == 1) {
                imdb.deleteRequest(selectedRequest);
            }
        }
    }
    private void displaySolveRequest() {
        JPanel contentPanel = new JPanel(new BorderLayout());

        int padding = 20;
        contentPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));

        DefaultListModel<String> listModel = new DefaultListModel<>();

        JList<String> requestsList = new JList<>(listModel);

        for(Request request : ((Staff<?>)user).getRequests()) {
            listModel.addElement(request.getDescription());
        }

        JScrollPane scrollPane = new JScrollPane(requestsList);

        JLabel emptyLabel = new JLabel("These are your requests: ");
        contentPanel.add(emptyLabel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        requestsList.addListSelectionListener(e -> handleSolveRequestSelection(requestsList));

        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleSolveRequestSelection(JList<String> requestsList) {
        int selectedIndex = requestsList.getSelectedIndex();

        if (selectedIndex != -1) {
            Request selectedRequest = ((Staff<?>) user).getRequests().get(selectedIndex);
            String message = selectedRequest.displayInfoGraphic();

            int option = JOptionPane.showOptionDialog(
                    this,
                    message,
                    "Mark Request as :",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"Solved", "Reject"},
                    "Solved"
            );

            if (option == 0) {
                imdb.deleteRequest(selectedRequest);
                selectedRequest.setStatus("Solved", user);

                DefaultListModel<String> listModel = (DefaultListModel<String>) requestsList.getModel();
                listModel.removeElementAt(selectedIndex);
            } else if(option == 1) {
                imdb.deleteRequest(selectedRequest);
            }
        }
    }

    private void displayProductionSystem() {
        JPanel contentPanel = new JPanel(new BorderLayout());

        int padding = 20;
        contentPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> productionsList = new JList<>(listModel);

        for(Object contribution : ((Staff<?>)user).getContributions()) {
            if(contribution instanceof Production) {
                Production production = (Production) contribution;
                listModel.addElement(production.getTitle());
            }
        }

        JScrollPane scrollPane = new JScrollPane(productionsList);

        JLabel emptyLabel = new JLabel("These are your productions contribution: ");
        contentPanel.add(emptyLabel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JButton addProductionButton = new JButton("Add Production");
        contentPanel.add(addProductionButton, BorderLayout.SOUTH);

        productionsList.addListSelectionListener(e -> handleProductionSelection(productionsList));
        addProductionButton.addActionListener(e -> handleAddProductionButtonClick(productionsList));

        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleAddProductionButtonClick(JList<String> productionsList) {
        String[] productionTypes = {"Movie", "Series"};

        JPanel panel = new JPanel(new GridLayout(6, 2));

        panel.add(new JLabel("Select Production Type:"));
        JComboBox<String> typeDropdown = new JComboBox<>(productionTypes);
        panel.add(typeDropdown);

        panel.add(new JLabel("Enter Title:"));
        JTextField titleField = new JTextField();
        panel.add(titleField);

        panel.add(new JLabel("Enter the number of directors:"));
        JTextField numDirectorsField = new JTextField();
        panel.add(numDirectorsField);

        panel.add(new JLabel("Enter the number of actors:"));
        JTextField numActorsField = new JTextField();
        panel.add(numActorsField);

        panel.add(new JLabel("Enter the number of genres:"));
        JTextField numGenresField = new JTextField();
        panel.add(numGenresField);

        panel.add(new JLabel("Enter the plot:"));
        JTextField plotField = new JTextField();
        panel.add(plotField);

        int option = JOptionPane.showOptionDialog(
                this,
                panel,
                "Add Production",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Add", "Cancel"},
                "Add"
        );

        if (option == 0) {
            try {
                Production p;

                String selectedType = (String) typeDropdown.getSelectedItem();
                if ("Movie".equals(selectedType)) {
                    p = new Movie();
                } else {
                    p = new Series();
                }

                String title = titleField.getText();
                p.setTitle(title);

                int numDirectors = Integer.parseInt(numDirectorsField.getText());
                List<String> directors = new ArrayList<>();
                for (int i = 0; i < numDirectors; i++) {
                    String director = JOptionPane.showInputDialog(this, "Enter the name for Director " + (i + 1) + ":");
                    directors.add(director);
                }
                p.setDirectors(directors);

                int numActors = Integer.parseInt(numActorsField.getText());
                List<String> actors = new ArrayList<>();
                for (int i = 0; i < numActors; i++) {
                    String actor = JOptionPane.showInputDialog(this, "Enter the name for Actor " + (i + 1) + ":");
                    actors.add(actor);
                }
                p.setActors(actors);

                int numGenres = Integer.parseInt(numGenresField.getText());
                List<Genre> genres = selectGenres(numGenres);
                p.setGenres(genres);

                String plot = plotField.getText();
                p.setPlot(plot);

                if (p instanceof Movie) {
                    String duration = JOptionPane.showInputDialog(this, "Enter the duration:");
                    ((Movie) p).setDuration(duration);

                    String year = JOptionPane.showInputDialog(this, "Enter the release year:");
                    ((Movie) p).setReleaseYear(Integer.parseInt(year));

                } else {
                    String year = JOptionPane.showInputDialog(this, "Enter the release year:");
                    ((Series) p).setReleaseYear(Integer.parseInt(year));

                    String numSeasonsStr = JOptionPane.showInputDialog(this, "Enter the number of seasons:");
                    int numSeasons = Integer.parseInt(numSeasonsStr);
                    ((Series) p).setNumSeasons(numSeasons);

                    Map<String, List<Episode>> seasons = new HashMap<>();

                    for (int i = 0; i < numSeasons; i++) {
                        String seasonName = JOptionPane.showInputDialog(this, "Enter the name of Season " + (i + 1) + ":");

                        String numEpisodesStr = JOptionPane.showInputDialog(this, "Enter the number of episodes for Season " + (i + 1) + ":");
                        int numEpisodes = Integer.parseInt(numEpisodesStr);

                        List<Episode> episodes = new ArrayList<>();

                        for (int j = 0; j < numEpisodes; j++) {
                            String episodeName = JOptionPane.showInputDialog(this, "Enter the name of Episode " + (j + 1) + ":");
                            String episodeDuration = JOptionPane.showInputDialog(this, "Enter the duration of Episode " + (j + 1) + ":");

                            episodes.add(new Episode(episodeName, episodeDuration));
                        }
                        seasons.put(seasonName, episodes);
                    }
                    ((Series) p).setSeasons(seasons);
                }

                if(user instanceof Contributor) {
                    if(((User<?>)user).getExperience() != Integer.MAX_VALUE){
                        int experienceGain = ((User<?>) user).getExperience() + ((Staff<?>)(user)).calculateExperience();
                        ((User<?>) user).setExperience(experienceGain);
                    }
                }

                imdb.addNewProduction(p);
                ((Staff) user).addContribution(p);

                DefaultListModel<String> listModel = (DefaultListModel<String>) productionsList.getModel();
                listModel.addElement(p.getTitle());
            } catch (NumberFormatException e) {
                showErrorDialog("Invalid input. Please enter a valid number for the number of performances.");
            }
        }
    }

    private List<Genre> selectGenres(int numGenres) {
        List<Genre> selectedGenres = new ArrayList<>();
        String[] genreNames = new String[Genre.values().length];

        for (int i = 0; i < Genre.values().length; i++) {
            genreNames[i] = Genre.values()[i].toString();
        }

        for (int i = 0; i < numGenres; i++) {
            JComboBox<String> genreDropdown = new JComboBox<>(genreNames);
            int option = JOptionPane.showOptionDialog(
                    this,
                    genreDropdown,
                    "Select Genre " + (i + 1),
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    null
            );

            if (option == JOptionPane.OK_OPTION) {
                selectedGenres.add(Genre.valueOf(genreDropdown.getSelectedItem().toString()));
            }
        }

        return selectedGenres;
    }


    private void handleAddActorButtonClick(JList<String> actorsList) {
        Actor newActor = new Actor();

        JPanel panel = new JPanel(new GridLayout(4, 2));

        panel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Biography:"));
        JTextField biographyField = new JTextField();
        panel.add(biographyField);

        panel.add(new JLabel("Number of Performances:"));
        JTextField numPerformancesField = new JTextField();
        panel.add(numPerformancesField);

        int option = JOptionPane.showOptionDialog(
                this,
                panel,
                "Add Actor",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Add", "Cancel"},
                "Add"
        );

        if (option == 0) {
            try {
                String name = nameField.getText();
                String biography = biographyField.getText();
                int numPerformances = Integer.parseInt(numPerformancesField.getText());

                newActor.setName(name);
                newActor.setBiography(biography);

                List<Actor.Performance> performances = new ArrayList<>();
                for (int i = 0; i < numPerformances; i++) {
                    String title = JOptionPane.showInputDialog(this, "Enter the title for Performance " + (i + 1) + ":");
                    String[] types = {"Movie", "Series"};
                    String type = (String) JOptionPane.showInputDialog(this, "Choose the type for Performance " + (i + 1) + ":", "Performance Type", JOptionPane.PLAIN_MESSAGE, null, types, types[0]);

                    performances.add(new Actor.Performance(title, type));
                }
                newActor.setPerformances(performances);

                imdb.addNewActor(newActor);
                ((Staff) user).addContribution(newActor);

                if(user instanceof Contributor) {
                    if(((User<?>)user).getExperience() != Integer.MAX_VALUE){
                        int experienceGain = ((User<?>) user).getExperience() + ((Staff<?>)(user)).calculateExperience();
                        ((User<?>) user).setExperience(experienceGain);
                    }
                }

                DefaultListModel<String> listModel = (DefaultListModel<String>) actorsList.getModel();
                listModel.addElement(newActor.getName());
            } catch (NumberFormatException e) {
                showErrorDialog("Invalid input. Please enter a valid number for the number of performances.");
            }
        }
    }

    private void handleProductionSelection(JList<String> productionsList) {
        int selectedIndex = productionsList.getSelectedIndex();

        if (selectedIndex != -1) {
            int option = JOptionPane.showOptionDialog(
                    this,
                    "Choose the action:",
                    "Production",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"Delete Production"},
                    "Delete Production"
            );

            if (option == 0) {
                String title = productionsList.getSelectedValue();
                Production found = ((Staff<?>) user).removeProductionSystem(title);

                if (found != null) {
                    imdb.deleteProduction(found);

                    DefaultListModel<String> listModel = (DefaultListModel<String>) productionsList.getModel();
                    listModel.remove(selectedIndex);
                }
            }
        }

    }

    private void displayProductionUpdate() {
        JPanel contentPanel = new JPanel(new BorderLayout());

        int padding = 20;
        contentPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));

        DefaultListModel<String> listModel = new DefaultListModel<>();

        JList<String> productionsList = new JList<>(listModel);

        for(Object contribution : ((Staff<?>)user).getContributions()) {
            if(contribution instanceof Production) {
                Production production = (Production) contribution;
                listModel.addElement(production.getTitle());
            }
        }

        JScrollPane scrollPane = new JScrollPane(productionsList);

        JLabel emptyLabel = new JLabel("These are your productions contribution: ");
        contentPanel.add(emptyLabel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        productionsList.addListSelectionListener(e -> handleProductionUpdate(productionsList));

        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleProductionUpdate(JList<String> productionsList) {
        String title = productionsList.getSelectedValue();
        Production found = imdb.findProductionByTitle(title, imdb.getProductions());

        if (found != null) {
            Object[] options = {"Modify Title", "Modify Directors", "Modify Actors", "Modify Plot", "Cancel"};
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Choose what you want to modify for " + title + ":",
                    "Modify Production",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            switch (choice) {
                case 0:
                    modifyProductionTitle(found, productionsList);
                    break;
                case 1:
                    modifyProductionDirectors(found);
                    break;
                case 2:
                    modifyProductionActors(found);
                    break;
                case 3:
                    modifyProductionPlot(found);
                    break;
                default:
                    break;
            }
        }
    }

    private void modifyProductionTitle(Production production, JList<String> productionsList) {
        String newTitle = JOptionPane.showInputDialog(
                this,
                "Enter the new title for the production:",
                "Modify Production Title",
                JOptionPane.PLAIN_MESSAGE
        );

        if (newTitle != null && !newTitle.trim().isEmpty()) {
            production.setTitle(newTitle);
            JOptionPane.showMessageDialog(this, "Production title updated successfully.");

            DefaultListModel<String> listModel = (DefaultListModel<String>) productionsList.getModel();
            int selectedIndex = productionsList.getSelectedIndex();
            listModel.setElementAt(newTitle, selectedIndex);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid or empty title. Production title not updated.");
        }
    }

    private void modifyProductionPlot(Production production) {
        String newTitle = JOptionPane.showInputDialog(
                this,
                "Enter the new plot for the production:",
                "Modify Production Plot",
                JOptionPane.PLAIN_MESSAGE
        );

        if (newTitle != null && !newTitle.trim().isEmpty()) {
            production.setTitle(newTitle);
            JOptionPane.showMessageDialog(this, "Production plot updated successfully.");

        } else {
            JOptionPane.showMessageDialog(this, "Invalid or empty plot. Production plot not updated.");
        }
    }

    private void modifyProductionDirectors(Production production) {
        JPanel panel = new JPanel(new GridLayout(4, 2));  // Increased rows to 4

        panel.add(new JLabel("Directors: " + String.join(", ", production.getDirectors()) + "\n"));
        panel.add(new JLabel());

        panel.add(new JLabel("Enter Director Name:"));
        JTextField directorNameField = new JTextField();
        panel.add(directorNameField);

        panel.add(new JLabel("Select Action:"));
        String[] actions = {"Remove Director", "Add Director"};
        JComboBox<String> actionDropdown = new JComboBox<>(actions);
        panel.add(actionDropdown);

        int option = JOptionPane.showOptionDialog(
                this,
                panel,
                "Modify Directors",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"OK", "Cancel"},
                "OK"
        );

        if (option == 0) {
            String directorName = directorNameField.getText().trim();
            String selectedAction = (String) actionDropdown.getSelectedItem();

            if ("Remove Director".equals(selectedAction)) {
                if (!directorName.isEmpty()) {
                    production.removeDirector(directorName);
                    JOptionPane.showMessageDialog(this, "Director removed successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid or empty director name. Director not removed.");
                }
            } else if ("Add Director".equals(selectedAction)) {
                if (!directorName.isEmpty()) {
                    production.addDirector(directorName);
                    JOptionPane.showMessageDialog(this, "Director added successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid or empty director name. Director not added.");
                }
            }
        }
    }

    private void modifyProductionActors(Production production) {
        JPanel panel = new JPanel(new GridLayout(4, 2));

        panel.add(new JLabel("Actors: " + String.join(", ", production.getActors()) + "\n"));
        panel.add(new JLabel());

        panel.add(new JLabel("Enter Actor Name:"));
        JTextField actorNameField = new JTextField();
        panel.add(actorNameField);

        panel.add(new JLabel("Select Action:"));
        String[] actions = {"Remove Actor", "Add Actor"};
        JComboBox<String> actionDropdown = new JComboBox<>(actions);
        panel.add(actionDropdown);

        int option = JOptionPane.showOptionDialog(
                this,
                panel,
                "Modify Actors",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"OK", "Cancel"},
                "OK"
        );

        if (option == 0) {
            String actorName = actorNameField.getText().trim();
            String selectedAction = (String) actionDropdown.getSelectedItem();

            if ("Remove Actor".equals(selectedAction)) {
                if (!actorName.isEmpty()) {
                    production.removeActor(actorName);
                    JOptionPane.showMessageDialog(this, "Actor removed successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid or empty actor name. Actor not removed.");
                }
            } else if ("Add Actor".equals(selectedAction)) {
                if (!actorName.isEmpty()) {
                    production.addActor(actorName);
                    JOptionPane.showMessageDialog(this, "Actor added successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid or empty actor name. Actor not added.");
                }
            }
        }
    }

    private void displayActorUpdate() {
        JPanel contentPanel = new JPanel(new BorderLayout());

        int padding = 20;
        contentPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> actorsList = new JList<>(listModel);

        for(Object contribution : ((Staff<?>)user).getContributions()) {
            if(contribution instanceof Actor) {
                Actor actor = (Actor) contribution;
                listModel.addElement(actor.getName());
            }
        }

        JScrollPane scrollPane = new JScrollPane(actorsList);

        JLabel emptyLabel = new JLabel("These are your actors contribution: ");
        contentPanel.add(emptyLabel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        actorsList.addListSelectionListener(e -> handleActorUpdate(actorsList));

        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleActorUpdate(JList<String> actorsList) {
        String name = actorsList.getSelectedValue();
        Actor found = imdb.findActorByName(name, imdb.getActors());

        if (found != null) {
            Object[] options = {"Modify Name", "Modify Biography", "Cancel"};
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Choose what you want to modify for " + name + ":",
                    "Modify Actor",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            switch (choice) {
                case 0:
                    modifyActorName(found, actorsList);
                    break;
                case 1:
                    modifyActorBiography(found);
                    break;
                default:
                    break;
            }
        }
    }

    private void modifyActorName(Actor actor, JList<String> actorsList) {
        String newName = JOptionPane.showInputDialog(this, "Enter the new name for " + actor.getName() + ":");
        if (newName != null && !newName.trim().isEmpty()) {
            actor.setName(newName);

            DefaultListModel<String> listModel = (DefaultListModel<String>) actorsList.getModel();
            int selectedIndex = actorsList.getSelectedIndex();
            listModel.setElementAt(newName, selectedIndex);

            JOptionPane.showMessageDialog(this, "Actor name updated successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid name. No modifications made.");
        }
    }

    private void modifyActorBiography(Actor actor) {
        String newBiography = JOptionPane.showInputDialog(this, "Enter the new biography for " + actor.getName() + ":");
        if (newBiography != null) {
            actor.setBiography(newBiography);
            JOptionPane.showMessageDialog(this, "Actor biography updated successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid biography. No modifications made.");
        }
    }

    private void displayActorSystem() {
        JPanel contentPanel = new JPanel(new BorderLayout());

        int padding = 20;
        contentPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> actorsList = new JList<>(listModel);

        for(Object contribution : ((Staff<?>)user).getContributions()) {
            if(contribution instanceof Actor) {
                Actor actor = (Actor) contribution;
                listModel.addElement(actor.getName());
            }
        }

        JScrollPane scrollPane = new JScrollPane(actorsList);
        JLabel emptyLabel = new JLabel("These are your actors contribution: ");
        contentPanel.add(emptyLabel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JButton addActorButton = new JButton("Add Actor");
        contentPanel.add(addActorButton, BorderLayout.SOUTH);

        actorsList.addListSelectionListener(e -> handleActorSelection(actorsList));
        addActorButton.addActionListener(e -> handleAddActorButtonClick(actorsList));


        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void handleActorSelection(JList<String> actorsList) {
        int selectedIndex = actorsList.getSelectedIndex();

        if (selectedIndex != -1) {
            int option = JOptionPane.showOptionDialog(
                    this,
                    "Choose the action:",
                    "Actor",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"Delete Actor"},
                    "Delete Actor"
            );

            if (option == 0) {
                String nameActor = actorsList.getSelectedValue();
                Actor found = ((Staff<?>) user).removeActorSystem(nameActor);

                if (found != null) {
                    imdb.deleteActor(found);
                    DefaultListModel<String> listModel = (DefaultListModel<String>) actorsList.getModel();
                    listModel.remove(selectedIndex);
                }
            }
        }

    }



    public void displayRequests() {
        JPanel contentPanel = new JPanel(new BorderLayout());

        int padding = 20;
        contentPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));
        DefaultListModel<String> listModel = new DefaultListModel<>();

        List<Request> created = new ArrayList<>();
        for (Request request : imdb.getRequests()) {
            if (user.getUsername() != null && user.getUsername().equals(request.getUsername())) {
                created.add(request);
            }
        }

        JLabel emptyLabel = new JLabel("Your requests list is: ");
        emptyLabel.setHorizontalAlignment(JLabel.CENTER);

        if (!created.isEmpty()) {
            for (Request request : created) {
                if (request.getDescription() != null) {
                    listModel.addElement("Request description: " + request.getDescription());
                }
            }
        }

        JList<String> requestsList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(requestsList);
        JButton addRequestButton = new JButton("Add Request");

        contentPanel.add(emptyLabel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(addRequestButton, BorderLayout.SOUTH);

        requestsList.addListSelectionListener(e -> handleRequestSelection(requestsList));
        addRequestButton.addActionListener(e -> handleAddRequestButtonClick(contentPanel, requestsList));

        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleRequestSelection(JList<String> requestsList) {
        int selectedIndex = requestsList.getSelectedIndex();
        if (selectedIndex != -1) {
            Request selectedRequest = getSelectedRequest(selectedIndex);
            String message = selectedRequest.displayInfoGraphic();

            int option = JOptionPane.showOptionDialog(
                    this,
                    message,
                    "Request Details",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"Delete Request"},
                    "Delete Request"
            );

            if (option == 0) {
                imdb.deleteRequest(selectedRequest);

                DefaultListModel<String> listModel = (DefaultListModel<String>) requestsList.getModel();
                listModel.clear();
                List<Request> created = new ArrayList<>();
                for (Request request : imdb.getRequests()) {
                    if (user.getUsername() != null && user.getUsername().equals(request.getUsername())) {
                        created.add(request);
                    }
                }

                if (!created.isEmpty()) {
                    for (Request request : created) {
                        if (request.getDescription() != null) {
                            listModel.addElement("Request description: " + request.getDescription());
                        }
                    }
                }
            }
        }
    }

    private Request getSelectedRequest(int selectedIndex) {
        List<Request> created = new ArrayList<>();
        for (Request request : imdb.getRequests()) {
            if (user.getUsername() != null && user.getUsername().equals(request.getUsername())) {
                created.add(request);
            }
        }
        return created.get(selectedIndex);
    }



    private void handleAddRequestButtonClick(JPanel contentPanel, JList<String> requestsList) {
        String[] requestTypes = {"DELETE_ACCOUNT", "ACTOR_ISSUE", "MOVIE_ISSUE", "OTHERS"};

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Select Type:"));
        JComboBox<String> typeDropdown = new JComboBox<>(requestTypes);
        panel.add(typeDropdown);

        panel.add(new JLabel("Enter Description:"));
        JTextField descriptionField = new JTextField();
        panel.add(descriptionField);

        int option = JOptionPane.showOptionDialog(
                this,
                panel,
                "Add Request",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Add", "Cancel"},
                "Add"
        );

        if (option == 0) {
            String title = "";
            String to = "";
            User<?> toUser = null;
            String selectedType = (String) typeDropdown.getSelectedItem();
            String description = descriptionField.getText();

            String username = user.getUsername();
            LocalDateTime createdDate = LocalDateTime.now();
            RequestTypes type = null;

            if ("DELETE_ACCOUNT".equals(selectedType) || "OTHERS".equals(selectedType)) {
                to = "ADMIN";
                if("DELETE_ACCOUNT".equals(selectedType))
                    type = RequestTypes.DELETE_ACCOUNT;
                else
                    type = RequestTypes.OTHERS;
            } else if("MOVIE_ISSUE".equals(selectedType)) {
                title = showTitleInputDialog();
                type = RequestTypes.MOVIE_ISSUE;

                Production production = imdb.findProductionByTitle(title, imdb.getProductions());

                if (production != null) {
                    toUser = imdb.findUserByProductionContribution(production);
                    to = toUser.getUsername();

                    if (toUser.equals(user)) {
                        showErrorDialog("You can not add a request for a production you added! Try again!");
                        dispose();
                        return;
                    }
                } else {
                    showErrorDialog("Production not found! Try again!");
                    dispose();
                    return;
                }
            } else if("ACTOR_ISSUE".equals(selectedType)) {
                title = showTitleInputDialog();
                type = RequestTypes.ACTOR_ISSUE;

                Actor actor = imdb.findActorByName(title, imdb.getActors());
                if(actor != null) {
                    toUser = imdb.findUserByActorContribution(actor);
                    to = toUser.getUsername();
                    if(toUser.equals(user)) {
                        showErrorDialog("You can not add a request for an actor you added! Try again!");
                        dispose();
                        return;
                    }
                } else {
                    showErrorDialog("Actor not found! Try again!");
                    dispose();
                    return;
                }
            }

            Request requestCreated = new Request(type, createdDate, title, description, username, to);
            if(toUser != null) {
                ((RequestsManager) user).createRequest(requestCreated, toUser);
            }

            imdb.addRequest(requestCreated);

            DefaultListModel<String> listModel = (DefaultListModel<String>) requestsList.getModel();
            listModel.clear();

            List<Request> created = new ArrayList<>();
            for (Request request : imdb.getRequests()) {
                if (user.getUsername() != null && user.getUsername().equals(request.getUsername())) {
                    created.add(request);
                }
            }

            if (!created.isEmpty()) {
                for (Request request : created) {
                    if (request.getDescription() != null) {
                        listModel.addElement("Request description: " + request.getDescription());
                    }
                }
            }
        }
    }


    private void showErrorDialog(String errorMessage) {
        JOptionPane.showMessageDialog(
                this,
                errorMessage,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private String showTitleInputDialog() {
        JPanel panel = new JPanel(new GridLayout(1, 2));

        panel.add(new JLabel("Enter Title/Name:"));
        JTextField titleField = new JTextField();
        panel.add(titleField);

        int option = JOptionPane.showOptionDialog(
                this,
                panel,
                "Enter Title/Name",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"OK", "Cancel"},
                "OK"
        );

        if (option == 0) {
            return titleField.getText();
        }
        return null;
    }
}
