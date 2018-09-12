package tz.co.neelansoft.millennium.data;

import android.graphics.Bitmap;

public class Book {
    private int id;
    private int edition;
    private int pages;
    private int year;
    private String title;
    private String author;
    private String filename;
    private String publisher;
    private String theme;
    private String thumbnailUrl;

    public Book() {
    }

    public int getEdition() {
        return edition;
    }
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Book(int id, String title, String author, String filename, String publisher, String theme, int edition, int year, int pages, String thumbnailUrl) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.filename = filename;
        this.publisher = publisher;
        this.theme = theme;
        this.edition = edition;
        this.year = year;
        this.pages = pages;
        this.thumbnailUrl = thumbnailUrl;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
