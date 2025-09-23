package com.example.bajaj;

public class SolutionRequest {
    private String finalQuery;

    public SolutionRequest() {
    }

    public SolutionRequest(String finalQuery) {
        this.finalQuery = finalQuery;
    }

    // Getter and Setter
    public String getFinalQuery() {
        return finalQuery;
    }

    public void setFinalQuery(String finalQuery) {
        this.finalQuery = finalQuery;
    }
}

