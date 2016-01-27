package net.saoju.dramatown.Models;

import java.util.List;

public class Drama {
    private int id;
    private String title;
    private String alias;
    private int type;
    private int era;
    private String genre;
    private int original;
    private int count;
    private int state;
    private String sc;
    private String introduction;
    private String poster_url;
    private int reviews;
    private List<Episode> episodes;
    private List<Tagmap> commtags;

    public Drama(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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
                return "耽美";
            case 1:
                return "全年龄";
            case 2:
                return "言情";
            case 3:
                return "百合";
            default:
                return "未知类型";
        }
    }

    public int getEra() {
        return era;
    }

    public void setEra(int era) {
        this.era = era;
    }

    public String getEraString() {
        switch (era) {
            case 0:
                return "现代";
            case 1:
                return "古风";
            case 2:
                return "民国";
            case 3:
                return "未来";
            case 4:
                return "其他时代";
            default:
                return "未知时代";
        }
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isOriginal() {
        return original == 1 ? true : false;
    }

    public void setOriginal(int original) {
        this.original = original;
    }

    public String getOriginalString() {
        if (original == 1) {
            return "原创";
        } else {
            return "改编";
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getState() {
        return state;
    }

    public String getStateString() {
        switch (state) {
            case 0:
                return "连载";
            case 1:
                return "完结";
            case 2:
                return "已坑";
            default:
                return "未知进度";
        }
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    public List<Tagmap> getCommtags() {
        return commtags;
    }

    public String getCommtagsString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Tagmap tagmap:commtags) {
            stringBuilder.append(' ');
            stringBuilder.append(tagmap.getTag().getName());
            stringBuilder.append('(');
            stringBuilder.append(tagmap.getCount());
            stringBuilder.append(')');
        }
        return stringBuilder.toString();
    }

    public void setCommtags(List<Tagmap> commtags) {
        this.commtags = commtags;
    }
}
