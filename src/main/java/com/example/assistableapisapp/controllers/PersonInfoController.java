package com.example.assistableapisapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.assistableapisapp.models.Person;


import com.google.gson.Gson;
import okhttp3.*;


@RestController
@RequestMapping("/pbverify")
public class PersonInfoController {


    private static final String API_URL = "https://veriid.com/Sandbox/webservice/pbverify-profile-id-verification";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json");

    @PostMapping("/person-info")
    public ResponseEntity<Person> verifyPersonInfo(@RequestParam("idNumber") String idNumber) {

        // Check if ID number has 13 digits
        if (idNumber.length() != 13) {
            String errorMessage = "Invalid ID number. It must be 13 digits.";
            Person person = new Person();
            person.setErrorMessage(errorMessage);
            return ResponseEntity.badRequest().body(person);
        }

        OkHttpClient client = new OkHttpClient();

        // Build the request body
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("memberkey", "PB-FLU001")
                .addFormDataPart("password", "062fd17a5e1b41b4e627406410e4769d")
                .addFormDataPart("consumer_details[idNumber]", idNumber)
                .addFormDataPart("consumer_details[yourReference]", "test")
                .build();

        // Build the request
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            // Send the request and get the response
            Response response = client.newCall(request).execute();

            // Process the response
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                Gson gson = new Gson();
                Person person = gson.fromJson(responseBody, Person.class);
                person.setStatus("success");
                return ResponseEntity.ok(person);
            } else {
                // Handle error response
                String errorMessage = "Request failed: " + response.code() + " - " + response.message();
                Person person = new Person();
                person.setErrorMessage(errorMessage);
                return ResponseEntity.status(response.code()).body(person);
            }
        } catch (Exception e) {
            // Handle exception
            Person person = new Person();
            System.out.println("An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(person);
        }

    }
}
