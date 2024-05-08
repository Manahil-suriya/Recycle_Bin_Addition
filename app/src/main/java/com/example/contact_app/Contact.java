package com.example.contact_app;

public class Contact {
    String name, phone, url, address;

    public Contact() {
    }

    public Contact(String name, String phone, String url, String address) {
        this.name = name;
        this.phone = phone;
        this.url = url;
        this.address = address;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
