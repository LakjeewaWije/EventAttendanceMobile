package com.example.kliq.eventattendancemobile.data.model;

public class Event {

    private String head;
    private String desc;
    private String id;

    public Event(String head, String desc, String id) {
        this.head = head;
        this.desc = desc;
        this.id = id;
    }

    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }

    public String getId() {
        return id;
    }
}
