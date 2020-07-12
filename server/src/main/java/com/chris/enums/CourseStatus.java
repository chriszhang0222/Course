package com.chris.enums;

public enum CourseStatus {
    PUBLISH("P", "Publish"),
    DRAFT("D", "Draft");

    private String code;
    private String desc;

    CourseStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
