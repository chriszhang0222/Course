package com.chris.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String id;
    private String loginName;
    private String name;
    private String password;
    private String imageCode;
    private String imageCodeToken;
}
