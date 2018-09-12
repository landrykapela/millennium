package tz.co.neelansoft.millennium.data;

import android.graphics.Bitmap;

public class Book {
    private int id;
    private String title;
    private String author;
    private String filename;
    private Bitmap thumbnail;

    public Book() {
    }

    public Book(int id, String title, String author, String filename, Bitmap thumbnail) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.filename = filename;
        this.thumbnail = thumbnail;
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

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }
}
