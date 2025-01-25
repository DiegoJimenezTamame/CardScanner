package local.cardscanner;

import java.util.List;

// SearchResponse.java
public class SearchResponse {
    private List<CardResponse> data;
    private int total_cards;
    private String next_page;

    // Getters and setters
    public List<CardResponse> getData() {
        return data;
    }

    public int getTotal_cards() {
        return total_cards;
    }

    public String getNext_page() {
        return next_page;
    }
}
