package SOLUTION;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Request implements Subject, ExperienceStrategy{
    private RequestTypes type;
    private LocalDateTime createdDate;
    private String username;
    private String title;
    private String to;
    private String description;
    private final List<Observer> observers = new ArrayList<>();

    public Request(RequestTypes type, LocalDateTime createdDate, String title, String description, String username, String to) {
        this.type = type;
        this.createdDate = createdDate;
        this.title = title;
        this.username = username;
        this.to = to;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public RequestTypes getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getDescription() {
        return description;
    }

    public String getTo() {
        return to;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(RequestTypes type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void displayInfo() {
        System.out.println("Request Information:");
        System.out.println("Type: " + type);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDate = createdDate.format(formatter);
        System.out.println("Created Date: " + formattedDate);
        System.out.println("Username: " + username);
        if(!title.isEmpty())
            System.out.println("Title: " + title);
        System.out.println("To: " + to);
        System.out.println("Description: " + description);
    }

    public String displayInfoGraphic() {
        StringBuilder infoBuilder = new StringBuilder();
        infoBuilder.append("Request Information:\n");
        infoBuilder.append("Type: ").append(type).append("\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDate = createdDate.format(formatter);
        infoBuilder.append("Created Date: ").append(formattedDate).append("\n");

        infoBuilder.append("Username: ").append(username).append("\n");

        if (!title.isEmpty()) {
            infoBuilder.append("Title: ").append(title).append("\n");
        }

        infoBuilder.append("To: ").append(to).append("\n");
        infoBuilder.append("Description: ").append(description).append("\n");

        return infoBuilder.toString();
    }

    @Override
    public int calculateExperience() {
        return 5;
    }

    public void setStatus(String status, User<?> user) {
        notifyObservers("Request status changed: " + status);
        user.addNotification("Request with description: " + this.getDescription() + " status changed: " + status);

        if("Solved".equals(status) && this.getType() == RequestTypes.ACTOR_ISSUE || this.getType() == RequestTypes.MOVIE_ISSUE) {
            if(user.getExperience() != Integer.MAX_VALUE) {
                int experienceGain = user.getExperience() + calculateExperience();
                user.setExperience(experienceGain);
            }
        }
    }

    public void requestCreated(User<?> user) {
        notifyObservers("Request newly add: " + this.getDescription());
        user.addNotification("Request with description: " + this.getDescription() + " newly add");
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
