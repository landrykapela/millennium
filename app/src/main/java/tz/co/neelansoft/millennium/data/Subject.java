package tz.co.neelansoft.millennium.data;

public class Subject {

    private int subject_id;
    private String subject_name;

    public int getSubjectId() {
        return subject_id;
    }

    public void setSubjectId(int subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubjectName() {
        return subject_name;
    }

    public void setSubjectName(String subject_name) {
        this.subject_name = subject_name;
    }

    public Subject(int subject_id, String subject_name){
        this.subject_id = subject_id;
        this.subject_name = subject_name;
    }
}
