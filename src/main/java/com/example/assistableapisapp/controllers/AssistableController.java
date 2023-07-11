package com.example.assistableapisapp.controllers;

import com.example.assistableapisapp.services.AccountService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
public class AssistableController {
    private final AccountService accountService;
    public AssistableController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/bank-account-verification")
    public String  verifyBankAccount(@RequestParam("accountNumber") String accountNumber,@RequestParam("idNumber") String idNumber
            ,@RequestParam("accountType") String accountType,@RequestParam("branchCode") String branchCode,@RequestParam("branchCode") String yourReference) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Build the request body
        RequestBody body = accountService.buildRequestBody(accountNumber,idNumber,accountType,branchCode,yourReference);
        // Build Request
        Request request = accountService.buildRequest(body);

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        return responseBody;
    }
}
