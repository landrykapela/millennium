package tz.co.neelansoft.millennium.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Connection {

    private String id;
    private String startTime;
    private String endTime;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM YYYY HH:mm:ss", Locale.getDefault());

    public Connection(String id, String startTime, String endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Connection() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartTime() {
        return dateFormat.format(new Date(startTime));
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return dateFormat.format(new Date(endTime));
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
