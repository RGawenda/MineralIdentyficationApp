package com.apps.mineralidentyficationapp.rest.messages;

import com.apps.mineralidentyficationapp.utils.AccountType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private AccountType accountType;
}
