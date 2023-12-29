package com.apps.mineralidentyficationapp.collection;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FoundMineralFilter {
    String mineralName;
    Double minMohsScale;
    Double maxMohsScale;
    String tagName;
    String user;
    String name;
    String comment;
    String discoveryPlace;
    Double minValue;
    Double maxValue;
    Double minWeight;
    Double maxWeight;
    Double minSize;
    Double maxSize;
    Boolean isNotNullInclusion;
    Boolean isNotNullClarity;
}
