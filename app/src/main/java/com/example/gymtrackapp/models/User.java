package com.example.gymtrackapp.models;

public class User {
    private String userId;
    private String name;
    private String email;
    private String studentId;
    private String paymentStatus;

    public User() {

    }

    public User(String userId, String name, String email, String studentId, String paymentStatus) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.studentId = studentId;
        this.paymentStatus = paymentStatus;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
