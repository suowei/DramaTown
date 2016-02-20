package net.saoju.dramatown.Models;

public class UserEpfavEpisode {
    private int id;
    private String dramaTitle;
    private String title;
    private String cv;
    private int duration;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDramaTitle() {
        return dramaTitle;
    }

    public void setDramaTitle(String dramaTitle) {
        this.dramaTitle = dramaTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
