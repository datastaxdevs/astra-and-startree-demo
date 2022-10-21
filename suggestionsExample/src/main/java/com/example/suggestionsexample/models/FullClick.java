package com.example.suggestionsexample.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FullClick(@JsonProperty("click_timestamp") Long clickTimestamp,
                        @JsonProperty("url_host") String urlHost,
                        @JsonProperty("url_protocol") String urlProtocol,
                        @JsonProperty("url_path") String urlPath,
                        @JsonProperty("url_query") String urlQuery,
                        @JsonProperty("browser_type") String browserType,
                        @JsonProperty("operating_system") String operatingSystem,
                        @JsonProperty("visitor_id") String visitorId,
                        long latitude,
                        long longitude){
}
