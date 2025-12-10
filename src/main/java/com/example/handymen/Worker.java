package com.example.handymen;

public class Worker {
    private String email;
    private String name;
    private String phone;
    private String experience;
    private String area;
    private String category;
    private String price;

    public Worker(String email, String name, String phone, String experience, String area, String category, String price) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.experience = experience;
        this.area = area;
        this.category = category;
        this.price = price;
    }

    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getExperience() { return experience; }
    public String getArea() { return area; }
    public String getCategory() { return category; }
    public String getPrice() { return price; }
}
