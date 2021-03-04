package com.example.linkapp.objects;

public class link_obj {

    private String description;
    private String links;
    private String id;
    private String sender_id;


    public link_obj(String description, String links, String id, String sender_id) {
        this.description = description;
        this.links = links;
        this.id = id;
        this.sender_id = sender_id;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }
}
