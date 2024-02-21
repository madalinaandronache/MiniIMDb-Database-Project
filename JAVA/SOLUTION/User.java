package SOLUTION;

import javax.swing.*;
import java.time.LocalDate;
import java.util.*;
public abstract class User<T extends Comparable<T>> {
        private String username;
        private int experience;
        private Information information;
        private AccountType userType;
        private List<String> notifications;
        private SortedSet<T> favorites;

    public static class Information {
        private final Credentials credentials;
        private final String name;
        private final String country;
        private final int age;
        private final String gender;
        private final LocalDate birthDate;

        public static class InformationBuilder {
            private Credentials credentials;
            private String name;
            private String country;
            private int age;
            private String gender;
            private LocalDate birthDate;

            public InformationBuilder() {}

            public InformationBuilder setName(String name) {
                this.name = name;
                return this;
            }

            public InformationBuilder setCountry(String country) {
                this.country = country;
                return this;
            }

            public InformationBuilder setAge(int age) {
                this.age = age;
                return this;
            }

            public InformationBuilder setGender(String gender) {
                this.gender = gender;
                return this;
            }

            public InformationBuilder setBirthDate(LocalDate birthDate) {
                this.birthDate = birthDate;
                return this;
            }

            public InformationBuilder setCredentials(Credentials credentials) {
                this.credentials = credentials;
                return this;
            }

            public Information build() throws InformationIncompleteException {
                if(name == null || credentials == null) {
                    throw new InformationIncompleteException("Information is incomplete. Name and credentials are required.");
                }
                return new Information(this);
            }
        }

        private Information(InformationBuilder builder) {
            this.credentials = builder.credentials;
            this.name = builder.name;
            this.country = builder.country;
            this.age = builder.age;
            this.gender = builder.gender;
            this.birthDate = builder.birthDate;
        }


        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public LocalDate getBirthDate() {
            return birthDate;
        }

        public String getCountry() {
            return country;
        }

        public String getGender() {
            return gender;
        }
    }

    public User(Information information, AccountType userType, String username, int experience) {
        this.information = information;
        this.userType = userType;
        this.username = username;
        this.experience = experience;
        this.notifications = new ArrayList<>();
        this.favorites = new TreeSet<>();
    }

    public boolean authenticate(String enteredEmail, String enteredPassword) {
        return information.getCredentials().getEmail().equals(enteredEmail)
                && information.getCredentials().getPassword().equals(enteredPassword);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public AccountType getUserType() {
        return userType;
    }

    public Information getInformation() {
        return information;
    }

    public int getExperience() {
        return experience;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public SortedSet<T> getFavorites() {
        return favorites;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setFavorites(SortedSet<T> favorites) {
        this.favorites = favorites;
    }

    public void setInformation(Information information) {
        this.information = information;
    }

    public void setUserType(AccountType userType) {
        this.userType = userType;
    }

    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }

    public boolean deleteFavoriteByName(String name) {
        for (T favorite : this.favorites) {
            if (favorite instanceof Actor) {
                Actor actor = (Actor) favorite;
                if(name.equals(actor.getName()))  {
                    removeFavorite(favorite);
                    return true;
                }
            } else if (favorite instanceof Production) {
                Production production = (Production) favorite;
                if(name.equals(production.getTitle()))  {
                    removeFavorite(favorite);
                    return true;
                }
            }
        }
        return false;
    }

    public void addFavorite(T favorite) {
        favorites.add(favorite);
    }

    public void removeFavorite(T favorite) {
        favorites.remove(favorite);
    }

    public void displayFavorites() {
        for (T favorite : this.getFavorites()) {
            if (favorite instanceof Actor) {
                Actor actor = (Actor) favorite;
                System.out.println("  Actor Name: " + actor.getName());
            } else if (favorite instanceof Production) {
                Production production = (Production) favorite;
                System.out.println("  Production Title: " + production.getTitle());
            }
        }
    }

    public DefaultListModel<String> createProductionListModel() {
        DefaultListModel<String> productionListModel = new DefaultListModel<>();
        for (T favorite : this.getFavorites()) {
            if (favorite instanceof Production) {
                Production production = (Production) favorite;
                productionListModel.addElement(production.getTitle());
            }
        }
        return productionListModel;
    }

    public DefaultListModel<String> createActorListModel() {
        DefaultListModel<String> actorListModel = new DefaultListModel<>();
        for (T favorite : this.getFavorites()) {
            if (favorite instanceof Actor) {
                Actor actor = (Actor) favorite;
                actorListModel.addElement(actor.getName());
            }
        }
        return actorListModel;
    }

    public void logout() {
        System.out.println("Thank you for using the application. Goodbye!");
        System.exit(0);
    }

    public void addNotification(String notification) {
        notifications.add(notification);
    }
}
