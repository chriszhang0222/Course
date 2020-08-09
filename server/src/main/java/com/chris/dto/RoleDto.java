package com.chris.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleDto {
    private String id;
    private String name;
    private String desc;
    private List<String> resourceIds;
    private List<String> userIds;
}
