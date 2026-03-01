package com.leadtime;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LeadTimeTaiga {
    // WIP
    public static void getLeadTimeInfo() {
        HttpClient client = HttpClient.newHttpClient();

        String json = """
        {
          "type": "normal",
          "username": "usernamehere",
          "password": "passwordhere"
        }
        """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://swent0linux.asu.edu/taiga/api/v1/auth"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        getLeadTimeInfo();
    }
}
