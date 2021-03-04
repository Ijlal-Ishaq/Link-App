package com.example.linkapp.objects;

public class directory_obj {


    private String name;
    private String discription;
    private String id;
    private String owner;
    private Boolean post_status;


    public directory_obj(String name, String discription, String id, String owner, Boolean post_status) {
        this.name = name;
        this.discription = discription;
        this.id = id;
        this.owner = owner;
        this.post_status = post_status;
    }

    public Boolean getPost_status() {
        return post_status;
    }

    public void setPost_status(Boolean post_status) {
        this.post_status = post_status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
