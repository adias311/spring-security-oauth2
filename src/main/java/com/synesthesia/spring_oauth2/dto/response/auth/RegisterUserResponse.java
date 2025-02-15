package com.synesthesia.spring_oauth2.dto.response.auth;

import com.synesthesia.spring_oauth2.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserResponse {

    private UserDTO user;

    private String message;
}
