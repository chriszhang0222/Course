package com.chris.enums;

import lombok.Data;
import lombok.Getter;

@Getter
public enum FileUse {
    COURSE("C", "Course"),
    TEACHER("T", "Teacher");

    FileUse(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;
    private String desc;


}
