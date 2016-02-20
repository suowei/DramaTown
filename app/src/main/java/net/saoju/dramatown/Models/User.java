package net.saoju.dramatown.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private int id;
    private String name;
    private String email;
    private String introduction;
    private int reviews;
    private int favorite0;
    private int favorite1;
    private int favorite2;
    private int favorite3;
    private int favorite4;
    private int epfav0;
    private int epfav2;
    private int epfav4;
    private String created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getFavorite0() {
        return favorite0;
    }

    public void setFavorite0(int favorite0) {
        this.favorite0 = favorite0;
    }

    public int getFavorite1() {
        return favorite1;
    }

    public void setFavorite1(int favorite1) {
        this.favorite1 = favorite1;
    }

    public int getFavorite2() {
        return favorite2;
    }

    public void setFavorite2(int favorite2) {
        this.favorite2 = favorite2;
    }

    public int getFavorite3() {
        return favorite3;
    }

    public void setFavorite3(int favorite3) {
        this.favorite3 = favorite3;
    }

    public int getFavorite4() {
        return favorite4;
    }

    public void setFavorite4(int favorite4) {
        this.favorite4 = favorite4;
    }

    public int getEpfav0() {
        return epfav0;
    }

    public void setEpfav0(int epfav0) {
        this.epfav0 = epfav0;
    }

    public int getEpfav2() {
        return epfav2;
    }

    public void setEpfav2(int epfav2) {
        this.epfav2 = epfav2;
    }

    public int getEpfav4() {
        return epfav4;
    }

    public void setEpfav4(int epfav4) {
        this.epfav4 = epfav4;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            User user = new User();
            user.name = source.readString();
            return user;
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
