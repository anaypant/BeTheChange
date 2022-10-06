package com.anaypant.qrated.Frames;
import java.util.ArrayList;

public class TrendingNews {
    private String status, totalResults;
    private ArrayList<ModelNews> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public ArrayList<ModelNews> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<ModelNews> articles) {
        this.articles = articles;
    }
}
