package com.example.handymen;
public class Worker {

    private String name, email, phone, profession, experience, rate, location;

    public Worker(String name, String email, String phone,
                  String experience, String rate,
                  String location, String profession) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.profession = profession;
        this.experience = experience;
        this.rate = rate;
        this.location = location;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getProfession() { return profession; }
    public String getExperience() { return experience; }
    public String getRate() { return rate; }
    public String getLocation() { return location; }
}
