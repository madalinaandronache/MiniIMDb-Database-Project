package SOLUTION;

import java.util.List;

public class Actor implements Comparable{
    private String name;
    private List<Performance> performances;
    private String biography;

    public static class Performance {
        private String title;
        private String type;
        public Performance(String title, String type) {
            this.title = title;
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public Actor() {}

    public Actor(String name, List<Performance> performances, String biography) {
        this.name = name;
        this.performances = performances;
        this.biography = biography;
    }

    public Performance findPerformanceByTitle(String title) {
        for (Performance performance: performances) {
            if (performance.getTitle().equals(title)) {
                return performance;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Performance> getPerformances() {
        return performances;
    }

    public void setPerformances(List<Performance> performances) {
        this.performances = performances;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public int compareTo(Object o) {
        if (o instanceof Actor) {
            Actor otherActor = (Actor) o;
            return this.getName().compareTo(otherActor.getName());
        } else if(o instanceof Production) {
            Production otherProduction = (Production) o;
            return this.getName().compareTo(otherProduction.getTitle());
        }
        return 0;
    }

    public void displayInfo() {
        System.out.println();
        System.out.println("Name: " + getName());
        System.out.println("Biography: " + getBiography() + "\n");
        System.out.println("Performances:");
        List<Performance> performances = getPerformances();
        if (performances != null) {
            for (Performance performance : performances) {
                System.out.println("  Title: " + performance.getTitle());
                System.out.println("  Type: " + performance.getType());
                System.out.println();
            }
        } else {
            System.out.println("No performances available.");
        }
    }

    public String displayInfoGraphic() {
        StringBuilder info = new StringBuilder();
        info.append("Name: ").append(getName()).append("\n");
        info.append("Biography: ").append(getBiography()).append("\n\n");
        info.append("Performances:\n");
        List<Performance> performances = getPerformances();
        if (performances != null) {
            for (Performance performance : performances) {
                info.append("  Title: ").append(performance.getTitle()).append("\n");
                info.append("  Type: ").append(performance.getType()).append("\n\n");
            }
        } else {
            info.append("No performances available.\n");
        }
        return info.toString();
    }
}
