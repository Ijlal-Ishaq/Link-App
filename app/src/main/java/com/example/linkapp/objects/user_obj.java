package com.example.linkapp.objects;

public class user_obj {


    String id;
    String name;

    String img_url;
    String email;

    public user_obj(String id, String name, String email) {
        this.id = id;
        this.name = name;

        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
