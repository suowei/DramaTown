package net.saoju.dramatown.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
    private int id;
    private int drama_id;
    private int episode_id;
    private int user_id;
    private String title;
    private String content;
    private int visible;
    private String created_at;
    private String updated_at;
    private Drama drama;
    private User user;
    private Episode episode;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Drama getDrama() {
        return drama;
    }

    public void setDrama(Drama drama) {
        this.drama = drama;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Episode getEpisode() {
        return episode;
    }

    public void setEpisode(Episode episode) {
        this.episode = episode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(drama_id);
        dest.writeInt(episode_id);
        dest.writeInt(user_id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeInt(visible);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeParcelable(drama, flags);
        dest.writeParcelable(user, flags);
        dest.writeParcelable(episode, flags);
    }

    public static final Parcelable.Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            Review review = new Review();
            review.id = source.readInt();
            review.drama_id = source.readInt();
            review.episode_id = source.readInt();
            review.user_id = source.readInt();
            review.title = source.readString();
            review.content = source.readString();
            review.visible = source.readInt();
            review.created_at = source.readString();
            review.updated_at = source.readString();
            review.drama = source.readParcelable(Drama.class.getClassLoader());
            review.user = source.readParcelable(User.class.getClassLoader());
            review.episode = source.readParcelable(Episode.class.getClassLoader());
            return review;
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
