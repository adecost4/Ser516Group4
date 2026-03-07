package com.leadtime;

import java.net.http.HttpResponse;
import java.util.Map;
import java.util.List;
import com.TaigaAPI.TaigaApiClient;

import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

// not the most efficient/elegant way to do this, but made this way in the interest of time
public class LeadTimeRetriever {
    
    // return Map<story name, lead time in business days>
    public static Map<String, Integer> getLeadTimeInfo(TaigaApiClient taiga, List<Integer> storyIds) {

        String usBase = taiga.getBase() + "/userstories/";
        Map<String, Integer> leadTimes = new HashMap<>();

        ObjectMapper mapper = new ObjectMapper();

        for (Integer id : storyIds) {
            try {
                HttpResponse<String> response = taiga.sendGetRawHttpResponse(usBase + id);
                int leadTime = LeadTimeCalculator.calculateLeadTime(response);
                String body = response.body();

                JsonNode story = mapper.readTree(body);
                String name = null;
                if (story.has("subject")) {
                    name = story.get("subject").asText();
                }
                
                leadTimes.put(name, leadTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return leadTimes;
    }
}
