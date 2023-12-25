package com.apps.mineralidentyficationapp.collection;

import java.io.Serializable;
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
public class MineralMessage implements Serializable {
    private Long id;
    private String name;
    private String comment;
    private String discoveryPlace;
    private String value;
    private String weight;
    private String size;
    private String inclusion;
    private String clarity;
    private List<Long> imagesID;
    private List<String> images;
    private List<Long> deletedImages;
    private List<String> tags;
    private String mineralName;
}
