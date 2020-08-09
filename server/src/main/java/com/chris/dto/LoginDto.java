package com.chris.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    private String id;
    private String loginName;
    private String name;
    private String token;
    private List<ResourceDto> resources;
    private HashSet<String> requests;
}
