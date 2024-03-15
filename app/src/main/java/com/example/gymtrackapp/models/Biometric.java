package com.example.gymtrackapp.models;


public class Biometric {
    private String userId;
    private String activityLevel;
    private String goal;
    private String gender;
    private int age;
    private double weight;
    private double height;

    // Empty constructor (required for Firestore)
    public Biometric() {
    }

    // Constructor
    public Biometric(String userId, String activityLevel, String gender, int age, double weight, double height,String goal) {
        this.userId = userId;
        this.activityLevel = activityLevel;
        this.gender = gender;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.goal= goal;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
    public String getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
