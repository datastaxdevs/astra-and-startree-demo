package com.example.suggestionsexample.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;

public record PageView(@JsonProperty("click_epoch") Long clickEpoch,
                       @JsonProperty("utc_offset") Integer utcOffset,
                       @JsonProperty("request_url") @Nullable String requestUrl,
                       @JsonProperty("user_agent") @Nullable String userAgent,
                       @JsonProperty("visitor_id") @Nullable String visitorId,
                       String coords){

}