package com.synesthesia.springoauth2.dto.request.auth;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserRequest {

    @Size(min = 3, max = 100)
    private String username;

    @Size(min = 5, max = 10)
    private String password;

}
