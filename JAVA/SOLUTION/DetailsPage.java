package SOLUTION;

import javax.swing.*;
import java.awt.*;

public class DetailsPage extends JDialog {
    private static final int DIALOG_WIDTH = 600;
    private static final int DIALOG_HEIGHT = 300;
    private JButton ratingButton;
    private final JTextArea detailsArea;
    private final User<?> authenticatedUser;

    public DetailsPage(JFrame parent, String title, String details, User<?> user, IMDB imdb, boolean isProduction) {
        super(parent, title, true);
        authenticatedUser = user;

        JPanel panel = new JPanel();
        setTitle("Details");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        detailsArea = new JTextArea(details);
        detailsArea.setEditable(false);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JScrollPane(detailsArea));

        ratingButton = new JButton("Add rating");
        JButton favoriteButton = new JButton("Add to your favorites");

        ratingButton.setVisible(false);
        if(user.getUserType() == AccountType.Regular && isProduction) {
            Production production = imdb.findProductionByTitle(title, imdb.getProductions());

            if (!production.containsRatingByUser(user.getUsername())) {
                ratingButton = new JButton("Add rating");
            } else {
                ratingButton = new JButton("Delete rating");
            }
            ratingButton.setVisible(true);
            ratingButton.addActionListener(e -> handleRatingButtonClick(production));
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(ratingButton);
        buttonPanel.add(favoriteButton);

        favoriteButton.addActionListener(e -> handleFavoriteButtonClick(title, imdb));

        panel.add(Box.createVerticalStrut(10));
        panel.add(buttonPanel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        setMinimumSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        setMaximumSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));

        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void handleRatingButtonClick(Production production){
        String buttonText = ratingButton.getText();
        if ("Add rating".equals(buttonText)) {
            String ratingString = JOptionPane.showInputDialog(this, "Enter a rating (1-10):");

            try {
                int ratingNumber = Integer.parseInt(ratingString);

                if (ratingNumber >= 1 && ratingNumber <= 10) {
                    String comment = JOptionPane.showInputDialog(this, "Enter a comment:");
                    Rating rating = new Rating(authenticatedUser.getUsername(), ratingNumber, comment);
                    production.addRating(rating, authenticatedUser);

                    JOptionPane.showMessageDialog(this, "Rating added with success!", "Rating Added", JOptionPane.PLAIN_MESSAGE);

                    detailsArea.setText(production.displayInfoGraphic());
                    ratingButton.setText("Delete rating");
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a valid rating between 1 and 10.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid integer rating.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            production.deleteRatingByUsername(authenticatedUser.getUsername());
            detailsArea.setText(production.displayInfoGraphic());
            JOptionPane.showMessageDialog(this, "Rating deleted with success!", "Rating Deleted", JOptionPane.PLAIN_MESSAGE);
            ratingButton.setText("Add rating");
        }
    }

    private void handleFavoriteButtonClick(String name, IMDB imdb){
        Production production = imdb.findProductionByTitle(name, imdb.getProductions());
        Actor actor = imdb.findActorByName(name, imdb.getActors());

        if (production != null) {
            ((User) authenticatedUser).addFavorite(production);
            JOptionPane.showMessageDialog(this, "The element was added successfully!", "Production Added to Favorite List", JOptionPane.PLAIN_MESSAGE);
        } else if (actor != null) {
            ((User) authenticatedUser).addFavorite(actor);
            JOptionPane.showMessageDialog(this, "The element was added successfully!", "Actor Added to Favorite List", JOptionPane.PLAIN_MESSAGE);
        }
    }
}
