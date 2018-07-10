package com.example.kliq.eventattendancemobile;

/**
 * Created by ajmal on 7/10/18.
 */

public class EventItem {

    private String head;
    private String desc;

    public EventItem(String head, String desc) {
        this.head = head;
        this.desc = desc;
    }

    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }
}
