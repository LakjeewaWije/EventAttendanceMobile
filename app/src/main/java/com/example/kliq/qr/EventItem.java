package com.example.kliq.qr;

class EventItem {
    private String head;
    private String desc;
    private String id;

    public EventItem(String head, String desc, String id) {
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
