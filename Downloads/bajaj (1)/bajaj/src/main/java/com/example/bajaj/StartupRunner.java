package com.example.bajaj;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@Component
public class StartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // Step 1: Call generateWebhook API
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> body = new HashMap<>();
        body.put("name", "Aditi Parwani"); // ✅ your name
        body.put("regNo", "0126CS221015"); // ✅ your regNo
        body.put("email", "0126CS221015@oriental.ac.in"); // ✅ fixed email format

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getBody() == null) {
            throw new RuntimeException("Failed to generate webhook");
        }

        String webhookUrl = (String) response.getBody().get("webhook");
        String accessToken = (String) response.getBody().get("accessToken");

        System.out.println("Webhook URL: " + webhookUrl);
        System.out.println("Access Token: " + accessToken);

        // Step 2: Correct SQL query for your odd regNo
        String finalQuery = "SELECT " +
                "    p.AMOUNT AS SALARY, " +
                "    CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, " +
                "    FLOOR(DATEDIFF(CURDATE(), e.DOB) / 365.25) AS AGE, " +
                "    d.DEPARTMENT_NAME " +
                "FROM PAYMENTS p " +
                "INNER JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID " +
                "INNER JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                "WHERE DAY(p.PAYMENT_TIME) != 1 " +
                "ORDER BY p.AMOUNT DESC " +
                "LIMIT 1";

        // Step 3: Submit SQL query to webhook
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setContentType(MediaType.APPLICATION_JSON);
        authHeaders.setBearerAuth(accessToken);

        Map<String, String> solutionBody = new HashMap<>();
        solutionBody.put("finalQuery", finalQuery);

        HttpEntity<Map<String, String>> solutionRequest = new HttpEntity<>(solutionBody, authHeaders);

        ResponseEntity<String> solutionResponse = restTemplate.postForEntity(webhookUrl, solutionRequest, String.class);

        System.out.println("Submission Response: " + solutionResponse.getBody());
    }
}
