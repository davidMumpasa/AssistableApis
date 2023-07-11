package com.example.assistableapisapp.models;

public class IdConfiguration {

    private String idNumber;

    public IdConfiguration(String idNumber){
        this.idNumber = idNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        // Check if idNumber is 13 digits
        if (idNumber.length() != 13) {
            throw new IllegalArgumentException("ID number must be 13 digits");
        }

        this.idNumber = idNumber;

    }
}
