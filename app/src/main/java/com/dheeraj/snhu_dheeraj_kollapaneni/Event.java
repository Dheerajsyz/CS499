package com.dheeraj.snhu_dheeraj_kollapaneni;

public class Event {
    private final int id;
    private String name;
    private final String date;
    private final String time;

    public Event(int id, String name, String date, String time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setName(String name) {
        this.name = name;
    }

}
