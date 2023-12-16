package com.apps.mineralidentyficationapp.collection;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MineralMessage {
    private Long id;
    private String name;
    private String comment;
    private String discoveryPlace;
    private String value;
    private String weight;
    private String size;
    private String inclusion;
    private String clarity;
    private List<String> images;
    private List<String> deletedImages;
    private List<String> tags;
    private String mineralName;
}
