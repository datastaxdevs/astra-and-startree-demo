package com.example.suggestionsexample.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Suggestion(@JsonProperty("img_url") String imgUrl,
                         String title,
                         String description,
                         @JsonProperty("product_link") String productLink,
                         String[] tags) {
}
