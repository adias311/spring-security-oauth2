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
public class LoginUserRequest {

    @Size(min = 3, max = 50)
    private String username;

    @Size(min = 8, max = 10)
    private String password;

}
