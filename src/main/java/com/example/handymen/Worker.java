package com.example.handymen;
public class Worker {

    private String name, email, phone, experience, rate, location, category;

    public Worker(String name, String email, String phone,
                  String experience, String rate,
                  String location, String category) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.experience = experience;
        this.rate = rate;
        this.location = location;
        this.category = category;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getExperience() { return experience; }
    public String getRate() { return rate; }
    public String getLocation() { return location; }
    public String getCategory() { return category; }
}
