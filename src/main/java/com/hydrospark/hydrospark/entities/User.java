package com.hydrospark.hydrospark.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int Id;

    public String firstName;

    public String lastName;

    public String email;

    public String password;

    public long number;

    public boolean visitedProduct;
    public Date dateOfProductVisit;
    public boolean contacted;

    public User() {
    }

    public User(String firstName, String lastName, String email, String password, long number) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.number = number;
    }

    public int getId() {
        return Id;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public boolean isVisitedProduct() {
        return visitedProduct;
    }

    public void setVisitedProduct(boolean visitedProduct) {
        this.visitedProduct = visitedProduct;
    }

    public Date getDateOfProductVisit() {
        return dateOfProductVisit;
    }

    public void setDateOfProductVisit(Date dateOfProductVisit) {
        this.dateOfProductVisit = dateOfProductVisit;
    }

    public boolean isContacted() {
        return contacted;
    }

    public void setContacted(boolean contacted) {
        this.contacted = contacted;
    }
}
