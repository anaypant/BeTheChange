package com.example.qrated.Frames;

import java.util.HashMap;

public class ModelNews {
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private String upVoteCt;
    private String downVoteCt;
    private String commentCt;
    private HashMap<String, Object> votes, comments;

    public ModelNews() {
    }

    public ModelNews(String author, String title, String description, String url, String urlToImage, String publishedAt, String upVoteCt, String downVoteCt, String commentCt, HashMap<String, Object> votes, HashMap<String, Object> comments) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.upVoteCt = upVoteCt;
        this.downVoteCt = downVoteCt;
        this.commentCt = commentCt;
        this.votes = votes;
        this.comments = comments;
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

    public String getUpVoteCt() {
        return upVoteCt;
    }

    public void setUpVoteCt(String upVoteCt) {
        this.upVoteCt = upVoteCt;
    }

    public String getDownVoteCt() {
        return downVoteCt;
    }

    public void setDownVoteCt(String downVoteCt) {
        this.downVoteCt = downVoteCt;
    }

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

    public String getCommentCt() {
        return commentCt;
    }

    public void setCommentCt(String commentCt) {
        this.commentCt = commentCt;
    }
}
