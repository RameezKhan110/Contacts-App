package com.example.contacts;

public class ModelContact {

    String id, image, name, phone, email, note, addedtime, updatedtime;

    public ModelContact(String id, String image, String name, String phone, String email, String note, String addedtime, String updatedtime) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.note = note;
        this.addedtime = addedtime;
        this.updatedtime = updatedtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAddedtime() {
        return addedtime;
    }

    public void setAddedtime(String addedtime) {
        this.addedtime = addedtime;
    }

    public String getUpdatedtime() {
        return updatedtime;
    }

    public void setUpdatedtime(String updatedtime) {
        this.updatedtime = updatedtime;
    }
}
