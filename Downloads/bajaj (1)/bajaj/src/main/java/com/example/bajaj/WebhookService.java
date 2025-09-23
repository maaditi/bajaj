package com.example.bajaj;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebhookService {

    private static final String GENERATE_WEBHOOK_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Step 1: Generate webhook
     */
    public WebhookResponse generateWebhook(String name, String regNo, String email) {
        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("regNo", regNo);
        body.put("email", email);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<WebhookResponse> response = restTemplate.postForEntity(GENERATE_WEBHOOK_URL, request,
                WebhookResponse.class);

        return response.getBody();
    }

    /**
     * Step 2: Submit solution SQL query
     */
    public String submitSolution(String sqlQuery, String accessToken, String webhookUrl) {
        Map<String, String> solutionBody = new HashMap<>();
        solutionBody.put("finalQuery", sqlQuery);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<Map<String, String>> solutionRequest = new HttpEntity<>(solutionBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, solutionRequest, String.class);

        return response.getBody();
    }
}
