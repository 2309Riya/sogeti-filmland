package com.sogeti.filmland.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegistrationResponse {
    private Long id;
    private String email;
}