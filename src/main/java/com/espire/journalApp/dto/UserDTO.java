package com.espire.journalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @NotEmpty
    private String username;
    private String email;
    private boolean sentimentAnalysis;
    @NotEmpty
    private String password;
}
