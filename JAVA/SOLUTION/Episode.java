package SOLUTION;

public class Episode {
    private String episodeName;
    private String duration;

    public Episode(String episodeName, String duration) {
        this.episodeName = episodeName;
        this.duration = duration;
    }

    public String getName() {
        return episodeName;
    }

    public void setName(String episodeName) {
        this.episodeName = episodeName;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDuration() {
        return duration;
    }

}
