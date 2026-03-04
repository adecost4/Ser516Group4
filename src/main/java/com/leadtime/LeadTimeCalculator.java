package com.leadtime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.net.http.HttpResponse;
import java.time.OffsetDateTime;

public class LeadTimeCalculator {
    public static int calculateLeadTime(HttpResponse<String> userStoryHTTP) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        String body = userStoryHTTP.body();

        UserStory story = null;
        try {
            story = mapper.readValue(body, UserStory.class);
        }
        catch (Exception e) {
            System.out.println("Error reading user story JSON: " + body);
            e.printStackTrace();
        }

        if (story == null || !story.isClosed) {
            return -1;
        } else {
            return (int) java.time.Duration.between(story.createdDate, story.finishedDate).toDays();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class UserStory {
        @JsonProperty("created_date")
        OffsetDateTime createdDate;

        @JsonProperty("finish_date")
        OffsetDateTime finishedDate;

        @JsonProperty("is_closed")
        boolean isClosed;
    }
}