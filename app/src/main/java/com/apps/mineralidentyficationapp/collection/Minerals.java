package com.apps.mineralidentyficationapp.collection;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Minerals {
    private String mineralName;
    private Double mohsScale;
    private String chemicalFormula;
    private String occurrencePlace;
}
