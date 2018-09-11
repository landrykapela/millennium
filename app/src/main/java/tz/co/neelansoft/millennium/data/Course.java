package tz.co.neelansoft.millennium.data;

import java.util.List;

public class Course {

    private int id;
    private String name;
    private String price;
    private List<Subject> subjectList;

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    public Course(int id, String name, String price, List<Subject> subjectList) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.subjectList = subjectList;

    }

    public Course() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
