package com.chris.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ResourceDto {

    private String id;
    private String parent;
    private String name;
    private String page;
    private String request;
    private List<ResourceDto> children;
}
