package com.example.timetableview;


import java.io.Serializable;
import java.time.Month;
import java.util.Date;


public class Schedule implements Serializable {

    String classTitle="";
    String classPlace="";
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

    public String getClassTitle() {
        return classTitle;
    }

    public void setClassTitle(String classTitle) {
        this.classTitle = classTitle;
    }

    public String getClassPlace() {
        return classPlace;
    }

    public void setClassPlace(String classPlace) {
        this.classPlace = classPlace;
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
