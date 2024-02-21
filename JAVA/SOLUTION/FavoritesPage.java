package SOLUTION;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
public class FavoritesPage extends JDialog{
    private static final int DIALOG_WIDTH = 600;
    private static final int DIALOG_HEIGHT = 300;
    private String selectedOption;
    private final IMDB imdb;
    private final User<?> user;
    private final JList<String> favoritesList;
    private DefaultListModel<String> productionsList;
    private DefaultListModel<String> actorsList;

    public FavoritesPage(JFrame parent, IMDB imdb, User<?> user) {
        this.user = user;
        this.imdb = imdb;
        setTitle("Favorites");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        int padding = 20;
        panel.setBorder(new EmptyBorder(padding, padding, padding, padding));

        JRadioButton productionsRadio = new JRadioButton("Productions");
        JRadioButton actorsRadio = new JRadioButton("Actors");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(productionsRadio);
        buttonGroup.add(actorsRadio);

        productionsList = user.createProductionListModel();
        actorsList = user.createActorListModel();

        favoritesList = new JList<>(productionsList);
        JScrollPane scrollPane = new JScrollPane(favoritesList);

        productionsRadio.setSelected(true);
        handleOptionSelection("Productions");

        productionsRadio.addActionListener(e -> handleOptionSelection("Productions"));
        actorsRadio.addActionListener(e -> handleOptionSelection("Actors"));
        favoritesList.addListSelectionListener(e -> handleListSelection());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(productionsRadio, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(actorsRadio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane, gbc);

        setContentPane(panel);
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void handleOptionSelection(String option) {
        favoritesList.clearSelection();
        selectedOption = option;

        if ("Productions".equals(option)) {
            favoritesList.setModel(productionsList);
        } else if ("Actors".equals(option)) {
            favoritesList.setModel(actorsList);
        }
    }

    private  void handleListSelection() {
        String selectedValue = favoritesList.getSelectedValue();

        if (selectedValue != null) {
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Options for " + selectedValue,
                    "Options",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Delete From Favorites", "Close"},
                    "Close");

            if (choice == JOptionPane.YES_OPTION) {
                user.deleteFavoriteByName(selectedValue);
                if("Productions".equals(selectedOption)) {
                    productionsList = user.createProductionListModel();
                    favoritesList.setModel(productionsList);
                } else {
                    actorsList = user.createActorListModel();
                    favoritesList.setModel(actorsList);
                }
            }
        }
    }
}
