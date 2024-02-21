package SOLUTION;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Graphic extends JFrame {
    private final IMDB imdb;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private User<?> authenticatedUser;

    public Graphic(IMDB imdb) {
        this.imdb = imdb;
        setTitle("IMDB");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel welcomePanel = createWelcomePanel();
        cardPanel.add(welcomePanel, "Welcome");
        setContentPane(cardPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        JLabel label = new JLabel("Welcome to IMDB App!");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 24));
        label.setForeground(Color.ORANGE);

        ImageIcon icon = new ImageIcon("SOLUTION/IMDB2.jpg");
        Image scaledImage = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setPreferredSize(new Dimension(300, 300));

        JTextField emailField = new JTextField(50);
        JPasswordField passwordField = new JPasswordField(50);

        JButton button = new JButton("Authenticate");
        button.setBackground(Color.ORANGE);

        button.addActionListener(new ActionListener() {
            private int authenticationAttempts = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                if (checkAuthentication(email, password) != null) {
                    authenticatedUser = checkAuthentication(email, password);
                    JPanel authPanel = createMainPagePanel();
                    cardPanel.add(authPanel, "Main Page");
                    cardLayout.show(cardPanel, "Main Page");
                } else {
                    authenticationAttempts++;
                    int maxAttempts = 3;
                    if (authenticationAttempts >= maxAttempts) {
                        JOptionPane.showMessageDialog(
                                Graphic.this,
                                "Maximum authentication attempts reached. Exiting.",
                                "Authentication Failed",
                                JOptionPane.ERROR_MESSAGE
                        );
                        System.exit(0);
                    } else {
                        JOptionPane.showMessageDialog(
                                Graphic.this,
                                "Authentication failed. Please try again.",
                                "Authentication Failed",
                                JOptionPane.WARNING_MESSAGE
                        );
                        emailField.setText("");
                        passwordField.setText("");
                    }
                }
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.2;
        panel.add(label, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0.2;
        panel.add(imageLabel, gbc);

        gbc.gridy = 2;
        gbc.weighty = 0.1;

        JLabel emailLabel = new JLabel("Email: ");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        emailLabel.setForeground(Color.ORANGE);
        panel.add(emailLabel, gbc);

        gbc.gridy = 3;
        gbc.weighty = 0.1;
        panel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordLabel.setForeground(Color.ORANGE);

        gbc.gridy = 4;
        gbc.weighty = 0.1;
        panel.add(passwordLabel, gbc);

        gbc.gridy = 5;
        gbc.weighty = 0.1;
        panel.add(passwordField, gbc);

        gbc.gridy = 6;
        gbc.weighty = 0.1;
        panel.add(button, gbc);

        panel.setBackground(Color.BLACK);
        return panel;
    }

    private JPanel createMainPagePanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        int padding = 20;
        panel.setBorder(new EmptyBorder(padding, padding, padding, padding));

        JPanel wrapperPanel = new JPanel(new GridBagLayout());

        DefaultListModel<String> productionListModel = createProductionListModel();
        DefaultListModel<String> actorListModel = createActorsListModel();

        JList<String> scrollList = new JList<>(productionListModel);
        JScrollPane scrollPane = new JScrollPane(scrollList);

        JComboBox<String> sortComboBox = new JComboBox<>(new String[]{"Default", "Filter by Genre", "Filter by the number of evaluations"});
        JComboBox<String> categoryComboBox = createOptions(scrollList, productionListModel, actorListModel, sortComboBox);

        Genre[] genres = Genre.values();
        String[] genreNames = new String[genres.length + 1];
        genreNames[0] = "Default";
        for (int i = 0; i < genres.length; i++) {
            genreNames[i + 1] = genres[i].name();
        }
        JComboBox<String> genreComboBox = new JComboBox<>(genreNames);

        String[] numbers = new String[101];
        numbers[0] = "Default";
        for(int i = 1; i < 101; i++) {
            numbers[i] = "Minimum number of ratings: " + String.valueOf(i);
        }
        JComboBox<String> numComboBox = new JComboBox<>(numbers);

        sortComboBox.addActionListener(e -> handleSortSelection(categoryComboBox, sortComboBox, genreComboBox, numComboBox, scrollList));
        genreComboBox.addActionListener(e -> handleGenreSelection(genreComboBox, scrollList));
        numComboBox.addActionListener(e -> handleRatingsSelection(numComboBox, scrollList));
        scrollList.addListSelectionListener(e -> handleListSelection(categoryComboBox, scrollList));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        wrapperPanel.add(categoryComboBox, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.BOTH;
        wrapperPanel.add(sortComboBox, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.BOTH;
        wrapperPanel.add(genreComboBox, gbc);
        wrapperPanel.add(numComboBox, gbc);
        genreComboBox.setVisible(false);
        numComboBox.setVisible(false);

        JPanel searchPanel = new JPanel();
        JTextField searchField = new JTextField(50);

        JMenuBar menuBar = new JMenuBar();
        JMenu userMenu = new JMenu("User Menu");
        menuBar.add(userMenu);
        setJMenuBar(menuBar);

        JButton favoritesButton = new JButton("Favorites");
        JButton searchButton = new JButton("Search");

        favoritesButton.addActionListener(e -> handleFavoritesButtonClick());
        searchButton.addActionListener(e -> handleSearchSelection(searchField));
        createMenuComponents(userMenu, authenticatedUser.getUserType().toString());

        searchPanel.add(menuBar, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.SOUTH);
        searchPanel.add(favoritesButton, BorderLayout.EAST);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.2;
        panel.add(searchPanel, gbc);

        gbc.weighty = 0;
        gbc.gridy = 1;
        panel.add(wrapperPanel, gbc);

        gbc.weighty = 0.8;
        gbc.gridy = 2;
        panel.add(scrollPane, gbc);

        panel.setBackground(Color.BLACK);
        return panel;
    }

    private static JComboBox<String> createOptions(JList<String> scrollList, DefaultListModel<String> productionListModel, DefaultListModel<String> actorListModel, JComboBox<String> sortComboBox) {
        JComboBox<String> categoryComboBox = new JComboBox<>(new String[]{"Productions", "Actors"});
        categoryComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCategory = (String) categoryComboBox.getSelectedItem();
                if ("Productions".equals(selectedCategory)) {
                    scrollList.setModel(productionListModel);
                    setSortOptions(sortComboBox, "Productions");
                } else if ("Actors".equals(selectedCategory)) {
                    scrollList.setModel(actorListModel);
                    setSortOptions(sortComboBox, "Actors");
                }
            }
        });
        return categoryComboBox;
    }

    private static void setSortOptions(JComboBox<String> sortComboBox, String category) {
        sortComboBox.removeAllItems();
        if ("Productions".equals(category)) {
            sortComboBox.addItem("Default");
            sortComboBox.addItem("Filter by Genre");
            sortComboBox.addItem("Filter by the number of evaluations");
        } else if ("Actors".equals(category)) {
            sortComboBox.addItem("Default");
            sortComboBox.addItem("Sort in alphabetical order");
            sortComboBox.addItem("Sort in reverse alphabetical order");
        }
    }

    private void handleListSelection(JComboBox<String> categoryComboBox, JList<String> scrollList) {
        if (!scrollList.isSelectionEmpty() && !scrollList.getValueIsAdjusting()) {
            String selectedCategory = (String) categoryComboBox.getSelectedItem();
            String selectedProduction = scrollList.getSelectedValue();
            viewDetails(selectedProduction, selectedCategory);
        }
    }

    private void handleSortSelection(JComboBox<String> categoryComboBox, JComboBox<String> sortComboBox, JComboBox<String> genreComboBox, JComboBox<String> numComboBox, JList<String> scrollList) {
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        String selectedSortOption = (String) sortComboBox.getSelectedItem();

        if("Productions".equals(selectedCategory)){
            if ("Filter by Genre".equals(selectedSortOption)) {
                scrollList.setModel(createProductionListModel());
                numComboBox.setVisible(true);
                genreComboBox.setVisible(true);
            } else if ("Filter by the number of evaluations".equals(selectedSortOption)) {
                scrollList.setModel(createProductionListModel());
                genreComboBox.setVisible(false);
                numComboBox.setVisible(true);
            } else {
                scrollList.setModel(createProductionListModel());
                genreComboBox.setVisible(false);
                numComboBox.setVisible(false);
            }
        } else {
            genreComboBox.setVisible(false);
            numComboBox.setVisible(false);
            if ("Sort in alphabetical order".equals(selectedSortOption)) {
                scrollList.setModel(createActorsListOrdered());
            } else if ("Sort in reverse alphabetical order".equals(selectedSortOption)) {
                scrollList.setModel(createActorsListDescending());
            } else {
                scrollList.setModel(createActorsListModel());
            }
        }
    }

    private void handleFavoritesButtonClick(){
        FavoritesPage favoritesPage = new FavoritesPage(this, imdb, authenticatedUser);
        favoritesPage.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                favoritesPage.dispose();
            }
        });
        favoritesPage.setVisible(true);
    }

    private void createMenuComponents(JMenu userMenu, String userType) {
        JMenuItem notifications = new JMenuItem("View notifications");
        JMenuItem request = new JMenuItem("Add/Delete request");
        JMenuItem logout = new JMenuItem("Logout");
        JMenuItem actorSystem = new JMenuItem("Add/Delete actor from system");
        JMenuItem productionSystem = new JMenuItem("Add/Delete production from system");
        JMenuItem actorUpdate = new JMenuItem("Update Actor Details");
        JMenuItem productionUpdate = new JMenuItem("Update Production Details");
        JMenuItem solveRequest = new JMenuItem("Solve a request from your list");
        JMenuItem solveRequestAdmin = new JMenuItem("Solve a request from the admins list");
        JMenuItem addUser = new JMenuItem("Add/Delete user");

        userMenu.add(notifications);
        if ("Regular".equals(userType)) {
            userMenu.add(request);
        } else if ("Contributor".equals(userType)){
            userMenu.add(request);
            userMenu.add(actorSystem);
            userMenu.add(productionSystem);
            userMenu.add(solveRequest);
            userMenu.add(actorUpdate);
            userMenu.add(productionUpdate);
        }else if ("Admin".equals(userType)) {
            userMenu.add(actorSystem);
            userMenu.add(productionSystem);
            userMenu.add(solveRequest);
            userMenu.add(solveRequestAdmin);
            userMenu.add(actorUpdate);
            userMenu.add(productionUpdate);
            userMenu.add(addUser);
        }
        userMenu.add(logout);

        notifications.addActionListener(e -> handleNotificationsClick());
        request.addActionListener(e -> handleRequestClick());
        logout.addActionListener(e -> handleLogoutClick());
        actorSystem.addActionListener(e -> handleActorSystemClick());
        productionSystem.addActionListener(e -> handleProductionSystemClick());
        actorUpdate.addActionListener(e -> handleActorUpdateClick());
        productionUpdate.addActionListener(e -> handleProductionUpdateClick());
        solveRequest.addActionListener(e -> handleSolveRequestClick());
        solveRequestAdmin.addActionListener(e -> handleSolveAdminClick());
        addUser.addActionListener(e -> handleUserClick());
    }

    private void handleNotificationsClick() {
        MenuPage menuPage = new MenuPage(this, "View notifications", authenticatedUser, imdb);
        menuPage.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                menuPage.dispose();
            }
        });
        menuPage.setVisible(true);
    }

    private void handleRequestClick() {
        MenuPage menuPage = new MenuPage(this, "Add/Delete request", authenticatedUser, imdb);
        menuPage.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                menuPage.dispose();
            }
        });
        menuPage.setVisible(true);
    }

    private void handleActorSystemClick() {
        MenuPage menuPage = new MenuPage(this, "Add/Delete actor from system", authenticatedUser, imdb);
        menuPage.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                menuPage.dispose();
            }
        });
        menuPage.setVisible(true);
    }

    private void handleProductionSystemClick() {
        MenuPage menuPage = new MenuPage(this, "Add/Delete production from system", authenticatedUser, imdb);
        menuPage.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                menuPage.dispose();
            }
        });
        menuPage.setVisible(true);
    }

    private void handleActorUpdateClick() {
        MenuPage menuPage = new MenuPage(this, "Update Actor Details", authenticatedUser, imdb);
        menuPage.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                menuPage.dispose();
            }
        });
        menuPage.setVisible(true);
    }

    private void handleProductionUpdateClick() {
        MenuPage menuPage = new MenuPage(this, "Update Production Details", authenticatedUser, imdb);
        menuPage.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                menuPage.dispose();
            }
        });
        menuPage.setVisible(true);
    }

    private void handleSolveRequestClick() {
        MenuPage menuPage = new MenuPage(this, "Solve a request", authenticatedUser, imdb);
        menuPage.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                menuPage.dispose();
            }
        });
        menuPage.setVisible(true);
    }

    private void handleSolveAdminClick() {
        MenuPage menuPage = new MenuPage(this, "Solve admin request", authenticatedUser, imdb);
        menuPage.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                menuPage.dispose();
            }
        });
        menuPage.setVisible(true);
    }

    private void handleUserClick() {
        MenuPage menuPage = new MenuPage(this, "Add/Delete user", authenticatedUser, imdb);
        menuPage.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                menuPage.dispose();
            }
        });
        menuPage.setVisible(true);
    }

    private void handleLogoutClick() {
        cardLayout.show(cardPanel, "Welcome");
    }

    private void handleGenreSelection(JComboBox<String> genreComboBox, JList<String> scrollList) {
        String selectedGenreOption = (String) genreComboBox.getSelectedItem();

        if("Default".equals(selectedGenreOption)) {
            scrollList.setModel(createProductionListModel());
        } else {
            Genre selectedGenre = Genre.valueOf(selectedGenreOption);
            DefaultListModel<String> productionListGenre = new DefaultListModel<>();

            for (Production production : imdb.getProductions()) {
                if (production.hasGenre(selectedGenre)) {
                    productionListGenre.addElement(production.getTitle());
                }
            }
            scrollList.setModel(productionListGenre);
        }
    }

    private void handleRatingsSelection(JComboBox<String> numComboBox, JList<String> scrollList) {
        String selectedRatingsOption = (String) numComboBox.getSelectedItem();

        if("Default".equals(selectedRatingsOption)) {
            scrollList.setModel(createProductionListModel());
        } else {
            DefaultListModel<String> productionListGenre = new DefaultListModel<>();
            assert selectedRatingsOption != null;
            String numberString = selectedRatingsOption.substring("Minimum number of ratings: ".length());
            try {
                int ratingsNumber = Integer.parseInt(numberString);

                for (Production production : imdb.getProductions()) {
                    if (production.getNumRatings() >= ratingsNumber) {
                        productionListGenre.addElement(production.getTitle());
                    }
                }
                scrollList.setModel(productionListGenre);
            } catch (NumberFormatException e) {
                // Handle the case where the substring is not a valid integer
                e.printStackTrace(); // or log the error
            }

        }
    }

    private DefaultListModel<String> createActorsListOrdered() {
        DefaultListModel<String> actorsList = new DefaultListModel<>();

        List<Actor> actors = imdb.getActors();
        if (actors != null) {
            List<Actor> sortedActors = new ArrayList<>(actors);
            sortedActors.sort(Comparator.comparing(Actor::getName));
            for (Actor actor : sortedActors) {
                actorsList.addElement(actor.getName());
            }
        }

        return actorsList;
    }

    private DefaultListModel<String> createActorsListDescending() {
        DefaultListModel<String> actorsList = new DefaultListModel<>();

        List<Actor> actors = imdb.getActors();
        if (actors != null) {
            List<Actor> sortedActors = new ArrayList<>(actors);
            sortedActors.sort(Comparator.comparing(Actor::getName).reversed());
            for (Actor actor : sortedActors) {
                actorsList.addElement(actor.getName());
            }
        }

        return actorsList;
    }

    private void viewDetails(String selectedObject, String category) {
        String details;
        if("Productions".equals(category)) {
            details = displayDetailsProduction(selectedObject);
            DetailsPage detailsPage = new DetailsPage(this, selectedObject, details, authenticatedUser, imdb, true);
            detailsPage.setVisible(true);
        } else {
            details = displayDetailsActor(selectedObject);
            DetailsPage detailsPage = new DetailsPage(this, selectedObject, details, authenticatedUser, imdb, false);
            detailsPage.setVisible(true);
        }
    }

    private String displayDetailsActor(String name) {
        if(imdb.findActorByName(name, imdb.getActors()) != null) {
            Actor found = imdb.findActorByName(name, imdb.getActors());
            return found.displayInfoGraphic();
        }
        return null;
    }

    private String displayDetailsProduction(String name) {
        if(imdb.findProductionByTitle(name, imdb.getProductions()) != null) {
            Production found = imdb.findProductionByTitle(name, imdb.getProductions());

            return found.displayInfoGraphic();
        }
        return null;
    }

    private void handleSearchSelection(JTextField searchField) {
        String searchText = searchField.getText();
        String details;

        if(displayDetailsActor(searchText) != null) {
            details = displayDetailsActor(searchText);
            DetailsPage detailsPage = new DetailsPage(this, searchText, details, authenticatedUser, imdb,false);
            detailsPage.setVisible(true);
        } else if(displayDetailsProduction(searchText) != null) {
            details = displayDetailsProduction(searchText);
            DetailsPage detailsPage = new DetailsPage(this, searchText, details, authenticatedUser, imdb,true);
            detailsPage.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a valid name/title!", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private User<?> checkAuthentication(String email, String password) {
        User<?> foundUser = imdb.findUserByEmail(email);

        if (foundUser != null) {
            if (foundUser.authenticate(email, password)) {
                return foundUser;
            } else {
                return foundUser;
            }
        } else {
            return null;
        }
    }

    private DefaultListModel<String> createProductionListModel() {
        DefaultListModel<String> productionListModel = new DefaultListModel<>();
        for(Production production : imdb.getProductions()) {
            productionListModel.addElement(production.getTitle());
        }

        return productionListModel;
    }

    private DefaultListModel<String> createActorsListModel() {
        DefaultListModel<String> actorsListModel = new DefaultListModel<>();
        for(Actor actor : imdb.getActors()) {
            actorsListModel.addElement(actor.getName());
        }

        return actorsListModel;
    }
}
