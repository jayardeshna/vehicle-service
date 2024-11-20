package com.vehicle.management.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestError {
    @JsonProperty("code")
    private int code;
    @JsonProperty("errorCode")
    private String errorCode;
    @JsonProperty("txt")
    private String txt;

    public RestError(int code, String txt) {
        this.code = code;
        this.txt = txt;
    }
}
