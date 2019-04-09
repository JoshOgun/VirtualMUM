package com.example.josh.virtualmum.JacksHomePageCode.TimetableView;


import java.io.Serializable;
import java.util.Date;


public class Schedule implements Serializable {

    String title ="";
    String professorName="";

    private Time startTime;
    private Time endTime;

    private Date date;

    public Schedule() {
        this.startTime = new Time();
        this.endTime = new Time();
        this.date = new Date();


    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }
}
