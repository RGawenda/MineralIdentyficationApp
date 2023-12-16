package com.apps.mineralidentyficationapp.collection;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Minerals {
    private String mineralName;
    private Double mohsScale;
    private String chemicalFormula;
    private String occurrencePlace;
}
