package com.apps.mineralidentyficationapp.rest.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClassificationMessage {
    private String authToken;
    private String stringImage;
}
