package net.saoju.dramatown.Models;

public class EpisodeFavorite {
    private int episode_id;
    private int user_id;
    private int type;
    private float rating;
    private String updated_at;
    private UserEpfavEpisode episode;

    public EpisodeFavorite(int type, float rating) {
        this.type = type;
        this.rating = rating;
    }

    public int getEpisode_id() {
        return episode_id;
    }

    public void setEpisode_id(int episode_id) {
        this.episode_id = episode_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeString() {
        switch (type) {
            case 0:
                return "想听";
            case 2:
                return "听过";
            case 4:
                return "抛弃";
            default:
                return "未知类型";
        }
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public UserEpfavEpisode getEpisode() {
        return episode;
    }

    public void setEpisode(UserEpfavEpisode episode) {
        this.episode = episode;
    }
}
