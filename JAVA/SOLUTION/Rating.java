package SOLUTION;

import java.util.ArrayList;
import java.util.List;

public class Rating implements Subject{
    private final String username;
    private final int rating;
    private final String comment;
    private final List<Observer> observers = new ArrayList<>();

    public Rating(String username, int rating, String comment) {
        this.username = username;
        this.rating = rating;
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setRating(String productionName) {
        notifyObservers("Production: " + productionName + " received a new rating by user: " + username);
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public void displayInfo() {
        System.out.println("    Username: " + getUsername());
        System.out.println("    Rating: " + getRating());
        System.out.println("    Comment: " + getComment());
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String notification) {
        for (Observer observer : observers) {
            observer.update(notification);
        }
    }
}
