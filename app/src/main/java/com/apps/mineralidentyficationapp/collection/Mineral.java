package com.apps.mineralidentyficationapp.collection;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Mineral {
    private String mineralName;
    private String vss;
    private String mohsScale;
    private String chemicalFormula;
    private String occurrencePlace;
}
