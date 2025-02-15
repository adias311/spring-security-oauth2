package com.synesthesia.spring_oauth2.dto.response.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    private int statusCode;
    private String error;
    private String message;
    private String path;
    private String timestamp;

}
