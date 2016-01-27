package net.saoju.dramatown.Models;

import java.util.List;

public class Reviews {
    private int per_page;
    private int current_page;
    private String next_page_url;
    private String prev_page_url;
    private int from;
    private int to;
    private List<Review> data;

    public Reviews(int per_page, int current_page, String next_page_url,
                   String prev_page_url, int from, int to, List<Review> reviews) {
        this.per_page = per_page;
        this.current_page = current_page;
        this.next_page_url = next_page_url;
        this.prev_page_url = prev_page_url;
        this.from = from;
        this.to = to;
        this.data = reviews;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

    public String getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(String prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public List<Review> getData() {
        return data;
    }

    public void setData(List<Review> reviews) {
        this.data = reviews;
    }
}
