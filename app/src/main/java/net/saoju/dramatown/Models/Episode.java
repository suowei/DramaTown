package net.saoju.dramatown.Models;

public class Episode {
    private int id;
    private int drama_id;
    private String title;
    private String alias;
    private int duration;
    private String release_date;
    private String sc;
    private String url;
    private String poster_url;
    private String introduction;
    private int reviews;
    private Drama drama;
    private EpisodeFavorite userFavorite;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDrama_id() {
        return drama_id;
    }

    public void setDrama_id(int drama_id) {
        this.drama_id = drama_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public Drama getDrama() {
        return drama;
    }

    public void setDrama(Drama drama) {
        this.drama = drama;
    }

    public String getAlias() {
        return alias;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public EpisodeFavorite getUserFavorite() {
        return userFavorite;
    }
}
