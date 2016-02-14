package net.saoju.dramatown.Models;

public class Favorite {
    private int id;
    private int drama_id;
    private int user_id;
    private int type;
    private float rating;
    private String tags;

    public Favorite(int type, float rating, String tags) {
        this.type = type;
        this.rating = rating;
        this.tags = tags;
    }

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
            case 1:
                return "在追";
            case 2:
                return "听过";
            case 3:
                return "搁置";
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
