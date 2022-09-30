package com.example.btc;

import java.util.HashMap;

public class ModelClass {
    private String author, title, description, url, urlToImage, publishedAt, upvotect, downvotect;

    public ModelClass() {
    }

    private HashMap<String, Object> votes, comments;

    public HashMap<String, Object> getVotes() {
        return votes;
    }

    public void setVotes(HashMap<String, Object> votes) {
        this.votes = votes;
    }

    public HashMap<String, Object> getComments() {
        return comments;
    }

    public void setComments(HashMap<String, Object> comments) {
        this.comments = comments;
    }

    public ModelClass(String author, String title, String description, String url, String urlToImage, String publishedAt, String upvotect, String downvotect, HashMap<String, Object> votes, HashMap<String, Object> comments) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.upvotect = upvotect;
        this.downvotect = downvotect;
        this.votes = votes;
        this.comments = comments;
    }

    public String getUpvotect() {
        return upvotect;
    }

    public void setUpvotect(String upvotect) {
        this.upvotect = upvotect;
    }

    public String getDownvotect() {
        return downvotect;
    }

    public void setDownvotect(String downvotect) {
        this.downvotect = downvotect;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public int compareTo(ModelClass m1){
        return Integer.parseInt(m1.upvotect)-Integer.parseInt(this.upvotect);
    }
}
