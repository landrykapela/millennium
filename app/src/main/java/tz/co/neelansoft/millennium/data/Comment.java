package tz.co.neelansoft.millennium.data;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Comment{
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss",Locale.getDefault());
    private int id;
    private String user;
    private String body;
    private Date date;

    public Comment(int id, String user, String body, Date date) {
        this.id = id;
        this.user = user;
        this.body = body;
        this.date = date;
    }

    public Comment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return dateFormat.format(date);
    }
    public void setDate(String date){
        Date myDate = new Date(date);
        this.date = myDate;
    }

/*
    public void setDate(Date date) {
        this.date = date;
    }
    */
}
