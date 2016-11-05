package com.ilya.portfolioproject;

/**
 * Created by Ilya on 11/5/2016.
 */
public class ArticlesItem {
    String title;
    String date;
    String description;
    String rubrics;
    String source;

    public ArticlesItem(String title, String date, String description, String source, String rubrics) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.source = source;
        this.rubrics = rubrics;
    }
}
