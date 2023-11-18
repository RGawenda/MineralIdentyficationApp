package com.gymapps.mineralidentyficationapp.rest.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClassificationResultMessageClass {
    String authToken;
    String classificationResult;
}
