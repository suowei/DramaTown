package net.saoju.dramatown.Models;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewEpisode {
    private String dramaTitle;
    private int type;
    private int original;
    private int episodeId;
    private String episodeTitle;
    private String releaseDate;
    private String sc;
    private String alias;
    private String posterUrl;
    private int era;
    private String genre;
    private int state;
    private int duration;

    public String getDramaTitle() {
        return dramaTitle;
    }

    public void setDramaTitle(String dramaTitle) {
        this.dramaTitle = dramaTitle;
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

    public int getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(int episodeId) {
        this.episodeId = episodeId;
    }

    public String getEpisodeTitle() {
        return episodeTitle;
    }

    public void setEpisodeTitle(String episodeTitle) {
        this.episodeTitle = episodeTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseDateString() {
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        if (releaseDate.equals(sdf.format(today.getTime()))) {
            return "今天";
        } else if (releaseDate.equals(sdf.format(yesterday.getTime()))) {
            return "昨天";
        } else {
            return releaseDate;
        }
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
